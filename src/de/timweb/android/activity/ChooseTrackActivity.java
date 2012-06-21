package de.timweb.android.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.timweb.android.R;
import de.timweb.android.activity.stats.StatisticActivity;
import de.timweb.android.track.Track;
import de.timweb.android.track.TrackManager;

public class ChooseTrackActivity extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Track> mTracks = null;
	private TrackAdapter m_adapter;
	private Runnable viewOrders;
	private int position = 0;

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
				// TODO
				// registerForContextMenu(v);
			}
			Track track = mTracks.get(position);
			if (track != null) {
				setIcon((ImageView) v.findViewById(R.id.ic_listview),
						track.getModus());
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

	public void setIcon(ImageView iv, int modus) {
		switch (modus) {
		case Track.MODE_JOGGING:
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_mode_jogging));
			break;
		case Track.MODE_BYCYCLE:
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_mode_bike));
			break;
		case Track.MODE_CAR:
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_mode_car));
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosetrack);
		setUpList();
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			//um die position der langangeklicken view zu erhalten...mit getlistview.getselecteditemposition() kommt nur -1 heraus...
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
					long id) {
//            	v.setBackgroundColor(0xffff0000);
            	ChooseTrackActivity.this.position = position;
//        		Toast.makeText(ChooseTrackActivity.this,"LOOONG"+this.position, Toast.LENGTH_SHORT).show();
				return false;

			}
        });
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
		registerForContextMenu(getListView());
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent intent = new Intent(this, StatisticActivity.class);
		intent.putExtra("_id", mTracks.get(position).getID());
		startActivity(intent);

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.choosetrack_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menu_delete:
			Builder builder = new Builder(this);
			builder.setTitle(R.string.delete_track)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(getString(R.string.sure_to_delete_track)+"\n"+mTracks.get(position).getDate()+"\n"+mTracks.get(position).getFormatedDistance()+"\n"+ mTracks.get(position).getElapsedTime())
					.setPositiveButton(R.string.di_delete,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(ChooseTrackActivity.this, "Position in ListView:"+ position+"\n"+"Trackid: "+mTracks.get(position).getID(), Toast.LENGTH_SHORT).show();
									TrackManager.deleteTrack(mTracks.get(position).getID());
									setUpList();
								}
							}).setNegativeButton(android.R.string.cancel, null)
					.show();

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	
	

	@Override
	protected void onRestart() {
		super.onRestart();
		setUpList();
	}

}
