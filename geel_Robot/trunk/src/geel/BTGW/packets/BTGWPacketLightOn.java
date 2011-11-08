package geel.BTGW.packets;

public class BTGWPacketLightOn extends BTGWPacket {
	public BTGWPacketLightOn() {
		setCommandCode(CMD_LIGHT_ON);
	}
}
