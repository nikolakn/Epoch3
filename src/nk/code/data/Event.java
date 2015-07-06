package nk.code.data;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import nk.code.epoch.ScalaView;
import nk.code.helper.Boja;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

//one Event in time, representing one date in history with start date
public class Event implements Serializable {
	private static final long serialVersionUID = 1420672609912364060L;

	public enum Visibility {ALWAYS("always"), HEREANDPLUS("here and -"),
		ONLYHERE("only here"), HEREANDMINUS("here and -");
	
		private final String fieldDescription;	
		
		private Visibility(String value) {
	        fieldDescription = value;
	    }	
	    public String getFieldDescription() {
	        return fieldDescription;
	    }
	};
	public static int DEFEVENTCOLOR = Boja.zelena;
	public static int DEFEVENTSIZE = 1;
	public static int DEFEVENTSTYLE = 2;

	public double start = 0; // start in Julian days
	public String name = ""; // name of event
	protected int size = 30; // real size in px
	public int look = 1; // relative size
	public int x = 50; // position
	public int colorLine = DEFEVENTCOLOR; // main color
	public int colorText = Boja.crna;
	public int style = DEFEVENTSTYLE;

	public Event(double s, int x, String n) {
		name = n;
		start = s;
		this.x = x;
	}

	public void draw(Canvas canvas, ScalaView skala, float dx) {
		// TODO Auto-generated method stub
		float y = skala.getPos(start);
		float xx = x + dx;
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		mPaint.setColor(colorLine);
		if (y >= 0) {
			mPaint.setColor(colorLine);
			if (style == 1)
				canvas.drawRect(xx - size / 2, y - size / 2, xx + 
						size / 2, y + size / 2, mPaint);
			else
				canvas.drawCircle(xx, y, size / 2, mPaint);
			mPaint.setColor(colorText);
			Rect bounds = new Rect();
			mPaint.getTextBounds(name, 0, name.length(), bounds);
			canvas.drawText(name, xx + size / 2 + 5, y + bounds.height() / 2,
					mPaint);
		}
	}

	// is event currently drawn on x,y position
	public boolean isOnPosition(float xx, float yy, ScalaView skala) {
		float y = skala.getPos(start);
		float ss = (size + 15) / 2;
		if (xx >= x - ss && xx <= x + ss) {
			if (yy >= y - ss && yy <= y + ss)
				return true;
		}
		return false;
	}
	// relative to absolute size
	public void setLook(int look) {
		if (look == 1)
			size = 30;

	}
	// write event to file
	public void serialize(ObjectOutputStream os) throws IOException {
		os.writeInt(0);
		os.writeObject(this);
	}
}
