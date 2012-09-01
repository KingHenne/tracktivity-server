package de.hliebau.tracktivity.util;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Service;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

@Service
public class GpxParser {

	private final XMLInputFactory factory;

	public GpxParser() {
		factory = XMLInputFactory.newInstance();
	}

	public Activity createActivity(InputStream in) {
		Activity activity = new Activity(new Track());
		TrackPoint currentPoint = null;
		TrackSegment currentSegment = null;

		Set<String> inElement = new HashSet<String>(20);

		try {
			XMLStreamReader reader = factory.createXMLStreamReader(in);
			for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					String startElementName = reader.getLocalName().toLowerCase();
					inElement.add(startElementName);
					if (startElementName.equals("trkseg")) {
						currentSegment = new TrackSegment();
					} else if (startElementName.equals("trkpt")) {
						double lon = Double.parseDouble(reader.getAttributeValue(null, "lon"));
						double lat = Double.parseDouble(reader.getAttributeValue(null, "lat"));
						currentPoint = new TrackPoint(lon, lat);
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					String endElementName = reader.getLocalName().toLowerCase();
					inElement.remove(endElementName);
					if (endElementName.equals("trk")) {
						Date date = activity.getTrack().getStartingPoint().getUtcTime();
						if (date != null) {
							activity.setCreated(date);
						} else {
							activity.setCreated(new Date());
						}
					} else if (endElementName.equals("trkseg")) {
						activity.getTrack().addSegment(currentSegment);
						currentSegment = null;
					} else if (endElementName.equals("trkpt")) {
						currentSegment.addPoint(currentPoint);
						currentPoint = null;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if (inElement.contains("trk")) {
						if (inElement.contains("name")) {
							activity.setName(reader.getText());
						} else if (inElement.contains("trkseg") && inElement.contains("trkpt")) {
							if (inElement.contains("ele")) {
								double ele = Double.parseDouble(reader.getText());
								double lon = currentPoint.getPoint().getX();
								double lat = currentPoint.getPoint().getY();
								currentPoint = new TrackPoint(lon, lat, ele);
							} else if (inElement.contains("time")) {
								Calendar cal = DatatypeConverter.parseDateTime(reader.getText());
								currentPoint.setUtcTime(cal.getTime());
							}
						}
					}
					break;
				case XMLStreamConstants.CDATA:
					if (inElement.contains("trk") && inElement.contains("name")) {
						activity.setName(reader.getText());
					}
					break;
				}
			}
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return null;
		}
		return activity;
	}

}
