package nk.code.data;

import java.util.ArrayList;

import nk.code.epoch.ScalaView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Document {


	private ArrayList<Event> list = new ArrayList<Event>();
	private Paint mPaint;
	
	public Document(){
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		//mPaint.setColor(0xFFF00000);

		mPaint.setColor(0xFF00FF00);
	}
	
	public void addEvent(Event e){
		list.add(e);
	}
	public void addEvent(double start,int x, String name){
		Event e = new Event(start, x, name);
		list.add(e);
	}	
	public void addEvent(int dan,int mesec, int godina,int x, String name){
		double startDate = DateTimeUtils.toJulianDay(new DateTime(godina,mesec,dan,0,0).getMillis());
		Event e = new Event(startDate,x, name);
		list.add(e);
	}		
	
	public void draw(Canvas canvas, ScalaView skala){
        for(Event e : list){
	        float y = skala.getPos(e.start);
	        if(y>=0){
	        	mPaint.setColor(0xFF00FF00);
		        canvas.drawRect(e.x, y-e.size/2,e.x+e.size, y+e.size/2, mPaint);
		        mPaint.setColor(0xFF000000);
		        Rect bounds = new Rect();
		        mPaint.getTextBounds(e.name, 0, e.name.length(), bounds);
		        canvas.drawText(e.name, e.x+e.size+5, y+ bounds.height()/2, mPaint);
	        }
        }
	
	}
	

}
