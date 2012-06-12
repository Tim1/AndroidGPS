package de.timweb.android.activity.stats;

import de.timweb.android.activity.R;
import de.timweb.android.activity.R.id;
import de.timweb.android.activity.R.layout;
import de.timweb.android.activity.R.string;
import de.timweb.android.track.Statistics;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;
import de.timweb.android.util.GraphLiveView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DiagrammActivity extends Activity {

	private Track track;
	private Statistics stats;

	private GraphLiveView graphSpeed;
	private GraphLiveView graphHeight;
	private GraphLiveView graphDistance;
	private GraphLiveView graphSteps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diagramm);

		track = TrackManager.generateFromDatabase(this, getIntent()
				.getIntExtra("_id", 0));
		stats = track.getStatistics();
		setUpLayout();

	}

	public void setUpLayout() {
		switch (getIntent().getIntExtra("mode", 0)) {
		case 1:
			graphSpeed = (GraphLiveView) findViewById(R.id.view_da_graphview_Speed);
			graphSpeed.setVisibility(View.VISIBLE);
			graphSpeed.setText(getResources().getString(R.string.graph_speed));
			graphSpeed.setValueArrayList(stats.getSpeedValues());

			graphDistance = (GraphLiveView) findViewById(R.id.view_da_graphview_Distance);
			graphDistance.setVisibility(View.VISIBLE);
			graphDistance.setText(getResources().getString(R.string.graph_distance));
			graphDistance.setValueArrayList(stats.getDistanceValues());

			break;

		case 2:

			graphHeight = (GraphLiveView) findViewById(R.id.view_da_graphview_Height);
			graphHeight.setVisibility(View.VISIBLE);
			graphHeight.setText(getResources().getString(R.string.graph_heigth));
			graphHeight.setValueArrayList(stats.getHeightValues());

			if (track.getModus() == 0) {
				graphSteps = (GraphLiveView) findViewById(R.id.view_da_graphview_Steps);
				graphSteps.setVisibility(View.VISIBLE);
				graphSteps.setText(getResources().getString(R.string.graph_steps));
				graphSteps.setValueArrayList(stats.getStepValues());
			}
			break;
		}
	}

}
