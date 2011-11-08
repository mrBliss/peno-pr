package geel.BTGW.infrastructure;
import geel.BTGW.packets.*;

/**
 * This interface is to be implemented by every class wanting to be
 * informed of incoming BTGWPackets of a certain type. Whenever a BTGWPacket
 * comes in of the type that is being listened for, the handlePacket()
 * method on the registered listener will be called.
 * 
 * @author Steven Van Acker
 *
 */
public interface IBTGWCommandListener {
	public void handlePacket(BTGWPacket packet);
}
