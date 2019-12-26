/**
 * 
 */
package de.cryxy.owntracks.gps103.processing;

import java.util.Properties;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.owntracks.gps103.dtos.LocationData;
import de.cryxy.owntracks.gps103.dtos.OwntracksLocation;
import de.cryxy.owntracks.gps103.events.LocationDataEvent;
import de.cryxy.owntracks.gps103.mqtt.OwntracksJsonEncoder;
import de.cryxy.owntracks.gps103.mqtt.OwntracksMqttClient;

import static de.cryxy.owntracks.gps103.constants.ConfigConstants.*;

/**
 * @author fabian
 *
 */
public class LocationDataEventHandler {

	private final Logger LOG = LoggerFactory.getLogger(LocationDataEventHandler.class);

	@Inject
	private OwntracksMqttClient owntracksMqttClient;

	@Inject
	private OwntracksJsonEncoder jsonEncoder;
	
	@Inject
	private Properties props;

	public void observe(@Observes LocationDataEvent locationDataEvent) {
		LOG.debug("Handle LocationDataEvent={}",locationDataEvent);
		
		LocationData locationData = locationDataEvent.getLocationData();
		
		String username = props.getProperty(String.format((PROP_IMEI_USER),locationData.getImei()));
		String devicename = props.getProperty(String.format((PROP_IMEI_DEVICE),locationData.getImei()));
		String tid = props.getProperty(String.format((PROP_IMEI_TRACKER),locationData.getImei()));
		
		OwntracksLocation owntracksLocation = OwntracksLocation.from(locationData);
		owntracksLocation.setTid(tid);
		String encodedOwntracksLocation = jsonEncoder.encode(owntracksLocation);
		
		LOG.trace("EncodedOwntracksLocation={}",encodedOwntracksLocation);
		
		owntracksMqttClient.publish(username, devicename, encodedOwntracksLocation);
		
	}

}
