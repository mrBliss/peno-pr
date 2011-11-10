package geel.BTGW.robot;

import geel.BTGW.infrastructure.BTGWConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * This class waits for an incoming bluetooth connection.
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWRealConnectionTaker extends BTGWConnection {
	private BTConnection $btc;
	
	public void realConnect() {
		$btc = Bluetooth.waitForConnection();		
		setOutputStream(new DataOutputStream($btc.openDataOutputStream()));
		setInputStream(new DataInputStream($btc.openDataInputStream()));
		setStatus(STATUS_CONNECTED);
	}

	public void realDisconnect() {
		$btc.close();
		setStatus(STATUS_DISCONNECTED);
	}
}
