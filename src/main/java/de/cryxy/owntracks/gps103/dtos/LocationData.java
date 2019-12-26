/**
 * 
 */
package de.cryxy.owntracks.gps103.dtos;

import java.time.ZonedDateTime;

/**
 * @author fabian
 *
 */
public class LocationData {
	
	private String imei;
	private Double latitude;
	private Double longitude;
	private ZonedDateTime timestamp;
	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the timestamp
	 */
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}
	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocationData [imei=" + imei + ", latitude=" + latitude + ", longitude=" + longitude + ", timestamp="
				+ timestamp + "]";
	}
	


}
