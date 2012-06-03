package org.rhok.bribealert.activities;

import java.io.IOException;

import org.rhok.bribealert.R;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class TIVideoCamera extends Activity implements SurfaceHolder.Callback{

	private static final String TAG = "TI CAMERA";

	private static final String VIDEO_FILE = "-video";
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private boolean previewRunning;
	private MediaRecorder mediaRecorder;
	private final int maxDurationInMs = 20000;
	private final long maxFileSizeInBytes = 500000;
	private final int videoFramesPerSecond = 30;
	
	private String path;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.camera);
            surfaceView = (SurfaceView) findViewById(R.id.camcorder_preview_surface);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		if (camera != null){
			Camera.Parameters params = camera.getParameters();
			camera.setParameters(params);
			camera.setDisplayOrientation(90);
		}
		else {
			Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (previewRunning){
			camera.stopPreview();
			Log.d(TAG, "Preview stopped");
		}
		//Camera.Parameters p = camera.getParameters();
		//p.setPreviewSize(width, height);
		//p.setPreviewFormat(PixelFormat.JPEG);
		//camera.setParameters(p);

		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			previewRunning = true;
			Log.d(TAG, "Preview running");
			startRecording();
		}
		catch (IOException e) {
			Log.e(TAG,e.getMessage());
			e.printStackTrace();
		}
	}


	public void surfaceDestroyed(SurfaceHolder holder) {
		stopRecording();
		camera.stopPreview();
		previewRunning = false;
		camera.release();
	}
	
	public boolean startRecording(){
		try {
			Log.d(TAG, "Unlock camera");
			camera.unlock();

			mediaRecorder = new MediaRecorder();

			mediaRecorder.setCamera(camera);
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

			mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

			path = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/recordings/" + System.currentTimeMillis() + VIDEO_FILE + ".mp4";
			
			mediaRecorder.setOutputFile(path);

			mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

            mediaRecorder.prepare();
            Log.d(TAG, "Prepared, start ...");
			mediaRecorder.start();
			
			return true;
		} catch (IllegalStateException e) {
			Log.e(TAG,e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.e(TAG,e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	public void stopRecording(){
		mediaRecorder.stop();
		Log.d(TAG, "Stopped recording");
		camera.lock();
	}
	
}
