package geel.behaviours;

import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketManualOverride;
import geel.BTGW.packets.BTGWPacketManualSteer;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

public class Manual implements Behavior, IBTGWCommandListener {
    private Motor rightMotor;
    private Motor leftMotor;
    private boolean active = false;

    public Manual(Motor rightMotor, Motor leftMotor) {
        this.rightMotor = rightMotor;
        this.leftMotor = leftMotor;
        
        BTGateway.getInstance().addListener(BTGWPacket.CMD_MANUAL_STEER, this);
        BTGateway.getInstance().addListener(BTGWPacket.CMD_MANUAL_OVERRIDE, this);
    }

    public boolean takeControl() {
    	return active;
    }

    public void action() {
    	active = true;
        leftMotor.stop();
        rightMotor.stop();
        
        while (active) {
        	try { Thread.sleep(10); } catch (InterruptedException e) {}
        }
    }

    public void suppress() {
        active = false;
    }
    
    public void setMotors(int left, int right) {
    	if(!active) return;
    	
    	if(left != 0) {
    		leftMotor.setSpeed(Math.abs(left));
    		if(left > 0) leftMotor.forward();
    		else leftMotor.backward();
    	} else leftMotor.stop();
    	
    	if(right != 0) {
    		rightMotor.setSpeed(Math.abs(right));
    		if(right > 0) rightMotor.forward();
    		else rightMotor.backward();
    	} else rightMotor.stop();
    	
    }

	@Override
	public void handlePacket(BTGWPacket packet) {
		if(packet.getCommandCode() == BTGWPacket.CMD_MANUAL_STEER) {
			BTGWPacketManualSteer p = (BTGWPacketManualSteer) packet;
			setMotors(p.getLeft(), p.getRight());
		}
		
		if(packet.getCommandCode() == BTGWPacket.CMD_MANUAL_OVERRIDE) {
			BTGWPacketManualOverride p = (BTGWPacketManualOverride) packet;
			active = p.isActive();
		}
	}
}
