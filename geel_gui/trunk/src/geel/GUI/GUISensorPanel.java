package geel.GUI;

import geel.BTGW.infrastructure.*;
import geel.BTGW.packets.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
/**
 * The GUISensorPanel will draw a visual representation of all the sensors
 *   - it will show if the touch sensor is pressed
 *   - it will show the recent history of lightsensor values
 *   - it will show the recent history of ultrasonic sensor values
 * 
 * This panel receives its information over the network by listening to
 * specific events from the robot. Everything else is automagic.
 * 
 * @author deepstar
 *
 */

public class GUISensorPanel extends JPanel implements IBTGWCommandListener {
	private static final long serialVersionUID = -2655975772061336677L;
	
	// sensor data
	private final int MAX_HISTORY = 400;
	private ArrayList<Integer> lightSensorRealValues = new ArrayList<Integer>();
	private ArrayList<Integer> sonarSensorRealValues = new ArrayList<Integer>();
	
	private ArrayList<Integer> lightSensorValues = new ArrayList<Integer>();
	private ArrayList<Integer> sonarSensorValues = new ArrayList<Integer>();
	
	private boolean touchSensorPressed = false;
	
	// panel stuff
	private Color bgColor = new Color(0, 50, 0);
	
	// lightsensor widget parameters
	private Color lightBGColor = bgColor;
	private int lightTopMargin = 20;
	private int lightSensorWidth = 40;
	private int MAXLIGHTVALUE = 1023;
	private Color brown = new Color(225, 169, 95);
	
	// sonar widget parameters	
	private Color sonarBGColor = bgColor;
	private Color sonarLineColor = Color.GREEN;
	private Color sonarRealLineColor = Color.RED;
	private int sonarTopMargin = 20;
	private int MAXSONARVALUE = 255;
	private int sonarCurrentValueIndicatorDiameter = 5;
	
	// touch sensor widget
	private Image touchSensorPressedImage;
	private Image touchSensorNotPressedImage;
	
	
	public GUISensorPanel() {
		touchSensorPressedImage = Toolkit.getDefaultToolkit().getImage("images/collision.png");
	    touchSensorNotPressedImage = Toolkit.getDefaultToolkit().getImage("images/nocollision.png");

		int collW = touchSensorPressedImage.getWidth(null);
		int collH = touchSensorPressedImage.getHeight(null);

		setMinimumSize(new Dimension(450, 600));
		setPreferredSize(new Dimension(450, 600));		
	        	    
	    if(BTGateway.getInstance() != null)
	    	BTGateway.getInstance().addListener(BTGWPacket.CMD_STATUSUPDATE, this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		int collW = touchSensorPressedImage.getWidth(null);
		int collH = touchSensorPressedImage.getHeight(null);
		
		drawCollisionIndicator(g2, 300 - collW/2, 0);
		drawSonarWave(g2, 0,collH);
		drawLightSensorValues(g2, 300 - lightSensorWidth, collH);	
	}
	
	private void drawLightSensorValues(Graphics2D g2, int tlx, int tly) {
		int areaW = lightSensorWidth;
		int areaH = MAX_HISTORY;
		// paint the background
		g2.setColor(lightBGColor);
		g2.fillRect(tlx, tly, areaW, areaH);
		
		// the data
		for(int i = 0; i < lightSensorRealValues.size(); i++) {
			int y = tly + i + lightTopMargin;
			float scaled = (float)lightSensorRealValues.get(i).intValue() / (float)MAXLIGHTVALUE; 
			g2.setColor(new Color(scaled,scaled,scaled));
			g2.drawLine(tlx, y, tlx+areaW, y);
		}
		
		for(int i = 0; i < lightSensorValues.size(); i++) {
			int y = tly + i + lightTopMargin;
			
			switch(lightSensorValues.get(i).intValue()) {
				case 0:
					g2.setColor(Color.BLACK);
					break;
				case 1:
					g2.setColor(brown);
					break;
				case 2:
					g2.setColor(Color.WHITE);
					break;
				default:
					g2.setColor(Color.RED);
					break;
			}
			g2.drawLine(tlx+areaW, y, tlx+(2*areaW), y);
		}
		
	}
	
	private void drawSonarWave(Graphics2D g2, int tlx, int tly) {
		int areaW = MAXSONARVALUE;
		int areaH = MAX_HISTORY;
		
		// paint the background
		g2.setColor(sonarBGColor);
		g2.fillRect(tlx, tly, areaW, areaH);
		
		// draw the real data
		g2.setColor(sonarRealLineColor);
		GeneralPath p = new GeneralPath();

		for(int i = 0; i < sonarSensorRealValues.size(); i++) {
			int x = areaW - sonarSensorRealValues.get(i).intValue();
			int y = tly + i + sonarTopMargin;
			
			if(i/4 < sonarCurrentValueIndicatorDiameter) {
				int diam = sonarCurrentValueIndicatorDiameter - i/4;
			g2.fillOval(x - diam, y - diam, 2 * diam, 2 * diam);
			}
			
			if(i == 0) {
				p.moveTo(x, y);
			} else p.lineTo(x, y);
		}
		
		Stroke currentStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		g2.draw(p);
		g2.setStroke(currentStroke);

		// draw the data
		g2.setColor(sonarLineColor);
		p = new GeneralPath();

		for(int i = 0; i < sonarSensorValues.size(); i++) {
			int x = areaW - sonarSensorValues.get(i).intValue();
			int y = tly + i + sonarTopMargin;
			
			if(i/4 < sonarCurrentValueIndicatorDiameter) {
				int diam = sonarCurrentValueIndicatorDiameter - i/4;
			g2.fillOval(x - diam, y - diam, 2 * diam, 2 * diam);
			}
			
			if(i == 0) {
				p.moveTo(x, y);
			} else p.lineTo(x, y);
		}
		
		currentStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		g2.draw(p);
		g2.setStroke(currentStroke);
	}
	
	private void drawCollisionIndicator(Graphics2D g2, int topleftX, int topleftY) {
		if(touchSensorPressed) {
			g2.drawImage(touchSensorPressedImage, topleftX, topleftY, this);
		} else {
			g2.drawImage(touchSensorNotPressedImage, topleftX, topleftY, this);
		}
	}

	@Override
	public void handlePacket(BTGWPacket packet) {
		if(packet.getCommandCode() == BTGWPacket.CMD_STATUSUPDATE) {
			BTGWPacketStatusUpdate p = (BTGWPacketStatusUpdate) packet;
			addSensorData(p.getGroundColor(), p.getLightSensorValue(), p.getSonarSensorValue(), p.getSonarSensorRawValue(), p.getTouchSensorValue());
		}
	}
	
	private void addSensorData(int _lightSensorValue, int _lightSensorRealValue, int _sonarSensorValue, int _sonarSensorRealValue, boolean _touchSensorPressed) {		
		// register values
		touchSensorPressed = _touchSensorPressed;
		lightSensorValues.add(0, new Integer(_lightSensorValue));
		sonarSensorValues.add(0, new Integer(_sonarSensorValue));
		lightSensorRealValues.add(0, new Integer(_lightSensorRealValue));
		sonarSensorRealValues.add(0, new Integer(_sonarSensorRealValue));
		
		//System.out.println("Added LS="+_lightSensorValue+" SS="+_sonarSensorValue+" TS="+_touchSensorPressed);
		
		// cap at max length
		for(int i = lightSensorValues.size() - 1; i >= MAX_HISTORY; i--) lightSensorValues.remove(i);
		for(int i = sonarSensorValues.size() - 1; i >= MAX_HISTORY; i--) sonarSensorValues.remove(i);
		for(int i = lightSensorRealValues.size() - 1; i >= MAX_HISTORY; i--) lightSensorRealValues.remove(i);
		for(int i = sonarSensorRealValues.size() - 1; i >= MAX_HISTORY; i--) sonarSensorRealValues.remove(i);
		
		
		repaint();
	}
	
	public static void main(String[] args) {
		final GUISensorPanel p = new GUISensorPanel();
		
		Thread t = new Thread() {
			public void run() {
				int counter = 0;
				while(true) {				
					int someval = (int)Math.abs(Math.sin(counter / 100.0) * 100.0);
					int someval2 = 0;
					if(someval > 33) someval2 = 1;
					if(someval > 66) someval2 = 2;
					
					BTGWPacketStatusUpdate pack = new BTGWPacketStatusUpdate(someval2, someval * 10, someval, someval + 10, (counter % 100 > 50));
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
		
		new GUIStandAloneFrame(p, "Testing sensorpanel");
	}
}
