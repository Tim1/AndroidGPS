package de.timweb.android.activity.stats;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import de.timweb.android.R;
import de.timweb.android.util.LocationReader;
import de.timweb.android.util.LocationReader.LocationAndSteps;
import de.timweb.android.util.MyOverlay;

public class GoogleMapActivity extends MapActivity {
	/** Called when the activity is first created. */
	@SuppressWarnings("rawtypes")
	private List mapOverlays;
	private Projection projection;
	private MapController mc;
	private MapView mapView;
	private GeoPoint gP;
	private MyOverlay myoverlay;
	private boolean hasLocations = false;
	private ArrayList<LocationAndSteps> mLocations;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.googlemap);
		((SeekBar) findViewById(R.id.sb_googlemaps))
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (progress == 0)
							progress = 1;
						((TextView) findViewById(R.id.tv_sbtext))
								.setText(getString(R.string.txt_step_size)
										+ progress);

					}
				});

		setUpGoogleMap();
	}

	@SuppressWarnings("unchecked")
	public void setUpGoogleMap() {
		mLocations = LocationReader.getLocations(this,
				getIntent().getIntExtra("_id", 0));
		if (mLocations.size() >= 2) {
			hasLocations = true;
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
			Toast.makeText(this, R.string.toast_no_locations_found,
					Toast.LENGTH_SHORT).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.googlemap_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_googlemap_accuracy:
			if (hasLocations) {
				showAccuracyMenu();
			} else {
				Toast.makeText(this, R.string.toast_no_locations_found,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_googlemap_mapstyle:
			if (hasLocations) {
				Builder builder = new Builder(this);
				builder.setTitle(R.string.di_mapstyle)
						.setNeutralButton(R.string.di_roadmap,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										mapView.setSatellite(false);

									}
								})
						.setPositiveButton(R.string.di_satellite,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										mapView.setSatellite(true);
									}
								}).show();
			} else {
				Toast.makeText(this, R.string.toast_no_locations_found,
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		return true;
	}

	public void showAccuracyMenu() {
		((SeekBar) findViewById(R.id.sb_googlemaps))
				.setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.but_set_accuracy))
				.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tv_sbtext)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tv_sbtext))
				.setText(getString(R.string.txt_step_size)
						+ ((SeekBar) findViewById(R.id.sb_googlemaps))
								.getProgress());
	}

	public void closeAccuracyMenu() {
		((SeekBar) findViewById(R.id.sb_googlemaps)).setVisibility(View.GONE);
		((Button) findViewById(R.id.but_set_accuracy)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_sbtext)).setVisibility(View.GONE);

	}

	public void onButtonClick(View view) {

		switch (view.getId()) {
		case R.id.but_set_accuracy:
			int step = (((SeekBar) findViewById(R.id.sb_googlemaps))
					.getProgress());
			if (step == 0)
				step = 1;
			myoverlay.setStep(step);

			closeAccuracyMenu();
			break;
		default:
			break;
		}
	}
}
