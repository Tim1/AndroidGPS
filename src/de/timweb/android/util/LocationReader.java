package de.timweb.android.util;

import java.util.ArrayList;

import de.timweb.android.activity.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * Zum Lesen der Locations aus der Datenbank (statische Klasse)
 * @author Tim
 *
 */
public final class LocationReader {

	/**
	 * debugging
	 */
	public static void readAll(Context  context){
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase database = dbManager.getReadableDatabase();
		
		String sql = "select longitude from gps_location";
		Cursor result = database.rawQuery(sql, null);
		
		String s = "";
		while(result.moveToNext()){
			s += result.getDouble(0)+"\n";
		}
		result.close();
		database.close();
		
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}
	
	public static ArrayList<Location> getLocations(Context context, int trackid){
		ArrayList<Location> result = new ArrayList<Location>();
		DatabaseManager dbManager = new DatabaseManager(context);
		SQLiteDatabase database = dbManager.getReadableDatabase();
		
		String sql = context.getResources().getString(R.string.db_select_location) + trackid;
		Cursor cursor = database.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			Location location = new Location(LocationManager.GPS_PROVIDER);
			location.setTime(cursor.getLong(0));
			location.setAccuracy(cursor.getFloat(1));
			location.setLongitude(cursor.getDouble(2));
			location.setLatitude(cursor.getDouble(3));
			location.setAltitude(cursor.getDouble(4));
			location.setSpeed(cursor.getFloat(5));
			
			result.add(location);
		}
		cursor.close();
		database.close();
		
		return result;
	}
}
