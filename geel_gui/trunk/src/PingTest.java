import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketPing;
import geel.BTGW.pc.BTGWRealConnectionMaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PingTest {

	public static void main(String[] args) throws IOException {

		initializeBTGW("WallE", "00:16:53:02:F7:9D");

		addPongListener();

		//cmdline interface for sending a ping
		BufferedReader cmdlineReader = new BufferedReader(
				new InputStreamReader(System.in));

		System.out.println("type 'exit' to stop, send a ping signal otherwise");

		String line = null;
		// prompt
		System.out.print("> ");
		line = cmdlineReader.readLine();
		while(!line.equals("exit")) {
			//send ping
			System.out.println("    sending PING");
			BTGateway.getInstance().sendPacket(new BTGWPacketPing());
			
			// prompt
			System.out.print("> ");
			line = cmdlineReader.readLine();
		}


		//closing program
		System.out.println("Main: starting program shutdown");

		BTGateway.getInstance().close();
		cmdlineReader.close();
		
		System.out.println("Main: stopped");

	}

	/**
	 * register a pong listener on the Bluetooth Gateway that simply prints
	 * "    Received PONG" to stdout when a pong signal is received
	 * 
	 * pre: the BTGW is already initialized
	 */
	private static void addPongListener() {

		BTGateway.getInstance().addListener(BTGWPacket.CMD_PONG,
				new IBTGWCommandListener() {

					public void handlePacket(BTGWPacket packet) {
						assert (packet.getCommandCode() == BTGWPacket.CMD_PONG);

						if (packet.getCommandCode() == BTGWPacket.CMD_PONG) {
							System.out.println("    Received PONG");
						}
					}
				});
	}

	/**
	 * initialize a BTGW connection with a certain NXT brick
	 * 
	 * @param name
	 *            of the NXT brick
	 * @param address
	 *            of the NXT brick as reported by '$hcitool scan'
	 * @throws IOException 
	 */
	private static void initializeBTGW(String name, String address) throws IOException {
		BTGWConnection btgwconn = new BTGWRealConnectionMaker(name, address);
		btgwconn.connect();

		/* wait untill we are connected */
		while (btgwconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
			// System.out.println("Not connected. Sleeping...");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("connected");

		/* create a gateway */
		BTGateway btgw = new BTGateway(btgwconn);
		// make the BTGW globally accessible through BTGateWay.getinstance()
		BTGateway.addInstance(btgw);
	}

}
