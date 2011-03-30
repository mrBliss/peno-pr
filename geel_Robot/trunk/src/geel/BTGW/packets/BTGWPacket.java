package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The BTGWPacket (Bluetooth Gateway Packet) class is used as a superclass
 * for all commands to be sent over a bluetooth link.
 * 
 * A command consists of a command code to identify it, and some data to 
 * be transmitted. 
 * 
 * When transmitting the packet, derived classes should
 * call the transmit() function of the superclass (BTGWPacket) so that 
 * the commandcode itself is already sent. The specific implemention then
 * only needs to worry about transmitting the command-specific data.
 * 
 * To receive the packet, the commandcode is used to identify a packet type.
 * After that, the receive() function of the identified type is called to read in
 * the remainder of the data. So, the receive() function of the derived type
 * only needs to worry about reading the command-specific data.
 * 
 * To add a command
 *  - create a constant CMD_<commandname> with a unique value
 *  - Update CMD_AMOUNT so that it reflects the total amount of commands
 *  - Modify getPacketOfType() to return a packet of the new command type aswell
 * 
 * @author Steven Van Acker
 *
 */

public class BTGWPacket {
	public static final int CMD_PING = 0;
	public static final int CMD_PONG = 1;
	public static final int CMD_MESSAGE = 2;
	public static final int CMD_DIE = 3;
	public static final int CMD_STATUSUPDATE = 4;
	public static final int CMD_CONFIGBOOLEAN = 5;
	public static final int CMD_CONFIGINTEGER = 6;
	public static final int CMD_CONFIGFLOAT = 7;
	public static final int CMD_CONFIGREQUEST = 8;
	public static final int CMD_LIGHT_ON = 9;
	public static final int CMD_LIGHT_OFF = 10;
	
	/* Update this when adding a new command */
	public static final int CMD_AMOUNT = 11;
	
	private int $commandCode;
	
	/* Update this when adding a new command */
	public static BTGWPacket getPacketOfType(int type) {
		switch (type) {
		case CMD_PING:
			return new BTGWPacketPing();
		case CMD_PONG:
			return new BTGWPacketPong();
		case CMD_MESSAGE:
			return new BTGWPacketMessage();
		case CMD_DIE:
			return new BTGWPacketDie();
		case CMD_STATUSUPDATE:
			return new BTGWPacketStatusUpdate();
		case CMD_CONFIGBOOLEAN:
			return new BTGWPacketConfigBoolean();
		case CMD_CONFIGINTEGER:
			return new BTGWPacketConfigInteger();
		case CMD_CONFIGFLOAT:
			return new BTGWPacketConfigFloat();
		case CMD_CONFIGREQUEST:
			return new BTGWPacketConfigRequest();
		case CMD_LIGHT_ON:
			return new BTGWPacketLightOn();
		case CMD_LIGHT_OFF:
			return new BTGWPacketLightOff();
		default:
			return null;
		}
	}

	protected void setCommandCode(int commandCode) {
		$commandCode = commandCode;
	}

	public int getCommandCode() {
		return $commandCode;
	}
	
	public void transmit(DataOutputStream output) throws IOException {	
			output.writeInt($commandCode);
	}
	
	/* the received data no longer contains the commandCode,
	 * because that code was already read to identify this command
	 */
	public void receive(DataInputStream input) throws IOException {
		
	}
	
	public String readString(DataInputStream input) throws IOException {
		int len = input.readInt();
		char m[] = new char[len];
		for(int i = 0; i < len; i++) {
			m[i] = input.readChar();
		}
		return new String(m);
	}
	 
	public void writeString(DataOutputStream output, String s) throws IOException {		
		output.writeInt(s.length());
		output.writeChars(s);
	}
}
