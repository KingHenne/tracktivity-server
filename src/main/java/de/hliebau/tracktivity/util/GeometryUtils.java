package de.hliebau.tracktivity.util;

import java.util.List;

import javax.annotation.PostConstruct;

import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

@Service
public class GeometryUtils {

	public static final int EARTH_RADIUS = 6378137;

	private static GeometryUtils INSTANCE;

	public static GeometryUtils getInstance() {
		return INSTANCE;
	}

	public static String toLatLngArrayString(Coordinate[] coords) {
		if (coords.length == 1) {
			return toLatLngString(coords[0]);
		}
		StringBuilder sb = new StringBuilder(coords.length * 21);
		sb.append('[');
		for (int i = 0; i < coords.length; i++) {
			sb.append(toLatLngString(coords[i])).append(',');
		}
		sb.deleteCharAt(sb.length() - 1); // remove last comma
		return sb.append(']').toString();
	}

	public static String toLatLngString(Coordinate coord) {
		double lat = coord.y;
		double lng = coord.x;
		return toLatLngString(lat, lng);
	}

	public static String toLatLngString(double lat, double lng) {
		StringBuilder sb = new StringBuilder(21);
		sb.append('[').append(lat).append(',').append(lng).append(']');
		return sb.toString();
	}

	public static String toLatLngString(TrackPoint point) {
		return toLatLngString(point.getLatitude(), point.getLongitude());
	}

	private GeodeticCalculator geocalc;

	@Autowired
	private GeometryFactory geometryFactory;

	public LineString createLineString(List<TrackPoint> points) {
		Coordinate[] coordinates = new Coordinate[points.size()];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = points.get(i).getPoint().getCoordinate();
		}
		return geometryFactory.createLineString(coordinates);
	}

	public MultiLineString createMultiLineString(List<TrackSegment> segments) {
		LineString[] lineStrings = new LineString[segments.size()];
		for (int i = 0; i < lineStrings.length; i++) {
			lineStrings[i] = segments.get(i).getLine();
		}
		return geometryFactory.createMultiLineString(lineStrings);
	}

	public Point createPoint(double lon, double lat) {
		return geometryFactory.createPoint(new Coordinate(lon, lat));
	}

	public Point createPoint(double lon, double lat, double ele) {
		return geometryFactory.createPoint(new Coordinate(lon, lat, ele));
	}

	public double getDistance(TrackPoint p1, TrackPoint p2) {
		geocalc.setStartingGeographicPoint(p1.getLongitude(), p1.getLatitude());
		geocalc.setDestinationGeographicPoint(p2.getLongitude(), p2.getLatitude());
		return geocalc.getOrthodromicDistance();
	}

	public double getTotalDistance(Track track) {
		double d = 0.0;
		for (TrackSegment segment : track.getSegments()) {
			d += getTotalDistance(segment);
		}
		return d;
	}

	public double getTotalDistance(TrackSegment segment) {
		double d = 0.0;
		final List<TrackPoint> points = segment.getPoints();
		for (int i = 0; i < points.size() - 1; i++) {
			d += getDistance(points.get(i), points.get(i + 1));
		}
		return d;
	}

	@PostConstruct
	public void initialize() {
		INSTANCE = this;
		geocalc = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
	}

}
