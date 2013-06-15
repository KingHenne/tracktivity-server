package de.hliebau.tracktivity.util;

import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

public abstract class XmlParser {

	protected Activity currentActivity;

	protected TrackPoint currentPoint;

	protected TrackSegment currentSegment;

	protected Set<String> inElement;

	protected final XMLInputFactory inputFactory;

	protected final XMLOutputFactory outputFactory;

	public XmlParser() {
		inputFactory = XMLInputFactory.newInstance();
		outputFactory = XMLOutputFactory.newInstance();
	}

	public Activity createActivity(InputStream in) {
		inElement = new HashSet<String>(20);
		currentActivity = new Activity(new Track());
		currentPoint = null;
		currentSegment = null;

		try {
			XMLStreamReader reader = inputFactory.createXMLStreamReader(in);
			for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					this.onStartElement(reader);
					break;
				case XMLStreamConstants.END_ELEMENT:
					this.onEndElement(reader);
					break;
				case XMLStreamConstants.CHARACTERS:
					this.onCharacters(reader);
					break;
				case XMLStreamConstants.CDATA:
					this.onCdata(reader);
					break;
				}
			}
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return null;
		}
		return currentActivity;
	}

	public abstract String exportActivity(Activity activity);

	public abstract void onCdata(XMLStreamReader reader);

	public abstract void onCharacters(XMLStreamReader reader);

	public abstract void onEndElement(XMLStreamReader reader);

	public abstract void onStartElement(XMLStreamReader reader);

	protected void updateCurrentPoint(double lat, double lon) {
		updateCurrentPoint(lat, lon, currentPoint.getElevation());
	}

	protected void updateCurrentPoint(double lat, double lon, Double ele) {
		Date time = currentPoint.getUtcTime();
		if (ele == null) {
			currentPoint = new TrackPoint(lon, lat);
		} else {
			currentPoint = new TrackPoint(lon, lat, ele);
		}
		currentPoint.setUtcTime(time);
	}

}