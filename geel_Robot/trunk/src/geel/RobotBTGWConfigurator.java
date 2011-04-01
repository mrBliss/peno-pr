package geel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketConfigBoolean;
import geel.BTGW.packets.BTGWPacketConfigFloat;
import geel.BTGW.packets.BTGWPacketConfigInteger;
import geel.BTGW.packets.BTGWPacketConfigRequest;
import geel.BTGW.packets.BTGWPacketMessage;

/**
 * This class allows centralized configuration of multiple configurable object over 
 * the bluetooth gateway.
 * 
 *  + It ensures that the parameter id's for each parameter type are unique across
 * all configurable objects.
 *  + a {@link BTGWPacketConfigRequest} is handles by sending the current configuration state
 *  of the registered configurable objects.
 *  + {@link BTGWPacketConfigBoolean}, {@link BTGWPacketConfigFloat} and {@link BTGWPacketConfigInteger}
 *  are handled by identifying the configurable object corresponding to the parameter id and calling the
 *  respective setter.
 *  
 * To ensure that configuration attempts can not be silently ignored,
 * setting or getting of a parameter ID that is not recognized is logged using a 
 * {@link BTGWPacketMessage}.
 * 
 * failed calls to a setter will also be logged over the {@link BTGateway} in the same way.
 * 
 * 
 * @author jeroendv
 *
 */
public class RobotBTGWConfigurator {
	
	/**
	 * a map that links interger parameter id's to a specific configurable object.
	 * filled by registerId(...) 
	 */
	private static Hashtable intIdMap = new Hashtable();
	
	/**
	 * a map that links float parameter id's to a specific configurable object.
	 * filled by registerId(...) 
	 */
	private static Hashtable floatIdMap = new Hashtable();
	
	/**
	 * a map that links boolean parameter id's to a specific configurable object.
	 * filled by registerId(...) 
	 */
	private static Hashtable boolIdMap = new Hashtable();
	
	
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
				registerBoolId(configurable, boolId);
			} catch (IllegalArgumentException e){
				//pass exception along
				throw e;
			}
		}
		
		//register the configurable integer parameters
		String[] intIdList = configurable.listConfigurableIntegers();
		
		for (String intId : intIdList) {
			try {
				registerIntId(configurable, intId);
			} catch (IllegalArgumentException e) {
				//pass exception along
				throw e;
			}
		}
		
		//register the configurable float parameters
		String[] floatIdList = configurable.listConfigurableFloat();
		
		for (String floatId : floatIdList) {
			try {
				registerFloatId(configurable, floatId);
			} catch (IllegalArgumentException e) {
				//pass exception along
				throw e;
			}
		}
		
		
	}

	/**
	 * register a parameter
	 * ensuring that the parameter id is unique
	 * 
	 * @param configurable
	 * @param id
	 * @throws IllegalArgumentException
	 * 	thrown if a non-unique configurable parameter tries to register itself
	 */
	private static void registerBoolId(Configurable configurable, String id)  throws IllegalArgumentException{
		if(RobotBTGWConfigurator.boolIdMap.get(id) == null){
			RobotBTGWConfigurator.boolIdMap.put(id, configurable);
		}else{
			throw new IllegalArgumentException("parameter id '"+id+"' already exists");
		}
	}
	
	/**
	 * register a parameter
	 * ensuring that the parameter id is unique
	 * 
	 * @param configurable
	 * @param id
	 * @throws IllegalArgumentException
	 * 	thrown if a non-unique configurable parameter tries to register itself
	 */
	private static void registerIntId(Configurable configurable, String id)  throws IllegalArgumentException{
		if(RobotBTGWConfigurator.intIdMap.get(id) == null){
			RobotBTGWConfigurator.intIdMap.put(id, configurable);
		}else{
			throw new IllegalArgumentException("parameter id '"+id+"' already exists");
		}
	}
	
	/**
	 * register a parameter
	 * ensuring that the parameter id is unique
	 * 
	 * @param configurable
	 * @param id
	 * @throws IllegalArgumentException
	 * 	thrown if a non-unique configurable parameter tries to register itself
	 */
	private static void registerFloatId(Configurable configurable, String id)  throws IllegalArgumentException{
		if( RobotBTGWConfigurator.floatIdMap.get(id) == null ){
			RobotBTGWConfigurator.floatIdMap.put(id, configurable);
		}else{
			throw new IllegalArgumentException("parameter id '"+id+"' already exists");
		}
	}
	

	/**
	 * register routines with the BTGateway for configuration purpose.
	 * handling config requests, boolean configs, integer configs and float configs.
	 * 
	 * it requires that the BTGateway is up and functional
	 */
	public static void registerWithBTGW(){
		BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGREQUEST, configRequestHandler);
		
		BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGBOOLEAN, configBooleanHandler);
		BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGINTEGER, configIntergerHandler);
		BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGFLOAT, configFloatHandler);
	}
	
	/**
	 * handler for {@link BTGWPacketConfigInteger}
	 * that sets the integer parameter of the respective configurable object
	 * 
	 * reports a failure using {@link BTGWPacketMessage} in case the parameter id is
	 * not recognized or the setting of the value somehow failed.
	 */
	private static IBTGWCommandListener configIntergerHandler = new IBTGWCommandListener() {
		
		@Override
		public void handlePacket(BTGWPacket packet) {
			String id = ((BTGWPacketConfigInteger) packet).getKey();
			int value = ((BTGWPacketConfigInteger) packet).getValue();
			
			Configurable configurable = (Configurable) RobotBTGWConfigurator.intIdMap.get(id);
			if(configurable == null) 
				BTGateway.getInstance().sendPacket(new BTGWPacketMessage("parameter id '"+id+"' is not registered"));
			else{
				try{
					configurable.setConfigurableInt(id, value);
				}catch(IllegalArgumentException e){
					BTGateway.getInstance().sendPacket(new BTGWPacketMessage("'"+id+"' = '"+value+"' could not be set"));
				}
			}				
		}
	};
	
	/**
	 * handler for {@link BTGWPacketConfigFloat}
	 * that sets the float parameter of the respective configurable object
	 * 
	 * reports a failure using {@link BTGWPacketMessage} in case the parameter id is
	 * not recognized or the setting of the value somehow failed.
	 */
	private static IBTGWCommandListener configFloatHandler = new IBTGWCommandListener() {
		
		@Override
		public void handlePacket(BTGWPacket packet) {	
			String id = ((BTGWPacketConfigFloat) packet).getKey();
			float value = ((BTGWPacketConfigFloat) packet).getValue();
		
			Configurable configurable = (Configurable) RobotBTGWConfigurator.floatIdMap.get(id);
			if(configurable == null) 
				BTGateway.getInstance().sendPacket(new BTGWPacketMessage("parameter id '"+id+"' is not registered"));
			else{
				try{
					configurable.setConfigurableFloat(id, value);
				}catch(IllegalArgumentException e){
					BTGateway.getInstance().sendPacket(new BTGWPacketMessage("'"+id+"' = '"+value+"' could not be set"));
				}
			}
		}
	};
	
	/**
	 * handler for {@link BTGWPacketConfigBoolean}
	 * that sets the boolean parameter of the respective configurable object
	 * 
	 * reports a failure using {@link BTGWPacketMessage} in case the parameter id is
	 * not recognized or the setting of the value somehow failed.
	 */
	private static IBTGWCommandListener configBooleanHandler = new IBTGWCommandListener() {
		
		@Override
		public void handlePacket(BTGWPacket packet) {
			String id = ((BTGWPacketConfigBoolean) packet).getKey();
			boolean value = ((BTGWPacketConfigBoolean) packet).getValue();
			
			Configurable configurable = (Configurable) RobotBTGWConfigurator.boolIdMap.get(id);
			if(configurable == null) 
				BTGateway.getInstance().sendPacket(new BTGWPacketMessage("parameter id '"+id+"' is not registered"));
			else{
				try{
					configurable.setConfigurableBoolean(id, value);
				}catch(IllegalArgumentException e){
					BTGateway.getInstance().sendPacket(new BTGWPacketMessage("'"+id+"' = '"+value+"' could not be set"));
				}
			}
		}
	};
	
	
	/**
	 * handler for {@link BTGWPacketConfigRequest} that
	 * responds by sending the current configuration state of the registered configurable objects.
	 */
	private static IBTGWCommandListener configRequestHandler = new IBTGWCommandListener() {
		
		@Override
		public void handlePacket(BTGWPacket packet) {
			Enumeration idEnum;
			String id;
			Configurable configurable;
			
			//send all boolean parameters
			idEnum = RobotBTGWConfigurator.boolIdMap.keys();
			
			while( idEnum.hasMoreElements() ){
				id = (String) idEnum.nextElement();
				configurable = (Configurable) RobotBTGWConfigurator.boolIdMap.get(id);
				boolean value = configurable.getConfigurableBoolean(id);
				
				BTGateway.getInstance().sendPacket(new BTGWPacketConfigBoolean(id,value));
			}
			
			//send all integer parameters
			idEnum = RobotBTGWConfigurator.intIdMap.keys();
			
			while( idEnum.hasMoreElements() ){
				id = (String) idEnum.nextElement();
				configurable  = (Configurable) RobotBTGWConfigurator.intIdMap.get(id);
				int value = configurable.getConfigurableInt(id);
				
				BTGateway.getInstance().sendPacket(new BTGWPacketConfigInteger(id,value));
			}
			
			//send all float parameters
			idEnum = RobotBTGWConfigurator.floatIdMap.keys();
			
			while( idEnum.hasMoreElements() ){
				id = (String) idEnum.nextElement();
				configurable  = (Configurable) RobotBTGWConfigurator.floatIdMap.get(id);
				float value = configurable.getConfigurableFloat(id);
				
				BTGateway.getInstance().sendPacket(new BTGWPacketConfigFloat(id,value));
			}
			
		}
	};
	
	
	
	
	
	

}
