package de.hliebau.tracktivity.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.util.GeometryUtils;

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
		User user = userService.retrieveUser(userName, false);
		if (user == null) {
			user = new User(userName);
			user.setFirstname("Test");
			user.setLastname("User");
			userService.createUser(user);
		}
		return user;
	}

	@Test
	@Ignore
	public void importMultipleTracks() {
		InputStream gpxFile = this.getClass().getResourceAsStream("/track.gpx");
		User testUser = getPersistentTestUser();
		long countBefore = activityService.getTrackCount();
		final int COUNT = 30;
		for (int i = 0; i < COUNT; i++) {
			activityService.importGpxAsUserActivity(gpxFile, testUser, ActivityType.CYCLING);
		}
		Assert.assertEquals(String.format("The track count has not been increased by %d.", COUNT), countBefore + COUNT,
				activityService.getTrackCount());
	}

	@Test
	public void testAddNewTrack() {
		List<TrackPoint> points = new ArrayList<TrackPoint>(3);
		points.add(new TrackPoint(10.0, 20.0, 0.0));
		points.add(new TrackPoint(15.0, 25.0, 50.0));
		points.add(new TrackPoint(12.0, 22.0, 45.0));
		TrackSegment segment = new TrackSegment(points);
		Track track = new Track(segment);

		long countBefore = activityService.getTrackCount();
		activityService.createTrack(track);
		track.getLatLngBounds();
		// track = trackService.retrieveTrack(track.getId());
		// ^ why do I need to do this?
		Assert.assertNotNull(track.getLengthInMeters());
		Assert.assertEquals("The track count has not been increased by 1.", countBefore + 1,
				activityService.getTrackCount());
	}

	@Test
	@Ignore
	public void testDatabaseFetchingPerformance() {
		long start = System.currentTimeMillis();
		Track track = activityService.getRecentTracks(1).get(0);
		long end = System.currentTimeMillis();
		long millis = end - start;
		Assert.assertNotNull(track);
		Assert.assertTrue(String.format("fetching took way too long: %d ms", millis), millis < 500);
	}

	@Ignore
	@Test
	public void testLengthCalculationPerformance() {
		Track track = activityService.getRecentTracks(1).get(0);
		Assert.assertNotNull(track);

		long start = System.currentTimeMillis();
		double length = track.getLengthInMeters();
		long end = System.currentTimeMillis();
		long millis = end - start;
		Assert.assertTrue("length was 0", length > 0);
		Assert.assertTrue(String.format("calculation took way too long: %d ms", millis), millis < 100);
	}

	@Test
	public void testRetrieveActivityWithTrack() {
		Activity activity = activityService.retrieveActivityWithTrack(285972L);
		Assert.assertNotNull(activity);
		Track track = activity.getTrack();
		int pointsCount = track.getPointsCount();
		List<TrackPoint> points = new ArrayList<TrackPoint>(pointsCount);
		for (TrackSegment s : track.getSegments()) {
			List<TrackPoint> segmentPoints = s.getPoints();
			points.addAll(segmentPoints);
			for (int i = 0; i < segmentPoints.size() - 1; i++) {
				TrackPoint p1 = segmentPoints.get(i);
				TrackPoint p2 = segmentPoints.get(i + 1);
				double d = GeometryUtils.getInstance().getDistance(p1, p2);
				Assert.assertTrue(
						String.format("A distance of %.2fm between two points of the same segment seems too high.", d),
						d < 50.0);
			}
		}
		Assert.assertEquals(pointsCount, points.size());
		for (int i = 0; i < pointsCount - 1; i++) {
			TrackPoint p1 = points.get(i);
			TrackPoint p2 = points.get(i + 1);
			System.out.println(p1);
			boolean before = p1.getUtcTime().before(p2.getUtcTime());
			boolean atTheSameTime = p1.getUtcTime().equals(p2.getUtcTime());
			Assert.assertTrue(p2.toString(), before || atTheSameTime);
			double d = GeometryUtils.getInstance().getDistance(p1, p2);
			Assert.assertTrue(String.format("A distance of %.2fm between two points seems too high.", d), d < 100.0);
		}
	}
}
