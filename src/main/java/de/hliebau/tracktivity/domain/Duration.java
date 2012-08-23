package de.hliebau.tracktivity.domain;

public class Duration {

	private int hours;

	private int millis;

	private int minutes;

	private int seconds;

	private long totalMillis;

	public Duration() {
		hours = 0;
		minutes = 0;
		seconds = 0;
		millis = 0;
		totalMillis = 0L;
	}

	public Duration(long millis) {
		convertMillis(millis);
	}

	public void add(Duration d) {
		convertMillis(totalMillis + d.getTotalMillis());
	}

	private void convertMillis(long millis) {
		this.totalMillis = millis;
		this.hours = (int) Math.floor(millis / 1000 / 60 / 60);
		long hoursInMillis = hours * 60 * 60 * 1000;
		this.minutes = (int) Math.floor((millis - hoursInMillis) / 1000 / 60);
		long minutesInMillis = minutes * 60 * 1000;
		this.seconds = (int) Math.floor((millis - hoursInMillis - minutesInMillis) / 1000);
		long secondsInMillis = seconds * 1000;
		this.millis = (int) (millis - hoursInMillis - minutesInMillis - secondsInMillis);
	}

	public int getHours() {
		return hours;
	}

	public int getMillis() {
		return millis;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public long getTotalMillis() {
		return totalMillis;
	}

	@Override
	public String toString() {
		return String.format("%1d%c %2d%c %2d%c", hours, 'h', minutes, 'm', seconds, 's');
	}

}
