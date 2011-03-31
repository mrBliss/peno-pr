package geel;

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
public abstract class RobotConfigurator implements IBTGWCommandListener {

	
	
	
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
