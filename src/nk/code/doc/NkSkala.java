package nk.code.doc;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;
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
	
	public static final int LENDEF = 100;
	public static final int GODINA = 365;
	
	private  double startDate = 3000000;
	private double len = LENDEF;
	private TextPaint textPaint;
	float textHeight;
	double dy = 75000;
	private int period=1;
	
	private int scalaHeith;
	private int zoomlen=LENDEF;
	
	private double timePerPix = period/len;
	

	public double getLen() {
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
        //DateTime now = new DateTime();
        startDate = DateTimeUtils.toJulianDay(new DateTime(3520,1,1,0,0).getMillis());
        Log.d("nk startDate",Double.toString(startDate));
	}
	public void Init(int sh){
		scalaHeith = sh;
		DateTime dt2 =  new DateTime(DateTimeUtils.fromJulianDay(getDate(0)));
		Log.d("nk start",dt2.toString());
		DateTime dt3 =  new DateTime(DateTimeUtils.fromJulianDay(getDate(scalaHeith/2)));
		Log.d("nk sr",dt3.toString());
		DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(getEndDate()));
		Log.d("nk e",dt.toString());


		Log.d("nk dy","--------------");
		Log.d("nk dy",Float.toString(scalaHeith));
		Log.d("nk dy","--------------");
		
		Log.d("nk start",Float.toString(getPos(DateTimeUtils.toJulianDay(dt2.getMillis()))));
		Log.d("nk sred",Float.toString(getPos(DateTimeUtils.toJulianDay(dt3.getMillis()))));
		
		Log.d("nk end",Float.toString(getPos(DateTimeUtils.toJulianDay(dt.getMillis()))));
		
		
	}
	public void draw(Canvas canvas,int w, int h) {
		int r = (int)(dy / len);
		//float raz= (float)(dy - (r*len));
		DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(getStartDate()));
		float raz = (float) (dy%len);
		//double date =startDate-(r*period*365);

		dt =dt.minusYears(r*period);
		dt =dt.plusYears(period);
		//canvas.drawLine(0, 600, 100, 600, textPaint);
		for(float y=-raz; y<=h; y+=getLen() ){
			//DateTime dt2 =  new DateTime(DateTimeUtils.fromJulianDay(date));
			String ss = Integer.toString(dt.getYear());
			canvas.drawText(ss,textPaint.getTextSize()+15, y,textPaint);
			dt = dt.minusYears(period);
			//date -= (period*365);
		}
	}
	//move scale by dy
	public void posmak(float y) {
		// koliko duzina se preskace a dodaje vremena
		dy-=y;
	}
	
	public void zoom(float sc, float y) {
		
		//zapamtiti datum koji se trenutno nalazi na y
		//uraditi skaliranje a zatim datum poji se nalazio na y koordinata
		//postaviti ga ponovo na y
		//koji datum se trenutno nalazi na y poziciji
		double d = getDate(y);
		//uvelicati
		
		zoomlen = (int)(LENDEF*sc);
		if(zoomlen >=50){
			len = zoomlen;
			period = 1;
		}
		else if (zoomlen >=40){
			len = LENDEF;
			period = 5;
		}
		else if (zoomlen >=30){
			len = LENDEF;
			period = 10;
			
		}
		else if (zoomlen >=30){
			len = LENDEF;
			period = 50;
		}
		else if (zoomlen >=20){
			len = LENDEF;
			period = 100;
		}
	    else if (zoomlen <20) {
	    	zoomlen = 20;
			period = 500;
		}
		//pomeriti skalu da se taj datum vrati na istu poziciju y
		setDateOnPos(d,y);
		
	}
	
	//postavlja datun na poziciji y na skali
	public void setDateOnPos(double date,float y){
		
		double d = getDate(y);
		double raz = d-date;
		dy += (int)((raz/365)*((double)len/(double)period));	
		
	}
	
	public double getDate(float y){
		if(y<(0))
			return ABOVE;
		if(y> scalaHeith)
			return BELOW;

		double podeoka = (double)(y+dy)/(double)getLen();
		double per = podeoka * period*365;
		double date = getStartDate()-per;
		return date;
	}

	//return position on scale for given date
	public float getPos(double date){
		//Log.d("nk-odnos","ulaz");
		if(date > getDate(0))
			return ABOVE;
		double enddate = getEndDate();
		if(date < enddate)
			return BELOW;
		//racunanje gde se nalazi datim u periodu datuma
		
		//int r = (int)(dy / len);
		//double y=-(float)(dy-(r*len));
		//DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(getStartDate()));
		//dt =dt.minusYears(r*period);
		//double pomerensec = Math.abs(startDate-date);
	
		//double odnos  = pomeren/(getDate(0)-enddate);
		//double gde = (double)((float)scalaHeith) * odnos;	

		//return (float)((pomerensec /  (((double)period/(double)len)*365))-dy);
		double per = getStartDate() - date;
		double podeoka = per/ (period*365);
		double y = (podeoka * (double)getLen()) - dy;
		
		return (float)y;

	}

	public void SetZoom(float scale) {
		setLen((int)scale); 	
	}
	
	//date on other side
	double getEndDate(){
		return getDate(scalaHeith);
	}
	public double getStartDate() {
		return startDate;
	}


}
