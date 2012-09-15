package de.hliebau.tracktivity.api.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.hliebau.tracktivity.domain.Activity;

@XmlRootElement(name = "activity")
public class ThinActivity {

	private Activity activity;

	public ThinActivity() {
	}

	public ThinActivity(Activity activity) {
		this.activity = activity;
	}

	@XmlElement
	public Date getCreated() {
		return activity.getCreated();
	}

	@XmlAttribute
	public Long getId() {
		return activity.getId();
	}

	@XmlElement
	public String getName() {
		return activity.getName();
	}

	@XmlElement
	public String getRef() {
		return String.format("/activities/%d", activity.getId());
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
