package de.timweb.android.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore.Images;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import de.timweb.android.R;
import de.timweb.android.util.LocationReader.LocationAndSteps;

public class MyOverlay extends Overlay {
	private ArrayList<LocationAndSteps> mLocations;
	private GeoPoint[] gpArray;
	private Point[] pointArray;
	private Path[] pathArray;
	private Paint mPaint;
	private GeoPoint firstGeoPoint, secGeoPoint;
	private Point firstPoint, secPoint;
	private int latitude;
	private int longitude;
	private Projection mProjection;
	private Context mcontext;

	private int step = 1;

	public MyOverlay(ArrayList<LocationAndSteps> locations,
			Projection projection, Context context) {
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
		mPaint.setTextSize(30);

	}

	@Override
	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		super.draw(canvas, mapv, shadow);
		drawPointsByStep(canvas, step);
	}

	private void drawPointsByStep(Canvas canvas, int step) {
		for (Path path : pathArray)
			path.reset();

		firstPoint = pointArray[0];
		firstGeoPoint = gpArray[0];
		mProjection.toPixels(firstGeoPoint, firstPoint);
//		canvas.drawText(mcontext.getString(R.string.draw_start), firstPoint.x,
//				firstPoint.y, mPaint);
		
		
		int firststep = step;
		for (int i = 0; step < pointArray.length; i++) {
			secGeoPoint = gpArray[step];
			secPoint = pointArray[step];

			mProjection.toPixels(secGeoPoint, secPoint);
			pathArray[i].moveTo(firstPoint.x, firstPoint.y);
			pathArray[i].lineTo(secPoint.x, secPoint.y);

			canvas.drawPath(pathArray[i], mPaint);

			firstPoint = secPoint;
			firstGeoPoint = secGeoPoint;

			step += firststep;
		}

		// draw till last point
		mProjection.toPixels(gpArray[gpArray.length - 1],
				pointArray[gpArray.length - 1]);
		pathArray[pathArray.length - 1].moveTo(firstPoint.x, firstPoint.y);
		pathArray[pathArray.length - 1].lineTo(
				pointArray[pointArray.length - 1].x,
				pointArray[pointArray.length - 1].y);
		canvas.drawPath(pathArray[pathArray.length - 1], mPaint);
		
		Drawable d = mcontext.getResources().getDrawable(R.drawable.ic_pin_start);
		Bitmap bmp = ((BitmapDrawable)d).getBitmap();
		canvas.drawBitmap(bmp,pointArray[0].x-20,pointArray[0].y-65, mPaint);
		
		d = mcontext.getResources().getDrawable(R.drawable.ic_pin_ende);
		bmp = ((BitmapDrawable)d).getBitmap();
		canvas.drawBitmap(bmp, pointArray[pointArray.length - 1].x-20,pointArray[pointArray.length - 1].y-65, mPaint);

	}

	public void setStep(int step) {
		this.step = step;
	}

}