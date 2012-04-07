package de.timweb.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_running:
			startActivity(new Intent(this, RunningActivity.class));
			break;

		case R.id.but_chooseTrack:
			startActivity(new Intent(this, ChooseTrackActivity.class));
			break;
		}

	}
}