package de.timweb.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import de.timweb.android.util.GPSManager;

public class GPSTestActivity extends Activity {

	private static GPSManager gpsmanager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gpstest);

		if(gpsmanager == null)
			gpsmanager = new GPSManager(this, 0);
		if(gpsmanager.isRunning())
			setProgressBarIndeterminateVisibility(true);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_gps_start:
			if (!gpsmanager.isRunning())
				gpsmanager.start();
			setProgressBarIndeterminateVisibility(true);
			break;
		case R.id.but_gps_stop:
			if (gpsmanager.isRunning())
				gpsmanager.stop();
			setProgressBarIndeterminateVisibility(false);
			break;
		case R.id.but_gps_delete:
			gpsmanager.deleteTrack();
			break;
		case R.id.but_gps_show:
			gpsmanager.show();
			break;
		}
	}
}
