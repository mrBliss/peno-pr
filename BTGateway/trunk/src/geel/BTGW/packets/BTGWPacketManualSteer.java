package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 
 * @author Steven Van Acker
 *
 */
public class BTGWPacketManualSteer extends BTGWPacket {
	private int left;
	private int right;

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public void setRight(int right) {
		this.right = right;
	}
	
	public BTGWPacketManualSteer() {
		setCommandCode(BTGWPacket.CMD_MANUAL_STEER);
	}
	
	public BTGWPacketManualSteer(int left, int right) {
		this();
		setLeft(left);
		setRight(right);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
	
		output.writeInt(getLeft());
		output.writeInt(getRight());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);

		setLeft(input.readInt());
		setRight(input.readInt());
	}
}
