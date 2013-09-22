package de.hliebau.tracktivity.live;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.util.JsonDateSerializer;

@JsonInclude(Include.NON_NULL)
public class WebSocketMessage {

	private ActivityType activityType;

	private EventType event;

	private TrackPoint point;

	private Date time;

	private String username;

	public ActivityType getActivityType() {
		return activityType;
	}

	public EventType getEvent() {
		return event;
	}

	public TrackPoint getPoint() {
		return point;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getTime() {
		return time;
	}

	public String getUsername() {
		return username;
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

	public void setUsername(String username) {
		this.username = username;
	}

}
