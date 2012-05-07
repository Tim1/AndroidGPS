package de.timweb.android.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RunningActivity extends Activity {

	private Button buttonSS;
	private Chronometer chro;
	private long eclipsedTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.running);

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
		Toast.makeText(this, getIntent().getIntExtra("modus", 0) + "",
				Toast.LENGTH_SHORT).show();

	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_start_pause:
			if (buttonSS.getText() == "Pause") {
				eclipsedTime = SystemClock.elapsedRealtime() - chro.getBase();
				chro.stop();
				buttonSS.setText("Start");
			} else {
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
