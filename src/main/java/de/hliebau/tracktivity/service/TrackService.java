package de.hliebau.tracktivity.service;

import java.io.File;
import java.util.List;

import de.hliebau.tracktivity.domain.Track;

public interface TrackService {

	public void createTrack(Track track);

	public List<Track> getRecentTracks(int count);

	public long getTrackCount();

	public Track importGpx(File gpxFile);

	public Track retrieveTrack(Long id);

}