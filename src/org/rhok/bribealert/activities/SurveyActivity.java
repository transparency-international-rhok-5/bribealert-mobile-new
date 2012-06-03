package org.rhok.bribealert.activities;

import org.rhok.bribealert.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper;

public class SurveyActivity extends Activity {

	private static int currentQuestion = 0;
	ViewFlipper viewFlipper;
	String tag="SurveyActivity"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.survey);

		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
	}

	public void nextLayout(View v) {
		currentQuestion++;
		viewFlipper.setDisplayedChild(currentQuestion);
	}

	public void showBribe(View v) {
		currentQuestion = 0;
		viewFlipper.setDisplayedChild(6);
	}

	public void showNoBribe(View v) {
		currentQuestion = 0;
		viewFlipper.setDisplayedChild(5);
	}

	public void startReportingActivity(View view) {
		startActivity(new Intent(SurveyActivity.this, ReportingActivity.class));
		Log.d(tag, "Started gethelp activity");
	}

}
