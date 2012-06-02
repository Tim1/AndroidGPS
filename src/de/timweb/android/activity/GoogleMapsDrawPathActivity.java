package de.timweb.android.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import de.timweb.android.util.LocationReader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

public class GoogleMapsDrawPathActivity extends MapActivity {
	/** Called when the activity is first created. */
	private List mapOverlays;
	private Projection projection;
	private MapController mc;
	private MapView mapView;
	private GeoPoint gP;
	// private GeoPoint gP2;
	private MyOverlay myoverlay;
	
	
	private ArrayList<Location> mLocations;
	private int latitude;
	private int longitude;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stat);
		
		//BEGIN
		try{
		mLocations = LocationReader.getLocations(this, getIntent().getIntExtra("_id", 0));
		}catch (Exception e) {
			Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
		}
//		Toast.makeText(this, ""+getIntent().getIntExtra("_id", 0), Toast.LENGTH_SHORT).show();
		// END
		

		mapView = (MapView) findViewById(R.id.googlemap);// Creating an instance
														// of MapView
		mapView.setBuiltInZoomControls(true);// Enabling the built-in Zoom
												// Controls

		//gP = new GeoPoint(33695043, 73000000);// Creating a GeoPoint
		latitude = (int)(mLocations.get(0).getLatitude()*1e6);
		longitude = (int)(mLocations.get(0).getLongitude()*1e6);
		gP = new GeoPoint(latitude,longitude);
		mc = mapView.getController();
		mc.setCenter(gP);
		mc.setZoom(15);// Initializing the MapController and setting the map to
				// center at the
		// defined GeoPoint

		mapOverlays = mapView.getOverlays();
		projection = mapView.getProjection();

		myoverlay = new MyOverlay(mLocations);
		mapOverlays.add(myoverlay);

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class MyOverlay extends Overlay {
		private ArrayList<Location> mLocations;
		private GeoPoint[] gpArray;
		public MyOverlay(ArrayList<Location> locations) {
			mLocations = locations;
			gpArray = new GeoPoint[mLocations.size()];
			
			for(int i = 0; i < gpArray.length; i++){
				latitude = (int)(mLocations.get(i).getLatitude()*1e6);
				longitude = (int)(mLocations.get(i).getLongitude()*1e6);
				gpArray[i] = new GeoPoint(latitude,longitude);
			}
		}

		public void draw(Canvas canvas, MapView mapv, boolean shadow) {
			super.draw(canvas, mapv, shadow);
//			Toast.makeText(GoogleMapsDrawPathActivity.this, "Length gpAray"+gpArray.length, Toast.LENGTH_SHORT).show();
//			if(true)
//				return;
			// Configuring the paint brush
			Paint mPaint = new Paint();
			mPaint.setDither(true);
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(4);

			//BEGIN
			
			Point[] pointArray = new Point[gpArray.length+1];
			Path[] pathArray = new Path[gpArray.length-1];
			
			
			//optimeriungsmöglichkeiten vorhanden !!
			for(int i = 0; i < pathArray.length; i++){
				pointArray[i] = new Point();
				pointArray[i+1] = new Point();
				pathArray[i] = new Path();
				
				projection.toPixels(gpArray[i], pointArray[i]);
				projection.toPixels(gpArray[i+1], pointArray[i+1]);
				pathArray[i].moveTo(pointArray[i].x, pointArray[i].y);
				pathArray[i].lineTo(pointArray[i+1].x, pointArray[i+1].y);
				
				canvas.drawPath(pathArray[i], mPaint);
			}
			
			// END
			
			
			
			
			
//			GeoPoint gP1 = new GeoPoint(40737944, -73985818);// starting point
//															// Abbottabad
//			GeoPoint gP2 = new GeoPoint(33695043, 73050000);// End point
//															// Islamabad
//
//			GeoPoint gP4 = new GeoPoint(33695043, 73050000);// Start point
//															// Islamabad
//			GeoPoint gP3 = new GeoPoint(33615043, 73050000);// End Point
//															// Rawalpindi
//
//			Point p1 = new Point();
//			Point p2 = new Point();
//			Path path1 = new Path();
//
//			Point p3 = new Point();
//			Point p4 = new Point();
//			Path path2 = new Path();
//			projection.toPixels(gP2, p3);
//			projection.toPixels(gP1, p4);
//
//			path1.moveTo(p4.x, p4.y);// Moving to Abbottabad location
//			path1.lineTo(p3.x, p3.y);// Path till Islamabad
//
//			projection.toPixels(gP3, p1);
//			projection.toPixels(gP4, p2);
//
//			path2.moveTo(p2.x, p2.y);// Moving to Islamabad location
//			path2.lineTo(p1.x, p1.y);// Path to Rawalpindi
//
//			canvas.drawPath(path1, mPaint);// Actually drawing the path from
//											// Abbottabad to Islamabad
//			canvas.drawPath(path2, mPaint);// Actually drawing the path from
//											// Islamabad to Rawalpindi

		}

	}
}