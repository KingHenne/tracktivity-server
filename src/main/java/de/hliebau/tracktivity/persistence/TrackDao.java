package de.hliebau.tracktivity.persistence;

import java.util.List;

import de.hliebau.tracktivity.domain.Track;

public interface TrackDao extends CommonDao<Track> {

	public List<Track> getRecentTracks(int count);

}
