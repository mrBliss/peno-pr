package geel;

/**
 * This interface indicates that a class is has configurable parameters.
 * Such configurable parameters are either booleans, integers or floats and are
 * identified by a string ID which must be unique.
 * 
 * every configurable class implement 3 basic operations for each type: 
 *  + a setter to set the value of a parameter,
 *  + a getter to get the value of a parameter and
 *  + a list operation to list all parameter id's of that type.
 *  
 *  To ensure that configuration attempts can not be silently ignored,
 *  setting or getting of a parameter ID that is not recognized must results in an 
 *  {@link IllegalArgumentException}. 
 *  
 *  The setter may also throw an {@link IllegalArgumentException} if the value to be 
 *  set is not valid for some reason.
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
