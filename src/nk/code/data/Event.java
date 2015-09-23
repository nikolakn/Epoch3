package nk.code.data;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import nk.code.epoch.R;
import nk.code.epoch.ScalaView;
import nk.code.helper.Boja;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

//one Event in time, representing one date in history with start date
public class Event implements Serializable {
	private static final long serialVersionUID = 1420672609912364060L;

	public enum Visibility {ALWAYS("always"), HEREANDPLUS("here and -"),
		ONLYHERE("only here"), HEREANDMINUS("here and +");

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
	public Visibility visibility = Visibility.ALWAYS;
	public double visibilityZoom;
	public String description;
	private transient Bitmap bitmap2;
	private transient Bitmap bitmap;
	public Event(double s, int x, String n) {
		name = n;
		start = s;
		this.x = x;
		bitmap2 = null;
		bitmap = null;

	}

	public void draw(Canvas canvas, ScalaView skala) {
		// TODO Auto-generated method stub
		if (bitmap==null || bitmap2==null ){
			Resources res = skala.getResources();
			bitmap = BitmapFactory.decodeResource(res, R.drawable.explosion32);
			bitmap2 = BitmapFactory.decodeResource(res, R.drawable.flag4x);
		}
		
		if(visibility == Visibility.ONLYHERE){
			Log.d("nk",Double.toString(skala.getZoomLvl()));
			if(skala.getZoomLvl() != visibilityZoom)
				return;
		}
		
		if(visibility == Visibility.HEREANDMINUS){
			Log.d("nk",Double.toString(skala.getZoomLvl()));
			if(skala.getZoomLvl() > visibilityZoom)
				return;
		}
		if(visibility == Visibility.HEREANDPLUS){
			Log.d("nk",Double.toString(skala.getZoomLvl()));
			if(skala.getZoomLvl() < visibilityZoom)
				return;
		}
		float y = skala.getPos(start);
		float xx = x + skala.getDx();
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		if (look == 0)
			mPaint.setTextSize(12);
		if (look == 1)
			mPaint.setTextSize(14);
		if (look == 2)
			mPaint.setTextSize(16);
		if (look == 3)
			mPaint.setTextSize(18);
		if (look == 4)
			mPaint.setTextSize(20);

		mPaint.setColor(colorLine);
		if (y >= 0) {
			mPaint.setColor(colorLine);
			if (style == 0)
				canvas.drawRect(xx - size / 2, y - size / 2, xx +
						size / 2, y + size / 2, mPaint);
			else if (style == 1)
				canvas.drawCircle(xx, y, size / 2, mPaint);

			else if (style == 2)
				canvas.drawBitmap(bitmap2,xx - size , y - size+5 , mPaint);

			else if (style == 3)
				canvas.drawBitmap(bitmap,xx - size , y - size , mPaint);

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
		if(visibility == Visibility.ONLYHERE){
			Log.d("nk",Double.toString(skala.getZoomLvl()));
			if(skala.getZoomLvl() != visibilityZoom)
				return false;
		}
		
		if(visibility == Visibility.HEREANDMINUS){
			Log.d("nk",Double.toString(skala.getZoomLvl()));
			if(skala.getZoomLvl() > visibilityZoom)
				return false;
		}
		if(visibility == Visibility.HEREANDPLUS){
			Log.d("nk",Double.toString(skala.getZoomLvl()));
			if(skala.getZoomLvl() < visibilityZoom)
				return false;
		}
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
		this.look = look;
		if (look == 0)
			size = 20;
		if (look == 1)
			size = 30;
		if (look == 2)
			size = 40;
		if (look == 3)
			size = 50;
		if (look == 4)
			size = 60;

	}
	// write event to file
	public void serialize(ObjectOutputStream os) throws IOException {
		os.writeInt(0);
		os.writeObject(this);
	}
}
