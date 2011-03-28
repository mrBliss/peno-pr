package geel.BTGW.infrastructure;

import geel.BTGW.packets.*;

public class BTGWDemoListener implements IBTGWCommandListener {

	public void handlePacket(BTGWPacket packet) {
		System.out.println("Received packet with type "+packet.getCommandCode());
	}
}
