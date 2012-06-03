package de.timweb.android.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.timweb.android.track.Track;

public class ChooseTrackActivity extends ListActivity {

	
	
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Track> mTracks = null;
    private TrackAdapter m_adapter;
    private Runnable viewOrders;
	
    private class TrackAdapter extends ArrayAdapter<Track> {

        public TrackAdapter(Context context, int textViewResourceId, ArrayList<Track> items) {
                super(context, textViewResourceId, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                Track track = mTracks.get(position);
                if (track != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if (tt != null) {
                              tt.setText("Date: "+track.getDate());                            }
                        if(bt != null){
                              bt.setText("Status: "+ track.getModus());
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
       
        viewOrders = new Runnable(){
            public void run() {
                getOrders();
            }
        };
    Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(ChooseTrackActivity.this,    
              "Please wait...", "Retrieving data ...", true);
	}
	
	
	private void getOrders(){
        try{
            mTracks = new ArrayList<Track>();
//            Track o1 = new Track(0,0);
//            Track o2 = new Track(1,0);
//            mTracks.add(o1);
//            mTracks.add(o2);
               Thread.sleep(2000);
            Log.i("ARRAY", ""+ mTracks.size());
          } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
      }
	
	
	private Runnable returnRes = new Runnable() {

        public void run() {
            if(mTracks != null && mTracks.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<mTracks.size();i++)
                m_adapter.add(mTracks.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
      };

}
