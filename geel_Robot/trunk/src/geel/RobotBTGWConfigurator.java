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

/**
 * This class centralized the code needed to interactively configure Configurable classes
 * using the  Bluetooth Gateway.
 * 
 * 
 * @author jeroendv
 *
 */
public class RobotBTGWConfigurator {
	
	/**
	 * a map that links parameter id's to a specific configurable object.
	 * filled by registerId(...) 
	 */
	private static Hashtable intIdMap = new Hashtable();
	
	/**
	 * a map that links parameter id's to a specific configurable object.
	 * filled by registerId(...) 
	 */
	private static Hashtable floatIdMap = new Hashtable();
	
	/**
	 * a map that links parameter id's to a specific configurable object.
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
				//pass exception allong
				throw e;
			}
		}
		
		//register the configurable integer parameters
		String[] intIdList = configurable.listConfigurableIntegers();
		
		for (String intId : intIdList) {
			try {
				registerIntId(configurable, intId);
			} catch (IllegalArgumentException e) {
				//pass exception allong
				throw e;
			}
		}
		
		//register the configurable float parameters
		String[] floatIdList = configurable.listConfigurableFloat();
		
		for (String floatId : floatIdList) {
			try {
				registerFloatId(configurable, floatId);
			} catch (IllegalArgumentException e) {
				//pass exception allong
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
		BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGREQUEST, new IBTGWCommandListener() {
			
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
		});
		
		BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGBOOLEAN, new IBTGWCommandListener() {
			
			@Override
			public void handlePacket(BTGWPacket packet) {
				String id = ((BTGWPacketConfigBoolean) packet).getKey();
				boolean value = ((BTGWPacketConfigBoolean) packet).getValue();
				
				Configurable configurable = (Configurable) RobotBTGWConfigurator.boolIdMap.get(id);
				if(configurable != null) configurable.setConfigurableBoolean(id, value);
			}
		});
		
		BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGINTEGER, new IBTGWCommandListener() {
			
			@Override
			public void handlePacket(BTGWPacket packet) {
				String id = ((BTGWPacketConfigInteger) packet).getKey();
				int value = ((BTGWPacketConfigInteger) packet).getValue();
				
				Configurable configurable = (Configurable) RobotBTGWConfigurator.intIdMap.get(id);
				if(configurable != null) configurable.setConfigurableInt(id, value);				
			}
		});

	BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGFLOAT, new IBTGWCommandListener() {
	
		@Override
		public void handlePacket(BTGWPacket packet) {	
			String id = ((BTGWPacketConfigFloat) packet).getKey();
			float value = ((BTGWPacketConfigFloat) packet).getValue();
		
			Configurable configurable = (Configurable) RobotBTGWConfigurator.floatIdMap.get(id);
			if(configurable != null) configurable.setConfigurableFloat(id, value);			
		}
	});
		
	}
	
	
	
	
	
	
	
	

}
