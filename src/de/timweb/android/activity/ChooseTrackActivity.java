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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;
import android.widget.Toast;
import de.timweb.android.util.DatabaseManager;

public class ChooseTrackActivity extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Track> mTracks = null;
	private TrackAdapter m_adapter;
	private Runnable viewOrders;

	private class TrackAdapter extends ArrayAdapter<Track> {

		public TrackAdapter(Context context, int textViewResourceId,
				ArrayList<Track> items) {
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
				int modus = track.getModus();
				ImageView iv = (ImageView) v.findViewById(R.id.ic_listview);
				switch (modus) {
				case Track.MODE_JOGGING:
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_jogging));
				case Track.MODE_BYCYCLE:
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_bike));
				case Track.MODE_CAR:
					iv.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_car));
				}

				TextView tt = (TextView) v.findViewById(R.id.tv_date);
				if (tt != null)
					tt.setText(track.getDate());

				tt = (TextView) v.findViewById(R.id.tv_distance);
				if (tt != null)
					tt.setText(track.getFormatedDistance());

				tt = (TextView) v.findViewById(R.id.tv_time);
				if (tt != null)
					tt.setText(track.getElapsedTime());

			}
			return v;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosetrack);

		setUpList();
	}

	public void setUpList() {
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
			mTracks = TrackManager.getLiteTrackArray(this, Track.MODE_ALL);

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

		Intent intent = new Intent(this, StatisticActivity.class);
		intent.putExtra("_id", mTracks.get(position).getID());
		startActivity(intent);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setUpList();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		setUpList(); // kklappt, aber onResume() nicht...wo ist mein fehler ?
	}

}
