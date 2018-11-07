package xyz.parala.game.util;

public class Timer {
	private double startTime;
	private double endTime;
	private float lastLoopDuration;
	
	public Timer() {
		
	}
	
	public void start() {
		startTime = System.nanoTime();
	}
	
	public float stop() {
		endTime = System.nanoTime();
		lastLoopDuration = (float) (endTime - startTime);
		return lastLoopDuration * (float)( Math.pow(10, -9));
	}

}
