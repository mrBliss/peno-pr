package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWPacketManualOverride extends BTGWPacket {
	private boolean active;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public BTGWPacketManualOverride() {
		setCommandCode(BTGWPacket.CMD_MANUAL_OVERRIDE);
	}
	
	public BTGWPacketManualOverride(boolean active) {
		this();
		setActive(active);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
	
		output.writeBoolean(isActive());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);

		setActive(input.readBoolean());
	}
}
