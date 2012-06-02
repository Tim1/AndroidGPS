package de.timweb.android.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.timweb.android.util.DatabaseManager;
import de.timweb.android.util.Track;

public class ChooseTrackActivity extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Track> mTracks = null;
	private TrackAdapter m_adapter;
	private Runnable viewOrders;

	private class TrackAdapter extends ArrayAdapter<Track> {

		public TrackAdapter(Context context, int textViewResourceId,ArrayList<Track> items) {
			super(context, textViewResourceId, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			Track track = mTracks.get(position);
			if (track != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText(getString(R.string.tv_txt_date) + ": "+ track.getID());
				}
				if (bt != null) {
					bt.setText(getString(R.string.tv_txt_status) + ": "
							+ track.getDistance());
				}
			}
			return v;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosetrack);
		mTracks = new ArrayList<Track>();
		this.m_adapter = new TrackAdapter(this, R.layout.row, mTracks);
		setListAdapter(this.m_adapter);

		viewOrders = new Runnable() {
			public void run() {
				getOrders();
			}
		};
		Thread thread = new Thread(null, viewOrders, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(ChooseTrackActivity.this,
				getString(R.string.pd_txt_please_wait),
				getString(R.string.pd_txt_loading_data), true);
	}

	private void getOrders() {
		try {
			mTracks = new ArrayList<Track>();
			DatabaseManager dbManager = new DatabaseManager(this);
			SQLiteDatabase mDatabase = dbManager.getWritableDatabase();
			String sql = "SELECT _id, track_date, track_distance, track_time, steps FROM gps_track";
			Cursor cursor = mDatabase.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				// zu ressourcenlastig? objekterstllung notwendig? wird eh nur
				// ES WIRD EINE LIGTHtRAK ERSTELLT OHNE STATISTIC & CO
				// ausgelesen....
				mTracks.add(new Track(cursor.getInt(0), cursor.getDouble(2),
						cursor.getInt(4)));
			}
			cursor.close();
			mDatabase.close();
			Log.i("ARRAY", "" + mTracks.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	private Runnable returnRes = new Runnable() {

		public void run() {
			if (mTracks != null && mTracks.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < mTracks.size(); i++)
					m_adapter.add(mTracks.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent intent = new Intent(this, GoogleMapsDrawPathActivity.class);
		intent.putExtra("_id", mTracks.get(position).getID());
		startActivity(intent);

	}
}
