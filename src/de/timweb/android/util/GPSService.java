package de.timweb.android.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import de.timweb.android.activity.R;

public class GPSService extends Service implements LocationListener{
	
	private IBinder binder = new Binder() {
	};
	private LocationManager locationManager;
	private SQLiteDatabase database;
	private SQLiteStatement sql;
	private int trackid;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		locationManager.removeUpdates(this);
	}

	/**
	 * trackid in intent übergeben putExtra("trackid",id);
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		trackid = intent.getExtras().getInt("trackid");
		DatabaseManager dbManager = new DatabaseManager(this);
		database = dbManager.getWritableDatabase();
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);

		sql = database.compileStatement(getResources().getString(R.string.db_insert_location));
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onLocationChanged(Location location) {
		sql.clearBindings();

		sql.bindLong(1, trackid);
		sql.bindLong(2, location.getTime());
		sql.bindDouble(3, location.getAccuracy());
		sql.bindDouble(4, location.getLongitude());
		sql.bindDouble(5, location.getLatitude());
		sql.bindDouble(6, location.getAccuracy());
		sql.bindDouble(7, location.getSpeed());
		
		sql.executeInsert();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
