package de.hliebau.tracktivity.api.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.hliebau.tracktivity.domain.Activity;

@XmlRootElement(name = "activities")
public class ThinActivities {

	private List<ThinActivity> activities;

	public ThinActivities() {
		activities = new ArrayList<ThinActivity>();
	}

	public ThinActivities(List<Activity> activities) {
		this();
		for (Activity activity : activities) {
			this.activities.add(new ThinActivity(activity));
		}
	}

	@XmlElement(name = "activity")
	public List<ThinActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<ThinActivity> activities) {
		this.activities = activities;
	}

}
