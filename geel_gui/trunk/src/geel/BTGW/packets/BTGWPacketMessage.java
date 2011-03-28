package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * This package is used to send a generic message from robot to PC
 * (The other way works too, but it pretty useless)
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWPacketMessage extends BTGWPacket {
	private String msg;
	
	public String getMessage() {
		return msg;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public BTGWPacketMessage() {
		setCommandCode(BTGWPacket.CMD_MESSAGE);
	}
	
	public BTGWPacketMessage(String msg) {
		this();
		setMessage(msg);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
		
		output.writeInt(getMessage().length());
		output.writeChars(getMessage());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);
		
		int len = input.readInt();
		char m[] = new char[len];
		for(int i = 0; i < len; i++) {
			m[i] = input.readChar();
		}
		setMessage(new String(m));
	}
}
