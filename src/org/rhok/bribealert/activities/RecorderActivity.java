package org.rhok.bribealert.activities;

import java.io.IOException;

import org.rhok.bribealert.R;
import org.rhok.bribealert.services.VideoRecorder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class RecorderActivity extends Activity {
    /** Called when the activity is first created. */
    
    public boolean hidden = false;
    final Handler mHandler = new Handler();
    public VideoRecorder vr;
    private ImageView iv;
//    private FrameLayout fl;
//    
//    private Activity mainer;
    Context co;
    
    boolean recording = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.player);
        
        vr = (VideoRecorder) findViewById(R.id.camcorder_preview);
        iv = (ImageView) findViewById(R.id.hider);
        co = this;

    }
    
    public void reset() {
        vr.setPath("/recordings/" + System.currentTimeMillis() + ".mp4");
        vr.recorder.reset();
    }
    
    public String getPath() {
        return vr.getPath();
    }
    
    public void onResume() {
        super.onResume();
    }
       
    
    public void start() {
            vr.setVisibility(View.VISIBLE);
            iv.setVisibility(View.VISIBLE);
            hidden = true;
            
            final VideoRecorder vvv = vr;
            vr.setPath("/recordings/" + System.currentTimeMillis() + ".mp4");
            mHandler.post(new Runnable() {

                public void run() {
                    try {
                        vvv.start(co);
                        recording = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }});
            
    }
    
    public void stop() {
            
            
            final VideoRecorder vvv = vr;
            mHandler.post(new Runnable() {

                public void run() {
                    try {
                        recording = false;
                        vvv.stop();
                        vr.setVisibility(View.GONE);
                        iv.setVisibility(View.GONE);
                        hidden = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }});
           
    }
    
//    public void setParentGroup(MainActivityGroup magg) {
//        mag = magg;
//    }
//    
//    public void setMainActivity(Activity magg) {
//        mainer = magg;
//        fl = (FrameLayout) mainer.findViewById(R.id.Recorder);
//    }
//    
//    public void setFL(FrameLayout magg) {
//        fl = magg;
//    }
    
    public boolean isVideoRecording() {
        return recording;
    }
    
}