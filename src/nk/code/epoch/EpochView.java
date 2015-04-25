package nk.code.epoch;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class EpochView extends View {

	private ScalaView skala;
    private Paint mPaint;
    float mRotation = 0f;
    float[] mPoints = {
            0.5f, 0f, 0.5f, 1f,
            0f, 0.5f, 1f, 0.5f};
    DateTime now = new DateTime();
    public EpochView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFFF00000);
        now =  now.minusYears(2);
        int color = 0xFF00FF00;
        float rotation = 0.0f;
        // Remember to call this when finished
        
        setColor(color);
        setRotation(rotation);
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
    
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//int pointerCount = event.getPointerCount();
		int actionIndex = event.getActionIndex();
		int action = event.getActionMasked();
		//int id = event.getPointerId(actionIndex);
		// Check if we received a down or up action for a finger
		Log.d("nk","clik");
		if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
			Log.d("nk", Integer.toString((int)event.getX(actionIndex)));
			Log.d("nk", Integer.toString((int)event.getY(actionIndex)));
			
		} else if (action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) {
		}

		return true;
	}
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        
        //int scale = getWidth();
        //int scale2 = getHeight();
        //canvas.scale(scale, scale2);
        
        //canvas.rotate(mRotation, 0.5f, 0.5f);
        //canvas.drawLines(mPoints, mPaint);

        
        if(skala != null){
	        double startDate = DateTimeUtils.toJulianDay(now.getMillis());
	        float y = skala.getPos(startDate);
	        if(y>0){
		        //Log.d("nk", Float.toString(y));
		        canvas.drawRect(getWidth()/2, y, getWidth()/2+50, y+50, mPaint);
	        }
        }
        canvas.restore();
    }

	public void init(ScalaView s) {
		skala = s;
	}
}