package de.timweb.android.activity.stats;

import android.R.menu;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import de.timweb.android.R;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;

public class OverviewActivity extends Activity {
	private RatingBar rb;
	private EditText et_note;
	private TextView tv_note;
	private Track track;
	private boolean editable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);
		track = TrackManager.generateFromDatabase(this, getIntent()
				.getIntExtra("_id", 0));
		rb = (RatingBar) findViewById(R.id.ratingBar);
		tv_note = (TextView) findViewById(R.id.tv_ov_note_edit);
		tv_note.setMovementMethod(new ScrollingMovementMethod());
		et_note = (EditText) findViewById(R.id.et_ov_note);
		TrackManager.selectRatingAndNote(getIntent().getIntExtra("_id", 0), rb,
				tv_note);

		et_note.setText(tv_note.getText().toString());

		setIcon(track.getModus());
		registerForContextMenu(tv_note);
		setUpValues();

	}

	private void setUpValues() {
		((TextView) findViewById(R.id.tv_ov_dateEdit)).setText(track.getDate()
				+ "");
		((TextView) findViewById(R.id.tv_ov_timeEdit)).setText(track
				.getElapsedTime() + "");
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		TrackManager.insertRatingAndNote(getIntent().getIntExtra("_id", 0),
				rb.getRating(), tv_note.getText().toString());

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.overview_context_menu, menu);
		switch (v.getId()) {
		case R.id.tv_ov_note_edit:
			menu.findItem(R.id.menu_edit_note).setVisible(true);
			menu.findItem(R.id.menu_save_note).setVisible(false);
			break;
		default:
			menu.findItem(R.id.menu_edit_note).setVisible(false);
			menu.findItem(R.id.menu_save_note).setVisible(true);
			break;
		}

	}

	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_edit_note:
			editable = true;
			tv_note.setVisibility(View.GONE);
			et_note.setVisibility(View.VISIBLE);
			registerForContextMenu(et_note);
			return true;
		case R.id.menu_save_note:
			editable = false;
			et_note.setVisibility(View.GONE);
			tv_note.setVisibility(View.VISIBLE);
			TrackManager.insertRatingAndNote(getIntent().getIntExtra("_id", 0),
					rb.getRating(), et_note.getText().toString());
			TrackManager.selectRatingAndNote(getIntent().getIntExtra("_id", 0),
					rb, tv_note);
			registerForContextMenu(tv_note);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

}
