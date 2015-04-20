package nk.code.epoch;

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
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class ScalaView extends View {

    private Paint mPaint;
    private TextPaint textPaint;
    private Paint gradPaint;
    private final int g1 = Color.rgb(60, 60, 230);
    private final int g2 = Color.rgb(50, 230, 50);;
    
    float mRotation = 0f;
    float[] mPoints = {
            0.5f, 0f, 0.5f, 1f,
            0f, 0.5f, 1f, 0.5f};

    public ScalaView(Context context, AttributeSet attrs) {
        super(context);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Style.FILL); 
        mPaint.setTypeface(Typeface.SERIF);
        
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(14);
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

        //canvas.drawPaint(mPaint); 
        
        canvas.drawPaint(gradPaint);
        //adjustTextSize("2015");
        canvas.save();
        //int scale = getWidth();
        //int scale2 = getHeight();  
        //canvas.scale(scale, scale2);
        //Log.d("nk scale",Integer.toString(scale2));
        //canvas.rotate(mRotation, 0.5f, 0.5f);
        //canvas.drawLines(mPoints, mPaint);
        float textHeight = textPaint.descent() - textPaint.ascent();
        //float textOffset = (textHeight / 2) - textPaint.descent();

        //RectF bounds = new RectF(0, 0, getWidth(), getHeight());
        
        //canvas.drawText("2015", 0, scale2/2, mPaint);
        canvas.drawText("2015",textPaint.getTextSize()+10, textHeight,textPaint);
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