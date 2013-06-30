package de.hliebau.tracktivity.live;

import java.util.Date;

import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.TrackPoint;

public class WebSocketMessage {

	private ActivityType activityType;

	private EventType event;

	private TrackPoint point;

	private Date time;

	public ActivityType getActivityType() {
		return activityType;
	}

	public EventType getEvent() {
		return event;
	}

	public TrackPoint getPoint() {
		return point;
	}

	public Date getTime() {
		return time;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public void setEvent(EventType event) {
		this.event = event;
	}

	public void setPoint(TrackPoint point) {
		this.point = point;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
