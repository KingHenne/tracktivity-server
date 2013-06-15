package de.hliebau.tracktivity.util;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Track;

public class TcxParserTest {

	private TcxParser tcxParser;

	@Before
	public void setup() {
		new GeometryUtils(new GeometryFactory(new PrecisionModel(), 4326));
		tcxParser = new TcxParser();
	}

	@Test
	public void testCreateActivity() {
		InputStream tcxFile = this.getClass().getResourceAsStream("/track.tcx");
		Activity activity = tcxParser.createActivity(tcxFile);
		Track track = activity.getTrack();
		Assert.assertNotNull(activity);
		Assert.assertNotNull(track);
		Assert.assertEquals(8, track.getPointsCount());
		Assert.assertEquals(110.0, Math.floor(track.getLengthInMeters()));
		Assert.assertEquals(15, track.getDuration(true).getSeconds());
		Assert.assertNull(activity.getName());
	}

}
