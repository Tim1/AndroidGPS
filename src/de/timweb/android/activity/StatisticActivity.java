package de.timweb.android.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

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


	    intent = new Intent().setClass(this, DiagrammActivity.class);
	    intent.putExtra("mode", 1);
	    intent.putExtra("_id", trackId);
	    spec = tabHost.newTabSpec("diagram_1").setIndicator("Graph 1",
	                      res.getDrawable(R.drawable.tab_diagramm_1))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    
	    
	    intent = new Intent().setClass(this, DiagrammActivity.class);
	    intent.putExtra("mode", 2);
	    intent.putExtra("_id", trackId);
	    spec = tabHost.newTabSpec("diagramm_2").setIndicator("Graph 2",
	                      res.getDrawable(R.drawable.tab_diagramm_2))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	  
	    intent = new Intent().setClass(this, GoogleMapActivity.class);
	    intent.putExtra("_id", trackId);
	    spec = tabHost.newTabSpec("googlemap").setIndicator("Map",
	    		res.getDrawable(R.drawable.tab_googlemap))
	    		.setContent(intent);
	    tabHost.addTab(spec);
	    
//	    tabHost.setCurrentTab(2);
	    /**
	     * Alles in einem Tab wuerde ich Sagen (dann sinds 4 Tabs)
	     * @author Tim
	     */
	   //noch zwei für notes und bewertung oder alles in einer
		
	}
}

