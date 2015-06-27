package nk.code.data;

import nk.code.epoch.ScalaView;
import nk.code.helper.Boja;
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
	public int colorLine = Boja.zelena;
	public int colorText = Boja.crna;
	protected Paint mPaint;
	public int style = 2;

	public Event(double s, int x, String n) {
		name = n;
		start = s;
		this.x = x;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		mPaint.setColor(colorLine);
	}

	public void draw(Canvas canvas, ScalaView skala, float dx) {
		// TODO Auto-generated method stub
		float y = skala.getPos(start);
		float xx = x + dx;
		if (y >= 0) {
			mPaint.setColor(colorLine);
			if(style == 1)
				canvas.drawRect(xx- size / 2, y - size / 2, xx + size/2, y + size / 2, mPaint);
			else
				canvas.drawCircle(xx, y, size / 2, mPaint);
			mPaint.setColor(colorText);
			Rect bounds = new Rect();
			mPaint.getTextBounds(name, 0, name.length(), bounds);
			canvas.drawText(name, xx + size + 5, y + bounds.height() / 2, mPaint);
		}
	}
}
