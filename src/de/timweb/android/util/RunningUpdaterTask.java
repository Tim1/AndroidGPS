package de.timweb.android.util;

import java.util.TimerTask;

import android.app.Activity;
import android.widget.TextView;
import de.timweb.android.activity.R;
import de.timweb.android.track.Statistics;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;

public class RunningUpdaterTask extends TimerTask{
	private Activity activity;
	private TextView tv_distance;
	private TextView tv_speeed;
	private TextView tv_steps;
	private GraphLiveView graphSpeed;
	private GraphLiveView graphHeight;
	private GraphLiveView graphDistance;
	private GraphLiveView graphSteps;
	private TrackManager trackmanager;
	private TextView tv_time;

	public RunningUpdaterTask(Activity activity,TrackManager trackmanager) {
		this.activity = activity;
		this.trackmanager = trackmanager;
		
		graphSpeed = (GraphLiveView) activity.findViewById(R.id.view_graphview_Speed);
		graphSteps = (GraphLiveView) activity.findViewById(R.id.view_graphview_Steps);
		graphHeight = (GraphLiveView) activity.findViewById(R.id.view_graphview_Height);
		graphDistance = (GraphLiveView) activity.findViewById(R.id.view_graphview_Distance);
		
		
		graphDistance.setContext(activity);
//		graphSpeed.setMaxValue(30);
//		graphSteps.setMaxValue(2);
//		graphDistance.setMaxValue(100);
//		
		graphHeight.setMinValue(500);
		graphHeight.setMaxValue(1000);
		
		
		graphSpeed.setText("Speed (in km/h)");
		graphSteps.setText("Length of Steps (in m)");
		graphHeight.setText("Difference in Altitude (in m)");
		graphDistance.setText("Distance between measure Points (in m)");
		
		tv_time = (TextView) activity.findViewById(R.id.tv_timeEdit);
		tv_distance = (TextView) activity.findViewById(R.id.tv_distanceEdit);
		tv_steps = (TextView) activity.findViewById(R.id.tv_stepEdit);
	}
	
	@Override
	public void run() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Track track = trackmanager.getTrack();
				if(track == null)
					return;
				
				Statistics stats = track.getStatistics();
				
				tv_speeed.setText(track.getFormatedSpeed());
				tv_time.setText(track.getElapsedTime());
				tv_distance.setText(track.getFormatedDistance());
				tv_steps.setText(track.getSteps() + "");
				
				graphSpeed.setValueArrayList(stats.getSpeedValues());
				graphSpeed.postInvalidate();
				graphSteps.setValueArrayList(stats.getStepValues());
				graphSteps.postInvalidate();
				graphHeight.setValueArrayList(stats.getHeightValues());
				graphHeight.postInvalidate();
				graphDistance.setValueArrayList(stats.getDistanceValues());
				graphDistance.postInvalidate();
			}
		});
		
	}

	public void reset() {
		graphSpeed.setMaxValue(0);
		graphSteps.setMaxValue(0);
		graphDistance.setMaxValue(0);
		
		graphHeight.setMinValue(500);
		graphHeight.setMaxValue(1000);
	}
}
