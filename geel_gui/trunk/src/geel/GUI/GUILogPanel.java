package geel.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketDie;
import geel.BTGW.packets.BTGWPacketMessage;
import geel.BTGW.pc.BTGWRealConnectionMaker;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class GUILogPanel extends JScrollPane implements IBTGWCommandListener {	
	private JTextArea output = new JTextArea(1,10);
	
	public GUILogPanel() {        
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setViewportView(output);
        output.setEditable(false);
        
        //for(int i = 0; i < 100; i++)
        	//output.append("["+i+"] Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world\n");
	}


	
	public void addText(String msg) {
		output.append(msg+"\n");
		output.setCaretPosition(output.getDocument().getLength());
	}

	@Override
	public void handlePacket(BTGWPacket packet) {
		if(packet.getCommandCode() == BTGWPacket.CMD_MESSAGE) {
			BTGWPacketMessage p = (BTGWPacketMessage) packet;			
			addText("[ROBOT] "+ p.getMessage());
		}
		
	}
	
	public static void main(String[] args) {
		final GUILogPanel p = new GUILogPanel();
		
		Thread t = new Thread() {
			public void run() {
				int counter = 0;
				while(true) {
					p.addText("["+counter+++"] Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world");
					try {
						sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		
		new GUIStandAloneFrame(p, "P&O Demo Log Panel");
	}
}
