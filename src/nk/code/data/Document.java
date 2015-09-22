package nk.code.data;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import nk.code.epoch.ScalaView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;

//Collection of Events that represent epoch
public class Document {

	private ArrayList<Event> list = new ArrayList<Event>();
	private int index=-1;
	private String title="";

	public Document(String t){
		title = t;
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

	public void draw(Canvas canvas, ScalaView skala){
        for(Event e : list){
        	e.draw(canvas, skala);
        }
	}

	public Event addEvent(int dan, int mesec, int godina,int sat, int minut, int x, String name) {
		double startDate = DateTimeUtils.toJulianDay(new DateTime(godina,mesec,dan,sat,minut).getMillis());
		Event e = new Event(startDate,x, name);
		e.description = "<h1>"+name+"</h1></br>\n";
		list.add(e);
		return e;
	}

	public Event addEpoch(int dan, int mesec, int godina,int sat, int minut,int dan2, int mesec2, int godina2,int sat2, int minut2, int x, String name) {
		double startDate = DateTimeUtils.toJulianDay(new DateTime(godina,mesec,dan,sat,minut).getMillis());
		double endDate = DateTimeUtils.toJulianDay(new DateTime(godina2,mesec2,dan2,sat2,minut2).getMillis());
		Epoch e = new Epoch(startDate, endDate,x, name);
		e.description = "<h1>"+name+"</h1></br>\n";
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
		os.writeUTF(title);
		/*
		os.writeInt(index);
		os.writeInt(list.size());
        for(Event e : list){
        	e.serialize(os);
        }
        */
	}
	// load document from file
	public void deSerialize(ObjectInputStream is) throws IOException, ClassNotFoundException{
		list.clear();
		title = is.readUTF();
		/*
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
		*/
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void saveToDatabase(SQLiteDatabase database){
		// Define 'where' part of query.
		String selection = EpochDatabase.U_EPOCH +"='"+title+"'";
		// Specify arguments in placeholder order.
		// Issue SQL statement.
		database.delete(EpochDatabase.U_TABLE, selection,null);
		for(Event e : list){
			ContentValues values=new ContentValues();
			values.put(EpochDatabase.U_EPOCH, title);
			values.put(EpochDatabase.U_NAME, e.name);
			values.put(EpochDatabase.U_START, e.start);
			values.put(EpochDatabase.U_DESCRIPTION, e.description);
			values.put(EpochDatabase.U_SIZE, e.size);
			values.put(EpochDatabase.U_STYLE, e.style);
			values.put(EpochDatabase.U_COLOR, e.colorLine);

			values.put(EpochDatabase.U_VISIBILITY, e.visibilityZoom);
			values.put(EpochDatabase.U_Y, e.x);
			values.put(EpochDatabase.U_LOOK, e.look);
			if(e instanceof Event){
				values.put(EpochDatabase.U_TYPE, 0);
			}
			if(e instanceof Epoch){
				Epoch a=(Epoch)e;
				values.put(EpochDatabase.U_TYPE, 1);
				values.put(EpochDatabase.U_END, a.end);

			}
			database.insert(EpochDatabase.U_TABLE,null,values);
        }
		

		String naslov = EpochDatabase.N_IME +"='"+title+"'";
		database.delete(EpochDatabase.N_TABLE, naslov,null);
		
		ContentValues values2=new ContentValues();
		values2.put(EpochDatabase.N_IME, title);
		
		database.insert(EpochDatabase.N_TABLE,null,values2);


	}
	public void openFromDatabase(SQLiteDatabase database){
		String[] allColumns = { EpochDatabase.U_NAME,EpochDatabase.U_START,EpochDatabase.U_DESCRIPTION,
								EpochDatabase.U_SIZE,EpochDatabase.U_STYLE,EpochDatabase.U_COLOR,
								EpochDatabase.U_VISIBILITY,EpochDatabase.U_Y,EpochDatabase.U_LOOK,
								EpochDatabase.U_TYPE,EpochDatabase.U_END};
		
		Cursor cursor = database.query(EpochDatabase.U_TABLE, allColumns, 
				EpochDatabase.U_EPOCH + " = '" + title+"'", null,null, null, null);
		cursor.moveToFirst();
		list.clear();
		  while (!cursor.isAfterLast()) {
			  if(cursor.getInt(9)==0){
					Event e = new Event(cursor.getDouble(1),cursor.getInt(7), cursor.getString(0));
					e.description = cursor.getString(2);
					e.size = cursor.getInt(3);
					e.style =cursor.getInt(4);
					e.colorLine = cursor.getInt(5);
					e.visibilityZoom = cursor.getInt(6);
					e.look= cursor.getInt(8);
					list.add(e);			  		  
			  }
			  if(cursor.getInt(9)==1){	  
					Epoch e = new Epoch(cursor.getDouble(1),cursor.getDouble(10),cursor.getInt(7), cursor.getString(0));
					e.description = cursor.getString(2);
					e.size = cursor.getInt(3);
					e.style =cursor.getInt(4);
					e.colorLine = cursor.getInt(5);
					e.visibilityZoom = cursor.getInt(6);
					e.look= cursor.getInt(8);
					list.add(e);			  
			  }
			  cursor.moveToNext();
		    }
	    // close the cursor
	    cursor.close();
	}
}
