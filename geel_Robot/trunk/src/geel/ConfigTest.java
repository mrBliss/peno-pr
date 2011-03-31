package geel;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketConfigBoolean;
import geel.BTGW.packets.BTGWPacketConfigFloat;
import geel.BTGW.packets.BTGWPacketConfigInteger;
import geel.BTGW.packets.BTGWPacketConfigRequest;
import geel.BTGW.robot.BTGWRealConnectionTaker;

/**
 * short config test that prints out if a Config packets has been received
 * and sends a bogus speed configuration on a config request
 * 
 * 
 * @author jeroendv
 *
 */
public class ConfigTest {
	
	
	public static void main(String[] args) {
		initilizeBTGateWay();
		
		
		BTGateway.getInstance().addOmniListener(new IBTGWCommandListener() {
			
			@Override
			public void handlePacket(BTGWPacket packet) {
				if(packet instanceof BTGWPacketConfigBoolean){
					System.out.println("received: BTGWPacketConfigBoolean");
				}
				
				if(packet instanceof BTGWPacketConfigFloat){
					System.out.println("received: BTGWPacketConfigFloat");
				}
				
				if(packet instanceof BTGWPacketConfigInteger){
					System.out.println("received: BTGWPacketConfigInterger");
				}
				
				if(packet instanceof BTGWPacketConfigRequest){
					System.out.println("received: BTGWPacketConfigRequest");
					BTGateway.getInstance().sendPacket(new BTGWPacketConfigInteger("speed",100));
				}
			}
		});
		
		
		
	}
	
	
	/**
	 * initialize a Bluetooth Gateway for communication between robot and pc and
	 * make it globally accessible through BTGateWay.getinstance()
	 * 
	 * note that this methods blocks until the gate way is ready for use
	 */
	public static void initilizeBTGateWay() {
		// create BT connection
		BTGWRealConnectionTaker btconn = new BTGWRealConnectionTaker();
		System.out.println("Waiting for BT...");
		btconn.connect();

		// wait till BT properly connected
		while (btconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		// make the BTGW globally accessible through BTGateWay.getinstance()
		BTGateway.addInstance(new BTGateway(btconn));
	}

}
