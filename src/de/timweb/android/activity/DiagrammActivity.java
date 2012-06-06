package de.timweb.android.activity;

import de.timweb.android.track.Statistics;
import de.timweb.android.track.TrackManager;
import de.timweb.android.util.GraphLiveView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DiagrammActivity extends Activity{
	Statistics stats;
	
	private GraphLiveView graphSpeed;
	private GraphLiveView graphHeight;
	private GraphLiveView graphDistance;
	private GraphLiveView graphSteps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diagramm);
		
		stats = TrackManager.generateFromDatabase(this, getIntent().getIntExtra("_id", 0)).getStatistics();
		
		
		
		switch (getIntent().getIntExtra("mode", 0)) {
		case 1:
			graphSpeed = (GraphLiveView) findViewById(R.id.view_da_graphview_Speed);
			graphSpeed.setVisibility(View.VISIBLE);
			graphSpeed.setText("Speed (in km/h)");
			graphSpeed.setValueArrayList(stats.getSpeedValues());
			
			
			graphDistance = (GraphLiveView) findViewById(R.id.view_da_graphview_Distance);
			graphDistance.setVisibility(View.VISIBLE);
			graphDistance.setText("Distance between measure Points (in m)");
			graphDistance.setValueArrayList(stats.getDistanceValues());

			break;

		case 2:
			graphSteps = (GraphLiveView) findViewById(R.id.view_da_graphview_Steps);
			graphSteps.setVisibility(View.VISIBLE);
			graphSteps.setText("Length of Steps (in m)");
			graphSteps.setValueArrayList(stats.getStepValues());


			graphHeight = (GraphLiveView) findViewById(R.id.view_da_graphview_Height);
			graphHeight.setVisibility(View.VISIBLE);
			graphHeight.setText("Difference in Altitude (in m)");
			graphHeight.setValueArrayList(stats.getHeightValues());

			break;
		}
	}
	
}