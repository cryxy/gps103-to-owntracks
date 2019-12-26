/**
 * 
 */
package de.cryxy.owntracks.gps103.events;

import de.cryxy.owntracks.gps103.dtos.LocationData;

/**
 * @author fabian
 *
 */
public class LocationDataEvent extends Gps103Event{
	
	private LocationData locationData;

	/**
	 * @return the locationData
	 */
	public LocationData getLocationData() {
		return locationData;
	}

	/**
	 * @param locationData the locationData to set
	 */
	public void setLocationData(LocationData locationData) {
		this.locationData = locationData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocationDataEvent [locationData=" + locationData + "]";
	}
	
	

}
