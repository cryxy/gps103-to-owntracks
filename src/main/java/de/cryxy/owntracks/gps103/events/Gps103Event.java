/**
 * 
 */
package de.cryxy.owntracks.gps103.events;

/**
 * @author fabian
 *
 */
public class Gps103Event {
	
	String imei;

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
		return "Gps103Command [imei=" + imei + "]";
	}
	
	

}
