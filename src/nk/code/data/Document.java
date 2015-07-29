package nk.code.data;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import nk.code.epoch.ScalaView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import android.graphics.Canvas;

//Collection of Events that represent epoch
public class Document {

	private ArrayList<Event> list = new ArrayList<Event>();
	private int index=-1;
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
		Epoch e = new Epoch(startDate,endDate,x, name);
		e.end = endDate;
		list.add(e);		
	}
	
	public void draw(Canvas canvas, ScalaView skala, float dx){
        for(Event e : list){
        	e.draw(canvas, skala, dx);    	
        }
	}

	public Event addEvent(int dan, int mesec, int godina,int sat, int minut, int x, String name) {
		double startDate = DateTimeUtils.toJulianDay(new DateTime(godina,mesec,dan,sat,minut).getMillis());
		Event e = new Event(startDate,x, name);
		e.description = "<h1>"+name+"</h1></br>";
		list.add(e);
		return e;
	}
	
	public Event addEpoch(int dan, int mesec, int godina,int sat, int minut,int dan2, int mesec2, int godina2,int sat2, int minut2, int x, String name) {
		double startDate = DateTimeUtils.toJulianDay(new DateTime(godina,mesec,dan,sat,minut).getMillis());
		double endDate = DateTimeUtils.toJulianDay(new DateTime(godina2,mesec2,dan2,sat2,minut2).getMillis());	
		Epoch e = new Epoch(startDate, endDate,x, name);
		e.description = "<h1>"+name+"</h1></br>";
		list.add(e);
		return e;
	}
	//Find event in array
	public Event find(Event e){
		if(e == null)
			return null;
		int i=list.indexOf(e);
		if(i !=- 1){
			return list.get(i);
		}
		return null;
	}
	// is Event currently drawn on x,y position on scale
	public Event getEventFromPos(float x, float y, ScalaView skala){
		Event ret = null;
        for(Event e : list){
        	if(e.isOnPosition(x,y,skala)){
        		return e;
        	}
        }
		return ret;
	}

	public void deleteEpoch(Event ev) {
		if(ev!=null)
			list.remove(ev);	
	}
	// write document to file
	public void serialize(ObjectOutputStream os) throws IOException{
		os.writeInt(index);
		os.writeInt(list.size());
        for(Event e : list){
        	e.serialize(os);   
        }
	}
	// load document from file
	public void deSerialize(ObjectInputStream is) throws IOException, ClassNotFoundException{
		list.clear();
		index = is.readInt();
		int s=is.readInt();
		for(int i=0; i<s ;i++){
			int o=is.readInt();
			if(o == 0){
				Event e = (Event) is.readObject();
				list.add(e);
			}
			if(o == 1){
				Event  e = (Epoch) is.readObject();
				list.add(e);
			}
		}  
	}

	public int getIndex(Event ev) {
		// TODO Auto-generated method stub
		if(ev == null)
			return -1;
		return list.indexOf(ev);
	}

	public void setCurrent(Event ev) {
		index = getIndex(ev);		
	}
	public Event getCurrent(){
		if(index==-1)
			return null;
		if(index>list.size())
			return null;
		return list.get(index);
	}
}
