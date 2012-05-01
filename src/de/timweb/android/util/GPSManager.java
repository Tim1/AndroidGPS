package de.timweb.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import de.timweb.android.activity.R;

public class GPSManager implements LocationListener {
	private LocationManager locationManager;
	private SQLiteDatabase database;
	private SQLiteStatement sql;
	private Context context;
	
	private boolean isRunning = false;
	private int trackid;
	private Track track;
	private DatabaseManager dbManager;
	/*
	 * trackid in intent uebergeben putExtra("trackid",id);
	 */
	
	public GPSManager(Context context, int trackid) {
		this.trackid = trackid;
		this.context = context;
		
		dbManager = new DatabaseManager(context);
		database = dbManager.getWritableDatabase();
		
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		sql = database.compileStatement(context.getResources().getString(
				R.string.db_insert_location));
		track = new Track(trackid);
	}
	
	

	/**
	 * debug: zeigt Informationen über Track
	 */
	public synchronized void show(){
		Toast.makeText(context, "Distance: "+ (float)track.getDistance() +" m", Toast.LENGTH_SHORT).show();
	}
	
	public synchronized void start() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 0, this);
		isRunning = true;
		Toast.makeText(context, "GPS started", Toast.LENGTH_SHORT).show();
	}
	public synchronized void stop() {
		locationManager.removeUpdates(this);
		database.close();
		isRunning = false;
		Toast.makeText(context, "GPS stopped", Toast.LENGTH_SHORT).show();
	}

	public synchronized void deleteTrack() {
//		database.delete("gps_location", "track", new String[]{trackid+""});
		if(!database.isOpen())
			database = dbManager.getWritableDatabase();
		
		database.execSQL(context.getResources().getString(R.string.db_delete_location)+trackid);
		
		track = new Track(trackid);
		Toast.makeText(context, "reset Track "+trackid, Toast.LENGTH_SHORT).show();
	}

	public synchronized void onLocationChanged(Location location) {
		sql.clearBindings();

		sql.bindLong(1, trackid);
		sql.bindLong(2, location.getTime());
		sql.bindDouble(3, location.getAccuracy());
		sql.bindDouble(4, location.getLongitude());
		sql.bindDouble(5, location.getLatitude());
		sql.bindDouble(6, location.getAccuracy());
		sql.bindDouble(7, location.getSpeed());

		track.addLocation(location);
		sql.executeInsert();
//		Toast.makeText(context, "insert SQL", Toast.LENGTH_SHORT).show();
	}
	public boolean isRunning() {
		return isRunning;
	}

	public synchronized void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public synchronized void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public synchronized void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
}
