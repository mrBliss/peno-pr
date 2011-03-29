package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BTGWPacketStatusUpdate extends BTGWPacket {
	private int lightSensorValue;
	private int sonarSensorValue;
	private boolean touchSensorValue;
	
	public int getLightSensorValue() {
		return lightSensorValue;
	}

	public int getSonarSensorValue() {
		return sonarSensorValue;
	}

	public boolean getTouchSensorValue() {
		return touchSensorValue;
	}

	private void setLightSensorValue(int lightSensorValue) {
		this.lightSensorValue = lightSensorValue;
	}

	private void setSonarSensorValue(int sonarSensorValue) {
		this.sonarSensorValue = sonarSensorValue;
	}

	private void setTouchSensorValue(boolean touchSensorValue) {
		this.touchSensorValue = touchSensorValue;
	}

	public BTGWPacketStatusUpdate() {
		setCommandCode(CMD_STATUSUPDATE);
	}
	
	public BTGWPacketStatusUpdate(int light, int sonar, boolean touch) {
		this();
		setLightSensorValue(light);
		setSonarSensorValue(sonar);
		setTouchSensorValue(touch);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
		
		output.writeInt(getLightSensorValue());
		output.writeInt(getSonarSensorValue());
		output.writeBoolean(getTouchSensorValue());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);
	
		setLightSensorValue(input.readInt());
		setSonarSensorValue(input.readInt());
		setTouchSensorValue(input.readBoolean());
	}
	
	
}
