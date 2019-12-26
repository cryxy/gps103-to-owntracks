/**
 * 
 */
package de.cryxy.owntracks.gps103.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.owntracks.gps103.constants.ConfigConstants;
import de.cryxy.owntracks.gps103.dtos.LocationData;
import de.cryxy.owntracks.gps103.events.LocationDataEvent;

/**
 * @author fabian
 *
 */
public class ClientHandler implements Runnable {

	private final Logger LOG = LoggerFactory.getLogger(ClientHandler.class);
	
	@Inject
	private Properties props;
	
	@Inject
	private Event<LocationDataEvent> event;

	private Socket client;
	private String imei;
	private Scanner in;
	private PrintWriter out;



	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			LOG.info("Handle connection from {}", client.getInetAddress().toString());
			client.setSoTimeout(65000);
			// in = new Sca(new InputStreamReader(client.getInputStream()));
			in = new Scanner(client.getInputStream());
			in.useDelimiter(";");
			out = new PrintWriter(client.getOutputStream(), true);

			while (client.isConnected() && !Thread.currentThread().isInterrupted() && in.hasNext()) {
				String message = in.next();

				LOG.debug("Received command " + message);

				RECEIVED_MSG_CMD retrievedMessageCommand = retrieveMessageCommand(message);

				switch (retrievedMessageCommand) {
				case LOG_ON_REQUEST:
					imei = message.substring(8, 23);
					LOG.info("LogOn imei: " + imei);

					out.print("LOAD");
					out.flush();

					sendTimeZoneCommand();
					sendTrackUponTimeIntervalCommand();
					break;

				case HEARTBEAT:
					LOG.info("[{}] Found heartbeat",imei);
					out.print("ON");
					out.flush();

					break;

				case LOCATION_DATA:
					LOG.info("[{}] Location data message",imei);
					LocationData locationData = extractLocationData(imei,message);
					LocationDataEvent locationDataEvent = new LocationDataEvent();
					locationDataEvent.setLocationData(locationData);
					
					event.fire(locationDataEvent);
					break;

				default:
					LOG.error("Unknown message: {}",message);
					break;
				}
			}

		} catch (IOException e) {
			LOG.error("Connection error", e);
		} finally {
			LOG.info("Closing Reader and Writer: Client-Connection: {}, in.hasNext(): {}", client.isConnected(), false);

			try {
				in.close();
				out.close();
				client.close();
			} catch (IOException e) {
				LOG.error("Error closing connection ...");
			}
		}

	}
	
	

	/**
	 * @param client the client to set
	 */
	public ClientHandler setClient(Socket client) {
		this.client = client;
		return this;
	}



	/**
	 * @param message
	 */
	protected LocationData extractLocationData(String imei, String message) {

		LocationData locationData = new LocationData();
		locationData.setImei(imei);
		

		List<String> tokens = Collections.list(new StringTokenizer(message, ",")).stream().map(token -> (String) token)
				.collect(Collectors.toList());

		// date
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		TemporalAccessor temporalAccessor = dateTimeFormatter.withZone(ZoneId.of("UTC")).parse(tokens.get(2));
		
		ZonedDateTime dateTime = ZonedDateTime.from(temporalAccessor);
		LOG.debug("Found timestamp: {}", dateTime);
		locationData.setTimestamp(dateTime);
		
		

		// coordinates
		Double latitude = degreesToDecimalDegrees(tokens.get(6), tokens.get(7));

		LOG.debug("Found latitude: {}", latitude);
		locationData.setLatitude(latitude);

		Double longitude = degreesToDecimalDegrees(tokens.get(8), tokens.get(9));
		LOG.debug("Found longitude: {}", longitude);
		locationData.setLongitude(longitude);
		
		LOG.debug("LocationData: {}",locationData);

		return locationData;

	}

	protected Double degreesToDecimalDegrees(String degreesMin, String hemisphere) {
		int seperationPoint = degreesMin.indexOf(".") - 2;

		Double coordinateDecimal = Double.valueOf(degreesMin.substring(0, seperationPoint));
		coordinateDecimal += Double.valueOf(degreesMin.substring(seperationPoint)) / 60;
		coordinateDecimal = round(coordinateDecimal, 7);

		if (hemisphere != null && (hemisphere.equals("S") || hemisphere.equals("W") || hemisphere.equals("-"))) {
			coordinateDecimal = -Math.abs(coordinateDecimal);
		}

		return coordinateDecimal;
	}
	
	private void sendTimeZoneCommand() {
		// **,imei:359586018966098,I,+0 
		StringBuilder builder = new StringBuilder();
		builder.append("**,imei:");
		builder.append(imei);
		builder.append(",I,+0");
	

		LOG.debug("sendTimeZoneCommand command: {}", builder.toString());
		out.write(builder.toString());

		out.flush();

	}

	/**
	 */
	private void sendTrackUponTimeIntervalCommand() {
		// **,imei:359586018966098,C,10s
		StringBuilder builder = new StringBuilder();
		builder.append("**,imei:");
		builder.append(imei);
		builder.append(",C,");
		String timeInterval = props.getProperty(ConfigConstants.PROP_TRACK_UPON_TIME_INTERVAL);
		builder.append(timeInterval);
		LOG.debug("Using trackUponTimeInterval: {}",timeInterval);
		

		LOG.debug("sendTrackUponTimeInterval command: {}", builder.toString());
		out.write(builder.toString());

		out.flush();

	}

	private double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	protected RECEIVED_MSG_CMD retrieveMessageCommand(String cmd) {
		if (cmd.matches("##,imei:\\d{15},A")) {
			return RECEIVED_MSG_CMD.LOG_ON_REQUEST;
		} else if (cmd.matches("\\d{15}")) {
			return RECEIVED_MSG_CMD.HEARTBEAT;
		} else if (cmd.matches(
				"imei:\\d{15},tracker,\\d{12},,F,\\d{6}.\\d{3},A,\\d{4}.\\d{4},(S|N),\\d{5}.\\d{4},(E|W),\\d{1}.\\d{2},\\S*")) {
			return RECEIVED_MSG_CMD.LOCATION_DATA;
		} else {
			return RECEIVED_MSG_CMD.UNKNOWN;
		}

	}

	public static enum RECEIVED_MSG_CMD {

		LOG_ON_REQUEST, HEARTBEAT, LOCATION_DATA, UNKNOWN

	}

}
