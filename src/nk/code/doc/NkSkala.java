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

	public static final int ABOVE = -3; // top
	public static final int BELOW = -2; // down
	public static final int INVALID = -1;

	public static final int LENDEF = 100; //standardna duzina intervala u pixelima
	public static final int GODINA = 365; //standardna godina

	private double startDate = 3000000; //pocetak skale u JD
	private double len = LENDEF;  //duzina intervala u pixelima
	private TextPaint textPaint;  //glavni text na skali
	private TextPaint mesecPaint; //pomocni tekst za mesece
	float textHeight;   		  //visina glavnog teksta 
	
	double dy = 75000;  		  //pocetak skale po u osi ovde se na pocetku
								  //nalazi startdate 
	
	private int period = 1; //koliko godina ima u intervalu len
	private int scalaHeith; 		//visina widgeta
	private int zoomlen = LENDEF;   //zoom

	private String[] meseci = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public double getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public NkSkala() {
		textPaint = new TextPaint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(12);
		textHeight = textPaint.getTextSize();
		startDate = DateTimeUtils.toJulianDay(new DateTime(3520, 1, 1, 0, 0)
		.getMillis());
		mesecPaint = new TextPaint();
		mesecPaint.setColor(Color.argb(255, 70, 70, 70));
		mesecPaint.setTextAlign(Paint.Align.CENTER);
		mesecPaint.setTextSize(10);
	}

	public void Init(int sh) {
		scalaHeith = sh;
	}

	public void draw(Canvas canvas, int w, int h) {
		int r = (int) (dy / len);
		DateTime dt = new DateTime(DateTimeUtils.fromJulianDay(getStartDate()));
		float raz = (float) (dy % len);
		// double date =startDate-(r*period*365);
		dt = dt.minusYears(r * period);
		dt = dt.plusYears(period);
		for (float y = -raz; y <= h; y += getLen()) {
			String ss = Integer.toString(dt.getYear());
			canvas.drawText(ss, textPaint.getTextSize() + 15, y + textHeight
					/ 2, textPaint);
			// canvas.drawLine(0, y, 10, y, textPaint);
			if (zoomlen > 340) {
				float n = (float) (y + getLen() / 12.0);
				for (int i = 11; i > 0; i--) {
					canvas.drawText(meseci[i], textPaint.getTextSize() + 15, n,
							mesecPaint);
					n = n + (float) (getLen() / 12.0);
				}
			}
			if (zoomlen > 100) {
				float n = (float) (y + getLen() / 2);
				canvas.drawLine(0, n - mesecPaint.getTextSize() / 2, 10, n
						- mesecPaint.getTextSize() / 2, textPaint);
			}
			dt = dt.minusYears(period);
		}
	}

	// move scale by dy
	public void posmak(float y) {
		// koliko duzina se preskace a dodaje vremena
		dy -= y;
		if (dy < 0)
			dy = 0;
	}

	public void zoom(float sc, float y) {

		// zapamtiti datum koji se trenutno nalazi na y
		// uraditi skaliranje a zatim datum poji se nalazio na y koordinata
		// postaviti ga ponovo na y
		// koji datum se trenutno nalazi na y poziciji
		double d = getDate(y);
		// uvelicati

		zoomlen = (int) (LENDEF * sc);
		if (zoomlen >= 50) {
			len = zoomlen;
			period = 1;
		} else if (zoomlen >= 40) {
			len = LENDEF;
			period = 5;
		} else if (zoomlen >= 35) {
			len = LENDEF;
			period = 10;

		} else if (zoomlen >= 30) {
			len = LENDEF;
			period = 50;
		} else if (zoomlen >= 25) {
			len = LENDEF;
			period = 100;
		} else if (zoomlen < 25) {
			zoomlen = 20;
			period = 500;
		}
		// pomeriti skalu da se taj datum vrati na istu poziciju y
		setDateOnPos(d, y);

	}

	// postavlja datun na poziciji y na skali
	public void setDateOnPos(double date, float y) {
		double d = getDate(y);
		double raz = d - date;
		dy += (int) ((raz / GODINA) * ((double) len / (double) period));
		if (dy < 0)
			dy = 0;
	}

	public double getDate(float y) {
		if (y < (0))
			return ABOVE;
		if (y > scalaHeith)
			return BELOW;

		double podeoka = (double) (y + dy) / (double) getLen();
		double per = podeoka * period * GODINA;
		double date = getStartDate() - per;
		return date;
	}

	// return position on scale for given date
	public float getPos(double date) {
		// Log.d("nk-odnos","ulaz");
		if (date > getDate(0))
			return ABOVE;
		double enddate = getEndDate();
		if (date < enddate)
			return BELOW;
		double per = getStartDate() - date;
		double podeoka = per / (period * GODINA);
		double y = (podeoka * (double) getLen()) - dy;

		return (float) y;
	}

	public void SetZoom(float scale) {
		setLen((int) scale);
	}

	// date on other side
	double getEndDate() {
		return getDate(scalaHeith);
	}

	public double getStartDate() {
		return startDate;
	}

}
