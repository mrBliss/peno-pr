package geel;

/**
 * this interface indicated that a class is configurable through the Bluetooth Gateway.
 * 
 * every configurable class must implement 3 basic operations for each type,
 * configurable booleans, integers and floats, namely: 
 *  + void set(id,value)
 *  + <type> get(id)
 *  + String[] list() operation that list all parameter id's the configurable parameters of that type
 * 
 * @author jeroendv
 *
 */
public interface Configurable {
	
	
	/**
	 * return string array with  all parameter id's 
	 * of configurable booleans
	 */
	public String[] listConfigurableBooleans();
	
	/**
	 * set the value of a certain configurable parameter.
	 * 
	 * @param id
	 * @param value
	 * @throws IllegalArgumentException
	 * 	if the id is not recognized
	 *  or if setting the parameter somehow failed
	 */
	public void setConfigurableBoolean(String id, boolean value) throws IllegalArgumentException;
	
	/**
	 * return the value of a certain configurable parameter
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException if the id is not recognized
	 */
	public boolean getConfigurableBoolean(String id)throws IllegalArgumentException;
	
	/**
	 * return string array with  all parameter id's 
	 * of configurable integers
	 */
	public String[] listConfigurableIntegers();
	
	/**
	 * set the value of a certain configurable parameter.
	 * 
	 * @param id
	 * @param value
	 * @throws IllegalArgumentException
	 * 	if the id is not recognized
	 *  or if setting the parameter somehow failed
	 */
	public void setConfigurableInt(String id, int value) throws IllegalArgumentException;
	
	/**
	 * return the value of a certain configurable parameter
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException if the id is not recognized
	 */
	public int getConfigurableInt(String id) throws IllegalArgumentException;
	
	/**
	 * return string array with  all parameter id's 
	 * of configurable floats
	 */
	public String[] listConfigurableFloat();
	
	/**
	 * set the value of a certain configurable parameter.
	 * 
	 * @param id
	 * @param value
	 * @throws IllegalArgumentException
	 * 	if the id is not recognized
	 *  or if setting the parameter somehow failed
	 */
	public void setConfigurableFloat(String id, float value) throws IllegalArgumentException;
	
	/**
	 * return the value of a certain configurable parameter
	 * 
	 * @param id
	 * @return
	 * @throws IllegalArgumentException if the id is not recognized
	 */
	public float getConfigurableFloat(String id) throws IllegalArgumentException;
	
	

}
