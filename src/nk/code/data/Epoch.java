package nk.code.data;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import nk.code.doc.NkSkala;
import nk.code.epoch.ScalaView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

//time interval, representing time interval in history with start and end date
public class Epoch extends Event implements Serializable {
	private static final long serialVersionUID = 1420672609912364061L;
	public Epoch(double s,double e, int x, String n) {
		super(s, x, n);
		this.end = e;
		// TODO Auto-generated constructor stub
	}

	public double end;

	@Override
	public void draw(Canvas canvas, ScalaView skala) {

		if(visibility == Visibility.ONLYHERE){
			if(skala.getZoomLvl() != visibilityZoom)
				return;
		}
		if(visibility == Visibility.HEREANDMINUS){
			if(skala.getZoomLvl() > visibilityZoom)
				return;
		}
		if(visibility == Visibility.HEREANDPLUS){
			if(skala.getZoomLvl() < visibilityZoom)
				return;
		}

		float y = skala.getPos(start);
		float y2 = skala.getPos(end);
		float xx = x + skala.getDx();
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		mPaint.setColor(colorLine);
		mPaint.setColor(colorLine);
		if (y == NkSkala.ABOVE) {
			if (y2 == NkSkala.BELOW) {
				// prolazi kroz ceo prozor
				if(style == 1)
					canvas.drawLine(xx, 0, xx, skala.getHeight(), mPaint);
				else
					canvas.drawRect(xx-size, 0, xx, skala.getHeight(), mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				canvas.drawText(name, -skala.getHeight() / 2, xx - 4, mPaint);
				canvas.restore();
			}
			if (y2 >= 0 && y2 != NkSkala.BELOW) {
				// donji kraj se savrsava u prozoru
				if(style == 1)
					canvas.drawLine(xx, 0, xx, y2, mPaint);
				else
					canvas.drawRect(xx-size, 0, xx, y2, mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				canvas.drawText(name, -y2, xx - 4, mPaint);
				canvas.restore();
			}
		}
		if (y != NkSkala.BELOW && y != NkSkala.ABOVE) {
			if (y2 == NkSkala.BELOW) {
				// gornji kraj pocenje u prozoru
				if(style == 1)
					canvas.drawLine(xx, y, xx, skala.getHeight(), mPaint);
				else
					canvas.drawRect(xx-size, y, xx, skala.getHeight(), mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				mPaint.setTextAlign(Align.RIGHT);
				canvas.drawText(name, -y, xx - 4, mPaint);
				mPaint.setTextAlign(Align.LEFT);
				canvas.restore();
			}
			if (y2 >= 0 && y2 != NkSkala.BELOW) {
				// cela je u prozoru
				if(style == 1)
					canvas.drawLine(xx, y, xx, y2, mPaint);
				else
					canvas.drawRect(xx-size, y, xx, y2, mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				mPaint.setTextAlign(Align.CENTER);
				canvas.drawText(name, -(y + (y2 - y) / 2), xx - 4, mPaint);
				mPaint.setTextAlign(Align.LEFT);
				canvas.restore();
			}
		}
	}
	// is epoch currently drawn on x,y position
	public boolean isOnPosition(float xx, float yy, ScalaView skala) {
		float y=skala.getPos(start);
		float y2=skala.getPos(end);
		if(y2 == NkSkala.ABOVE)
			return false;
		if(y2 == NkSkala.BELOW)
			y2 =  skala.getHeight();
		float ss = (size+10)/2;
		if(xx>=x-ss && xx<=x+ss){
			if(yy>=y-10 && yy<=y2+10)
				return true;
		}
		return false;
	}
	// write epoch to file
	public void serialize(ObjectOutputStream os) throws IOException {
		os.writeInt(1);
		os.writeObject(this);
	}

}
