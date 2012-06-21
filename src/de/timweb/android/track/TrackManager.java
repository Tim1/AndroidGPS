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
import android.view.Gravity;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import de.timweb.android.R;
import de.timweb.android.util.DatabaseManager;
import de.timweb.android.util.LocationReader;
import de.timweb.android.util.LocationReader.LocationAndSteps;

public class TrackManager {
	private LocationListenerImpl mLocationListener;
	public SensorEventListenerImpl mSensorListener;
	private LocationManager mLocationManager;
	private SensorManager mSensorManager;
	private SQLiteDatabase mDatabase;
	private SQLiteStatement sql;
	private static Context context;

	private boolean isRunning = false;
	private boolean isSaved = true;
	private int trackid = -1;
	private Track track;
	private DatabaseManager dbManager;

	private int steps;

	public TrackManager() {
		dbManager = new DatabaseManager(context);
		mDatabase = dbManager.getWritableDatabase();
		sql = mDatabase.compileStatement(context.getResources().getString(
				R.string.db_insert_location));

		mLocationListener = new LocationListenerImpl();
		mLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		mSensorListener = new SensorEventListenerImpl();
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

	}

	private synchronized void setTrack(int trackid, int modus) {
		if (isRunning)
			throw new IllegalStateException("Another Track(" + this.trackid
					+ ") is still running!");

		this.trackid = trackid;
		track = new Track(trackid, modus);
	}

	/**
	 * startet (erneut) das GPS-Tracking. Legt einen neuen Track an, wenn es
	 * noch nicht laeuft.
	 * 
	 * @param modus
	 *            Modus des Tracks (Track.MODE_JOGGING etc.)
	 */
	public synchronized void start(int modus) {
		boolean newtrack = false;
		isSaved = false;
		if (trackid == -1 && !isRunning) {
			setTrack(getNextTrackID(), modus);
			Toast toast = Toast.makeText(context, context.getResources()
					.getString(R.string.toast_track_record_start) + trackid,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			newtrack = true;
		}
		if (trackid == -1 || isRunning)
			return;

		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, 0, mLocationListener);
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

		isRunning = true;
		if (!newtrack) {
			Toast toast = Toast.makeText(context, context.getResources()
					.getString(R.string.toast_gps_start), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			track.continueTrack();
		}
	}

	/**
	 * pausiert das GPS-Tracking
	 */
	public synchronized void pause() {
		if (trackid == -1 || !isRunning)
			return;
		mLocationManager.removeUpdates(mLocationListener);
		mSensorManager.unregisterListener(mSensorListener);
		isRunning = false;
		Toast toast = Toast.makeText(context,
				context.getResources().getString(R.string.toast_gps_pause),
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
		track.pause();
	}

	/**
	 * stopt GPS-Tracking, schreibt alle Daten über einen Track in die Datenbank
	 */
	public synchronized void stop() {

		if (trackid == -1)
			return;
		isRunning = false;
		mSensorManager.unregisterListener(mSensorListener);

		if (!mDatabase.isOpen())
			mDatabase = dbManager.getWritableDatabase();
		track.writeToDatabase(context, mDatabase);
		isSaved = true;
		track = null;
		trackid = -1;
	}

	@Deprecated
	/**
	 * nur noch für debugginggründe
	 */
	public synchronized void deleteCurrentTrack() {
		if (trackid == -1)
			return;

		if (!mDatabase.isOpen())
			mDatabase = dbManager.getWritableDatabase();

		mDatabase.execSQL(context.getResources().getString(
				R.string.db_delete_location)
				+ trackid);

		track = new Track(trackid, Track.MODE_JOGGING);
		Toast.makeText(context, "reset Track " + trackid, Toast.LENGTH_SHORT)
				.show();
	}

	public boolean isSaved() {
		return isSaved;
	}

	private int getNextTrackID() {
		String sql = context.getResources().getString(
				R.string.db_select_track_maxid);
		if (!mDatabase.isOpen())
			mDatabase = dbManager.getWritableDatabase();
		Cursor cursor = mDatabase.rawQuery(sql, null);
		int result = -1;
		if (cursor.moveToFirst()) {
			result = cursor.getInt(0) + 1;
		}
		cursor.close();
		return result;
	}

	public class SensorEventListenerImpl implements SensorEventListener {
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

			gravity = alpha * gravity + (1 - alpha) * event.values[1];
			accl = event.values[1] - gravity;

			if (accl > SCHWELLE) {
				if (accl > max)
					max = accl;
			} else {
				if (max > 0) {
					steps++;
					if (track == null || track.isPaused())
						return;
					track.addStep();
				}
				max = 0;
			}
		}

		@Deprecated
		public String getAccl() {
			return accl + "";
		}

		@Deprecated
		public String getSteps() {
			return steps + "";
		}
	}

	private class LocationListenerImpl implements LocationListener {

		public synchronized void onLocationChanged(Location location) {
			if (trackid == -1 || track == null || track.isPaused())
				return;

			if (!mDatabase.isOpen()) {
				mDatabase = dbManager.getWritableDatabase();
			}

			track.addLocation(location, sql);
		}

		public synchronized void onProviderDisabled(String provider) {

		}

		public synchronized void onProviderEnabled(String provider) {

		}

		public synchronized void onStatusChanged(String provider, int status,
				Bundle extras) {

		}

	}

	public Track getTrack() {
		return track;
	}

	@Override
	protected void finalize() throws Throwable {
		// Datenbank wird erst dann geschlossen, wenn das Objekt nicht mehr
		// besteht
		mDatabase.close();

		super.finalize();
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
		String sql = context.getString(R.string.db_select_track) + id;

		Cursor cursor = mDatabase.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			result = new Track(cursor.getInt(0), cursor.getLong(1),
					cursor.getLong(2), cursor.getInt(3));
		}

		cursor.close();
		mDatabase.close();

		ArrayList<LocationAndSteps> loc = LocationReader.getLocations(context,
				id);

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
					.getFloat(2), cursor.getLong(3), cursor.getInt(4), cursor
					.getInt(5)));
		}

		cursor.close();
		mDatabase.close();
		return result;
	}

	public static void setContext(Context context) {
		TrackManager.context = context;
	}

	public static void deleteTrack(int trackid) {
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();
		SQLiteStatement sql = mDatabase.compileStatement(context.getResources()
				.getString(R.string.db_delete_track));
		sql.execute();

		// delte from gps_location too
		mDatabase.execSQL(context.getResources().getString(
				R.string.db_delete_location)
				+ trackid);
		mDatabase.close();

		Toast toast = Toast.makeText(context,
				context.getResources().getString(R.string.toast_track_deleted)
						+ trackid, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public static void insertRatingAndNote(int trackid, float rating,
			String note) {

		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();

		SQLiteStatement sql = mDatabase.compileStatement(context.getResources()
				.getString(R.string.db_update_rating_and_note));

		sql.bindDouble(1, rating);
		sql.bindString(2, note);
		sql.bindLong(3, trackid);
		sql.execute();

		mDatabase.close();
	}

	public static void selectRatingAndNote(int trackid, RatingBar rb,
			TextView tv_note) {
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();

		Cursor cursor = mDatabase.rawQuery(
				context.getResources().getString(
						R.string.db_select_rating_and_note)
						+ trackid, null);
		while (cursor.moveToNext()) {
			rb.setRating((float) cursor.getDouble(0));
			tv_note.setText(cursor.getString(1));
		}
		cursor.close();
		mDatabase.close();
	}
}
