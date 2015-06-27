package nk.code.data;

import nk.code.doc.NkSkala;
import nk.code.epoch.ScalaView;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Epoch extends Event {

	public Epoch(double s,int x, String n) {
		super(s, x, n);
		// TODO Auto-generated constructor stub
	}

	public double end;
	
	@Override
	public void draw(Canvas canvas, ScalaView skala) {
        float y = skala.getPos(start);
        float y2 = skala.getPos(end);
        if(y==NkSkala.ABOVE){
        	if(y2==NkSkala.BELOW){
        	//prolazi kroz ceo prozor
        		
        	}
        	if(y2 >= 0 && y2 != NkSkala.BELOW) {
        	//donji kraj se savrsava u prozoru	
        	}
        }        		
        if(y != NkSkala.BELOW && y != NkSkala.ABOVE ) {
        	if(y2==NkSkala.BELOW){
        	//gornji kraj pocenje u prozoru
        		
        	}
        	if(y2 >= 0 && y2 != NkSkala.BELOW) {
        	//cela je u prozoru	
        	}	
        }		
	}
}
