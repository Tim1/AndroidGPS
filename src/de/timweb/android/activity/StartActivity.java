package de.timweb.android.activity;

import de.timweb.android.activity.test.GPSTestActivity;
import de.timweb.android.activity.test.SensorTestActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class StartActivity extends Activity {

	String selected;
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
			
		case R.id.but_SENSORTest:
			startActivity(new Intent(this, SensorTestActivity.class));
			break;
			
		case R.id.but_go:
			final Intent intent = new Intent(StartActivity.this,
					RunningActivity.class);

			Builder builder = new Builder(this);
			builder.setTitle("Choose Modus")
					.setSingleChoiceItems(R.array.runningmode, 0,new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,int which) {

									// selected =getResources().getStringArray(R.array.runningmode)[which];//scheint nciht zu klappen -.-
									lv = ((AlertDialog) dialog).getListView();
									lv.setTag(new Integer(which));

								}

							})
					.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,int which) {

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

}
