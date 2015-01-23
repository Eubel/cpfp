package cpfp.app;

import cpfp.exceptions.StopwatchException;

public class StopWatch {
	private long startMillis = -1;
	private long endMillis = -1;
	
	public void start() {			
		this.startMillis = System.currentTimeMillis();
	}
	
	public void stop() throws StopwatchException{
		if(this.startMillis == -1) throw new StopwatchException("The Stopwatch has not been startet yet!");
		
		this.endMillis = System.currentTimeMillis();
	}
	
	public long getMillis () throws StopwatchException {
		if(this.startMillis == -1 || this.endMillis == -1) throw new StopwatchException("Stopwatch has to be startet end stopped!");
		
		long res = this.endMillis - this.startMillis;
		this.endMillis = -1;
		this.startMillis = -1;
		return res;
	}
	
	public long getSeconds () throws StopwatchException {
		if(this.startMillis == -1 || this.endMillis == -1) throw new StopwatchException("Stopwatch has to be startet end stopped!");
		
		long res = Math.round((double)(this.endMillis - this.startMillis) / 1000);
		this.endMillis = -1;
		this.startMillis = -1;
		return res;
	}

}
