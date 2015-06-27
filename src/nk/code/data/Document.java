package nk.code.data;

import java.util.ArrayList;
import nk.code.epoch.ScalaView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import android.graphics.Canvas;


public class Document {

	private ArrayList<Event> list = new ArrayList<Event>();
	
	public Document(){

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
        	e.draw(canvas, skala);    	
        }
	}
	

}
