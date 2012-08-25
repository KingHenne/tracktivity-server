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

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;
import de.hliebau.tracktivity.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/application.xml" })
@Transactional
public class ActivityServiceTest {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserService userService;

	protected User createTestUser() {
		User newUser = new User("testUser");
		userService.createUser(newUser);
		return newUser;
	}

	protected User getPersistentTestUser() {
		String userName = "testUser";
		User user = userService.retrieveUser(userName);
		if (user == null) {
			user = new User(userName);
			userService.createUser(user);
		}
		return user;
	}

	@Test
	@Rollback(value = false)
	public void importMultipleTracks() {
		File gpxFile = new File("src/test/resources/track.gpx");
		User testUser = getPersistentTestUser();
		long countBefore = activityService.getTrackCount();
		final int COUNT = 30;
		for (int i = 0; i < COUNT; i++) {
			activityService.importGpxForUser(gpxFile, testUser);
		}
		Assert.assertEquals(String.format("The track count has not been increased by %d.", COUNT), countBefore + COUNT,
				activityService.getTrackCount());
	}

	@Test
	public void testAddNewTrack() {
		List<TrackPoint> points = new ArrayList<TrackPoint>(2);
		points.add(new TrackPoint(10.0, 20.0, 0.0));
		points.add(new TrackPoint(15.0, 25.0, 50.0));
		TrackSegment segment = new TrackSegment(points);
		Track track = new Track(segment);

		long countBefore = activityService.getTrackCount();
		activityService.createTrack(track);
		// track = trackService.retrieveTrack(track.getId());
		// ^ why do I need to do this?
		Assert.assertNotNull(track.getLengthInMeters());
		Assert.assertEquals("The track count has not been increased by 1.", countBefore + 1,
				activityService.getTrackCount());
	}

	@Test
	public void testDatabaseFetchingPerformance() {
		long start = System.currentTimeMillis();
		Track track = activityService.getRecentTracks(1).get(0);
		long end = System.currentTimeMillis();
		long millis = end - start;
		Assert.assertNotNull(track);
		Assert.assertTrue(String.format("fetching took way too long: %d ms", millis), millis < 500);
	}

	@Test
	@Rollback(true)
	public void testImportGpx() {
		File gpxFile = new File("src/test/resources/track.gpx");
		long countBefore = activityService.getActivityCount();
		Activity activity = activityService.importGpxForUser(gpxFile, createTestUser());
		Track track = activity.getTrack();
		Assert.assertNotNull(activity);
		Assert.assertNotNull(track);
		Assert.assertEquals(1181, track.getPointsCount());
		Assert.assertEquals(35541.0, Math.floor(track.getLengthInMeters()));
		Assert.assertEquals(1, track.getDuration(false).getHours());
		Assert.assertEquals("Bergtour 2", activity.getName());
		Assert.assertEquals("The track count has not been increased by 1.", countBefore + 1,
				activityService.getActivityCount());
	}

	@Test
	public void testLengthCalculationPerformance() {
		Track track = activityService.getRecentTracks(1).get(0);
		Assert.assertNotNull(track);

		long start = System.currentTimeMillis();
		double length = track.getLengthInMeters();
		long end = System.currentTimeMillis();
		long millis = end - start;
		Assert.assertTrue("length was 0", length > 0);
		Assert.assertTrue(String.format("calculation took way too long: %d ms", millis), millis < 50);
	}

}
