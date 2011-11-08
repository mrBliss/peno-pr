package pcpanic.NXT;

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
public class Pilot {

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
    	NXTComm open = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
    	open.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "WallE", "00:16:53:02:F7:9D"));
        outputStream = open.getOutputStream();
        inputStream = open.getInputStream();
    }

    public void close() {
        try {
            open.close();
        } catch (IOException ex) {
        }
    }

    public void send(byte n) {
        System.out.println("e: " + n);
        try {
            outputStream.write(n);
            outputStream.flush();
        } catch (IOException ex) {
        }
    }

    public void send(byte i, byte j, byte k) {
        System.out.println("m: " + i + " " + j + " " + k);
        
        try {
            outputStream.write(i);
            outputStream.write(j);
            outputStream.write(k);
            outputStream.flush();
        } catch (IOException ex) {
        }
        
    }

    public void startRem() {
        send((byte) 10);
    }

    public void stopRem() {
        send((byte) 11);
    }

    public void globalSpeed(byte i) {
        send((byte) 20, (byte) (100 + i), (byte) (100 + i));
    }

    public void toeter(int i) {
        send((byte) (30 + i));
    }

    public void send(int i, int lSpeed, int rSpeed) {
        send((byte) i, (byte) lSpeed, (byte) rSpeed);
    }
}
