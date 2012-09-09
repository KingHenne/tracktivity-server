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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class Activity extends AbstractEntity {

	@JsonProperty
	private Date created;

	@JsonProperty
	private String name;

	@JsonProperty
	private Track track;

	@JsonProperty
	private ActivityType type;

	private User user;

	public Activity() {
	}

	public Activity(ActivityType type) {
		this.type = type;
	}

	public Activity(Track track) {
		this.track = track;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@XmlElement
	public Date getCreated() {
		return created;
	}

	@Override
	@Transient
	@XmlAttribute
	@JsonProperty
	public Long getId() {
		return super.getId();
	}

	@XmlElement
	public String getName() {
		return name;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
	@JoinColumn(name = "track_id", insertable = true, nullable = true)
	@XmlElement(name = "trk")
	public Track getTrack() {
		return track;
	}

	@Enumerated(EnumType.STRING)
	@XmlElement
	public ActivityType getType() {
		return type;
	}

	@ManyToOne(optional = false)
	public User getUser() {
		return user;
	}

	public void setCreated(Date created) {
		this.created = created;
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

}
