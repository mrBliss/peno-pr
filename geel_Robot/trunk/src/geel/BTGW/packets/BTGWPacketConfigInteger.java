package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWPacketConfigInteger extends BTGWPacket {
	private String key;
	private int value;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	
	public BTGWPacketConfigInteger() {
		setCommandCode(BTGWPacket.CMD_CONFIGINTEGER);
	}
	
	public BTGWPacketConfigInteger(String key, int value) {
		this();
		setKey(key);
		setValue(value);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
		
		writeString(output, getKey());
		output.writeInt(getValue());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);
		
		setKey(readString(input));
		setValue(input.readInt());
	}
}
