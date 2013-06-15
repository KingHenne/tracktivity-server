package de.hliebau.tracktivity.util;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Service;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

@Service
public class TcxParser extends XmlParser {

	@Override
	public String exportActivity(Activity activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCdata(XMLStreamReader reader) {
	}

	@Override
	public void onCharacters(XMLStreamReader reader) {
		if (inElement.contains("activity")) {
			if (inElement.contains("track") && inElement.contains("trackpoint")) {
				if (inElement.contains("position")) {
					if (inElement.contains("latitudedegrees")) {
						double lat = Double.parseDouble(reader.getText());
						double lon = currentPoint.getLongitude();
						updateCurrentPoint(lat, lon);
					} else if (inElement.contains("longitudedegrees")) {
						double lat = currentPoint.getLatitude();
						double lon = Double.parseDouble(reader.getText());
						updateCurrentPoint(lat, lon);
					}
				} else if (inElement.contains("altitudemeters")) {
					double ele = Double.parseDouble(reader.getText());
					double lon = currentPoint.getLongitude();
					double lat = currentPoint.getLatitude();
					updateCurrentPoint(lat, lon, ele);
				} else if (inElement.contains("time")) {
					Calendar cal = DatatypeConverter.parseDateTime(reader.getText());
					currentPoint.setUtcTime(cal.getTime());
				}
			}
		}
	}

	@Override
	public void onEndElement(XMLStreamReader reader) {
		String endElementName = reader.getLocalName().toLowerCase();
		inElement.remove(endElementName);
		if (endElementName.equals("activity")) {
			Date date = currentActivity.getTrack().getStartingPoint().getUtcTime();
			if (date != null) {
				currentActivity.setCreated(date);
			} else {
				currentActivity.setCreated(new Date());
			}
		} else if (endElementName.equals("track")) {
			currentActivity.getTrack().addSegment(currentSegment);
			currentSegment = null;
		} else if (endElementName.equals("trackpoint")) {
			currentSegment.addPoint(currentPoint);
			currentPoint = null;
		}
	}

	@Override
	public void onStartElement(XMLStreamReader reader) {
		String startElementName = reader.getLocalName().toLowerCase();
		inElement.add(startElementName);
		if (startElementName.equals("track")) {
			currentSegment = new TrackSegment();
		} else if (startElementName.equals("trackpoint")) {
			currentPoint = new TrackPoint(0, 0); // just placeholder values
		}
	}

}
