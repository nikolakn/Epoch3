package nk.code.data;

public class Event {	
	public double start = 0;
	public String name = "";
	public int size = 20;
	public boolean hasimage = false;
	public int look = 1;	
	public int x = 50;
	
	public Event(double s, int x,String n){
		name = n;
		start = s;
		this.x = x;
	}
}
