package de.hliebau.tracktivity.api.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonRootName;

import de.hliebau.tracktivity.domain.Activity;

@XmlRootElement(name = "activity")
@JsonRootName("activity")
public class ThinActivity {

	private Activity activity;

	public ThinActivity() {
	}

	public ThinActivity(Activity activity) {
		this.activity = activity;
	}

	// @XmlElement
	// public Date getCreated() {
	// return activity.getCreated();
	// }

	@XmlAttribute
	public String getId() {
		return activity.getId().toString();
	}

	// @XmlElement
	// public String getName() {
	// return activity.getName();
	// }

	// @XmlElement
	// public String getRef() {
	// return String.format("/activities/%d", activity.getId());
	// }

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
