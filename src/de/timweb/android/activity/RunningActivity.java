package de.timweb.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class RunningActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.running);
//		Toast.makeText(this, "" + getIntent().getExtras().getLong("modus"), Toast.LENGTH_LONG).show();
	
		String str = getResources().getStringArray(R.array.runningmode)[(int) getIntent().getExtras().getLong("modus")];
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	public void onButtonClick(View view) {
		
		switch (view.getId()) {
		case R.id.but_jogging:
			
//			startActivity(new Intent(this, RunningActivity.class));
			break;

		case R.id.but_bycycle:
//			startActivity(new Intent(this, ChooseTrackActivity.class));
			break;

		case R.id.but_walk:
//			startActivity(new Intent(this, PreferencesActivity.class));
			break;

		case R.id.but_daily:
//			startActivity(new Intent(this, PreferencesActivity.class));
			break;
		}
	}
}
