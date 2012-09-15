package de.hliebau.tracktivity.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "TracktivityUser")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class User extends AbstractEntity {

	private List<Activity> activities = new ArrayList<Activity>();

	@Size(min = 2, max = 50, message = "Your first name must be between 2 and 50 characters long.")
	private String firstname;

	@Size(min = 2, max = 50, message = "Your last name must be between 2 and 50 characters long.")
	private String lastname;

	@Size(min = 6, message = "The password must be at least 6 characters long.")
	private String password;

	@Size(min = 3, max = 20, message = "The username must be between 3 and 20 characters long.")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "The username must be alphanumeric with no spaces.")
	private String username;

	public User() {
	}

	public User(String username) {
		this.username = username;
	}

	public void addActivity(Activity activity) {
		activities.add(activity);
		activity.setUser(this);
	}

	public void deleteActivity(Activity activity) {
		activities.remove(activity);
	}

	public boolean equals(User user) {
		return user.getId() == this.getId();
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	public List<Activity> getActivities() {
		return activities;
	}

	@XmlElement
	@JsonProperty
	public String getFirstname() {
		return firstname;
	}

	@XmlElement
	@JsonProperty
	public String getLastname() {
		return lastname;
	}

	public String getPassword() {
		return password;
	}

	@Column(unique = true)
	@XmlElement
	@JsonProperty
	public String getUsername() {
		return username;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
