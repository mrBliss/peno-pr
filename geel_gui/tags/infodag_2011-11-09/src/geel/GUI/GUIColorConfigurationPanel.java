package geel.GUI;

import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketConfigInteger;
import geel.BTGW.packets.BTGWPacketConfigRequest;
import geel.BTGW.packets.BTGWPacketStatusUpdate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JPanel;
/**
 * The ConfigurationPanel will allow the user to calibrate the lightsensor on the go
 *   - it reads the latest values automatically
 *   - it uploads the new values automatically
 *   - it listens for configuration updates
 *   - it listens for sensor values
 *   - it draws the sensor values while they come in
 *   - the user can drag the threshold indicators around
 * 
 * @author deepstar
 *
 */

public class GUIColorConfigurationPanel extends JPanel implements IBTGWCommandListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 869134796279706363L;
	
	private ArrayList<Integer> lightValues = new ArrayList<Integer>();
	private int MAX_HISTORY = 1000;
	
	private int tresholdWhiteBrown = 666;
	private String tresholdWhiteBrownToken = "tresholdWhiteBrown";
	private int tresholdBrownBlack = 333;
	private String tresholdBrownBlackToken = "tresholdBrownBlack";
	
	private int maxValue = 1023;
	
	private boolean whiteBrownSelected = false;
	
	public GUIColorConfigurationPanel() {
		
		addMouseListener(this);
		addMouseMotionListener(this);
	    
	    if(BTGateway.getInstance() != null) {
	    	BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGINTEGER, this);
	    	BTGateway.getInstance().addListener(BTGWPacket.CMD_STATUSUPDATE, this);
	    } else {
	    	return;
	    }	  
	    
	    System.out.println("I don't know what my tresholds are ... Requesting information");
	    BTGateway.getInstance().sendPacket(new BTGWPacketConfigRequest());
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		Color white = Color.white;
		Color black = Color.black;
		Color brown = new Color(225, 169, 95);
		
		int currHeight = getHeight();
		int currWidth = getWidth();
	
		int t1 = convertYToScreen(tresholdBrownBlack);
		int t2 = convertYToScreen(tresholdWhiteBrown);
		
		// first, color the areas
		g2.setColor(white);
		g2.fillRect(0, 0, currWidth, t2);
		g2.setColor(brown);
		g2.fillRect(0, t2, currWidth, t1);
		g2.setColor(black);
		g2.fillRect(0, t1, currWidth, currHeight);
		

		int size = 8;
		int counter = 0;
		g2.setColor(Color.RED);
		
		GeneralPath p = new GeneralPath();
		
		for(Integer v: lightValues) {
			int y = convertYToScreen(v.intValue());
			
			if(counter == 0) {
				g2.fillOval(20 + counter - size, y - size, 2 * size, 2 * size);
				p.moveTo(20 + counter, y);
			} else {
				p.lineTo(20 + counter, y);
			}
		
			counter++;
		}
		
		Stroke currentStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		g2.draw(p);
		g2.setStroke(currentStroke);
		
		
	}

	private int convertXFromScreen(int val) {
		int currWidth = getWidth();

		return maxValue - (val * maxValue / currWidth);
	}
	
	private int convertXToScreen(int val) {
		int currWidth = getWidth();
		
		return (maxValue - val) * currWidth / maxValue;
	}

	
	private int convertYFromScreen(int val) {
		int currHeight = getHeight();

		return maxValue - (val * maxValue / currHeight);
	}
	
	private int convertYToScreen(int val) {
		int currHeight = getHeight();
		
		return (maxValue - val) * currHeight / maxValue;
	}


	@Override
	public void handlePacket(BTGWPacket packet) {
		if(packet.getCommandCode() == BTGWPacket.CMD_STATUSUPDATE) {
			BTGWPacketStatusUpdate p = (BTGWPacketStatusUpdate) packet;
			addLightValue(p.getLightSensorValue());
		}
		
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGINTEGER) {
			BTGWPacketConfigInteger p = (BTGWPacketConfigInteger) packet;
			
			if(p.getKey().equals(tresholdBrownBlackToken)) {
				tresholdBrownBlack = p.getValue();
			}
			
			if(p.getKey().equals(tresholdWhiteBrownToken)) {
				tresholdWhiteBrown = p.getValue();
			}			
		}
	}

	private void addLightValue(int _lightSensorValue) {		
		// register values
		lightValues.add(0, new Integer(_lightSensorValue));
		
		// cap at max length
		for(int i = lightValues.size() - 1; i >= MAX_HISTORY; i--) lightValues.remove(i);
		
		repaint();
	}


	public void mousePressed(MouseEvent e) {
		// select field
		if(e.getY() < (convertYToScreen(tresholdWhiteBrown) + convertYToScreen(tresholdBrownBlack)) / 2)
			whiteBrownSelected = true;
		else 
			whiteBrownSelected = false;
	}
	public void mouseReleased(MouseEvent e) {
		// update value in robot
		System.out.println("Sending color calibration");
		
		BTGateway.getInstance().sendPacket(new BTGWPacketConfigInteger(tresholdBrownBlackToken, tresholdBrownBlack));
		BTGateway.getInstance().sendPacket(new BTGWPacketConfigInteger(tresholdWhiteBrownToken, tresholdWhiteBrown));
		
		//let the robot send his new config to all listeners
		BTGateway.getInstance().sendPacket(new BTGWPacketConfigRequest());
	}
	public void mouseDragged(MouseEvent e) {
		int v = convertYFromScreen(e.getY());
		if(whiteBrownSelected) {
			tresholdWhiteBrown = v;
			if(tresholdBrownBlack > tresholdWhiteBrown) tresholdBrownBlack = tresholdWhiteBrown;
		} else {
			tresholdBrownBlack = v;
			if(tresholdBrownBlack > tresholdWhiteBrown) tresholdWhiteBrown = tresholdBrownBlack;
		}
		
		if(tresholdBrownBlack < 0) tresholdBrownBlack = 0;
		if(tresholdBrownBlack > maxValue) tresholdBrownBlack = maxValue;
		
		if(tresholdWhiteBrown < 0) tresholdWhiteBrown = 0;
		if(tresholdWhiteBrown > maxValue) tresholdWhiteBrown = maxValue;

		repaint();
	}
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	
	public static void main(String[] args) {
		final GUIColorConfigurationPanel p = new GUIColorConfigurationPanel();
		
		Thread t = new Thread() {
			public void run() {
				int counter = 0;
				while(true) {				
					int someval = (int)Math.abs(Math.sin(counter / 100.0) * 1023.0);
					BTGWPacketStatusUpdate pack = new BTGWPacketStatusUpdate(0,someval,0,0,false);
					p.handlePacket(pack);
					counter++;
					
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		
		new GUIStandAloneFrame(p, "Testing colorconfigpanel");
	}
	
}
