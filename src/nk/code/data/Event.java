package nk.code.data;

import nk.code.epoch.ScalaView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Event {	
	public double start = 0;
	public String name = "";
	public int size = 20;
	public boolean hasimage = false;
	public int look = 1;	
	public int x = 50;
	
	protected Paint mPaint;
	
	public static int zelena = 0xFF00FF00;
	public static int crna = 0xFF000000;
	
	public Event(double s, int x,String n){
		name = n;
		start = s;
		this.x = x;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		mPaint.setColor(zelena);
	}

	public void draw(Canvas canvas, ScalaView skala) {
		// TODO Auto-generated method stub
        float y = skala.getPos(start);
        if(y>=0){
        	mPaint.setColor(zelena);
	        canvas.drawRect(x, y-size/2,x+size, y+size/2, mPaint);
	        mPaint.setColor(crna);
	        Rect bounds = new Rect();
	        mPaint.getTextBounds(name, 0, name.length(), bounds);
	        canvas.drawText(name, x+size+5, y+ bounds.height()/2, mPaint);
        }		
	}
}
