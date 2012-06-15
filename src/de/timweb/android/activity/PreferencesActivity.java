package de.timweb.android.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioGroup;
import de.timweb.android.R;

public class PreferencesActivity extends Activity {

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;

	private DisplayMetrics dm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		pref = getSharedPreferences("PreferencesActivity", MODE_PRIVATE);
		editor = pref.edit();
		dm = getResources().getDisplayMetrics();
	}


	@Override
	protected void onResume() {
		super.onResume();
		((RadioGroup) findViewById(R.id.radioGroup_Language)).check(pref
				.getInt("language", 0));
	}

	public void onRadioButtonClicked(View v) {
		editor.putInt("language",
				((RadioGroup) findViewById(R.id.radioGroup_Language))
						.getCheckedRadioButtonId());
		editor.commit();
		setLanguage();
	}

	private void setLanguage() {
		android.content.res.Configuration conf = getResources()
				.getConfiguration();

		if (pref.getInt("language", 0) == R.id.rb_Lan_de) {
			conf.locale = new Locale(Locale.GERMAN.toString());
		}
		if (pref.getInt("language", 0) == R.id.rb_Lan_en) {

			conf.locale = new Locale(Locale.ENGLISH.toString());
		}
		getResources().updateConfiguration(conf, dm);
	}

}
