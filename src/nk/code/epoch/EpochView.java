package nk.code.epoch;

import nk.code.data.Document;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class EpochView extends View {
	
	private Document doc;
	private ScalaView skala;
    
    float mRotation = 0f;
    float[] mPoints = {
            0.5f, 0f, 0.5f, 1f,
            0f, 0.5f, 1f, 0.5f};
    DateTime now = new DateTime();
    public EpochView(Context context, AttributeSet attrs) {
        super(context, attrs);
        doc = new Document();
        doc.addEvent(19, 10, 1982,50, "Nikola");
        doc.addEvent(1, 1, 1990,100, "90");
        doc.addEvent(31, 12, 1980,120, "80");
       
        doc.addEpoch(1, 1, 2015, 20, "epoha", 1, 1, 1950);
        now =  now.minusYears(2);
        
        float rotation = 0.0f;
        // Remember to call this when finished
     
        setRotation(rotation);
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
		//int actionIndex = event.getActionIndex();
		int action = event.getActionMasked();
		//int id = event.getPointerId(actionIndex);
		// Check if we received a down or up action for a finger
		//Log.d("nk","clik");
		if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
			//Log.d("nk", Integer.toString((int)event.getX(actionIndex)));
			//Log.d("nk", Integer.toString((int)event.getY(actionIndex)));
			
		} else if (action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) {
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
        	doc.draw(canvas, skala);
        }
        canvas.restore();
    }

	public void init(ScalaView s) {
		skala = s;
	}
}