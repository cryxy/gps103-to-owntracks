package de.cryxy.owntracks.gps103.socket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.cryxy.owntracks.gps103.dtos.LocationData;
import de.cryxy.owntracks.gps103.socket.ClientHandler;
import de.cryxy.owntracks.gps103.socket.ClientHandler.RECEIVED_MSG_CMD;

public class ClientHandlerTest {
	
	private final static String MSG_CMD_LOCATION_DATA = "imei:945280036814285,tracker,180506130722,,F,184959.000,A,5526.0243,N,00816.7500,E,0.00,0";

	@Spy
	private ClientHandler handler;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRetrieveMessageCommandLogOnRequest() {
		Assert.assertEquals(RECEIVED_MSG_CMD.LOG_ON_REQUEST,
				handler.retrieveMessageCommand("##,imei:945280036814285,A"));
	}
	
	@Test
	public void testRetrieveMessageCommandLocationData() {
		Assert.assertEquals(RECEIVED_MSG_CMD.LOCATION_DATA,
				handler.retrieveMessageCommand(MSG_CMD_LOCATION_DATA));
	}

	@Test
	public void testExtractLocationData() throws Exception {
		final String imei = "945280036814285";
		LocationData locationData = handler.extractLocationData(imei,MSG_CMD_LOCATION_DATA);
		System.out.println(locationData);
		Assert.assertEquals(imei, locationData.getImei());
		Assert.assertEquals("55.4337383", locationData.getLatitude().toString());
		Assert.assertEquals("8.2791667", locationData.getLongitude().toString());
		//Assert.assertEquals("2019-05-03T18:49:59Z[UTC]",locationData.getTimestamp());
	}


}
