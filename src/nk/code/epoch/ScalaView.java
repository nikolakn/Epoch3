package nk.code.epoch;

import nk.code.doc.NkSkala;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class ScalaView extends View {

    private static final int INVALID_POINTER_ID = -1;
	private Paint mPaint;
    private NkSkala skala;
    private Paint gradPaint;
    private final int g1 = Color.rgb(60, 60, 230);
    private final int g2 = Color.rgb(50, 230, 50);;
    
    float mRotation = 0f;
    float[] mPoints = {
            0.5f, 0f, 0.5f, 1f,
            0f, 0.5f, 1f, 0.5f};
	private float mLastTouchX;
	private float mLastTouchY;
	//private float mPosX;
	//private float mPosY;
	private int mActivePointerId = INVALID_POINTER_ID;

    public ScalaView(Context context, AttributeSet attrs) {
        super(context);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Style.FILL); 
        mPaint.setTypeface(Typeface.SERIF);
        
        skala = new NkSkala();
        gradPaint = new Paint();
        setGrad();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }
    
    public void setRotation(float degrees) {
        mRotation = degrees;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top,int right, int bottom) {
	    super.onLayout(changed, left, top, right, bottom);


    }   
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		setGrad();
    }
    
    private void setGrad(){
    	gradPaint.setShader(new LinearGradient(0,0,0,getHeight(), g1, g2,Shader.TileMode.CLAMP)); 
    }
    
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		 // Let the ScaleGestureDetector inspect all events.
	             
	    final int action = MotionEventCompat.getActionMasked(ev); 
	        
	    switch (action) { 
	    case MotionEvent.ACTION_DOWN: {
	        final int pointerIndex = MotionEventCompat.getActionIndex(ev); 
	        final float x = MotionEventCompat.getX(ev, pointerIndex); 
	        final float y = MotionEventCompat.getY(ev, pointerIndex); 
	            
	        // Remember where we started (for dragging)
	        mLastTouchX = x;
	        mLastTouchY = y;
	        // Save the ID of this pointer (for dragging)
	        mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
	        break;
	    }
	            
	    case MotionEvent.ACTION_MOVE: {
	        // Find the index of the active pointer and fetch its position
	        final int pointerIndex = 
	                MotionEventCompat.findPointerIndex(ev, mActivePointerId);  
	            
	        final float x = MotionEventCompat.getX(ev, pointerIndex);
	        final float y = MotionEventCompat.getY(ev, pointerIndex);
	            
	        // Calculate the distance moved
	        //final float dx = x - mLastTouchX;
	        final float dy = y - mLastTouchY;

	        //mPosX += dx;
	        //mPosY += dy;
	        skala.posmak(dy);
	        invalidate();

	        // Remember this touch position for the next move event
	        mLastTouchX = x;
	        mLastTouchY = y;

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
	            
	        final int pointerIndex = MotionEventCompat.getActionIndex(ev); 
	        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex); 

	        if (pointerId == mActivePointerId) {
	            // This was our active pointer going up. Choose a new
	            // active pointer and adjust accordingly.
	            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
	            mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex); 
	            mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex); 
	            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
	        }
	        break;
	    }
	    }       
	    return true;
	}
    @SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(gradPaint);
        canvas.save();
        
        skala.draw(canvas,getWidth(),getHeight());
        
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
        float target = (float)mViewHeight*.7f;
        // figure out what textSize setting would create that height
        // of text
        float size = ((target/h)*100f);
        // and set it into the paint
        mPaint.setTextSize(size);
    }

}