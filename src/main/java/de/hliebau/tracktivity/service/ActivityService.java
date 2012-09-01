package de.hliebau.tracktivity.service;

import java.io.InputStream;
import java.util.List;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.User;

public interface ActivityService {

	public void createActivity(Activity activity);

	public void createTrack(Track track);

	public long getActivityCount();

	public List<Activity> getRecentActivities(int count);

	public List<Track> getRecentTracks(int count);

	public long getTrackCount();

	public List<Activity> getUserActivities(User user);

	public Activity importGpxForUser(InputStream in, User user);

	public Activity retrieveActivity(Long id);

	public Activity retrieveActivityWithTrack(Long id);

}