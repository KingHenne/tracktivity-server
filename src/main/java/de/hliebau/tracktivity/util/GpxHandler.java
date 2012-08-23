package de.hliebau.tracktivity.util;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

public class GpxHandler extends DefaultHandler {

	enum ElementName {
		DESC, ELE, NAME, TIME, TRK, TRKPT, TRKSEG;

		public static ElementName findByName(String name) {
			for (ElementName v : values()) {
				if (v.name().equalsIgnoreCase(name)) {
					return v;
				}
			}
			return null;
		}
	}

	private TrackPoint currentPoint;

	private TrackSegment currentSegment;

	private final Track gpxTrack;

	private final Logger logger;

	private final StringBuffer sb = new StringBuffer(128);

	public GpxHandler(Track track) {
		super();
		logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		this.gpxTrack = track;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		sb.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		ElementName elementName = ElementName.findByName(qName);
		String s = sb.toString();
		if (elementName != null) {
			switch (elementName) {
			case TRK:
				// gpxTrack.generateLines();
				break;
			case NAME:
				logger.debug("Importing new track with name: " + s);
				gpxTrack.setName(s);
				break;
			case DESC:
				gpxTrack.setDescription(s);
				break;
			case TRKSEG:
				// currentSegment.generateLine();
				gpxTrack.addSegment(currentSegment);
				break;
			case TRKPT:
				currentSegment.addPoint(currentPoint);
				break;
			case ELE:
				double ele = Double.parseDouble(s);
				double lon = currentPoint.getPoint().getX();
				double lat = currentPoint.getPoint().getY();
				currentPoint = new TrackPoint(lon, lat, ele);
				break;
			case TIME:
				try {
					Calendar cal = DatatypeConverter.parseDateTime(s);
					currentPoint.setUtcTime(cal.getTime());
				} catch (IllegalArgumentException e) {
					logger.error("Failed parsing date string: " + s);
				}
				break;
			default:
				break;
			}
		}
	}

	public Track getGpxTrack() {
		return gpxTrack;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		ElementName elementName = ElementName.findByName(qName);
		if (elementName != null) {
			sb.setLength(0); // reset string buffer
			switch (elementName) {
			case TRKSEG:
				currentSegment = new TrackSegment();
				break;
			case TRKPT:
				double lon = Double.parseDouble(attributes.getValue("lon"));
				double lat = Double.parseDouble(attributes.getValue("lat"));
				currentPoint = new TrackPoint(lon, lat);
				break;
			default:
				break;
			}
		}
	}
}
