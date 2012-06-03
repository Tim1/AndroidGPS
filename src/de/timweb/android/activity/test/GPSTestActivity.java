package de.timweb.android.activity.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import de.timweb.android.activity.R;
import de.timweb.android.activity.R.id;
import de.timweb.android.activity.R.layout;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;

/**
 * nur für Debuggingzwecke
 * @author Tim
 *
 */
@Deprecated
public class GPSTestActivity extends Activity {

	private static TrackManager gpsmanager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.gpstest);

		if(gpsmanager == null)
			gpsmanager = new TrackManager();
		if(gpsmanager.isRunning())
			setProgressBarIndeterminateVisibility(true);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_gps_start:
			gpsmanager.start(Track.MODE_JOGGING);
			setProgressBarIndeterminateVisibility(true);
			break;
		case R.id.but_gps_pause:
			gpsmanager.pause();
			setProgressBarIndeterminateVisibility(false);
			break;
		case R.id.but_gps_stop:
			gpsmanager.stop();
			setProgressBarIndeterminateVisibility(false);
			break;
		case R.id.but_gps_delete:
			gpsmanager.deleteCurrentTrack();
			break;
		case R.id.but_gps_show:
			gpsmanager.show();
			break;
		}
	}
}
