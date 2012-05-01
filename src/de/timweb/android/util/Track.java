package de.timweb.android.util;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;

/**
 * repraesentiert eine komplett zurueckgelegte Strecke
 * @author Tim
 *
 */
public class Track {
	private int _id;
	private int steps;
	private double distance;
	private long starttime;
	private double altitudeDiff;

	ArrayList<Location> locations = new ArrayList<Location>();
	
	public Track(int id) {
		this._id = id;
	}
	
	public void updateTrack(Context context){
		ArrayList<Location> locations = LocationReader.getLocations(context, _id);
		
		//zurueckgelegte Strecke berechnen
		distance = 0;
		if(locations.size() > 1){
			Location first = locations.get(0);
			for(Location second :locations ){
				distance += second.distanceTo(first);
				first = second;
			}
		}
	}
	
	public void addLocation(Location location) {
		
		if(locations.size() > 0){
			distance += locations.get(locations.size()-1).distanceTo(location);
		}else{
			starttime = location.getTime();
		}
		
		locations.add(location);
	}
	
	/**
	 * @return distance of track in meter
	 */
	public double getDistance() {
		return distance;
	}
	public long getStarttime() {
		return starttime;
	}
	/**
	 * @return time in ms
	 */
	public long getTime() {
		return System.currentTimeMillis()-starttime;
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

}
