package geel.GUI;

import geel.BTGW.infrastructure.*;
import geel.BTGW.packets.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;
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
	private ArrayList<Integer> lightSensorValues = new ArrayList<Integer>();
	private ArrayList<Integer> sonarSensorValues = new ArrayList<Integer>();
	private boolean touchSensorPressed = false;
	
	// panel stuff
	private Color bgColor = new Color(0, 50, 0);
	
	// lightsensor widget parameters
	private Color lightBGColor = bgColor;
	private int lightTopMargin = 20;
	private int lightSensorWidth = 40;
	private int MAXLIGHTVALUE = 255;
	
	// sonar widget parameters	
	private Color sonarBGColor = bgColor;
	private Color sonarLineColor = Color.GREEN;
	private int sonarTopMargin = 20;
	private int MAXSONARVALUE = 255;
	private int sonarCurrentValueIndicatorDiameter = 5;
	
	// sonar animation stuff
	private int sonarTick = 0;
	private int sonarSpeed = 2;
	private Image sonarSweepImage;
	private BufferedImage sonarSweepImageBuffer;
	
	// touch sensor widget
	private Image touchSensorPressedImage;
	private Image touchSensorNotPressedImage;
	
	
	public GUISensorPanel() {
	    sonarSweepImage = Toolkit.getDefaultToolkit().getImage("images/swipe.png");
	    touchSensorPressedImage = Toolkit.getDefaultToolkit().getImage("images/collision.png");
	    touchSensorNotPressedImage = Toolkit.getDefaultToolkit().getImage("images/nocollision.png");
	        
		Thread t = new Thread() {
			public void run() {
				while(true) {
					sonarTick+=sonarSpeed;
					
					double someval = Math.abs(Math.sin(sonarTick / 100.0) * 100.0);
					addSensorData((int)someval, (int)someval, (sonarTick % 100 > 50));
					
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
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(bgColor);
		g2.fillRect(0, 0, 600, 600);
		
		int collW = touchSensorPressedImage.getWidth(null);
		int collH = touchSensorPressedImage.getHeight(null);
		
		drawCollisionIndicator(g2, 300 - collW/2, 0);
		drawSonarWave(g2, 0,collH);
		drawLightSensorValues(g2, 300 - lightSensorWidth / 2, collH);		
		
	}
	
	private void drawLightSensorValues(Graphics2D g2, int tlx, int tly) {
		int areaW = lightSensorWidth;
		int areaH = MAX_HISTORY;
		// paint the background
		g2.setColor(lightBGColor);
		g2.fillRect(tlx, tly, areaW, areaH);
		
		// the data
		for(int i = 0; i < lightSensorValues.size(); i++) {
			int y = tly + i + lightTopMargin;
			float scaled = (float)lightSensorValues.get(i).intValue() / (float)MAXLIGHTVALUE; 
			g2.setColor(new Color(scaled,scaled,scaled));
			g2.drawLine(tlx, y, tlx+areaW, y);
		}
	}
	
	private void drawSonarWave(Graphics2D g2, int tlx, int tly) {
		int areaW = MAXSONARVALUE;
		int areaH = MAX_HISTORY;
		int swipeWidth = sonarSweepImage.getWidth(this);
		
		if(swipeWidth < 0)
			return;
		
		if(sonarSweepImageBuffer == null) {
			sonarSweepImageBuffer = new BufferedImage(swipeWidth, areaH ,BufferedImage.TYPE_INT_ARGB);
			Graphics newg = sonarSweepImageBuffer.getGraphics();
			newg.drawImage(sonarSweepImage, 0, 0, swipeWidth, areaH, this);
		}
		
		// paint the background
		g2.setColor(sonarBGColor);
		g2.fillRect(tlx, tly, areaW, areaH);
		
		// draw the data
		g2.setColor(sonarLineColor);
		GeneralPath p = new GeneralPath();

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
		
		Stroke currentStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		g2.draw(p);
		g2.setStroke(currentStroke);

		
//		// draw the animated sweep
//		int swipeX = tlx + areaW - (sonarTick % areaW);
//		int swipeY = tly;
//		int swipeW = Math.min(sonarTick % areaW, swipeWidth); 
//		int swipeH = areaH;
//				
//		if(swipeW > 0) {
//			BufferedImage piece = sonarSweepImageBuffer.getSubimage(0, 0, swipeW, swipeH);
//			g2.drawImage(piece, swipeX,swipeY, this);
//		}
	}
	
	private void drawCollisionIndicator(Graphics2D g2, int topleftX, int topleftY) {
//		int a = 20;
//		int b = 100;
//		
//		if(touchSensorPressed) {
//			g2.setColor(Color.RED);
//		} else {
//			g2.setColor(Color.BLUE);
//		}
//		
//		GeneralPath p = new GeneralPath();
//		p.moveTo(topleftX, topleftY + a);
//		p.lineTo(topleftX + a, topleftY);
//		p.lineTo(topleftX + a + b, topleftY);
//		p.lineTo(topleftX + a + b + a, topleftY + a);
//		p.lineTo(topleftX + a + b, topleftY + a + a);
//		p.lineTo(topleftX + a + b - a, topleftY + a);
//		p.lineTo(topleftX + a + a, topleftY + a);
//		p.lineTo(topleftX + a, topleftY + a + a);
//		p.closePath();
//		g2.fill(p);
		
		if(touchSensorPressed) {
			g2.drawImage(touchSensorPressedImage, topleftX, topleftY, this);
		} else {
			g2.drawImage(touchSensorNotPressedImage, topleftX, topleftY, this);
		}
	}

	@Override
	public void handlePacket(BTGWPacket packet) {
	}
	
	private void addSensorData(int _lightSensorValue, int _sonarSensorValue, boolean _touchSensorPressed) {		
		// register values
		touchSensorPressed = _touchSensorPressed;
		lightSensorValues.add(0, new Integer(_lightSensorValue));
		sonarSensorValues.add(0, new Integer(_sonarSensorValue));
		
		// cap at max length
		for(int i = lightSensorValues.size() - 1; i >= MAX_HISTORY; i--) lightSensorValues.remove(i);
		for(int i = sonarSensorValues.size() - 1; i >= MAX_HISTORY; i--) sonarSensorValues.remove(i);
		
		repaint();
	}
}
