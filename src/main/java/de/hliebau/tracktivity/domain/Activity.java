package de.hliebau.tracktivity.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Activity extends AbstractEntity {

	private String description;

	private String name;

	private Track track;

	private ActivityType type;

	private User user;

	private Date utcStart;

	public Activity() {
	}

	public Activity(ActivityType type) {
		this.type = type;
	}

	public Activity(Track track) {
		this.track = track;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getUtcStart() {
		return utcStart;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setUtcStart(Date utcStart) {
		this.utcStart = utcStart;
	}

}
