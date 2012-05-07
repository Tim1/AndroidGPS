package de.timweb.android.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import de.timweb.android.activity.R;

public class TrackManager {
	private LocationListenerImpl mLocationListener;
	private SensorEventListener mSensorListener; 
	private LocationManager mLocationManager;
	private SensorManager mSensorManager;
	private SQLiteDatabase mDatabase;
	private SQLiteStatement sql;
	private Context context;
	
	private boolean isRunning = false;
	private int trackid = -1;
	private Track track;
	private DatabaseManager dbManager;
		
	public TrackManager(Context context) {
		this.context = context;
		

		dbManager = new DatabaseManager(context);
		mDatabase = dbManager.getWritableDatabase();
		sql = mDatabase.compileStatement(context.getResources().getString(
				R.string.db_insert_location));
		
		mLocationListener = new LocationListenerImpl();
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		mSensorListener = new SensorEventListenerImpl();
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		
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
		Toast.makeText(context, "Distance: "+ (float)track.getDistance() +" m\nSteps: "+track.getSteps(), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * startet (erneut) das GPS-Tracking.
	 * Legt einen neuen Track an, wenn es noch nicht laeuft.
	 */
	public synchronized void start() {
		if(trackid == -1 && !isRunning){
			setTrack(getNextTrackID());
			Toast.makeText(context, "start Track "+trackid, Toast.LENGTH_SHORT).show();
		}
		if(trackid == -1 || isRunning)
			return;
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, 0, mLocationListener);
		mSensorManager.registerListener(mSensorListener,mSensorManager.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
		
		
		isRunning = true;
		Toast.makeText(context, "GPS started", Toast.LENGTH_SHORT).show();
	}
	/**
	 * pausiert das GPS-Tracking
	 */
	public synchronized void pause() {
		if(trackid == -1 || !isRunning)
			return;
		mLocationManager.removeUpdates(mLocationListener);
		mSensorManager.unregisterListener(mSensorListener);
		mDatabase.close();
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
		mLocationManager.removeUpdates(mLocationListener);
		mSensorManager.unregisterListener(mSensorListener);
		
		if(!mDatabase.isOpen())
			mDatabase = dbManager.getWritableDatabase();
		track.writeToDatabase(context, mDatabase);
		track = null;
		trackid = -1;
	}
	

	public synchronized void deleteCurrentTrack() {
		if(trackid == -1)
			return;
		
		if(!mDatabase.isOpen())
			mDatabase = dbManager.getWritableDatabase();
		
		mDatabase.execSQL(context.getResources().getString(R.string.db_delete_location)+trackid);
		
		track = new Track(trackid);
		Toast.makeText(context, "reset Track "+trackid, Toast.LENGTH_SHORT).show();
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	private int getNextTrackID(){
		String sql =  context.getResources().getString(R.string.db_select_track_maxid);
		Cursor cursor = mDatabase.rawQuery(sql, null);
		
		int result = -1;
		if(cursor.moveToFirst()){
			result = cursor.getInt(0)+1;
		}
		cursor.close();
		
		return result;
	}
	
	private class SensorEventListenerImpl implements SensorEventListener{

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}

		public void onSensorChanged(SensorEvent event) {
			//TODO: platzhalter
			if(Math.random() > 0.9)
				track.addStep();
			
		}
		
	}
	
	
 	private class LocationListenerImpl implements LocationListener{

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
