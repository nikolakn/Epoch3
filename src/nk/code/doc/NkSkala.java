package nk.code.doc;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

public class NkSkala {

	private double startDate = 2456293;
	private double enddate;
	private int scale = 0;
	private float len = 50;
	private double period=365;
	private TextPaint textPaint;
	float textHeight;
	
	public NkSkala(){
		textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(12);
        textHeight = textPaint.descent() - textPaint.ascent();
	}
	public void draw(Canvas canvas,int w, int h) {
		DateTime dt =  new DateTime(DateTimeUtils.fromJulianDay(startDate));
		for(float y=textHeight; y<=h; y+=len ){
			String ss = Integer.toString(dt.getYear());
			canvas.drawText(ss,textPaint.getTextSize()+15, y,textPaint);
			dt = dt.plusYears(1);
		}
	}
}
