package de.hliebau.tracktivity.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Activities {

	@XmlElement(name = "activity")
	public List<Activity> activities;

	public Activities() {
	}

	public Activities(List<Activity> activities) {
		this();
		this.activities = activities;
	}

}
