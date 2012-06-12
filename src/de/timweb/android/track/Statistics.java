package de.timweb.android.track;

import java.util.ArrayList;

/**
 * enthaelt Statistiken (Geschwindigkeit, Höhenunterschied,Schrittweite), welche
 * in Echtzeit erzeugt und abgerufen werden koennen
 * 
 * @author Tim
 * 
 */
public class Statistics {
	private ArrayList<Float> distances = new ArrayList<Float>();
	private ArrayList<Float> speeds = new ArrayList<Float>();
	private ArrayList<Float> height = new ArrayList<Float>();
	private ArrayList<Float> stepsToDist = new ArrayList<Float>();

	public void addSpeed(float speed) {
		speeds.add(speed);
	}

	public void addAltitude(float altitude) {
		height.add(altitude);
	}

	public void addStepDistance(int stepsDiff, float distanceToLast) {
		if (stepsDiff <= 0)
			return;

		stepsToDist.add((float) (distanceToLast / stepsDiff));
	}

	public ArrayList<Float> getSpeedValues() {
		return speeds;
	}

	public ArrayList<Float> getHeightValues() {
		return height;
	}

	public ArrayList<Float> getDistanceValues() {
		return distances;
	}
	
	public ArrayList<Float> getStepValues() {
		return stepsToDist;
	}
	
	public void addDistance(float distanceToLast) {
		distances.add(distanceToLast);
	}
}
