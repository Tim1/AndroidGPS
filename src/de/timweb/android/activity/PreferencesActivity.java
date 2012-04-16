package de.timweb.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PreferencesActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
	}
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_GPS:
//			startActivity(new Intent(this, GPSActivity erstellen .class));
			break;
			
		case R.id.but_style:
			//startActivity(new Intent(this, Styleactivity erstellen.class));
			break;
		}
	}
}
