package de.hliebau.tracktivity.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.stereotype.Service;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

@Service
public class GpxParser {

	private final XMLInputFactory inputFactory;

	private final XMLOutputFactory outputFactory;

	public GpxParser() {
		inputFactory = XMLInputFactory.newInstance();
		outputFactory = XMLOutputFactory.newInstance();
	}

	public Activity createActivity(InputStream in) {
		Activity activity = new Activity(new Track());
		TrackPoint currentPoint = null;
		TrackSegment currentSegment = null;

		Set<String> inElement = new HashSet<String>(20);

		try {
			XMLStreamReader r = inputFactory.createXMLStreamReader(in);
			for (int event = r.next(); event != XMLStreamConstants.END_DOCUMENT; event = r.next()) {
				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					String startElementName = r.getLocalName().toLowerCase();
					inElement.add(startElementName);
					if (startElementName.equals("trkseg")) {
						currentSegment = new TrackSegment();
					} else if (startElementName.equals("trkpt")) {
						double lon = Double.parseDouble(r.getAttributeValue(null, "lon"));
						double lat = Double.parseDouble(r.getAttributeValue(null, "lat"));
						currentPoint = new TrackPoint(lon, lat);
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					String endElementName = r.getLocalName().toLowerCase();
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
							activity.setName(r.getText());
						} else if (inElement.contains("trkseg") && inElement.contains("trkpt")) {
							if (inElement.contains("ele")) {
								double ele = Double.parseDouble(r.getText());
								double lon = currentPoint.getPoint().getX();
								double lat = currentPoint.getPoint().getY();
								currentPoint = new TrackPoint(lon, lat, ele);
							} else if (inElement.contains("time")) {
								Calendar cal = DatatypeConverter.parseDateTime(r.getText());
								currentPoint.setUtcTime(cal.getTime());
							}
						}
					}
					break;
				case XMLStreamConstants.CDATA:
					if (inElement.contains("trk") && inElement.contains("name")) {
						activity.setName(r.getText());
					}
					break;
				}
			}
			r.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return null;
		}
		return activity;
	}

	public String exportActivity(Activity activity) {
		OutputStream stream = new ByteArrayOutputStream();
		try {
			XMLStreamWriter w = outputFactory.createXMLStreamWriter(stream);
			w.writeStartDocument();
			w.writeStartElement("gpx");
			w.writeDefaultNamespace("http://www.topografix.com/GPX/1/1");
			w.writeNamespace("xsi", "http://www.w3.org/2000/10/XMLSchema-instance");
			w.writeAttribute("http://www.w3.org/2000/10/XMLSchema-instance", "schemaLocation",
					"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
			w.writeStartElement("trk");
			w.writeStartElement("name");
			w.writeCharacters(activity.getName());
			w.writeEndElement(); // name
			for (TrackSegment segment : activity.getTrack().getSegments()) {
				writeSegment(w, segment);
			}
			w.writeEndElement(); // trk
			w.writeEndElement(); // gpx
			w.writeEndDocument();
			w.flush();
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return null;
		}
		return stream.toString();
	}

	private void writePoint(XMLStreamWriter w, TrackPoint p) throws XMLStreamException {
		w.writeStartElement("trkpt");

		// write latitude and longitude attribute values
		w.writeAttribute("lat", Double.toString(p.getLatitude()));
		w.writeAttribute("lon", Double.toString(p.getLongitude()));

		// write elevation element
		Double ele = p.getElevation();
		if (ele != null) {
			w.writeStartElement("ele");
			w.writeCharacters(ele.toString());
			w.writeEndElement(); // ele
		}

		// write time element
		w.writeStartElement("time");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(p.getUtcTime());
		w.writeCharacters(DatatypeConverter.printDateTime(cal));
		w.writeEndElement(); // time

		w.writeEndElement(); // trkpt
	}

	private void writeSegment(XMLStreamWriter w, TrackSegment s) throws XMLStreamException {
		w.writeStartElement("trkseg");
		for (TrackPoint point : s.getPoints()) {
			writePoint(w, point);
		}
		w.writeEndElement();
	}

}
