package de.timweb.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import de.timweb.android.activity.R.layout;
import de.timweb.android.track.TrackManager;
import de.timweb.android.util.LocationReader;
import de.timweb.android.util.MyOverlay;

public class GoogleMapActivity extends MapActivity {
	/** Called when the activity is first created. */
	private List mapOverlays;
	private Projection projection;
	private MapController mc;
	private MapView mapView;
	private GeoPoint gP;
	private MyOverlay myoverlay;

	private ArrayList<Location> mLocations;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.googlemap);

		setUpGoogleMap();
	}

	public void setUpGoogleMap() {
		mLocations = LocationReader.getLocations(this,
				getIntent().getIntExtra("_id", 0));
		if (mLocations.size() >= 2) {
			mapView = (MapView) findViewById(R.id.googlemap);
			mapView.setBuiltInZoomControls(true);
			mapView.setSatellite(true);
			
			
			gP = new GeoPoint((int) (mLocations.get(0).getLatitude() * 1e6),
					(int) (mLocations.get(0).getLongitude() * 1e6));

			mc = mapView.getController();
			mc.setCenter(gP);
			mc.setZoom(15);

			mapOverlays = mapView.getOverlays();
			projection = mapView.getProjection();

			myoverlay = new MyOverlay(mLocations, projection, this);
			mapOverlays.add(myoverlay);
		} else
			Toast.makeText(this, R.string.toast_no_locations_found, Toast.LENGTH_SHORT)
					.show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
