package de.hliebau.tracktivity.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

import de.hliebau.tracktivity.util.GeometryUtils;

@Entity
public class TrackPoint extends AbstractEntity {

	private Point point;

	private Date utcTime;

	public TrackPoint() {
		super();
	}

	public TrackPoint(double lon, double lat) {
		this();
		this.point = GeometryUtils.getInstance().createPoint(lon, lat);
	}

	public TrackPoint(double lon, double lat, double ele) {
		this();
		this.point = GeometryUtils.getInstance().createPoint(lon, lat, ele);
	}

	@Type(type = "org.hibernate.spatial.GeometryType")
	public Point getPoint() {
		return point;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getUtcTime() {
		return utcTime;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public void setUtcTime(Date utcTime) {
		this.utcTime = utcTime;
	}

}
