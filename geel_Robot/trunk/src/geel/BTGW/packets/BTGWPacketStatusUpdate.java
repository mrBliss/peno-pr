package geel.BTGW.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BTGWPacketStatusUpdate extends BTGWPacket {
	private int groundColor;
	private int lightSensorValue;
	
	private int sonarSensorRawValue;
	private int sonarSensorValue;
	
	private boolean touchSensorValue;
	
	




	public int getGroundColor() {
		return groundColor;
	}

	public void setGroundColor(int goundColor) {
		this.groundColor = goundColor;
	}

	public int getSonarSensorRawValue() {
		return sonarSensorRawValue;
	}

	public void setSonarSensorRawValue(int sonarSensorRawValue) {
		this.sonarSensorRawValue = sonarSensorRawValue;
	}

	
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
	
	public BTGWPacketStatusUpdate(int groundColor, int light, int rawSonar,int sonar, boolean touch) {
		this();
		setGroundColor(groundColor);
		setLightSensorValue(light);
		setSonarSensorRawValue(rawSonar);
		setSonarSensorValue(sonar);
		setTouchSensorValue(touch);
	}
	
	public void transmit(DataOutputStream output) throws IOException {
		super.transmit(output);
		
		output.writeInt(getGroundColor());
		output.writeInt(getLightSensorValue());
		output.writeInt(getSonarSensorRawValue());
		output.writeInt(getSonarSensorValue());
		output.writeBoolean(getTouchSensorValue());
	}
	
	public void receive(DataInputStream input) throws IOException {
		super.receive(input);
		setGroundColor(input.readInt());
		setLightSensorValue(input.readInt());
		setSonarSensorRawValue(input.readInt());
		setSonarSensorValue(input.readInt());
		setTouchSensorValue(input.readBoolean());
	}
	
	
}
