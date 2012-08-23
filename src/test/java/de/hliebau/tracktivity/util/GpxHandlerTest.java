package de.hliebau.tracktivity.util;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Ignore;
import org.xml.sax.SAXException;

import de.hliebau.tracktivity.domain.Track;

public class GpxHandlerTest {

	@Ignore
	public void testGpxParsing() {
		File gpxFile = new File("src/test/resources/track.gpx");

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(gpxFile, new GpxHandler(new Track()));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail("ParserConfigurationException");
		} catch (SAXException e) {
			e.printStackTrace();
			fail("SAXException");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException");
		}
	}

}
