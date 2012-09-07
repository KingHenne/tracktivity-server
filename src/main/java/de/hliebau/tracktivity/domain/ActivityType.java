package de.hliebau.tracktivity.domain;

public enum ActivityType {

	CYCLING("Cycling"), //
	HIKING("Hiking"), //
	INLINE("Inline Skating"), //
	RUNNING("Running");

	String label;

	ActivityType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
