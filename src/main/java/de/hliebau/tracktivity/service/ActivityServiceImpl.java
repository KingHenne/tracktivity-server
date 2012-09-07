package de.hliebau.tracktivity.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.persistence.ActivityDao;
import de.hliebau.tracktivity.persistence.TrackDao;
import de.hliebau.tracktivity.util.GpxParser;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private GpxParser gpxParser;

	@Autowired
	private TrackDao trackDao;

	@Override
	@Transactional
	public void createActivity(Activity activity) {
		activityDao.save(activity);
	}

	@Override
	@Transactional
	public void createTrack(Track track) {
		trackDao.save(track);
	}

	@Override
	public String exportActivityAsGpx(Activity activity) {
		return gpxParser.exportActivity(activity);
	}

	@Override
	public long getActivityCount() {
		return activityDao.getCount();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Activity> getRecentActivities(int count) {
		return activityDao.getRecentActivities(count);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Track> getRecentTracks(int count) {
		return trackDao.getRecentTracks(count);
	}

	@Override
	@Transactional(readOnly = true)
	public long getTrackCount() {
		return trackDao.getCount();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Activity> getUserActivities(User user) {
		return activityDao.getUserActivities(user);
	}

	@Override
	@Transactional
	public Activity importGpxAsUserActivity(InputStream in, User user, ActivityType type) {
		Activity activity = gpxParser.createActivity(in);
		if (activity != null) {
			activity.setUser(user);
			activity.setType(type);
			activityDao.save(activity);
		}
		return activity;
	}

	@Override
	@Transactional(readOnly = true)
	public Activity retrieveActivity(Long id) {
		return activityDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Activity retrieveActivityWithTrack(Long id) {
		Activity activity = activityDao.findById(id);
		if (activity != null) {
			activity.setTrack(trackDao.findById(activity.getTrack().getId()));
		}
		return activity;
	}

}
