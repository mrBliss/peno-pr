package geel.BTGW.pc;

/* errors in this file are normal, because they should be compiled
 * for the GUI, not the robot
 */

import geel.BTGW.infrastructure.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

/**
 * This class sets up a real bluetooth connection between the PC and the robot.
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWRealConnectionMaker extends BTGWConnection {

	private String $robotname, $robotMAC;
	
	private NXTComm $nxtComm;
	
	public BTGWRealConnectionMaker(String robotname, String robotMAC) {
		super();
		
		$robotname = robotname;
		$robotMAC = robotMAC;
	}

	public void realConnect() {
		try {
			$nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTInfo robot = new NXTInfo(NXTCommFactory.BLUETOOTH, $robotname, $robotMAC);
			$nxtComm.open(robot);

			setInputStream(new DataInputStream($nxtComm.getInputStream()));
			setOutputStream(new DataOutputStream($nxtComm.getOutputStream()));
			setStatus(STATUS_CONNECTED);
		} catch (NXTCommException e) {
			setStatus(STATUS_DISCONNECTED);
			e.printStackTrace();
		}
	}

	public void realDisconnect() {
		try {
			$nxtComm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setStatus(STATUS_DISCONNECTED);
	}

}
