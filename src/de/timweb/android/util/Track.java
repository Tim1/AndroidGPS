package de.timweb.android.util;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.widget.Toast;
import de.timweb.android.activity.ChooseTrackActivity;
import de.timweb.android.activity.R;

/**
 * repraesentiert eine komplett zurueckgelegte Strecke
 */
public class Track {
	public static final int MODE_JOGGING = 0;
	public static final int MODE_BYCYCLE = 1;
	public static final int MODE_WALK = 2;

	private int _id;
	private int date;
	private double distance;
	private int time;
	private int mode;
	private int steps;
	private long starttime;
	private double altitudeDiff;

	ArrayList<Location> locations = new ArrayList<Location>();

	public Track(int id, int date, double distance, int time, int mode,
			int steps) {
		this._id = id;
		this.date = date;
		this.distance = distance;
		this.time = time;
		this.mode = mode;
		this.steps = steps;

	}

	public Track(int id) {
		this._id = id;
	}

	public void updateTrack(Context context) {
		ArrayList<Location> locations = LocationReader.getLocations(context,
				_id);

		// zurueckgelegte Strecke berechnen
		distance = 0;
		if (locations.size() > 1) {
			Location first = locations.get(0);
			for (Location second : locations) {
				distance += second.distanceTo(first);
				first = second;
			}
		}
	}

	public void addLocation(Location location, SQLiteStatement sql) {

		if (locations.size() > 0) {
			float distanceToLast = locations.get(locations.size() - 1)
					.distanceTo(location);
			if (distanceToLast < location.getAccuracy())// sehr kleine
														// Entfernungen nicht
														// berücksichtigen
				return;

			distance += locations.get(locations.size() - 1)
					.distanceTo(location);
		} else {
			starttime = System.currentTimeMillis();
		}

		sql.clearBindings();

		sql.bindLong(1, _id);
		sql.bindLong(2, location.getTime());
		sql.bindDouble(3, location.getAccuracy());
		sql.bindDouble(4, location.getLongitude());
		sql.bindDouble(5, location.getLatitude());
		sql.bindDouble(6, location.getAccuracy());
		sql.bindDouble(7, location.getSpeed());

		sql.executeInsert();

		locations.add(location);
	}

	public void writeToDatabase(Context context, SQLiteDatabase database) {

		SQLiteStatement sql = database.compileStatement(context.getResources()
				.getString(R.string.db_insert_track));

		sql.bindLong(1, _id);
		sql.bindLong(2, starttime);
		sql.bindDouble(3, distance);
		sql.bindLong(4, System.currentTimeMillis() - starttime);
		sql.bindLong(5, mode);
		sql.bindLong(6, steps);

		sql.executeInsert();

		Toast.makeText(context, "wrote Track: "+_id, Toast.LENGTH_SHORT).show();
	}

	//noch nicht engesetzt, sql nicht definiert !
	//warum nicht in trackmanager ?
	public static ArrayList<Track> getLiteTrackArray(Context context,
			int modusfilter, int max) {
		ArrayList<Track> result = new ArrayList<Track>();

		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();
		String sql = context.getString(R.string.db_select_alltrack);

		Cursor cursor = mDatabase.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			result.add(new Track(cursor.getInt(0), cursor.getInt(1), cursor
					.getFloat(2), cursor.getInt(3), cursor.getInt(4),
					cursor.getInt(5)));
		}

		cursor.close();
		mDatabase.close();
		return result;
	}
	
	public static void deleteTrack(Context context,int id){
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase mDatabase = dbManager.getWritableDatabase();
		SQLiteStatement sql = mDatabase.compileStatement(context.getResources().getString(R.string.db_delte_track)+id);
		sql.execute();
	}

	/**
	 * @return distance of track in meter
	 */
	public double getDistance() {
		return Math.round(distance);
	}

	public long getStarttime() {
		return starttime;
	}

	/**
	 * @return time in ms
	 */
	public long getTime() {
		return System.currentTimeMillis() - starttime;
	}

	/**
	 * @return Hoehenunterschied in m
	 */
	public double getAltitudeDiff() {
		return altitudeDiff;
	}

	/**
	 * 
	 * @return Schrittanzahl
	 */
	public int getSteps() {
		return steps;
	}

	public void addStep() {
		steps++;
	}

	public String getDate() {
		String result = "";

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(starttime);

		result += cal.get(Calendar.DAY_OF_MONTH) + ".";
		result += cal.get(Calendar.MONTH) + ".";
		result += cal.get(Calendar.YEAR) + " ";

		// result += cal.get(Calendar.HOUR_OF_DAY)+":";
		// result += cal.get(Calendar.MINUTE);

		return result;
	}

	public int getModus() {
		return mode;
	}

	public int getID() {
		return _id;
	}
}
