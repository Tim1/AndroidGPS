package de.timweb.android.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;

public class StartActivity extends Activity {

	private static final boolean DEVELOPER_MODE = false;
	String selected;
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (DEVELOPER_MODE) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {

		case R.id.but_chooseTrack:
			startActivity(new Intent(this, ChooseTrackActivity.class));
			break;

		case R.id.but_GPSTest:
			startActivity(new Intent(this, GPSTestActivity.class));
			break;

		case R.id.but_preferences:
			startActivity(new Intent(this, PreferencesActivity.class));
			break;

		case R.id.but_go:
			final Intent intent = new Intent(StartActivity.this,
					RunningActivity.class);

			Builder builder = new Builder(this);
			builder.setTitle(R.string.choose_modus)
					.setSingleChoiceItems(R.array.runningmode, 0,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									lv = ((AlertDialog) dialog).getListView();
									lv.setTag(new Integer(which));

								}

							})
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									lv = ((AlertDialog) dialog).getListView();
									Integer selected = (Integer) lv.getTag();

									intent.putExtra("modus", selected);
									startActivity(intent);
								}
							}).setNegativeButton(android.R.string.cancel, null)
					.show();

			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * Da Activites in Stacks gespeichert werden, wird bei Backtaste von PrefernecesActivity
		 * die alte evtl sprachlich veraltete StartActivity gerufen.
		 * Bei onResume wird dann das layout neu gesetzt.
		 * */
		setContentView(R.layout.start);
	}



}
