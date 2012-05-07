package de.timweb.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PreferencesActivity extends Activity {

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
		pref = getSharedPreferences("PreferencesActivity", MODE_PRIVATE);
		editor = pref.edit();
		 ((SeekBar)findViewById(R.id.seekBar1)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				editor.putInt("seekBar", progress);
				editor.commit();
				((TextView)findViewById(R.id.tv_progress)).setText(""+pref.getInt("seekBar", 0));

			}
		});
		
	}

	public void onButtonClick(View view) {}

	@Override
	protected void onResume() {
		super.onResume();
		((RadioGroup)findViewById(R.id.radioGroup1)).check(pref.getInt("style", 0));
		((SeekBar)findViewById(R.id.seekBar1)).setProgress(pref.getInt("seekBar", 0));
	}

	

	public void onRadioButtonClicked(View v) {
		editor.putInt("style", ((RadioGroup)findViewById(R.id.radioGroup1)).getCheckedRadioButtonId());
		editor.commit();
	}
	

	
	

}
