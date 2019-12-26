package de.cryxy.owntracks.gps103.mqtt;

import java.time.ZonedDateTime;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.cryxy.owntracks.gps103.dtos.OwntracksLocation;

public class OwntracksJsonEncoderTest {
	
	private OwntracksJsonEncoder encoder;
	
	@Before
	public void setup() {
		encoder = new OwntracksJsonEncoder();
	}

	@Test
	public void testEncode() throws Exception {
		OwntracksLocation location = new OwntracksLocation();
		location.setLat(50.123456);
		location.setLon(7.987654);
		location.setTid("tk104");
		location.setTst(ZonedDateTime.now());
		
		String encoded = encoder.encode(location);
		System.out.println(encoded);
		
		Jsonb jsonB = JsonbBuilder.create();
		OwntracksLocation actual = jsonB.fromJson(encoded, OwntracksLocation.class);
		Assert.assertEquals(location, actual);
	}

}
