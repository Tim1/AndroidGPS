package de.timweb.android.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Location;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import de.timweb.android.activity.stats.GoogleMapActivity;

public class MyOverlay extends Overlay {
	private ArrayList<Location> mLocations;
	private GeoPoint[] gpArray;
	private Point[] pointArray;
	private Path[] pathArray;
	private Paint mPaint;
	private GeoPoint firstGeoPoint,secGeoPoint;
	private Point firstPoint, secPoint;
	private int latitude;
	private int longitude;
	private Projection mProjection;
	private Context mcontext;

	private int step = 1;
	
	public MyOverlay(ArrayList<Location> locations, Projection projection,Context context) {
		mLocations = locations;
		mProjection = projection;
		mcontext = context;
		gpArray = new GeoPoint[mLocations.size()];
		for (int i = 0; i < gpArray.length; i++) {
			latitude = (int) (mLocations.get(i).getLatitude() * 1e6);
			longitude = (int) (mLocations.get(i).getLongitude() * 1e6);
			gpArray[i] = new GeoPoint(latitude, longitude);
		}

		pointArray = new Point[gpArray.length];
		for (int i = 0; i < pointArray.length; i++)
			pointArray[i] = new Point();

		// x points; x-1 paths
		pathArray = new Path[gpArray.length - 1];
		for (int i = 0; i < pathArray.length; i++)
			pathArray[i] = new Path();

		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(4);

	}
    
	@Override
	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		super.draw(canvas, mapv, shadow);
		drawPointsByStep(canvas, step);
	}

	private void drawPointsByStep(Canvas canvas, int step) {
		for (Path path: pathArray)
			path.reset(); // oder rewind(); ?

		firstPoint = pointArray[0];
		firstGeoPoint = gpArray[0];
		mProjection.toPixels(firstGeoPoint, firstPoint);
		int firststep = step;

		for (int i = 0; step < pointArray.length; i++) {
			secGeoPoint = gpArray[step];
			secPoint = pointArray[step];
			
			mProjection.toPixels(secGeoPoint,secPoint);
			pathArray[i].moveTo(firstPoint.x, firstPoint.y);
			pathArray[i].lineTo(secPoint.x, secPoint.y);

			canvas.drawPath(pathArray[i], mPaint);

			firstPoint = secPoint;
			firstGeoPoint = secGeoPoint;

			step += firststep;
		}

		// draw till last point
		mProjection.toPixels(gpArray[gpArray.length - 1],pointArray[gpArray.length - 1]);
		pathArray[pathArray.length - 1].moveTo(firstPoint.x, firstPoint.y);
		pathArray[pathArray.length - 1].lineTo(pointArray[pointArray.length - 1].x,pointArray[pointArray.length - 1].y);
		canvas.drawPath(pathArray[pathArray.length - 1], mPaint);

	}
	
	public void setStep(int step){
		
		this.step = new Integer(step);
	}

}