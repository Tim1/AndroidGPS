package de.timweb.android.activity;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import de.timweb.android.track.TrackManager;
import de.timweb.android.util.RunningUpdaterTask;

public class RunningActivity extends Activity {
	private static TrackManager trackmanager;

	private int graphView = 0;
	private RunningUpdaterTask timerTask;
	private ImageButton buttonSS;
	private int modus;
	private boolean isPaused = true;
	
	private Timer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.running);

		if (trackmanager == null)
			trackmanager = new TrackManager();
		if (trackmanager.isRunning())
			setProgressBarIndeterminateVisibility(true);

		modus = getIntent().getIntExtra("modus", 0);
		setIcon(modus);

		buttonSS = (ImageButton) findViewById(R.id.but_start_pause);

		timerTask = new RunningUpdaterTask(this, trackmanager);
		timer = new Timer();
	}

	private void setIcon(int modus) {
		switch (modus) {
		case 0:
			((ImageView) findViewById(R.id.im_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_jogging));
			break;
		case 1:
			((ImageView) findViewById(R.id.im_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_bike));
			((TextView) findViewById(R.id.tv_stepEdit))
					.setVisibility(View.INVISIBLE);
			break;
		case 2:
			((ImageView) findViewById(R.id.im_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_car));
			break;
		}
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_start_pause:
			if (isPaused) {
				timerTask.reset();
				timer.schedule(timerTask, 1000, 1000);
				trackmanager.start(modus);
				setProgressBarIndeterminateVisibility(true);
				
				isPaused = false;
				buttonSS.setImageResource(R.drawable.ic_pause);
			} else {
				trackmanager.stop();
				timer.cancel();
				
				setProgressBarIndeterminateVisibility(false);
				
				buttonSS.setImageResource(R.drawable.ic_play);
				isPaused = true;
			}
			break;
		case R.id.but_left:
			graphView--;
			if (graphView == -1)
				graphView = 3;
			switchGraphView();
			break;
		case R.id.but_right:
			graphView = ++graphView % 4;
			switchGraphView();
			break;
		}
	}

	private void switchGraphView() {
		switch (graphView) {
		case 0:
			findViewById(R.id.view_graphview_Speed).setVisibility(View.VISIBLE);
			findViewById(R.id.view_graphview_Height).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Distance).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Steps).setVisibility(View.GONE);
			break;
		case 1:
			findViewById(R.id.view_graphview_Speed).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Height)
					.setVisibility(View.VISIBLE);
			findViewById(R.id.view_graphview_Distance).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Steps).setVisibility(View.GONE);
			break;
		case 2:
			findViewById(R.id.view_graphview_Speed).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Height).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Distance).setVisibility(
					View.VISIBLE);
			findViewById(R.id.view_graphview_Steps).setVisibility(View.GONE);
			break;
		case 3:
			findViewById(R.id.view_graphview_Speed).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Height).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Distance).setVisibility(View.GONE);
			findViewById(R.id.view_graphview_Steps).setVisibility(View.VISIBLE);
			break;
		}

	}

	@Override
	public void onBackPressed() {
		if (!trackmanager.isRunning()) {
			finish();
			return;
		}

		Builder builder = new Builder(this);
		builder.setTitle("Close")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("Are you sure you want to quit?")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								trackmanager.stop();
								finish();
							}
						}).setNegativeButton(android.R.string.cancel, null)
				.show();

	}
}