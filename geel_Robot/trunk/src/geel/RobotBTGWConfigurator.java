package geel;

import java.util.Hashtable;
import java.util.List;

import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;

/**
 * This class centralized the code needed to interactively configure Configurable classes
 * using the  Bluetooth Gateway.
 * 
 * 
 * @author jeroendv
 *
 */
public class RobotBTGWConfigurator implements IBTGWCommandListener {
	
	/**
	 * a map that links parameter id's to a specific configurable object.
	 * filled by registerId(...) 
	 */
	private static Hashtable idmap = new Hashtable();
	
	
	/**
	 * register all the  configurable parameters of a configurable object
	 * ensuring that each parameter has a unique id
	 * 
	 * @param configurable
	 * @throws IllegalArgumentException
	 * 	thrown if a non-unique configurable parameter tries to register itself
	 */
	public static void register(Configurable configurable)throws IllegalArgumentException{
		/* 
		 * todo: parameter id can be prefixed by a namespace so that parameters id's
		 * only need to be unique in each configurable object instead of requiring them
		 * to be unique across all configurable objects
		 */
		
		
		//register the configurable boolean parameters
		String[] boolIdList = configurable.listConfigurableBooleans();
		
		for (String boolId : boolIdList) {
			try{
				registerId(configurable, boolId);
			} catch (IllegalArgumentException e){
				//pass exception allong
				throw e;
			}
		}
		
		//register the configurable integer parameters
		String[] intIdList = configurable.listConfigurableIntegers();
		
		for (String intId : intIdList) {
			try {
				registerId(configurable, intId);
			} catch (IllegalArgumentException e) {
				//pass exception allong
				throw e;
			}
		}
		
		//register the configurable float parameters
		String[] floatIdList = configurable.listConfigurableFloat();
		
		for (String floatId : floatIdList) {
			try {
				registerId(configurable, floatId);
			} catch (IllegalArgumentException e) {
				//pass exception allong
				throw e;
			}
		}
		
		
	}

	/**
	 * register a parameter in this.idmap
	 * ensuring that the parameter id is unique
	 * 
	 * @param configurable
	 * @param id
	 * @throws IllegalArgumentException
	 * 	thrown if a non-unique configurable parameter tries to register itself
	 */
	private static void registerId(Configurable configurable, String id)  throws IllegalArgumentException{
		if(!isIdRegistered(id)){
			RobotBTGWConfigurator.idmap.put(id, configurable);
		}else{
			throw new IllegalArgumentException("parameter id '"+id+"' already exists");
		}
	}
	
	/**
	 * check if certain parameter id is already registered
	 * @param id
	 * @return
	 */
	private static boolean isIdRegistered(String id) {
		return RobotBTGWConfigurator.idmap.get(id) != null;
	}









	
	
	
	
	
	
	
	
	/* 
	 * 
	 * (non-Javadoc)
	 * @see geel.BTGW.infrastructure.IBTGWCommandListener#handlePacket(geel.BTGW.packets.BTGWPacket)
	 */
	@Override
	public void handlePacket(BTGWPacket packet) {
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGREQUEST){
			
			
		}
		
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGBOOLEAN){
			
		}
		
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGFLOAT){
			
		}
		
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGINTEGER){
			
		}
		
		
	}
	
	
	
	
	
	
	

}
