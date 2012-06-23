package de.timweb.android;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import de.timweb.android.activity.ChooseTrackActivity;
import de.timweb.android.activity.CreditsActivity;
import de.timweb.android.activity.PreferencesActivity;
import de.timweb.android.activity.RunningActivity;
import de.timweb.android.track.TrackManager;

public class StartActivity extends Activity {
	private static final boolean DEVELOPER_MODE = false;
	private ListView lv;
	private UpdateTimerTask task;
	private Timer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (DEVELOPER_MODE) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedSqlLiteObjects()
					.penaltyLog().penaltyDeath().build());

		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		TrackManager.setContext(getApplicationContext());
		timer = new Timer();
		task = new UpdateTimerTask();
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	@SuppressLint("UseValueOf")
	public void onButtonClick(View view) {
		switch (view.getId()) {

		case R.id.but_wifi:
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			try {
				boolean before = wifi.isWifiEnabled();
				wifi.setWifiEnabled(!before);

				String str;
				if (before)
					str = getString(R.string.WLAN_disable);
				else
					str = getString(R.string.WLAN_enable);

				Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				Toast.makeText(this,
						getString(R.string.toast_Wifi) + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.but_start_gps:
			final Intent intent2 = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent2);
			break;
		case R.id.but_chooseTrack:
			startActivity(new Intent(this, ChooseTrackActivity.class));
			break;
		case R.id.but_start_credits:
			startActivity(new Intent(this, CreditsActivity.class));
			break;
		case R.id.but_start_preferences:
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
									lv.setTag(Integer.valueOf(which));

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

	/**
	 * Da Activites in Stacks gespeichert werden, wird bei Backtaste von
	 * PrefernecesActivity die alte evtl sprachlich veraltete StartActivity
	 * gerufen. Bei onResume wird dann das layout neu gesetzt.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.start);
		task.update();
	}

	private class UpdateTimerTask extends TimerTask {
		WifiManager wifi;
		LocationManager gps;
		ImageButton but_wifi;
		ImageButton but_gps;

		public UpdateTimerTask() {
			update();
		}

		/**
		 * muss aufgerufen werden, wenn nachdem das Layout neu gesetzt wird
		 */
		public void update() {
			wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			gps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			but_wifi = (ImageButton) findViewById(R.id.but_wifi);
			but_gps = (ImageButton) findViewById(R.id.but_start_gps);

		}

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					if (wifi.isWifiEnabled())
						but_wifi.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_start_wifi_on));
					else
						but_wifi.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_start_wifi_off));

					if (gps.isProviderEnabled(LocationManager.GPS_PROVIDER))
						but_gps.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_start_gps_on));
					else
						but_gps.setImageDrawable(getResources().getDrawable(
								R.drawable.ic_start_gps_off));

				}
			});

		}
	}
}
