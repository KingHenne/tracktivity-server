package de.hliebau.tracktivity.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/application.xml" })
@Transactional
public class TrackServiceTest {

	@Autowired
	private TrackService trackService;

	@Test
	public void testAddNewTrack() {
		List<TrackPoint> points = new ArrayList<TrackPoint>(2);
		points.add(new TrackPoint(10.0, 20.0, 0.0));
		points.add(new TrackPoint(15.0, 25.0, 50.0));
		TrackSegment segment = new TrackSegment(points);
		Track track = new Track(segment);

		long countBefore = trackService.getTrackCount();
		trackService.createTrack(track);
		// track = trackService.retrieveTrack(track.getId());
		// ^ why do I need to do this?
		Assert.assertNotNull(track.getLengthInMeters());
		Assert.assertEquals("The track count has not been increased by 1.", countBefore + 1,
				trackService.getTrackCount());
	}

	@Test
	public void testDatabaseFetchingPerformance() {
		long start = System.currentTimeMillis();
		Track track = trackService.getRecentTracks(1).get(0);
		long end = System.currentTimeMillis();
		long millis = end - start;
		Assert.assertNotNull(track);
		Assert.assertTrue(String.format("fetching took way too long: %d ms", millis), millis < 500);
	}

	@Test
	@Rollback(true)
	public void testImportGpx() {
		File gpxFile = new File("src/test/resources/track.gpx");
		long countBefore = trackService.getTrackCount();
		Track track = trackService.importGpx(gpxFile);
		Assert.assertNotNull(track);
		Assert.assertEquals(1181, track.getPointsCount());
		Assert.assertEquals(35541.0, Math.floor(track.getLengthInMeters()));
		Assert.assertEquals(1, track.getDuration(false).getHours());
		Assert.assertEquals("Bergtour 2", track.getName());
		Assert.assertEquals("The track count has not been increased by 1.", countBefore + 1,
				trackService.getTrackCount());
	}

}
