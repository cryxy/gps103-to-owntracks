/**
 * 
 */
package de.cryxy.owntracks.gps103.mqtt;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import de.cryxy.owntracks.gps103.dtos.OwntracksLocation;

/**
 * @author fabian
 *
 */
@Singleton
public class OwntracksJsonEncoder {
	
	private Jsonb jsonB = JsonbBuilder.create();
	
	public String encode(OwntracksLocation owntracksLocation) {
		return jsonB.toJson(owntracksLocation);
	}

	
	

}
