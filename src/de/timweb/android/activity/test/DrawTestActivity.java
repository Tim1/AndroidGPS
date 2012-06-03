package de.timweb.android.activity.test;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import de.timweb.android.activity.R;

public class DrawTestActivity extends Activity {
	private GraphView graphview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawtest);

		graphview = (GraphView) findViewById(R.id.graphview);
		graphview.setContext(this);
		
		TimerTask timer = new TimerTask() {
			
			@Override
			public void run() {
				graphview.addValue((float) (Math.random()*20));
				
				graphview.postInvalidate();
			}
		};
		Timer t = new Timer();
		t.schedule(timer, 1000, 200);
	}

	public static class GraphView extends View {
		private Context context;
		private Paint mPaint = new Paint();
		private Canvas mCanvas = new Canvas();
		private Bitmap mBitmap;
		
		private float maxValue = 20;
		private ArrayList<Float> values = new ArrayList<Float>();

		public GraphView(Context context, AttributeSet att) {
			super(context, att);
			mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		}

		public GraphView(Context context) {
			this(context, null);
		}

		public void setContext(Context context) {
			this.context = context;
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBitmap);
			mCanvas.drawColor(0xFF222222);
			
			super.onSizeChanged(w, h, oldw, oldh);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if(mBitmap == null)
				return;
			
			int height = this.getHeight();
			int width = this.getWidth();

			canvas.drawColor(0xFF1A1A1A);
			drawLines(canvas,width,height);

			mPaint.setColor(Color.YELLOW);
			float step = (float)width / values.size();
			
			for(int i=0;i<values.size()-1;i++){
				float x1 = i*step;
				float x2 = (i+1)*step;
				float y1 = values.get(i)/maxValue * height;
				float y2 = values.get(i+1)/maxValue * height;
				
				canvas.drawLine(x1,y1,x2,y2, mPaint);
			}
			
			drawScale(canvas,width,height);
			
//			canvas.drawBitmap(mBitmap, 0, 0,null);
		}
		
		private void drawLines(Canvas canvas, int width, int height) {
			mPaint.setColor(0x66FFFFFF);
			
			canvas.drawLine(0, height/4f, width, height/4f, mPaint);
			canvas.drawLine(0, height/2f, width, height/2f, mPaint);
			canvas.drawLine(0, height/4f*3, width, height/4f*3, mPaint);
		}

		private void drawScale(Canvas canvas, int width, int height) {
			mPaint.setColor(Color.WHITE);
			
			canvas.drawText("" +maxValue, 1, 0, mPaint);
			canvas.drawText("" +maxValue/4*3, 1, height/4f, mPaint);
			canvas.drawText("" +maxValue/2, 1, height/2f, mPaint);
			canvas.drawText("" +maxValue/4, 1, height/4f*3, mPaint);
		}

		public void addValue(float value){
			values.add(value);
		}
	}
}
