package geel.BTGW.packets;

public class BTGWPacketPong extends BTGWPacket {
	public BTGWPacketPong() {
		setCommandCode(CMD_PONG);
	}
}
