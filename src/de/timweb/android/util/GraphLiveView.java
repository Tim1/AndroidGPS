package de.timweb.android.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GraphLiveView extends View {
	private Paint mPaint = new Paint();
	private String text = "";

	private float minValue = 0;
	private float maxValue = 0;
	private ArrayList<Float> values = new ArrayList<Float>();
	@SuppressWarnings("unused")
	private Context context;

	private boolean isAnimated;
	private long lastDraw;

	public GraphLiveView(Context context, AttributeSet att) {
		super(context, att);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	public GraphLiveView(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int height = this.getHeight();
		int width = this.getWidth();

		canvas.drawColor(0xFF444444);
		drawLines(canvas, width, height);

		mPaint.setColor(Color.YELLOW);
		float step = (float) width / values.size();

		for (int i = 0; i < values.size() - 1; i++) {
			float x1 = i * step;
			float x2 = (i + 1) * step;
			float y1 = height
					- ((values.get(i) - minValue) / (maxValue - minValue) * height);
			float y2 = height
					- ((values.get(i + 1) - minValue) / (maxValue - minValue) * height);

			canvas.drawLine(x1, y1, x2, y2, mPaint);
		}

		if (isAnimated)
			drawAnimation(canvas, width, height);

		drawScale(canvas, width, height);

	}

	private void drawAnimation(Canvas canvas, int width, int height) {
		@SuppressWarnings("unused")
		int delta = (int) (System.currentTimeMillis() - lastDraw);

		lastDraw = System.currentTimeMillis();
	}

	private void drawLines(Canvas canvas, int width, int height) {
		mPaint.setColor(0xAAFFFFFF);

		canvas.drawLine(0, height / 4f, width, height / 4f, mPaint);
		canvas.drawLine(0, height / 2f, width, height / 2f, mPaint);
		canvas.drawLine(0, height / 4f * 3, width, height / 4f * 3, mPaint);
	}

	private void drawScale(Canvas canvas, int width, int height) {
		mPaint.setColor(Color.WHITE);

		float step = (maxValue - minValue) / 4f;

		String pattern = "";
		if (maxValue > 5)
			pattern = "#0.0";
		else
			pattern = "#0.00";

		NumberFormat format = new DecimalFormat(pattern);
		canvas.drawText(format.format((3 * step + minValue)), 1, height / 4f,
				mPaint);
		canvas.drawText(format.format((2 * step + minValue)), 1, height / 2f,
				mPaint);
		canvas.drawText(format.format((1 * step + minValue)), 1,
				height / 4f * 3, mPaint);

		canvas.drawText(text, 1, height - 4, mPaint);
	}

	public void setValueArrayList(ArrayList<Float> values) {
		this.values = values;

		for (float f : values) {
			if (f > maxValue)
				maxValue = 1.2f * f;
		}
	}

	public void addValue(float value) {
		if (value > maxValue)
			maxValue = 1.2f * value;

		values.add(value);
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setAnimated(boolean animated) {
		this.isAnimated = animated;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}