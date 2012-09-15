package de.hliebau.tracktivity.domain;

import java.util.ArrayList;

// A wrapper class, so that type info (of TrackSegment) is passed through the message converters to Jackson.
public class TrackSegments extends ArrayList<TrackSegment> {

	private static final long serialVersionUID = 8165041520656705745L;

	public TrackSegments() {
		super();
	}

}