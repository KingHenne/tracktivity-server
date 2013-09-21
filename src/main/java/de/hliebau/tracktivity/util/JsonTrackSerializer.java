package de.hliebau.tracktivity.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vividsolutions.jts.geom.Coordinate;

import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

public class JsonTrackSerializer extends StdSerializer<Track> {

	public JsonTrackSerializer(Class<Track> t) {
		super(t);
	}

	@Override
	public void serialize(Track track, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();

		// write the MultiPolyline
		jgen.writeArrayFieldStart("multiPolyline");
		for (TrackSegment s : track.getSegments()) {
			jgen.writeStartArray();
			for (TrackPoint p : s.getPoints()) {
				jgen.writeStartArray();
				jgen.writeNumber(p.getLatitude());
				jgen.writeNumber(p.getLongitude());
				jgen.writeEndArray();
			}
			jgen.writeEndArray();
		}
		jgen.writeEndArray();

		// write the latitude/longitude bounds
		jgen.writeArrayFieldStart("latLngBounds");
		Coordinate[] convexHull = track.getLines().convexHull().getCoordinates();
		for (Coordinate c : convexHull) {
			jgen.writeStartArray();
			jgen.writeNumber(c.y);
			jgen.writeNumber(c.x);
			jgen.writeEndArray();
		}
		jgen.writeEndArray();

		jgen.writeEndObject();
		jgen.close();
	}

}
