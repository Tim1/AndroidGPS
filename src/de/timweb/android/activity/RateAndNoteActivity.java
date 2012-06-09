package de.timweb.android.activity;

import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RateAndNoteActivity extends Activity {
	private RatingBar rb;
	private EditText et_note;
	private Button but_note;
	private TextView tv_note;
	private boolean editable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rateandnote);
		et_note = (EditText) findViewById(R.id.et_ran_note);
		but_note = (Button) findViewById(R.id.but_save_edit_note);
		rb = (RatingBar) findViewById(R.id.ratingBar);
		tv_note = (TextView) findViewById(R.id.tv_ran_note_edit);
		tv_note.setMovementMethod(new ScrollingMovementMethod());
		TrackManager.selectRatingAndNote(getIntent().getIntExtra("_id", 0), rb,
				tv_note);

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
				TrackManager.selectRatingAndNote(getIntent().getIntExtra("_id", 0), rb,
						tv_note);
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
