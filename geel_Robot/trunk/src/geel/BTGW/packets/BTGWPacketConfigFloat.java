package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWPacketConfigFloat extends BTGWPacket {
	private String key;
	private float value;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	
	public BTGWPacketConfigFloat() {
		setCommandCode(BTGWPacket.CMD_CONFIGFLOAT);
	}
	
	public BTGWPacketConfigFloat(String key, float value) {
		this();
		setKey(key);
		setValue(value);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
		
		writeString(output, getKey());
		output.writeFloat(getValue());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);
		
		setKey(readString(input));
		setValue(input.readFloat());
	}
}
