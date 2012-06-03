package org.rhok.bribealert.activities;

import org.rhok.bribealert.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class SurveyActivity extends Activity {

	private static int currentQuestion = 0;
ViewFlipper viewFlipper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.survey);
		
		viewFlipper= (ViewFlipper) findViewById(R.id.viewFlipper);
	}
	


	public void nextLayout(View v) {
	currentQuestion++;
	viewFlipper.setDisplayedChild(currentQuestion);
	}
}
