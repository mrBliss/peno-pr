package geel.BTGW.packets;

public class BTGWPacketConfigRequest extends BTGWPacket {
	public BTGWPacketConfigRequest() {
		setCommandCode(CMD_CONFIGREQUEST);
	}
}
