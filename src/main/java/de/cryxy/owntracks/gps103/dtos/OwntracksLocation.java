/**
 * 
 */
package de.cryxy.owntracks.gps103.dtos;

import java.time.ZonedDateTime;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;

import de.cryxy.owntracks.gps103.adapters.TimestampAdapter;

/**
 * @author fabian
 *
 */
public class OwntracksLocation {
	
	private Double lat;
	private Double lon;
	
	/**
	 *  Tracker ID used to display the initials of a user
	 */
	private String tid;
	/**
	 * UNIX epoch timestamp in seconds of the location fix 
	 */
    @JsonbTypeAdapter(TimestampAdapter.class)
	private ZonedDateTime tst;
    
    @JsonbProperty(value="_type")
    private String type = "location";
    
    @JsonbProperty(value="acc")
    private Integer accuracy = 30;
    
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}
	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * @return the tst
	 */
	public ZonedDateTime getTst() {
		return tst;
	}
	
	
	/**
	 * @return the accuracy
	 */
	public Integer getAccuracy() {
		return accuracy;
	}
	/**
	 * @param tst the tst to set
	 */
	public void setTst(ZonedDateTime tst) {
		this.tst = tst;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OwntracksLocation [lat=" + lat + ", lon=" + lon + ", tid=" + tid + ", tst=" + tst + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lon == null) ? 0 : lon.hashCode());
		result = prime * result + ((tid == null) ? 0 : tid.hashCode());
		result = prime * result + ((tst == null) ? 0 : tst.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwntracksLocation other = (OwntracksLocation) obj;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lon == null) {
			if (other.lon != null)
				return false;
		} else if (!lon.equals(other.lon))
			return false;
		if (tid == null) {
			if (other.tid != null)
				return false;
		} else if (!tid.equals(other.tid))
			return false;
		if (tst == null) {
			if (other.tst != null)
				return false;
		} else if (!((Long)tst.toEpochSecond()).equals(other.tst.toEpochSecond()))
			return false;
		return true;
	}
    
	
    public static OwntracksLocation from(LocationData locationData) {
    	OwntracksLocation owntracksLocation = new OwntracksLocation();
    	owntracksLocation.setTid(locationData.getImei());
    	owntracksLocation.setLat(locationData.getLatitude());
    	owntracksLocation.setLon(locationData.getLongitude());
    	owntracksLocation.setTst(locationData.getTimestamp());
    	return owntracksLocation;
    }

}
