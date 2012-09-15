package de.hliebau.tracktivity.api;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.util.GeometryUtils;

public class JSONTest {

	@Test
	public void testConvertJSONToProduct() throws Exception {
		// Create the shared instance of GeometryUtils.
		new GeometryUtils(new GeometryFactory(new PrecisionModel(), 4326));

		ObjectMapper m = new ObjectMapper();
		InputStream jsonFile = this.getClass().getResourceAsStream("/activity.json");

		Activity activity = m.readValue(jsonFile, Activity.class);

		Assert.assertEquals("Test Activity", activity.getName());
		Assert.assertEquals(ActivityType.CYCLING, activity.getType());
		Assert.assertEquals(1, activity.getTrack().getSegments().size());
		Assert.assertEquals(2, activity.getTrack().getPointsCount());
		Assert.assertEquals(12, activity.getTrack().getDuration(true).getSeconds());
	}

}
