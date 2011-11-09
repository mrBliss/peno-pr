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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
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

public class GUISteerPanel extends JPanel implements FocusListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 7164671065144321970L;
	private boolean inFocus = false;
	
	private int steerSpeed = 200;
	private int driveSpeed = 700;

	private boolean upActive = false;
	private boolean downActive = false;
	private boolean leftActive = false;
	private boolean rightActive = false;

	public boolean isUpActive() {
		return upActive;
	}

	public boolean isDownActive() {
		return downActive;
	}

	public boolean isLeftActive() {
		return leftActive;
	}

	public boolean isRightActive() {
		return rightActive;
	}
	
	public GUISteerPanel() {	 
		setFocusable(true);
		addKeyListener(this);
		addFocusListener(this);
		addMouseListener(this);
		
		Thread steerThread = new Thread() {
			public void run() {
				boolean lastUp = false;
				boolean lastDown = false;
				boolean lastLeft = false;
				boolean lastRight = false;
				
				boolean update = false;
				
				while(true) {
					update = false;
					
					boolean currUp = isUpActive();
					boolean currDown = isDownActive();
					boolean currLeft = isLeftActive();
					boolean currRight = isRightActive();
					
					if(lastUp != currUp) {
						lastUp = currUp;
						update = true;						
					}

					if(lastDown != currDown) {
						lastDown = currDown;
						update = true;
					}

					if(lastLeft != currLeft) {
						lastLeft = currLeft;
						update = true;
					}

					if(lastRight != currRight) {
						lastRight = currRight;
						update = true;
					}
					
					if(update) {

						int leftSpeed = 0;
						int rightSpeed = 0;
						
						if(lastUp) { leftSpeed += driveSpeed; rightSpeed += driveSpeed; }
						if(lastDown) { leftSpeed -= driveSpeed; rightSpeed -= driveSpeed; }
						if(lastLeft) { leftSpeed -= steerSpeed; rightSpeed += steerSpeed; }
						if(lastRight) { leftSpeed += steerSpeed; rightSpeed -= steerSpeed; }
						
						System.out.println("Setting new speeds to L="+leftSpeed + " R="+rightSpeed);
						
						if(BTGateway.getInstance() != null)
							BTGateway.getInstance().sendPacket(new BTGWPacketManualSteer(leftSpeed, rightSpeed));
					}
					
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		steerThread.start();
		
		setMinimumSize(new Dimension(150, 150));
		setPreferredSize(new Dimension(150, 150));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		
		if(inFocus) {
			g2.setColor(new Color(0.8f, 1.0f, 0.8f));
		} else {
			g2.setColor(new Color(1.0f, 0.8f, 0.8f));
		}
		
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		// up button
		if(upActive) g2.setColor(Color.BLUE); else g2.setColor(Color.GRAY); 
		g2.fillRect(60, 30, 30, 30);
		
		// down
		if(downActive) g2.setColor(Color.BLUE); else g2.setColor(Color.GRAY);
		g2.fillRect(60, 90, 30, 30);
		
		// left
		if(leftActive) g2.setColor(Color.BLUE); else g2.setColor(Color.GRAY);
		g2.fillRect(30, 60, 30, 30);
		
		//right
		if(rightActive) g2.setColor(Color.BLUE); else g2.setColor(Color.GRAY);
		g2.fillRect(90, 60, 30, 30);
		
	}
		
	public static void main(String[] args) {
		final GUISteerPanel p = new GUISteerPanel();
		new GUIStandAloneFrame(p, "Testing steeringpanel");
	}

	@Override
	public void keyPressed(KeyEvent e) {		
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT: leftActive = true; break;
			case KeyEvent.VK_RIGHT: rightActive = true; break;
			case KeyEvent.VK_UP: upActive = true; break;
			case KeyEvent.VK_DOWN: downActive = true; break;
		}
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT: leftActive = false; break;
			case KeyEvent.VK_RIGHT: rightActive = false; break;
			case KeyEvent.VK_UP: upActive = false; break;
			case KeyEvent.VK_DOWN: downActive = false; break;
		}
		
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		inFocus = true;
		System.out.println("Taking manual control");
		if(BTGateway.getInstance() != null) BTGateway.getInstance().sendPacket(new BTGWPacketManualOverride(true));
		repaint();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		inFocus = false;
		System.out.println("Release manual control");
		if(BTGateway.getInstance() != null) BTGateway.getInstance().sendPacket(new BTGWPacketManualOverride(false));
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(inFocus)
			transferFocus();
		else 
			requestFocus();
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e){}
}
