/**
 * 
 */
package de.cryxy.owntracks.gps103.mqtt;

import static de.cryxy.owntracks.gps103.constants.ConfigConstants.PROP_MQTT_MAIN_TOPIC;
import static de.cryxy.owntracks.gps103.constants.ConfigConstants.PROP_MQTT_PASSWORD;
import static de.cryxy.owntracks.gps103.constants.ConfigConstants.PROP_MQTT_SERVER;
import static de.cryxy.owntracks.gps103.constants.ConfigConstants.PROP_MQTT_USER;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fabian
 *
 */
@Singleton
public class OwntracksMqttClient {
	
	private final Logger LOG = LoggerFactory.getLogger(OwntracksMqttClient.class);
	private final String CLIENT_ID = "gps103-to-owntracks";

	private MqttConnectOptions connOptions;

	private MqttClient client;
	
	private String baseTopic;
	
	@Inject
	private Properties props; 

	public OwntracksMqttClient() {
		LOG.info("Creating client ...");
	}


	public void connect(String serverURI, String userName, String password, String topic) throws MqttException {
		this.baseTopic = topic;

		client = new MqttClient(serverURI, CLIENT_ID);

		client.setCallback(new MqttCallbackExtended() {

			public void connectionLost(Throwable throwable) {
				LOG.error("Connection lost! Try automatic reconnect ...", throwable);
			}

			public void deliveryComplete(IMqttDeliveryToken arg0) {

			}

			public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

			}

			@Override
			public void connectComplete(boolean reconnect, String serverURI) {
				LOG.info("Connected to mqtt server ... " + serverURI);

			}

		});

		connOptions = new MqttConnectOptions();
		connOptions.setUserName(userName);
		connOptions.setPassword(password.toCharArray());
		connOptions.setAutomaticReconnect(true);
		
		LOG.info("Connect to mqtt server ... " + client.getServerURI());
		client.connect(connOptions);

	}
	
	public void publish(String userName, String deviceName, String messagePayload) {
		StringBuilder builder = new StringBuilder();
		builder.append(baseTopic).append("/");
		builder.append(userName).append("/");
		builder.append(deviceName);
		String topic = builder.toString();
		
		LOG.debug("Publishing to topic={},message={}",topic,messagePayload);
		MqttMessage message = new MqttMessage();
		message.setPayload(messagePayload.getBytes());
		message.setQos(1);
		message.setRetained(true);
		
		try {
			client.publish(builder.toString(), message);
		} catch (Exception e) {
			LOG.error("Error publishing message!",e);
		}
	}
	
	@PostConstruct
	public void connect() throws MqttException {
		String mqttServerUri = props.getProperty(PROP_MQTT_SERVER);
		String mqttUser = props.getProperty(PROP_MQTT_USER);
		String mqttPassword = props.getProperty(PROP_MQTT_PASSWORD);
		String mqttMainUri = props.getProperty(PROP_MQTT_MAIN_TOPIC);
		
		connect(mqttServerUri, mqttUser, mqttPassword, mqttMainUri);
	}


	@PreDestroy
	public void disconnect() {
		LOG.info("[MQTT] Disconnecting from MQTT Server");
		try {
			LOG.info("Disconnecting ...");
			client.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void observeEvent(@Observes BeforeShutdown event) throws RuntimeException {
		disconnect();
	}


}
