package de.timweb.android.activity.stats;

import de.timweb.android.activity.R;
import de.timweb.android.activity.R.drawable;
import de.timweb.android.activity.R.id;
import de.timweb.android.activity.R.layout;
import de.timweb.android.activity.R.menu;
import de.timweb.android.activity.R.string;
import de.timweb.android.track.TrackManager;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class StatisticActivity extends TabActivity{

	private int trackId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistic);
		trackId = getIntent().getIntExtra("_id", 0);
		
		
		Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    
	    
	   
	    intent = new Intent().setClass(this, OverviewActivity.class);
	    intent.putExtra("_id", trackId);
	    spec = tabHost.newTabSpec("overview").setIndicator(getString(R.string.txt_overview),
	    		res.getDrawable(R.drawable.tab_overview))
	    		.setContent(intent);
	    tabHost.addTab(spec);
	    
	    
	    intent = new Intent().setClass(this, DiagrammActivity.class);
	    intent.putExtra("mode", 1);
	    intent.putExtra("_id", trackId);
	    spec = tabHost.newTabSpec("diagram_1").setIndicator(getString(R.string.txt_graph_1),
	                      res.getDrawable(R.drawable.tab_diagramm_1))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    
	    
	    intent = new Intent().setClass(this, DiagrammActivity.class);
	    intent.putExtra("mode", 2);
	    intent.putExtra("_id", trackId);
	    spec = tabHost.newTabSpec("diagramm_2").setIndicator(getString(R.string.txt_graph_2),
	                      res.getDrawable(R.drawable.tab_diagramm_2))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	  
	    intent = new Intent().setClass(this, GoogleMapActivity.class);
	    intent.putExtra("_id", trackId);
	    spec = tabHost.newTabSpec("googlemap").setIndicator(getString(R.string.txt_map),
	    		res.getDrawable(R.drawable.tab_googlemap))
	    		.setContent(intent);
	    tabHost.addTab(spec);
	    
	    
//	    tabHost.setCurrentTab(2);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.statistic_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_statistic_delete:
			
			Builder builder = new Builder(this);
			builder.setTitle(R.string.delete_track)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(R.string.sure_to_delete_track)
					.setPositiveButton(R.string.di_delete,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									TrackManager.deleteTrack(getIntent().getIntExtra("_id", 0));
									finish();
								}
							}).setNegativeButton(android.R.string.cancel, null)
					.show();
			break;
		default:
			break;
		}
		return true;
	}
}

