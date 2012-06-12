package de.timweb.android.activity.stats;

import de.timweb.android.activity.R;
import de.timweb.android.activity.R.drawable;
import de.timweb.android.activity.R.id;
import de.timweb.android.activity.R.layout;
import de.timweb.android.activity.R.string;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class OverviewActivity extends Activity {
	private RatingBar rb;
	private EditText et_note;
	private Button but_note;
	private TextView tv_note;
	private Track track;
	private boolean editable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);
		track = TrackManager.generateFromDatabase(this, getIntent()
				.getIntExtra("_id", 0));
		et_note = (EditText) findViewById(R.id.et_ov_note);
		but_note = (Button) findViewById(R.id.but_save_edit_note);
		rb = (RatingBar) findViewById(R.id.ratingBar);
		tv_note = (TextView) findViewById(R.id.tv_ov_note_edit);
		tv_note.setMovementMethod(new ScrollingMovementMethod());
		TrackManager.selectRatingAndNote(getIntent().getIntExtra("_id", 0), rb,
				tv_note);

		setIcon(track.getModus());
		setUpValues();

	}

	private void setUpValues() {
		((TextView) findViewById(R.id.tv_ov_dateEdit)).setText(track.getDate()
				+ "");
		((TextView) findViewById(R.id.tv_ov_timeEdit)).setText(track.getElapsedTime()
				+ "");
		((TextView) findViewById(R.id.tv_ov_distanceEdit)).setText(track
				.getFormatedDistance() + "");
		((TextView) findViewById(R.id.tv_ov_stepEdit)).setText(track.getSteps()
				+ "");
	}

	private void setIcon(int modus) {
		switch (modus) {
		case 0:
			((ImageView) findViewById(R.id.im_ov_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_mode_jogging));
			break;
		case 1:
			((ImageView) findViewById(R.id.im_ov_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_mode_bike));
			((TextView) findViewById(R.id.tv_ov_stepEdit))
					.setVisibility(View.GONE);

			((ImageView) findViewById(R.id.im_ov_steps))
					.setVisibility(View.GONE);
			break;
		case 2:
			((ImageView) findViewById(R.id.im_ov_modus))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_mode_car));
			((TextView) findViewById(R.id.tv_ov_stepEdit))
					.setVisibility(View.GONE);
			((ImageView) findViewById(R.id.im_ov_steps))
					.setVisibility(View.GONE);
			break;
		}
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.but_save_edit_note:
			if (!isEditable()) {
				tv_note.setVisibility(View.GONE);
				et_note.setVisibility(View.VISIBLE);
				et_note.setText(tv_note.getText().toString());
				editable = true;
				but_note.setText(R.string.bt_txt_save_note);
			} else {
				editable = false;
				et_note.setVisibility(View.GONE);
				tv_note.setVisibility(View.VISIBLE);
				but_note.setText(R.string.bt_txt_edit_note);
				TrackManager.insertRatingAndNote(
						getIntent().getIntExtra("_id", 0), rb.getRating(),
						et_note.getText().toString());
				TrackManager.selectRatingAndNote(
						getIntent().getIntExtra("_id", 0), rb, tv_note);
			}
			break;

		default:
			break;
		}
	}

	private boolean isEditable() {
		return editable;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		TrackManager.insertRatingAndNote(getIntent().getIntExtra("_id", 0),
				rb.getRating(), et_note.getText().toString());

	}

}
