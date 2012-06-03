package de.timweb.android.activity.test;

import java.util.Timer;
import java.util.TimerTask;

import de.timweb.android.activity.R;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * nur für Debuggingzwecke
 * @author Tim
 *
 */
@Deprecated
public class SensorTestActivity extends Activity{
	private static TrackManager gpsmanager;
	TextView tv1;
	TextView tv2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sensortest);
		if(gpsmanager == null)
			gpsmanager = new TrackManager(this);
		
		tv1 = (TextView) findViewById(R.id.tv_sensor);
		tv2 = (TextView) findViewById(R.id.tv_sensor_steps);
		
		TimerTask timer = new TimerTask() {
			
			@Override
			public void run() {
				SensorTestActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						tv1.setText(gpsmanager.mSensorListener.getAccl());
						tv2.setText(gpsmanager.mSensorListener.getSteps());
					}
				});
			}
		};
		Timer t = new Timer();
		t.schedule(timer, 1000, 200);
	}
	
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_gps_start:
			gpsmanager.start(Track.MODE_JOGGING);
			setProgressBarIndeterminateVisibility(true);
			break;
		case R.id.but_gps_stop:
			gpsmanager.stop();
			setProgressBarIndeterminateVisibility(false);
			break;
		}
	}
}
