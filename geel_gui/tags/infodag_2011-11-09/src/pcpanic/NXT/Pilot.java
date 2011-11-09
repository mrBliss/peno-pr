package pcpanic.NXT;

import geel.BTGW.infrastructure.*;
import geel.BTGW.packets.*;
import geel.BTGW.pc.*;
import geel.GUI.GUIColorConfigurationPanel;
import geel.GUI.GUIConfigurationPanel;
import geel.GUI.GUISensorPanel;
import geel.GUI.GUIStandAloneFrame;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSlider;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommandConnector;
import lejos.pc.comm.NXTInfo;

/**
 *
 * @author Me
 */
public class Pilot implements IBTGWCommandListener {

    private JSlider sliderL;
    private JSlider sliderR;
    public InputStream inputStream;
    private OutputStream outputStream;
    private NXTComm open;

    public int getL() {
        return sliderL.getValue();
    }

    public int getR() {
        return sliderR.getValue();
    }

    public final void setL(int i) {
        sliderL.setValue(i);
    }

    public final void setR(int i) {
        sliderR.setValue(i);
    }

    public void difL(int i) {
        sliderL.setValue(getL() + i);
    }

    public void difR(int i) {
        sliderR.setValue(getR() + i);
    }

    public void multL(int i) {
        sliderL.setValue(getL() * i);
    }

    public void multR(int i) {
        sliderR.setValue(getR() * i);
    }

    public Pilot(JSlider sliderL, JSlider sliderR) throws Exception {
        this.sliderL = sliderL;
        this.sliderR = sliderR;
        setL(0);
        setR(0);
        connect();
    }

    public final void apply() {
        setL(Math.min(Math.max(-99, getL()), 99));
        setR(Math.min(Math.max(-99, getR()), 99));
        send(20, getL(), getR());
    }

    public void fullStop() {
        setL(0);
        setR(0);
        apply();
    }

    public void meanSpeed() {
        int nSpeed = (getL() + getR()) / 2;
        setL(nSpeed);
        setR(nSpeed);
    }

    public final void connect() throws Exception {
//    	open = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
//    	//open.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "WallE", "00:16:53:02:F7:9D"));
//    	open.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "ROOD", "00:16:53:06:23:A0"));
//        outputStream = open.getOutputStream();
//        inputStream = open.getInputStream();
        
        //fixme: this should be set by the Gui or something
//        BTGWConnection btgwconn = new BTGWRealConnectionMaker("ROOD", "00:16:53:06:23:A0");
    	BTGWConnection btgwconn = new BTGWRealConnectionMaker("WallE", "00:16:53:02:F7:9D");

        /* connect to it */
        btgwconn.connect();
        /* wait untill we are connected */
        while(btgwconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
                //System.out.println("Not connected. Sleeping...");
                try {
                        Thread.sleep(200);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }

        System.out.println("connected");
        /* create a gateway */
        BTGateway btgw = new BTGateway(btgwconn);
        BTGateway.addInstance(btgw);
        
        BTGateway.getInstance().addOmniListener(this);
        
        new GUIStandAloneFrame(new GUISensorPanel(), "Robot sensor data");
        new GUIStandAloneFrame(new GUIConfigurationPanel(), "Configuration values");
        new GUIStandAloneFrame(new GUIColorConfigurationPanel(), "Lightsensor calibrator");
    }

    public void close() {
    	if(BTGateway.getInstance() != null) {
        	BTGateway.getInstance().getConnection().disconnect();
        }
    	
//        try {
//            open.close();
//            
//        } catch (IOException ex) {
//        }
    }

    public void send(byte n) {
//        System.out.println("e: " + n);
//        try {
//            outputStream.write(n);
//            outputStream.flush();
//        } catch (IOException ex) {
//        }
    }

    public void send(byte i, byte j, byte k) {
//        System.out.println("m: " + i + " " + j + " " + k);
//        
//        try {
//            outputStream.write(i);
//            outputStream.write(j);
//            outputStream.write(k);
//            outputStream.flush();
//        } catch (IOException ex) {
//        }
//        
    }

    public void startRem() {
        //send((byte) 10);
    }

    public void stopRem() {
        //send((byte) 11);
    }

    public void globalSpeed(byte i) {
        //send((byte) 20, (byte) (100 + i), (byte) (100 + i));
    }

    public void toeter(int i) {
        //send((byte) (30 + i));
    }

    public void send(int i, int lSpeed, int rSpeed) {
        //send((byte) i, (byte) lSpeed, (byte) rSpeed);
    }

	@Override
	public void handlePacket(BTGWPacket packet) {
		// TODO Auto-generated method stub
		if(packet.getCommandCode() == BTGWPacket.CMD_PONG) {
			System.out.println("Received PONG");
		}
	}
}
