package diet;

public class WorkingHours implements Comparable<WorkingHours> {

	
	private final Time open;
	private final Time close;
	
	public WorkingHours(String open, String close) {
		String [] open_h_m = open.split(":");
		String [] close_h_m = close.split(":");
		this.open = new Time(Integer.parseInt(open_h_m[0]), Integer.parseInt(open_h_m[1]));
		this.close = new Time(Integer.parseInt(close_h_m[0]), Integer.parseInt(close_h_m[1]));
	}
	
	public Time getOpen() {
		return open;
	}

	boolean includes(Time t) {
		return open.compareTo(t)<=0 && close.compareTo(t)>=0;
	}

	@Override
	public int compareTo(WorkingHours o) {
		return open.compareTo(o.open);
	}

}
