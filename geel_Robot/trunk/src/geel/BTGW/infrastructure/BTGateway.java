package geel.BTGW.infrastructure;

import geel.BTGW.packets.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Bluetooth Gateway is the communications channel between this process and a
 * remote process, using Bluetooth or any other means implemented by the BTGWConnection.
 * 
 * The task of the BTGateway is to transmit BTGWPackets from and to the remote site using
 * non-blocking primitives (transmit() and receive()) with queueing.
 * 
 * In a regular situation there would only be 1 BTGateway in each running process, allowing 
 * the usage of static function calls like BTGateway.transmit() and others.
 * However, in a situation where a remote robot or GUI is simulated, we require 2 BTGateways
 * to be available in the running process: one on each side of the simulation (one for 
 * the PC, one for the robot).
 * Because of this, a generalised version of the Singleton design pattern is used,
 * allowing storage and retrieval of any number of BTGateway objects by using
 * BTGateway.addInstance(...) and BTGateway.getInstance(...)
 * 
 * @author Steven Van Acker
 *
 */

public class BTGateway {
	private static ArrayList $btgwList = new ArrayList();
	private static Object $defaultBTGateway;
	private static int $identityCounter = 0;
 
	private ArrayList[] $listeners = new ArrayList[BTGWPacket.CMD_AMOUNT];
	private ArrayList $queue = new ArrayList();
	private DataInputStream $input;
	private DataOutputStream $output;
	
	private BTGWConnection connection;
	
	private int $identity;
	
	public BTGateway(BTGWConnection conn) {
		this(conn.getInputStream(), conn.getOutputStream());
		setConnection(conn);
	}
	
	public BTGateway(DataInputStream input, DataOutputStream output) {
		$identity = $identityCounter++;
		$input = input;
		$output = output;
		
		// send packets from the queue
		Thread sendThread = new Thread() {
			public void run() {
				BTGWPacket p = null;
				while(true) {
					
					while((p = popNextPacket()) != null) {
						try {
							p.transmit($output);
							$output.flush();
						} catch (IOException e1) {
							BTGWConnection c = getConnection();
							if(c != null) {
								c.autoReconnect();
							}
						}
					}
				}
			}
		};
		sendThread.start();
		
		// receive thread
		Thread recvThread = new Thread() {
			public void run() {
				BTGWPacket p = null;
				int cmdtype = 0;
				while(true) {
					try {
						cmdtype = $input.readInt();
						
						if(cmdtype < BTGWPacket.CMD_AMOUNT) {
							p = BTGWPacket.getPacketOfType(cmdtype);
							if(p != null) {
								p.receive($input);
								//System.out.println("BTGateway["+$identity+"] received packet "+cmdtype);
								notifyListeners(cmdtype, p);
							} else {
								// received packet with unknown command code
								// silently ignoring that for now...
							}
						}
					} catch (IOException e) {
						BTGWConnection c = getConnection();
						if(c != null) {
							c.autoReconnect();
						}
					}
				}
			}
		};
		recvThread.start();
	}
	
	public int getIdentity() {
		return $identity;
	}
	
	private synchronized BTGWPacket popNextPacket() {
		if($queue == null || $queue.size() < 1)
			return null;
		
		BTGWPacket p = (BTGWPacket) $queue.remove(0);
		
		return p;
	}
	
	public synchronized void sendPacket(BTGWPacket p) {
		$queue.add(p);
	}
	
	private void notifyListeners(int cmdtype, BTGWPacket packet) {
		ArrayList typeListeners = (ArrayList) $listeners[cmdtype];
		
		if(typeListeners == null) {
			/* another job well done */
			//System.out.println("BTGateway["+$identity+"] Nothing to do for packet "+cmdtype);
			return;
		}
		
    	for(int i = 0; i < typeListeners.size(); i++) {
    		((IBTGWCommandListener)typeListeners.get(i)).handlePacket(packet);
    		//System.out.println("BTGateway["+$identity+"] BTGW sent notify to class "+typeListeners.get(i).getClass().getName());
    	}
    }
	
	public synchronized void addListener(int cmdtype, IBTGWCommandListener listener) {
		//System.out.println("BTGateway["+$identity+"] registered listener("+cmdtype+") "+listener.getClass().getName()); //FIXME
		ArrayList typeListeners = $listeners[cmdtype];
		if(typeListeners == null) {
			typeListeners = new ArrayList();
			$listeners[cmdtype] = typeListeners;
		}
		typeListeners.add(listener);
	}
	
	/* register this object as a listener for all events */
	public synchronized void addOmniListener(IBTGWCommandListener listener) {
		for(int cmd = 0; cmd < BTGWPacket.CMD_AMOUNT; cmd++) {
			addListener(cmd, listener);
		}
	}
	
	
	public static void addInstance(BTGateway btgw) {
		$btgwList.add(btgw);
	}
	
	public static void setDefaultBTGateway(BTGateway btgw) {
		$defaultBTGateway = btgw;
	}
	
	public static BTGateway getInstance() {
		if($defaultBTGateway == null) {
			return getInstance(0);
		} else {
			return (BTGateway)$defaultBTGateway;
		}
	}
	
	public static BTGateway getInstance(int n) {
		if(n < $btgwList.size())
			return (BTGateway)$btgwList.get(n);
		else
			return null;
	}

	public BTGWConnection getConnection() {
		return connection;
	}

	private void setConnection(BTGWConnection connection) {
		this.connection = connection;
	}
}