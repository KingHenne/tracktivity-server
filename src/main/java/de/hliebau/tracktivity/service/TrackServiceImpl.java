package de.hliebau.tracktivity.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.persistence.TrackDao;
import de.hliebau.tracktivity.util.GpxParser;

@Service
public class TrackServiceImpl implements TrackService {

	@Autowired
	private GpxParser gpxParser;

	@Autowired
	private TrackDao trackDao;

	@Transactional
	public void createTrack(Track track) {
		trackDao.save(track);
	}

	@Transactional(readOnly = true)
	public List<Track> getRecentTracks(int count) {
		return trackDao.getRecentTracks(count);
	}

	@Transactional(readOnly = true)
	public long getTrackCount() {
		return trackDao.getCount();
	}

	@Transactional
	public Track importGpx(File gpxFile) {
		Track track = gpxParser.createTrack(gpxFile);
		if (track != null) {
			trackDao.save(track);
		}
		return track;
	}

	@Transactional(readOnly = true)
	public Track retrieveTrack(long id) {
		return trackDao.findById(id);
	}

}
