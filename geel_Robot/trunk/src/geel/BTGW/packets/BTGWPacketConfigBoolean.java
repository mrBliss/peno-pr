package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWPacketConfigBoolean extends BTGWPacket {
	private String key;
	private boolean value;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	
	public BTGWPacketConfigBoolean() {
		setCommandCode(BTGWPacket.CMD_CONFIGBOOLEAN);
	}
	
	public BTGWPacketConfigBoolean(String key, boolean value) {
		this();
		setKey(key);
		setValue(value);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
		
		writeString(output, getKey());
		output.writeBoolean(getValue());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);
		
		setKey(readString(input));
		setValue(input.readBoolean());
	}
}
