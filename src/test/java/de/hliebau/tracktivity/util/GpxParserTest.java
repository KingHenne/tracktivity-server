package de.hliebau.tracktivity.util;

import java.io.InputStream;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

public class GpxParserTest {

	private XmlParser gpxParser;

	@Before
	public void setup() {
		new GeometryUtils(new GeometryFactory(new PrecisionModel(), 4326));
		gpxParser = new GpxParser();
	}

	@Test
	public void testCreateActivity() {
		InputStream gpxFile = this.getClass().getResourceAsStream("/track.gpx");
		Activity activity = gpxParser.createActivity(gpxFile);
		Track track = activity.getTrack();
		Assert.assertNotNull(activity);
		Assert.assertNotNull(track);
		Assert.assertEquals(1181, track.getPointsCount());
		Assert.assertEquals(35541.0, Math.floor(track.getLengthInMeters()));
		Assert.assertEquals(1, track.getDuration(false).getHours());
		Assert.assertEquals("Bergtour 2", activity.getName());
	}

	@Test
	public void testExportActivity() {
		TrackSegment segment = new TrackSegment();
		segment.addPoint(new TrackPoint(10, 20, 30, new Date(5000)));
		Activity activity = new Activity(new Track(segment));
		String exportedActivity = gpxParser.exportActivity(activity);
		Assert.assertEquals("<?xml version=\"1.0\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" "
				+ "xmlns:xsi=\"http://www.w3.org/2000/10/XMLSchema-instance\" "
				+ "xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 "
				+ "http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk><name></name><trkseg>"
				+ "<trkpt lat=\"20.0\" lon=\"10.0\"><ele>30.0</ele><time>1970-01-01T00:00:05Z</time>"
				+ "</trkpt></trkseg></trk></gpx>", exportedActivity);
	}

}
