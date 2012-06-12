package de.timweb.android.track;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.widget.Toast;
import de.timweb.android.activity.R;
import de.timweb.android.util.LocationReader.LocationAndSteps;

/**
 * repraesentiert eine komplett zurueckgelegte Strecke
 */
public class Track {
	public static final int MODE_JOGGING = 0;
	public static final int MODE_BYCYCLE = 1;
	public static final int MODE_CAR = 2;
	public static final int MODE_ALL = 3;

	private int _id;
	private int modus;
	private int steps;
	private int stepsOld;
	private double distance;
	private long starttime;
	private long elapsedTime;
	private double currentSpeed;
	private long pauseTime;
	private Statistics stats = new Statistics();

	ArrayList<Location> locations = new ArrayList<Location>();

	/**
	 * Konstruktor fuer "LiteTrack" (keine Statistiken erstellen etc.)
	 * 
	 * @param id
	 * @param date
	 *            Starttime des Tracks
	 * @param distance
	 *            in m
	 * @param time 
	 */
	Track(int id,long date, float distance,long time,int modus, int steps){
		this._id = id;
		this.starttime = date;
		this.distance = distance;
		this.elapsedTime = time;
		this.modus = modus;
		this.steps = steps;
	}
	
	Track(int id, int modus) {
		this._id = id;
		this.modus = modus;
		starttime = System.currentTimeMillis();
	}

	public void addLocation(Location location, SQLiteStatement sql) {

		float distanceDelta = 0;
		currentSpeed = location.getSpeed();
		if (locations.size() > 0) {
			float distanceToLast = locations.get(locations.size() - 1).distanceTo(
					location);
			// sehr kleine Entfernungen nicht beruecksichtigen
			if (distanceToLast < location.getAccuracy())
				return;

			distanceDelta = locations.get(locations.size() - 1)
			.distanceTo(location);
			distance += distanceDelta;
		} else {
			// starttime = System.currentTimeMillis();
		}

		int stepsDiff;
		if(location instanceof LocationAndSteps){
			stepsDiff = ((LocationAndSteps) location).getSteps();
		}else{
			
			//FIXME: Debug Schritte simulieren
			steps += (Math.random()*10);
			
			stepsDiff = steps - stepsOld; 
			stepsOld = steps;
		}
		
		stats.addStepDistance(stepsDiff, distanceDelta);
		stats.addSpeed(location.getSpeed()*3.6f);
		stats.addAltitude((float) location.getAltitude());
		stats.addDistance(distanceDelta);

		locations.add(location);

		if (sql == null)
			return;

		sql.clearBindings();

		sql.bindLong(1, _id);
		sql.bindLong(2, location.getTime());
		sql.bindDouble(3, location.getAccuracy());
		sql.bindDouble(4, location.getLongitude());
		sql.bindDouble(5, location.getLatitude());
		sql.bindDouble(6, location.getAltitude());
		sql.bindDouble(7, location.getSpeed());
		sql.bindLong(8, stepsDiff);

		sql.executeInsert();

	}

	public void writeToDatabase(Context context, SQLiteDatabase database) {

		SQLiteStatement sql = database.compileStatement(context.getResources()
				.getString(R.string.db_insert_track));

		sql.bindLong(1, _id);
		sql.bindLong(2, starttime);
		sql.bindDouble(3, distance);
		sql.bindLong(4, System.currentTimeMillis() - starttime - pauseTime);
		sql.bindLong(5, modus);
		sql.bindLong(6, steps);
		sql.bindDouble(7, 0);
		sql.bindString(8, "");

		sql.executeInsert();

		Toast.makeText(context, R.string.toast_track_wrote, Toast.LENGTH_SHORT).show();

	}
	

	/**
	 * @return distance of track in meter
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * @return zurueckgelegte Distanz (Format: "12.345 km")
	 */
	public String getFormatedDistance(){
		NumberFormat format = new DecimalFormat("#0.000");
		
		return format.format(distance/1000)+" km";
	}
	
	/**
	 * @return momentane Geschwinigkeit (Format: "12.3 km/h")
	 */
	public String getFormatedSpeed(){
		NumberFormat format = new DecimalFormat("#0.0");
		
		return format.format(currentSpeed*3.6)+ " km/h";
	}
	
	public int getID() {
		return _id;
	}

	/**
	 * 
	 * @return aktuelle geschwindigkeit in km/h
	 */
	public double getCurrentSpeed() {
		return currentSpeed*3.6;
	}

	public String getElapsedTime() {
		long secTotal = 0;		
		if(elapsedTime == 0)
			secTotal = (System.currentTimeMillis() - starttime - pauseTime)/1000;
		else
			secTotal = (elapsedTime - pauseTime)/1000;
		
		long hours = secTotal / (60*60);
		long mins = (secTotal-(hours*60*60)) / 60;
		long secs = (secTotal-(hours*60*60) - (mins*60));

		String strHour = hours < 10 ? "0"+hours : ""+hours;
		String strMins = mins < 10 ? "0"+mins : ""+mins;
		String strSecs = secs < 10 ? "0"+secs : ""+secs;
		
		return strHour+":"+strMins+":"+strSecs;
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
		result += (cal.get(Calendar.MONTH)+1) + ".";
		result += cal.get(Calendar.YEAR) + " ";

		result += cal.get(Calendar.HOUR_OF_DAY) + ":";
		int min = cal.get(Calendar.MINUTE);
		result += min > 9 ? min :"0"+min;

		return result;
	}

	public int getModus() {
		return modus;
	}

	public Statistics getStatistics() {
		return stats;
	}

	/**
	 * 
	 * @param time Dauer der Pause in ms 
	 */
	public void addPauseTime(long time) {
		pauseTime += time;
	}
	
}
