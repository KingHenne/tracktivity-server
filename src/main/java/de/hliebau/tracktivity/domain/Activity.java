package de.hliebau.tracktivity.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Activity extends AbstractEntity {

	private Track track;

	private ActivityType type;

	private User user;

	public Activity() {
	}

	public Activity(ActivityType type) {
		this.type = type;
	}

	@OneToOne(fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
	@JoinColumn(name = "track_id", insertable = true, nullable = true)
	public Track getTrack() {
		return track;
	}

	@Enumerated(EnumType.STRING)
	public ActivityType getType() {
		return type;
	}

	@ManyToOne(optional = false)
	public User getUser() {
		return user;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
