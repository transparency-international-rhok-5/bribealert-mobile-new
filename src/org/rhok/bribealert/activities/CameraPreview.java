package org.rhok.bribealert.activities;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String TAG = "CameraPreview";
	private SurfaceHolder mHolder;
	private Camera mCamera;

	public final MediaRecorder recorder = new MediaRecorder();
	String path;

	public CameraPreview(Context context, Camera camera) {
		super(context);

		if (camera == null) {
			return;
		}
		mCamera = camera;

		camera.unlock();
		recorder.setCamera(camera);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		path = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/recordings/" + System.currentTimeMillis() + ".mp4";
		try {
			start(getContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	/**
	 * Starts a new recording.
	 */
	public void start(Context c) throws IOException {
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			throw new IOException("SD Card is not mounted.  It is " + state
					+ ".");
		}

		// make sure the directory we plan to store the recording in exists
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created.");
		}

		if ((Integer.parseInt(Build.VERSION.SDK) >= 8)) {
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
			recorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_HIGH));
		} else {
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
			recorder.setVideoFrameRate(30);
		}
		recorder.setOutputFile(path);
		Surface s = mHolder.getSurface();
		recorder.setPreviewDisplay(s);
		recorder.prepare();
		recorder.start();
	}

	/**
	 * Stops a recording that has been previously started.
	 */
	public void stop() throws IOException {
		try {
			recorder.stop();
			recorder.release();
			mCamera.release();
		} catch (Exception e) {
		}
	}

	public String getPath() {
		return path;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
		try {
			CameraPreview.this.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			CameraPreview.this.start(getContext());

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

}