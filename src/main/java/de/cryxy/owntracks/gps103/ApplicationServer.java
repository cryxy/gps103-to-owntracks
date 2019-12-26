/**
 * 
 */
package de.cryxy.owntracks.gps103;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.owntracks.gps103.constants.ConfigConstants;
import de.cryxy.owntracks.gps103.mqtt.OwntracksMqttClient;
import de.cryxy.owntracks.gps103.socket.ClientHandler;

/**
 * @author fabian
 *
 */
public class ApplicationServer {

	final Logger LOG = LoggerFactory.getLogger(ApplicationServer.class);

	private boolean shutdownRequested = false;

	private ExecutorService executorService = Executors.newFixedThreadPool(8);

	private ServerSocket server;

	SeContainerInitializer initializer = SeContainerInitializer.newInstance();

	SeContainer container = initializer.initialize();

	public void start() throws IOException {

		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				LOG.info("Shutdown requested ...");
				shutdownRequested = true;
				shutdown();
			}
		});
		
		// start MqttClient
		container.select(OwntracksMqttClient.class).get();

		Instance<ClientHandler> clientHandlers = container.select(ClientHandler.class);
		Properties props = container.select(Properties.class).get();

		server = new ServerSocket(Integer.valueOf(props.getProperty(ConfigConstants.PROP_GPS_103_SOCKET_PORT)));

		while (!shutdownRequested) {
			Socket client = null;

			try {
				LOG.info("Waiting for connection ...");
				client = server.accept();

				executorService.execute(clientHandlers.get().setClient(client));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		shutdown();

	}

	public void shutdown() {
		try {
			server.close();
			executorService.shutdown();
			executorService.awaitTermination(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOG.error("Error while shutting down ...");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		ApplicationServer server = new ApplicationServer();
		server.start();
	}

}
