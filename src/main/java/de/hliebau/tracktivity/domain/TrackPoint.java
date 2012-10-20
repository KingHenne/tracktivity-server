package de.hliebau.tracktivity.domain;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import de.hliebau.tracktivity.api.JsonDateSerializer;
import de.hliebau.tracktivity.util.GeometryUtils;

@Entity
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
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

	@JsonCreator
	public TrackPoint(@JsonProperty("lon") double lon, @JsonProperty("lat") double lat,
			@JsonProperty("ele") double ele, @JsonProperty("time") Date utcTime) {
		this(lon, lat, ele);
		this.utcTime = utcTime;
	}

	@Transient
	@XmlElement(name = "ele")
	@JsonProperty("ele")
	public Double getElevation() {
		if (!Double.isNaN(this.point.getCoordinate().z)) {
			return point.getCoordinate().z;
		}
		return null;
	}

	@Transient
	@XmlAttribute(name = "lat")
	@JsonProperty("lat")
	public double getLatitude() {
		return point.getY();
	}

	@Transient
	@XmlAttribute(name = "lon")
	@JsonProperty("lon")
	public double getLongitude() {
		return point.getX();
	}

	@Type(type = "org.hibernate.spatial.GeometryType")
	public Point getPoint() {
		return point;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@XmlElement(name = "time")
	@JsonProperty("time")
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getUtcTime() {
		return utcTime;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public void setUtcTime(Date utcTime) {
		this.utcTime = utcTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("[%.5f, %.5f", getLatitude(), getLongitude()));
		Double ele = getElevation();
		if (ele != null) {
			sb.append(String.format(", %.1f", ele));
		}
		sb.append(']');
		if (utcTime != null) {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
			sb.append(" @ ").append(df.format(utcTime));
		}
		return sb.toString();
	}
}
