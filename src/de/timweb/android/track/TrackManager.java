package de.timweb.android.track;

import java.util.ArrayList;

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
import de.timweb.android.util.DatabaseManager;
import de.timweb.android.util.LocationReader;

public class TrackManager {
	private LocationListenerImpl mLocationListener;
	public SensorEventListenerImpl mSensorListener; 
	private LocationManager mLocationManager;
	private SensorManager mSensorManager;
	private SQLiteDatabase mDatabase;
	private SQLiteStatement sql;
	private static Context context;
	
	private boolean isRunning = false;
	private int trackid = -1;
	private Track track;
	private DatabaseManager dbManager;
	
	private int steps;
		
	
	public TrackManager() {
//		this.context = context;
		

		dbManager = new DatabaseManager(context);
		mDatabase = dbManager.getWritableDatabase();
		sql = mDatabase.compileStatement(context.getResources().getString(
				R.string.db_insert_location));
		
		mLocationListener = new LocationListenerImpl();
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		mSensorListener = new SensorEventListenerImpl();
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		
	}
	
	private synchronized void setTrack(int trackid, int modus){
		if(isRunning)
			throw new IllegalStateException("Another Track("+this.trackid+") is still running!");
		
		this.trackid = trackid;
		track = new Track(trackid,modus);
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
	 * @param modus Modus des Tracks (Track.MODE_JOGGING etc.)
	 */
	public synchronized void start(int modus) {
		if(trackid == -1 && !isRunning){
			setTrack(getNextTrackID(),modus);
			Toast.makeText(context, "start Track "+trackid, Toast.LENGTH_SHORT).show();
		}
		if(trackid == -1 || isRunning)
			return;
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, 0, mLocationListener);
		mSensorManager.registerListener(mSensorListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
		
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
	
	@Deprecated
	/**
	 * nur noch für debugginggründe
	 */
	public synchronized void deleteCurrentTrack() {
		if(trackid == -1)
			return;
		
		if(!mDatabase.isOpen())
			mDatabase = dbManager.getWritableDatabase();
		
		mDatabase.execSQL(context.getResources().getString(R.string.db_delete_location)+trackid);
		
		track = new Track(trackid,Track.MODE_JOGGING);
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
	
	public class SensorEventListenerImpl implements SensorEventListener{
		private static final float SCHWELLE = 3;
		private float max;
		private float alpha = 0.8f;
		private float gravity;
		private float accl;
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;

			
			gravity = alpha  * gravity + (1 - alpha) * event.values[1];
			accl = event.values[1] - gravity;

			if(accl > SCHWELLE){
				if(accl > max)
					max = accl;
			}else{
				if(max > 0){
					steps++;
					track.addStep();
				}
				max = 0;
			}
		}
		
		@Deprecated
		public String getAccl() {
			return accl+"";
		}
		@Deprecated
		public String getSteps() {
			return steps+"";
		}
	}
	
	
 	private class LocationListenerImpl implements LocationListener{

 		public synchronized void onLocationChanged(Location location) {
 			if(trackid == -1)
 				return;
 			track.addLocation(location,sql);
 		}

 		public synchronized void onProviderDisabled(String provider) {

 		}

 		public synchronized void onProviderEnabled(String provider) {

 		}

 		public synchronized void onStatusChanged(String provider, int status, Bundle extras) {

 		}
 		
 	}

	public Track getTrack() {
		return track;
	}
	
	/**
	 * generiert ein Track mit der angegeben ID aus der Datenbank alle
	 * Statstiken werden mitgeneriert
	 * 
	 * @param id
	 *            des Tracks (aus Datenbank)
	 * @return generierter vollwertiger Track (mit allen Statistiken)
	 */
	public static Track generateFromDatabase(Context context, int id) {
		Track result = null;

		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();
		String sql = context.getString(R.string.db_select_track)+id;

		Cursor cursor = mDatabase.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			result = new Track(cursor.getInt(0), cursor.getLong(1), cursor
					.getFloat(2),cursor.getLong(3));
		}

		cursor.close();
		mDatabase.close();
		

		ArrayList<Location> loc = LocationReader.getLocations(context, id);

		for (Location l : loc)
			result.addLocation(l, null);

		return result;
	}

	/**
	 * erstellt leichtgewichtige Trackobjekte aller vorhanden Tracks aus der
	 * Datenbank Statisiken & Co. werden NICHT mitgeneriert und können nicht
	 * verwendet werden
	 * 
	 * @param modusfilter
	 *            Filter um nach veschieden Modi zu sortieren
	 *            (Track.MODE_JOGGING etc.)
	 * @return
	 */
	public static ArrayList<Track> getLiteTrackArray(Context context,
			int modusfilter) {
		ArrayList<Track> result = new ArrayList<Track>();

		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();
		String sql = context.getString(R.string.db_select_alltrack);

		Cursor cursor = mDatabase.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			result.add(new Track(cursor.getInt(0), cursor.getLong(1), cursor
					.getFloat(2),cursor.getLong(4)));
//			Toast.makeText(context, "time: "+cursor.getLong(4), 0).show();
		}

		cursor.close();
		mDatabase.close();
		return result;
	}
	
	public static void setContext(Context context){
		TrackManager.context = context;
	}

	public static void deleteTrack(int trackid) {
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();
		SQLiteStatement sql = mDatabase.compileStatement(context.getResources().getString(R.string.db_delte_track)+trackid);
		sql.execute();
	}
}
