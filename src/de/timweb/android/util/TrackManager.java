package de.timweb.android.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import de.timweb.android.activity.R;

public class TrackManager {
	private LocationListener listener;
	private LocationManager locationManager;
	private SQLiteDatabase database;
	private SQLiteStatement sql;
	private Context context;
	
	private boolean isRunning = false;
	private int trackid = -1;
	private Track track;
	private DatabaseManager dbManager;
		
	public TrackManager(Context context) {
		this.context = context;
		
		listener = new LocationListener();
		dbManager = new DatabaseManager(context);
		database = dbManager.getWritableDatabase();
		
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		sql = database.compileStatement(context.getResources().getString(
				R.string.db_insert_location));
	}
	
	public synchronized void setTrack(int trackid){
		if(isRunning)
			throw new IllegalStateException("Another Track("+this.trackid+") is still running!");
		
		this.trackid = trackid;
		track = new Track(trackid);
	}
	

	/**
	 * debug: zeigt Informationen über Track
	 */
	public synchronized void show(){
		if(trackid == -1)
			return;
		Toast.makeText(context, "Distance: "+ (float)track.getDistance() +" m", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * startet (erneut) das GPS-Tracking
	 */
	public synchronized void start() {
		if(trackid == -1 && !isRunning){
			setTrack(getNextTrackID());
			Toast.makeText(context, "start Track "+trackid, Toast.LENGTH_SHORT).show();
		}
		
		if(trackid == -1 || isRunning)
			return;
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 0, listener);
		isRunning = true;
		Toast.makeText(context, "GPS started", Toast.LENGTH_SHORT).show();
	}
	/**
	 * pausiert das GPS-Tracking
	 */
	public synchronized void pause() {
		if(trackid == -1 || !isRunning)
			return;
		locationManager.removeUpdates(listener);
		database.close();
		isRunning = false;
		Toast.makeText(context, "GPS paused", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * stopt GPS-Tracking, schreibt alle Daten über einen Track in die Datenbank
	 */
	public synchronized void stop(){
		if(trackid == -1)
			return;
		isRunning = false;
		locationManager.removeUpdates(listener);
		
		if(!database.isOpen())
			database = dbManager.getWritableDatabase();
		track.writeToDatabase(context, database);
		track = null;
		trackid = -1;
	}
	

	public synchronized void deleteCurrentTrack() {
		if(trackid == -1)
			return;
		
		if(!database.isOpen())
			database = dbManager.getWritableDatabase();
		
		database.execSQL(context.getResources().getString(R.string.db_delete_location)+trackid);
		
		track = new Track(trackid);
		Toast.makeText(context, "reset Track "+trackid, Toast.LENGTH_SHORT).show();
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	private int getNextTrackID(){
		String sql =  context.getResources().getString(R.string.db_select_track_maxid);
		Cursor cursor = database.rawQuery(sql, null);
		
		int result = -1;
		if(cursor.moveToFirst()){
			result = cursor.getInt(0)+1;
		}
		cursor.close();
		
		return result;
	}
	
 	private class LocationListener implements android.location.LocationListener{

 		public synchronized void onLocationChanged(Location location) {
 			if(trackid == -1)
 				return;
 			
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
// 			Toast.makeText(context, "insert SQL", Toast.LENGTH_SHORT).show();
 		}

 		public synchronized void onProviderDisabled(String provider) {

 		}

 		public synchronized void onProviderEnabled(String provider) {

 		}

 		public synchronized void onStatusChanged(String provider, int status, Bundle extras) {

 		}
 		
 	}
}
