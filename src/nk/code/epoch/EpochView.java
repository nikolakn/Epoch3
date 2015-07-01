package nk.code.epoch;

import nk.code.data.Document;
import nk.code.data.Event;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;


public class EpochView extends View {
	
	private Document doc;
	private ScalaView skala;
	private int mActivePointerId = -1;
    float mRotation = 0f;
	

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final int DRAW = 3;
	int mode = NONE;
	
    float[] mPoints = {
            0.5f, 0f, 0.5f, 1f,
            0f, 0.5f, 1f, 0.5f};
    DateTime now = new DateTime();
	private float mLastTouchX;
	private float dx;
	private float xposLong;
	private float yposLong;
	private GestureDetector gestureDetector;

	private boolean isEventLongClick = false; 
	
	 private View.OnCreateContextMenuListener vC = new View.OnCreateContextMenuListener() {
	    @Override
	    public void onCreateContextMenu(ContextMenu arg0, View arg1,
	            ContextMenuInfo arg2) {
	        if(!isEventLongClick){
		        arg0.add(0, 0, 0, "add event").setOnMenuItemClickListener(mMenuItemClickListener);
		        arg0.add(0, 1, 0, "add epoch").setOnMenuItemClickListener(mMenuItemClickListener);
		        arg0.add(0, 2, 0, "add people").setOnMenuItemClickListener(mMenuItemClickListener);
	        } else {
		        arg0.add(0, 0, 0, "edit");
		        arg0.add(0, 0, 0, "delete");
	        }
	    }
	};
		
	private OnMenuItemClickListener mMenuItemClickListener = new OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
			case 0:
				addEpoch();
				return true;
			case 1:
				Log.i("nk", "epoch" + Float.toString(xposLong));
				return true;
			case 2:
				Log.i("nk", "people" + Float.toString(xposLong));
				return true;
			}
			return false;
		}
	};
	public Context context;
    public EpochView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        doc = new Document();
        doc.addEvent(19, 10, 1982,100, "Nikola");
        doc.addEvent(1, 1, 1990,200, "90");
        doc.addEvent(31, 12, 1980,300, "80");
        doc.addEpoch(1, 1, 2015, 40, "epoha", 1, 1, 1950);
        now =  now.minusYears(2);
        
        float rotation = 0.0f;
        // Remember to call this when finished
     
        setRotation(rotation);
        this.setOnCreateContextMenuListener(vC);
        gestureDetector = new GestureDetector(context, new LongListener());


    }
    
	protected void addEpoch() {
		// TODO Auto-generated method stub
		((EpochActivity)context).StartAddEventActivity();
	}

	private class LongListener extends GestureDetector.SimpleOnGestureListener {
	@Override
    public void onLongPress(MotionEvent e) {
		final int pointerIndex = MotionEventCompat.getActionIndex(e);
		xposLong = MotionEventCompat.getX(e, pointerIndex);
		setYposLong(MotionEventCompat.getY(e, pointerIndex));
		Event ev=doc.getEventFromPos(xposLong-dx,yposLong,skala);
		if(ev==null)
			isEventLongClick = false;
		else 
			isEventLongClick = true;
        showContextMenu();
	}
	
	}
    
    public void setRotation(float degrees) {
        mRotation = degrees;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top,int right, int bottom) {
	    super.onLayout(changed, left, top, right, bottom);
    }  
    
    
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		final int action = MotionEventCompat.getActionMasked(ev);
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			final float x = MotionEventCompat.getX(ev, pointerIndex);
			mLastTouchX = x;
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {

			if (mode == ZOOM) {

			} else {
				// Find the index of the active pointer and fetch its position
				int pointerIndex = MotionEventCompat.findPointerIndex(ev,
						mActivePointerId);
				// final float x = MotionEventCompat.getX(ev, pointerIndex);
				float x = MotionEventCompat.getX(ev, pointerIndex);
				// Calculate the distance moved
				// final float dx = x - mLastTouchX;
				dx+=(x - mLastTouchX);
				invalidate();
				mLastTouchX = x;

			}

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = -1;
			break;
		}
	
		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = -1;
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
				mLastTouchX = MotionEventCompat.getY(ev, newPointerIndex);
				mActivePointerId = MotionEventCompat.getPointerId(ev,
						newPointerIndex);
			}
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN:
			break;
		}

		return true;
	}
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save(); 
  
        //canvas.rotate(mRotation, 0.5f, 0.5f);
        //canvas.drawLines(mPoints, mPaint);  
        if(skala != null){
        	doc.draw(canvas, skala, dx);
        }
        canvas.restore();
    }

	public void init(ScalaView s) {
		skala = s;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public void addEpoch(String name, DateTime dateTime) {
	     doc.addEvent(dateTime.getDayOfMonth(),dateTime.getMonthOfYear(),dateTime.getYear(),dateTime.getHourOfDay(),dateTime.getMinuteOfHour() ,(int)xposLong, name);
	}

	public float getYposLong() {
		return yposLong;
	}

	public void setYposLong(float yposLong) {
		this.yposLong = yposLong;
	}	
	
}