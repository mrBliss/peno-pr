package geel.BTGW.packets;

public class BTGWPacketPing extends BTGWPacket {
	public BTGWPacketPing() {
		setCommandCode(CMD_PING);
	}
}
