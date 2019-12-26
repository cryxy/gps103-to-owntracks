/**
 * 
 */
package de.cryxy.owntracks.gps103.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * @author fabian
 *
 */
@Singleton
public class AppConfig {
	
	private Properties properties = new Properties();
	
	@Produces
	public Properties get() {
		return properties;
	}
	
	@PostConstruct
	public void setup() throws FileNotFoundException, IOException {
		properties.load(new FileInputStream("config/app.properties"));
	}

}
