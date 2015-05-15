package nk.code.doc;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
//import android.util.Log;
/**
 * 
 * @author Nikola
 * 
 */
public class NkSkala {
	
	public static final int ABOVE = -3;  //top 
	public static final int BELOW = -2;  //down
	public static final int INVALID = -1;
	
	private double startDate = 2456293;
	private int len = 50;
	private int period=1;
	private float scale = 365;
	private TextPaint textPaint;
	float textHeight;
	float dy = 0;
	private int scalaHeith;
	
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public NkSkala(){
		textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(12);
        textHeight = textPaint.descent() - textPaint.ascent();
        DateTime now = new DateTime();
        setStartDate(DateTimeUtils.toJulianDay(now.getMillis()));
	}
	public void Init(int sh){
		scalaHeith = sh;
	}
	public void draw(Canvas canvas,int w, int h) {
		DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(getStartDate()));
		for(float y=textHeight-dy; y<=h; y+=getLen() ){
			String ss = Integer.toString(dt.getYear());
			canvas.drawText(ss,textPaint.getTextSize()+15, y,textPaint);
			//canvas.drawLine(3, y-textHeight/2, 15, y-textHeight/2, textPaint);
			dt = dt.minusYears(period);
		}
	}
	//move scale by dy
	public void posmak(float dy) {
		// koliko duzina se preskace a dodaje vremena
		if(dy != 0){
			int pomakuvremenu= Math.abs((int)(dy/getLen())); 
			int ostatak = pomakuvremenu*getLen();
			float dlen = Math.abs(dy) - ostatak;
			
			DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(getStartDate()));
			//Log.d("nk dy",Float.toString(dy));
			//Log.d("nk",Integer.toString(pomakuvremenu));
			if(dy > 0){
				dt = dt.plusYears(pomakuvremenu);
				this.dy-=dlen;
				if(pomakuvremenu ==0 && Math.abs(this.dy)>getLen())
					dt = dt.plusYears( Math.abs((int)this.dy/getLen()));
				this.dy=this.dy%getLen();
			}
			else{
				dt = dt.minusYears(pomakuvremenu);
				this.dy+=dlen;
				if(pomakuvremenu ==0 && Math.abs(this.dy)>getLen())
					dt = dt.minusYears(Math.abs((int)this.dy/getLen()));
				this.dy=this.dy%getLen();
			}
			setStartDate(DateTimeUtils.toJulianDay(dt.getMillis()));
		}
	}
	
	public void zoom(int sc, float y) {
		/*
		//koji datum se trenutno nalazi na y poziciji
		double d = getDate(y);
		//uvelicati
		len = sc;
		//pomeriti skalu da se taj datum vrati na istu poziciju y
		float pos = getPos(d);
		float duzina = y-pos;
		double vreme = (len/scale)* duzina;
		
		startDate += vreme-dy;
		//pomocna funkcija: 
		//1 vraca datum za prosledjenu poziciju
		//2 postavlja zadati datum na zadati polozaj
		
		*/
	}
	
	
	//get date for given position on scale
	public double getDate(float y){
		if(y<0)
			return ABOVE;
		if(y> scalaHeith)
			return BELOW;
		double podeoka = (float)y/(float)getLen();
		double malih = dy/y;
		double per = podeoka * period;
		double per2 = malih * period;
		double date = getStartDate()-per*scale-per2*scale;
		return date;
	}
	
	
	public void SetZoom(float scale) {
		setLen((int)scale); 
		
	}
	//return position on scale for given date
	public float getPos(double date){
		//Log.d("nk-odnos","ulaz");
		if(date > (getStartDate()+scale))
			return ABOVE;
		double enddate = getEndDate();
		if(date < enddate)
			return BELOW;
		//racunanje gde se nalazi datim u periodu datuma
		double pomeren = getStartDate()-date;
		double odnos  = pomeren/(getStartDate()-enddate);
		double gde = (double)scalaHeith * odnos;	
		return (float)gde-dy+textHeight/2.0f;
	}
	//date on other side
	double getEndDate(){
		double podeoka = (float)scalaHeith/(float)getLen();
		double per = podeoka * period;	
		double enddate = getStartDate()-per*scale;
		return enddate;
	}
	public double getStartDate() {
		return startDate;
	}
	public void setStartDate(double startDate) {
		this.startDate = startDate;
	}

}
