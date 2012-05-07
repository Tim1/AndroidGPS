package de.timweb.android.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import de.timweb.android.util.TrackManager;

public class RunningActivity extends Activity {
	private static TrackManager gpsmanager;
	
	private Button buttonSS;
	private Chronometer chro;
	private long eclipsedTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.running);
		
		if(gpsmanager == null)
			gpsmanager = new TrackManager(this);
		if(gpsmanager.isRunning())
			setProgressBarIndeterminateVisibility(true);

		switch (getIntent().getIntExtra("modus", 0)) {
		case 0:
			((ImageView) findViewById(R.id.im_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_jogging));
			break;
		case 1:
			((ImageView) findViewById(R.id.im_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_bycycle));
			((TextView) findViewById(R.id.tv_step)).setVisibility(4);
			((TextView) findViewById(R.id.tv_stepEdit)).setVisibility(4);
			break;
		case 2:
			((ImageView) findViewById(R.id.im_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_walk));

			break;
		case 3:
			((ImageView) findViewById(R.id.im_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_walk));

			break;

		default:
			break;
		}

		buttonSS = (Button) findViewById(R.id.but_start_pause);
		chro = (Chronometer) findViewById(R.id.chronometer1);
		final TextView tv_distance = (TextView) findViewById(R.id.tv_distanceEdit);
		final TextView tv_steps = (TextView) findViewById(R.id.tv_stepEdit);
//		Toast.makeText(this, getIntent().getIntExtra("modus", 0) + "",
//				Toast.LENGTH_SHORT).show();

		chro.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv_distance.setText((float)(gpsmanager.getTrack().getDistance()/1000)+" m");
				tv_steps.setText(gpsmanager.getTrack().getSteps()+"");
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			public void afterTextChanged(Editable s) {
			}
		});
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_start_pause:
			if (buttonSS.getText() == "Pause") {
				gpsmanager.stop();
				setProgressBarIndeterminateVisibility(false);
				
				
				eclipsedTime = SystemClock.elapsedRealtime() - chro.getBase();
				chro.stop();
				buttonSS.setText("Start");
			} else {
				gpsmanager.start();
				setProgressBarIndeterminateVisibility(true);
				
				chro.setBase(SystemClock.elapsedRealtime() - eclipsedTime);
				chro.start();
				buttonSS.setText("Pause");
			}
			break;

		case R.id.but_pref:
			startActivity(new Intent(this, PreferencesActivity.class));

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		Builder builder = new Builder(this);
		builder.setTitle("Close")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("Are you sure you want to quit?")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								finish();

							}
						}).setNegativeButton(android.R.string.cancel, null)
				.show();

	}
}
