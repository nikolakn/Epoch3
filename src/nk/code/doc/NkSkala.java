package nk.code.doc;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
//import android.util.Log;

public class NkSkala {

	private double startDate = 2456293;
	private int len = 50;
	private int period=1;
	private TextPaint textPaint;
	float textHeight;
	float dy = 0;
	
	public NkSkala(){
		textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(12);
        textHeight = textPaint.descent() - textPaint.ascent();
	}
	public void draw(Canvas canvas,int w, int h) {
		DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(startDate));
		for(float y=textHeight-dy; y<=h; y+=len ){
			String ss = Integer.toString(dt.getYear());
			canvas.drawText(ss,textPaint.getTextSize()+15, y,textPaint);
			dt = dt.minusYears(period);
		}
	}
	public void posmak(float dy) {
		// koliko duzina se preskace a dodaje vremena
		if(dy != 0){
			int pomakuvremenu= Math.abs((int)(dy/len)); 
			int ostatak = pomakuvremenu*len;
			float dlen = Math.abs(dy) - ostatak;
			
			DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(startDate));
			//Log.d("nk dy",Float.toString(dy));
			//Log.d("nk",Integer.toString(pomakuvremenu));
			if(dy > 0){
				dt = dt.plusYears(pomakuvremenu);
				this.dy-=dlen;
				if(pomakuvremenu ==0 && Math.abs(this.dy)>len)
					dt = dt.plusYears( Math.abs((int)this.dy/len));
				this.dy=this.dy%len;
			}
			else{
				dt = dt.minusYears(pomakuvremenu);
				this.dy+=dlen;
				if(pomakuvremenu ==0 && Math.abs(this.dy)>len)
					dt = dt.minusYears(Math.abs((int)this.dy/len));
				this.dy=this.dy%len;
			}
			startDate = DateTimeUtils.toJulianDay(dt.getMillis());
		}
	}
	public void zoom(int sc, float y) {
		len = 50+sc;
		
	}

}
