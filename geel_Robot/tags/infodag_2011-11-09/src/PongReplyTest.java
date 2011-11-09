import java.io.IOException;

import lejos.nxt.Button;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketPong;
import geel.BTGW.robot.BTGWRealConnectionTaker;


public class PongReplyTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		initilizeBTGateWay();

		addPingListener();
		
		
		
		Button.ESCAPE.waitForPress();
		System.out.println("Main: starting program shutdown");
		
		BTGateway.getInstance().close();
		
		System.out.println("Main: stopped");

	}
	
	
	/*
	 * when receiving a ping command: 
	 *  1) print 'ping pong' to sysout 
	 *  2) reply with a pong command
	 */
	private static void addPingListener() {
		BTGateway.getInstance().addListener(BTGWPacket.CMD_PING,
				new IBTGWCommandListener() {

					@Override
					public void handlePacket(BTGWPacket packet) {
						if (packet.getCommandCode() == BTGWPacket.CMD_PING) {
							System.out.println("PING? PONG!");
							
							BTGateway.getInstance().sendPacket(
									new BTGWPacketPong());
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
