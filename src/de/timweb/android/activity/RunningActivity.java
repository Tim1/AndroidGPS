package de.timweb.android.activity;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.timweb.android.track.TrackManager;
import de.timweb.android.util.RunningUpdaterTask;

public class RunningActivity extends Activity {
	private static TrackManager trackmanager;

	private RunningUpdaterTask timerTask;
	private Button buttonSS;
	private int modus;
	private boolean trackIsStarted;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.running);
		trackIsStarted = false;

		if (trackmanager == null)
			trackmanager = new TrackManager();
		if (trackmanager.isRunning())
			setProgressBarIndeterminateVisibility(true);

		modus = getIntent().getIntExtra("modus", 0);
		setIcon(modus);

		buttonSS = (Button) findViewById(R.id.but_start_pause);

		timerTask = new RunningUpdaterTask(this, trackmanager);
		Timer timer = new Timer();
		timer.schedule(timerTask, 1000, 1000);
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
			((TextView) findViewById(R.id.tv_step))
					.setVisibility(View.INVISIBLE);
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
		trackIsStarted = true;
		switch (view.getId()) {
		case R.id.but_start_pause:
			if (buttonSS.getText() == getString(R.string.bt_txt_pause)) {
				trackmanager.pause();
				setProgressBarIndeterminateVisibility(false);
				buttonSS.setText(getString(R.string.bt_txt_start));
			} else {

				trackmanager.start(modus);
				setProgressBarIndeterminateVisibility(true);
				buttonSS.setText(getString(R.string.bt_txt_pause));
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		//nötig, was, wenn backtaste gdrückt, dann könnte löschmethode ausgefürht werden->exceptions
		if (trackIsStarted == true) {
			//besser ein .setItemList ?
			Builder builder = new Builder(this);
			builder.setTitle(R.string.close)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(R.string.sure_to_close)
					.setPositiveButton(R.string.di_save,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									timerTask.reset();
									trackmanager.stop();
									finish();
								}
							})
					.setNegativeButton(R.string.di_drop,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									timerTask.reset();
									trackmanager.deleteTrack(TrackManager
											.getTrack().getID());
									finish();
								}
							}).setNeutralButton(R.string.di_back, null)
							.show();
		} else
			super.onBackPressed();

	}
}
