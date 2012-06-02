package de.timweb.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import de.timweb.android.util.TrackManager;

/**
 * nur für Debuggingzwecke
 * @author Tim
 *
 */
@Deprecated
public class GPSTestActivity extends Activity {

	private static TrackManager trackmanager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gpstest);

		if(trackmanager == null)
			trackmanager = new TrackManager(this);
		if(trackmanager.isRunning())
			setProgressBarIndeterminateVisibility(true);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_gps_start:
			trackmanager.start();
			setProgressBarIndeterminateVisibility(true);
			break;
		case R.id.but_gps_pause:
			trackmanager.pause();
			setProgressBarIndeterminateVisibility(false);
			break;
		case R.id.but_gps_stop:
			trackmanager.stop();
			setProgressBarIndeterminateVisibility(false);
			break;
		case R.id.but_gps_delete:
			trackmanager.deleteCurrentTrack();
			break;
		case R.id.but_gps_show:
			trackmanager.show();
			break;
		}
	}
}
