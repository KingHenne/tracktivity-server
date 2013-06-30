package de.hliebau.tracktivity.live;

import de.hliebau.tracktivity.domain.TrackPoint;

public class WebSocketMessage {

	private EventType event;

	private TrackPoint point;

	public EventType getEvent() {
		return event;
	}

	public TrackPoint getPoint() {
		return point;
	}

	public void setEvent(EventType event) {
		this.event = event;
	}

	public void setPoint(TrackPoint point) {
		this.point = point;
	}

}
