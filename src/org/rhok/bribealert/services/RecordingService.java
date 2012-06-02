package org.rhok.bribealert.services;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class RecordingService extends Service{
    	
	AudioRecorder recorder = new AudioRecorder("/recordings/" + System.currentTimeMillis() + ".3gp");
	//private boolean running=false;
	
    public class RecordingServiceBinder extends Binder {
    	RecordingService getService() {
            return RecordingService.this;
        }
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	private final IBinder mBinder = new RecordingServiceBinder();
	
	public void startRecording() throws IOException{
		recorder.start();
	}
	
	public void stopRecording() throws IOException{
		recorder.stop();
	}

}

