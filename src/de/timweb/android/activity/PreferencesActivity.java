package de.timweb.android.activity;

import android.app.Activity;
import android.os.Bundle;

public class PreferencesActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
	}
}