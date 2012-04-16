package de.timweb.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class StartActivity extends Activity {

	
	private Spinner s;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		s = (Spinner) findViewById(R.id.spinner_modus);
			ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
					R.array.runningmode, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s.setAdapter(adapter);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_running:
			startActivity(new Intent(this, RunningActivity.class));
			break;

		case R.id.but_chooseTrack:
			startActivity(new Intent(this, ChooseTrackActivity.class));
			break;

		case R.id.but_preferences:
			startActivity(new Intent(this, PreferencesActivity.class));
			break;

		case R.id.but_go:
			//info vom spinner muss an runningactivity üebrgeben werden.
			Intent intent = new Intent(this,RunningActivity.class);
			intent.putExtra("modus", s.getSelectedItemId());
			startActivity(intent);
		}
	}

}