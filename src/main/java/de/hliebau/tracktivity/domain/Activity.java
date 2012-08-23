package de.hliebau.tracktivity.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class Activity extends AbstractEntity {

	private ActivityType type;

	private User user;

	public Activity() {
	}

	public Activity(ActivityType type) {
		this.type = type;
	}

	@Enumerated(EnumType.STRING)
	public ActivityType getType() {
		return type;
	}

	@ManyToOne(optional = false)
	public User getUser() {
		return user;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
