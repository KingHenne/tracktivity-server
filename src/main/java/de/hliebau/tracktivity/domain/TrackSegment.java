package de.hliebau.tracktivity.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.LineString;

import de.hliebau.tracktivity.util.GeometryUtils;

@Entity
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class TrackSegment extends AbstractEntity {

	private LineString line;

	private List<TrackPoint> points = new ArrayList<TrackPoint>();

	public TrackSegment() {
		super();
	}

	public TrackSegment(List<TrackPoint> points) {
		this();
		this.setPoints(points);
	}

	public void addPoint(TrackPoint point) {
		points.add(point);
	}

	@PostLoad
	@PrePersist
	public void generateLine() {
		line = GeometryUtils.getInstance().createLineString(points);
	}

	@Transient
	public TrackPoint getDestination() {
		return points.get(points.size() - 1);
	}

	@Transient
	public Duration getDuration() {
		long start = getStartingPoint().getUtcTime().getTime();
		long end = getDestination().getUtcTime().getTime();
		return new Duration(end - start);
	}

	@Transient
	public Double getLengthInMeters() {
		return GeometryUtils.getInstance().getTotalDistance(this);
	}

	@Transient
	public LineString getLine() {
		return line;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "segment_id", insertable = true, nullable = false)
	@OrderBy("utcTime ASC, id")
	@XmlElement(name = "trkpt")
	@JsonProperty
	public List<TrackPoint> getPoints() {
		return points;
	}

	@Transient
	public TrackPoint getStartingPoint() {
		return points.get(0);
	}

	public void setPoints(List<TrackPoint> points) {
		this.points = points;
	}

}
