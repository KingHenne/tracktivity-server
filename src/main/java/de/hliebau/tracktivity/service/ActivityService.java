package de.hliebau.tracktivity.service;

import java.io.InputStream;
import java.util.List;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.User;

public interface ActivityService {

	public void createActivity(Activity activity);

	public void createTrack(Track track);

	public void deleteActivity(Activity activity);

	public String exportActivityAsGpx(Activity activity);

	public long getActivityCount();

	public List<Activity> getLiveActivities();

	public List<Activity> getRecentActivities(int count);

	public List<Track> getRecentTracks(int count);

	public long getTrackCount();

	public List<Activity> getUserActivities(User user);

	public Activity importGpxAsUserActivity(InputStream in, User user, ActivityType type);

	public Activity importTcxAsUserActivity(InputStream in, User user, ActivityType type);

	public Activity retrieveActivity(Long id);

	public Activity retrieveActivityWithTrack(Long id);

}