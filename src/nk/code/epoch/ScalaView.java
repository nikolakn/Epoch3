package nk.code.epoch;

//import org.joda.time.DateTime;
//import org.joda.time.DateTimeUtils;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import nk.code.data.EpochDatabase;

import nk.code.doc.NkSkala;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * 
 * @author Nikola Scale view
 */
public class ScalaView extends View {

	private static final int INVALID_POINTER_ID = -1;
	private EpochView epochv;
	private Paint mPaint;
	private NkSkala skala;
	private Paint gradPaint;
	private final int g1 = Color.rgb(60, 60, 230);
	private final int g2 = Color.rgb(50, 230, 50);;
	float mRotation = 0f;
	float[] mPoints = { 0.5f, 0f, 0.5f, 1f, 0f, 0.5f, 1f, 0.5f };
	private float mLastTouchY;
	// private float mPosX;
	// private float mPosY;
	private int mActivePointerId = INVALID_POINTER_ID;
	private float scale = 50;
	// private static final String TAG = "Touch";

	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final int DRAW = 3;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;

	public ScalaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Style.FILL);

		skala = new NkSkala();
		skala.SetZoom(getScale());
		gradPaint = new Paint();
		setGrad();

	}
	public double getDy(){
		return skala.getDy();
	}
	public int getZoomLen(){
		return skala.getZoomLen();
	}
	public int getPeriod(){
		return skala.getPeriod();
	}
	
	public double getLen() {
		return skala.getLen();
	}

	public void setColor(int color) {
		mPaint.setColor(color);
	}

	public void setRotation(float degrees) {
		mRotation = degrees;
	}

	public double getZoomLvl(){
		return skala.getZoomLvl();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		setGrad();
		skala.Init(getHeight());

	}

	private void setGrad() {
		gradPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), g1, g2,
				Shader.TileMode.CLAMP));
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Let the ScaleGestureDetector inspect all events.

		final int action = MotionEventCompat.getActionMasked(ev);
		mScaleDetector.onTouchEvent(ev);
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			// final float x = MotionEventCompat.getX(ev, pointerIndex);
			final float y = MotionEventCompat.getY(ev, pointerIndex);

			// Remember where we started (for dragging)
			// mLastTouchX = x;
			mLastTouchY = y;
			// Save the ID of this pointer (for dragging)
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {

			if (mode == ZOOM) {

			} else {

				// Find the index of the active pointer and fetch its position
				final int pointerIndex = MotionEventCompat.findPointerIndex(ev,
						mActivePointerId);

				// final float x = MotionEventCompat.getX(ev, pointerIndex);
				final float y = MotionEventCompat.getY(ev, pointerIndex);

				// Calculate the distance moved
				// final float dx = x - mLastTouchX;
				final float dy = y - mLastTouchY;
				skala.posmak(dy);
				invalidate();
				epochv.invalidate();
				// mLastTouchX = x;
				mLastTouchY = y;

			}

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			mode = NONE;
			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			final int pointerId = MotionEventCompat.getPointerId(ev,
					pointerIndex);

			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				// mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
				mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
				mActivePointerId = MotionEventCompat.getPointerId(ev,
						newPointerIndex);
			}
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(ev);
			// Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, ev);
				mode = ZOOM;
				// Log.d(TAG, "mode=ZOOM");
			}
			break;
		}

		return true;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPaint(gradPaint);
		canvas.save();

		// draw scale
		skala.draw(canvas, getWidth(), getHeight());
		// side line
		canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), mPaint);
		mPaint.setColor(Color.DKGRAY);
		canvas.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight(), mPaint);
		mPaint.setColor(Color.GRAY);
		canvas.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight(), mPaint);

		// side line 2
		mPaint.setColor(Color.BLACK);
		canvas.drawLine(0, 0, 0, getHeight(), mPaint);
		mPaint.setColor(Color.DKGRAY);
		canvas.drawLine(1, 0, 1, getHeight(), mPaint);
		mPaint.setColor(Color.GRAY);
		canvas.drawLine(2, 0, 2, getHeight(), mPaint);
		canvas.restore();
	}

	void adjustTextSize(String mText) {
		int mViewHeight = getWidth();
		mPaint.setTextSize(100);
		mPaint.setTextScaleX(1.0f);
		Rect bounds = new Rect();
		// ask the paint for the bounding rect if it were to draw this
		// text
		mPaint.getTextBounds(mText, 0, mText.length(), bounds);
		// get the height that would have been produced
		int h = bounds.bottom - bounds.top;
		// make the text text up 70% of the height
		float target = (float) mViewHeight * .7f;
		// figure out what textSize setting would create that height
		// of text
		float size = ((target / h) * 100f);
		// and set it into the paint
		mPaint.setTextSize(size);
	}

	// *******************Determine the space between the first two fingers
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	// ************* Calculate the mid point of the first two fingers
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public float getPos(double d) {
		return skala.getPos(d);
	}
	public String getDate(float d) {
		
		return (new DateTime(DateTimeUtils.fromJulianDay(skala.getDate(d)))).toString();
	}
	
	public double getDateDouble(float d) {
		
		return skala.getDate(d);
	}
	
	public void init(EpochView ev) {
		epochv = ev;
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 80.0f));

			// Log.d("nk",Float.toString(mScaleFactor));
			skala.zoom(mScaleFactor, detector.getFocusY());
			
			invalidate();
			epochv.invalidate();
			return true;
		}

	}

	public void setLen(double l) {
		skala.setLen(l);
	}
	public void setDy(Double y) {
		skala.setDy(y);	
	}
	public void setZoomLen(int i) {
		skala.setZoomLen(i);
		
	}
	public void setPeriod(int i) {
		skala.setPeriod(i);	
	}
	public float getScaleFactor() {
		return mScaleFactor;
	}
	public void setScaleFactor(float scale) {
		this.mScaleFactor = scale;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}

	public void serialize(ObjectOutputStream os) throws IOException{	
		os.writeDouble(getDy());
		os.writeDouble(getLen());
		os.writeInt(getZoomLen());
		os.writeInt(getPeriod());
		os.writeFloat(getScaleFactor());
	}
	public void deSerialize(ObjectInputStream is) throws IOException, ClassNotFoundException{
		setDy(is.readDouble());
		setLen(is.readDouble());
		setZoomLen(is.readInt());
		setPeriod(is.readInt());
		setScaleFactor(is.readFloat());
		is.close();      
	}
	
	public void saveToDatabase(SQLiteDatabase database,String title){
		ContentValues values=new ContentValues();
		values.put(EpochDatabase.S_EPOCH, title);
		values.put(EpochDatabase.S_DY, getDy());
		values.put(EpochDatabase.S_LEN, getLen());
		values.put(EpochDatabase.S_ZOOM,getZoomLen());
		values.put(EpochDatabase.S_PERIOD,getPeriod());
		values.put(EpochDatabase.S_SCALE, getScaleFactor());

		database.insert(EpochDatabase.S_TABLE,null,values);

	}
}
