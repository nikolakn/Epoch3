package nk.code.data;

import nk.code.doc.NkSkala;
import nk.code.epoch.ScalaView;
import android.graphics.Canvas;
import android.graphics.Paint.Align;

public class Epoch extends Event {

	public Epoch(double s, int x, String n) {
		super(s, x, n);
		// TODO Auto-generated constructor stub
	}

	public double end;

	@Override
	public void draw(Canvas canvas, ScalaView skala) {
		float y = skala.getPos(start);
		float y2 = skala.getPos(end);
		mPaint.setColor(colorLine);
		if (y == NkSkala.ABOVE) {
			if (y2 == NkSkala.BELOW) {
				// prolazi kroz ceo prozor
				canvas.drawLine(x, 0, x, skala.getHeight(), mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				canvas.drawText(name, -skala.getHeight() / 2, x - 4, mPaint);
				canvas.restore();
			}
			if (y2 >= 0 && y2 != NkSkala.BELOW) {
				// donji kraj se savrsava u prozoru
				canvas.drawLine(x, 0, x, y2, mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				canvas.drawText(name, -y2, x - 4, mPaint);
				canvas.restore();
			}
		}
		if (y != NkSkala.BELOW && y != NkSkala.ABOVE) {
			if (y2 == NkSkala.BELOW) {
				// gornji kraj pocenje u prozoru
				canvas.drawLine(x, y, x, skala.getHeight(), mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				mPaint.setTextAlign(Align.RIGHT);
				canvas.drawText(name, -y, x - 4, mPaint);
				mPaint.setTextAlign(Align.LEFT);
				canvas.restore();
			}
			if (y2 >= 0 && y2 != NkSkala.BELOW) {
				// cela je u prozoru
				canvas.drawLine(x, y, x, y2, mPaint);
				canvas.save();
				canvas.rotate(-90);
				mPaint.setColor(colorText);
				mPaint.setTextAlign(Align.CENTER);
				canvas.drawText(name, -(y + (y2 - y) / 2), x - 4, mPaint);
				mPaint.setTextAlign(Align.LEFT);
				canvas.restore();
			}
		}
	}
}
