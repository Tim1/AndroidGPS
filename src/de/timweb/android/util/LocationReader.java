package de.timweb.android.util;

import java.util.ArrayList;

import de.timweb.android.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * Zum Lesen der Locations aus der Datenbank (statische Klasse)
 * 
 * @author Tim
 * 
 */
public final class LocationReader {
	public static class LocationAndSteps extends Location {
		private int steps;

		public LocationAndSteps(String provider) {
			super(provider);
		}

		public void setSteps(int steps) {
			this.steps = steps;
		}

		public int getSteps() {
			return steps;
		}

	}

	/**
	 * debugging
	 */
	public static void readAll(Context context) {
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase database = dbManager.getReadableDatabase();

		String sql = context.getString(R.string.db_select_longitude);
		Cursor result = database.rawQuery(sql, null);

		String s = "";
		while (result.moveToNext()) {
			s += result.getDouble(0) + "\n";
		}
		result.close();
		database.close();

		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}

	public static ArrayList<LocationAndSteps> getLocations(Context context,
			int trackid) {
		ArrayList<LocationAndSteps> result = new ArrayList<LocationAndSteps>();
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase database = dbManager.getReadableDatabase();

		String sql = context.getResources().getString(
				R.string.db_select_location)
				+ trackid;
		Cursor cursor = database.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			LocationAndSteps location = new LocationAndSteps(
					LocationManager.GPS_PROVIDER);
			location.setTime(cursor.getLong(0));
			location.setAccuracy(cursor.getFloat(1));
			location.setLongitude(cursor.getDouble(2));
			location.setLatitude(cursor.getDouble(3));
			location.setAltitude(cursor.getDouble(4));
			location.setSpeed(cursor.getFloat(5));
			location.setSteps(cursor.getInt(6));
			result.add(location);
		}
		cursor.close();
		database.close();

		return result;
	}
}
