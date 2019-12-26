/**
 * 
 */
package de.cryxy.owntracks.gps103.adapters;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;

/**
 * @author fabian
 *
 */
public class TimestampAdapter implements JsonbAdapter<ZonedDateTime, Long> {

	/* (non-Javadoc)
	 * @see javax.json.bind.adapter.JsonbAdapter#adaptToJson(java.lang.Object)
	 */
	@Override
	public Long adaptToJson(ZonedDateTime tst) throws Exception {
		return tst.toEpochSecond();
	}

	/* (non-Javadoc)
	 * @see javax.json.bind.adapter.JsonbAdapter#adaptFromJson(java.lang.Object)
	 */
	@Override
	public ZonedDateTime adaptFromJson(Long obj) throws Exception {
		Instant i = Instant.ofEpochSecond(Integer.valueOf(obj.toString()));
		ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
		return z;
	}
	
	

}
