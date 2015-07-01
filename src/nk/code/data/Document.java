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
	
	public void addEpoch(int sdan,int smesec, int sgodina,int x, String name,int edan,int emesec, int egodina){
		double startDate = DateTimeUtils.toJulianDay(new DateTime(sgodina,smesec,sdan,0,0).getMillis());
		double endDate = DateTimeUtils.toJulianDay(new DateTime(egodina,emesec,edan,0,0).getMillis());
		Epoch e = new Epoch(startDate,x, name);
		e.end = endDate;
		list.add(e);		
	}
	
	public void draw(Canvas canvas, ScalaView skala, float dx){
        for(Event e : list){
        	e.draw(canvas, skala, dx);    	
        }
	}

	public void addEvent(int dan, int mesec, int godina,int sat, int minut, int x, String name) {
		double startDate = DateTimeUtils.toJulianDay(new DateTime(godina,mesec,dan,sat,minut).getMillis());
		Event e = new Event(startDate,x, name);
		list.add(e);
	}
	
	public Event getEventFromPos(float x, float y, ScalaView skala){
		Event ret = null;
        for(Event e : list){
        	if(e.isOnPosition(x,y,skala)){
        		return e;
        	}
        }
		return ret;
	}
	

}
