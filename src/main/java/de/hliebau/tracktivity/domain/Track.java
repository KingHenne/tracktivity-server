package de.hliebau.tracktivity.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;

import de.hliebau.tracktivity.util.GeometryUtils;

@Entity
public class Track extends AbstractEntity {

	private MultiLineString lines;

	private List<TrackSegment> segments = new ArrayList<TrackSegment>();

	public Track() {
		super();
	}

	public Track(List<TrackSegment> segments) {
		this();
		this.setSegments(segments);
	}

	public Track(TrackSegment segment) {
		this();
		this.addSegment(segment);
	}

	public void addSegment(TrackSegment segment) {
		segments.add(segment);
	}

	@PostLoad
	@PrePersist
	public void generateLines() {
		for (TrackSegment segment : segments) {
			segment.generateLine();
		}
		lines = GeometryUtils.getInstance().createMultiLineString(segments);
	}

	@Transient
	public Duration getDuration(boolean includePauses) {
		if (includePauses) {
			long start = segments.get(0).getStartingPoint().getUtcTime().getTime();
			long end = segments.get(segments.size() - 1).getDestination().getUtcTime().getTime();
			return new Duration(end - start);
		}
		Duration d = new Duration();
		for (TrackSegment segment : segments) {
			d.add(segment.getDuration());
		}
		return d;
	}

	@Transient
	public String getLatLngBounds() {
		Coordinate[] convexHull = lines.convexHull().getCoordinates();
		return GeometryUtils.toLatLngArrayString(convexHull);
	}

	@Transient
	public Double getLengthInMeters() {
		return GeometryUtils.getInstance().getTotalDistance(this);
	}

	@Transient
	public int getPointsCount() {
		int count = 0;
		for (TrackSegment segment : segments) {
			count += segment.getPoints().size();
		}
		return count;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "track_id", insertable = true, nullable = false)
	public List<TrackSegment> getSegments() {
		return segments;
	}

	@Transient
	public String getSparseMultiPolyline() {
		StringBuilder sb = new StringBuilder("[");
		for (TrackSegment s : segments) {
			sb.append(s.getSparsePolyline()).append(",\n");
		}
		sb.deleteCharAt(sb.length() - 1); // remove line break
		sb.deleteCharAt(sb.length() - 1); // remove last comma
		return sb.append(']').toString();
	}

	public void setSegments(List<TrackSegment> segments) {
		this.segments = segments;
	}

}
