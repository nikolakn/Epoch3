package nk.code.data;

import nk.code.epoch.ScalaView;
import nk.code.helper.Boja;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Event {
	public static int DEFEVENTCOLOR=Boja.zelena;
	public static int DEFEVENTSIZE=1;
	public static int DEFEVENTSTYLE=2;
	public double start = 0;
	public String name = "";
	protected int size = 30;
	public boolean hasimage = false;
	public int look = 1;
	public int x = 50;
	public int colorLine = DEFEVENTCOLOR;
	public int colorText = Boja.crna;
	protected Paint mPaint;
	public int style = DEFEVENTSTYLE;
	private Bitmap image = null;

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
			canvas.drawText(name, xx + size/2 + 5, y + bounds.height() / 2, mPaint);
		}
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public boolean isOnPosition(float xx, float yy, ScalaView skala) {
		float y=skala.getPos(start);
		float ss = (size+10)/2;
		if(xx>=x-ss && xx<=x+ss){
			if(yy>=y-ss && yy<=y+ss)
				return true;
		}
		return false;
	}

	public void setLook(int look) {
		if(look == 1)
			size = 30;
		
	}
}
