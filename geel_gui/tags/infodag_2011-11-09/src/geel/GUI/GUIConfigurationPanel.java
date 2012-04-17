package geel.GUI;

import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketConfigBoolean;
import geel.BTGW.packets.BTGWPacketConfigFloat;
import geel.BTGW.packets.BTGWPacketConfigInteger;
import geel.BTGW.packets.BTGWPacketConfigRequest;
import geel.BTGW.packets.BTGWPacketLightOff;
import geel.BTGW.packets.BTGWPacketLightOn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 * The ConfigurationPanel will allow the user to see and alter configurable values on the robot
 *   - it has a fetch button to read the latest values
 *   - it has an update button to display the current values
 *   - it listens for configuration updates
 *   - it shows a list of all name/value pairs
 * 
 * 
 * @author deepstar
 *
 */

public class GUIConfigurationPanel extends JPanel implements IBTGWCommandListener {
	private HashMap<String, String> configIntegerValues = new HashMap<String, String>();
	private HashMap<String, String> configFloatValues = new HashMap<String, String>();
	private HashMap<String, String> configBooleanValues = new HashMap<String, String>();
	
	private DefaultTableModel myIntegerTableModel;
	private DefaultTableModel myFloatTableModel;
	private DefaultTableModel myBooleanTableModel;
	
	public GUIConfigurationPanel() {
	    if(BTGateway.getInstance() != null) {
	    	BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGBOOLEAN, this);
	    	BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGFLOAT, this);
	    	BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGINTEGER, this);
	    	BTGateway.getInstance().addListener(BTGWPacket.CMD_CONFIGREQUEST, this);
	    }
	    
	    myIntegerTableModel = new DefaultTableModel();
	    JTable myIntegerTable = new JTable(myIntegerTableModel);    
	    myIntegerTableModel.addColumn("Name");
	    myIntegerTableModel.addColumn("Integer Value");	    
	    //myIntegerTable.setBorder(BorderFactory.createTitledBorder("Integers"));	    

	    myFloatTableModel = new DefaultTableModel();
	    JTable myFloatTable = new JTable(myFloatTableModel);    
	    myFloatTableModel.addColumn("Name");
	    myFloatTableModel.addColumn("Float Value");
	    //myFloatTable.setBorder(BorderFactory.createTitledBorder("Floats"));

	    myBooleanTableModel = new DefaultTableModel();
	    JTable myBooleanTable = new JTable(myBooleanTableModel);    
	    myBooleanTableModel.addColumn("Name");
	    myBooleanTableModel.addColumn("Boolean Value");
	    //myBooleanTable.setBorder(BorderFactory.createTitledBorder("Booleans"));

	    
	    updateGUITable();
	    
	    add(myIntegerTable);
	    add(myFloatTable);
	    add(myBooleanTable);
	    
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    buttonPane.add(Box.createHorizontalGlue());
	    
	    final JButton fetchButton = new JButton("Fetch");
	    fetchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fetchButton.setLabel("Fetching...");
				submitConfigSubmissionRequest();
				fetchButton.setLabel("Fetch");
			}
		});
	    buttonPane.add(fetchButton);
	    
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    
	    final JButton uploadButton = new JButton("Upload");
	    uploadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				uploadButton.setLabel("Uploading...");
				submitConfiguration();
				uploadButton.setLabel("Upload");
			}
		});
	    buttonPane.add(uploadButton);	   

	    final JButton lightButton = new JButton("Light");
	    lightButton.addActionListener(new ActionListener() {
			private boolean lightOn = false;
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lightOn = !lightOn;
				activeLightSource(lightOn);
				lightButton.setLabel(lightOn ? "Light off" : "Light On");
			}
		});
	    //buttonPane.add(lightButton);
	    
	    add(buttonPane, BorderLayout.PAGE_END);
	    
	    setMinimumSize(new Dimension(200, 100));
	    setPreferredSize(new Dimension(200, 200));
	    
	}
	
	protected void activeLightSource(boolean lightOn) {
		if(BTGateway.getInstance() == null) {
			System.out.println("Can not use light source, not connected.");
			return;
		}
		
		if(lightOn) {
			BTGateway.getInstance().sendPacket(new BTGWPacketLightOn());
		} else {
			BTGateway.getInstance().sendPacket(new BTGWPacketLightOff());
		}
	}

	public synchronized void updateGUITable() {
		// clear the panel first
		myIntegerTableModel.setRowCount(0);
		myFloatTableModel.setRowCount(0);
		myBooleanTableModel.setRowCount(0);
		//for(int i = 0; i < myIntegerTableModel.getRowCount(); i++) myIntegerTableModel.removeRow(0);
		//for(int i = 0; i < myFloatTableModel.getRowCount(); i++) myFloatTableModel.removeRow(0);
		//for(int i = 0; i < myBooleanTableModel.getRowCount(); i++) myBooleanTableModel.removeRow(0);
		
		// for all integer values, add their fields
		Iterator it = configIntegerValues.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Entry<String, String>) it.next();	        
	        myIntegerTableModel.addRow(new String[] {pairs.getKey(), pairs.getValue()});
	    }
	    
		it = configFloatValues.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Entry<String, String>) it.next();	        
	        myFloatTableModel.addRow(new String[] {pairs.getKey(), pairs.getValue()});
	    }
	    
		it = configBooleanValues.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Entry<String, String>) it.next();	        
	        myBooleanTableModel.addRow(new String[] {pairs.getKey(), pairs.getValue()});
	    }
	}
	
	public synchronized void printFields() {
		for(int i = 0; i < myIntegerTableModel.getRowCount(); i++) {
			String key = (String)myIntegerTableModel.getValueAt(i, 0);
			Integer value = Integer.parseInt((String)myIntegerTableModel.getValueAt(i, 1));
			
			System.out.println("Integer " + key + " = "+ value);			
		}
		
		for(int i = 0; i < myFloatTableModel.getRowCount(); i++) {
			String key = (String)myFloatTableModel.getValueAt(i, 0);
			Float value = Float.parseFloat((String)myFloatTableModel.getValueAt(i, 1));
			
			System.out.println("Float " + key + " = "+ value);
		}

		for(int i = 0; i < myBooleanTableModel.getRowCount(); i++) {
			String key = (String)myBooleanTableModel.getValueAt(i, 0);
			Boolean value = Boolean.parseBoolean((String)myBooleanTableModel.getValueAt(i, 1));
			
			System.out.println("Boolean " + key + " = "+ value);
		}

	}
	
	public synchronized void submitConfigSubmissionRequest() {
		if(BTGateway.getInstance() == null) {
			System.out.println("Not connected yet, not sending configuration submission request.");
			return;
		}

		System.out.println("Sending config submission request to robot");
		// clear current values
		configBooleanValues.clear();
		configFloatValues.clear();
		configIntegerValues.clear();
		updateGUITable();
		// submit request		
		BTGateway.getInstance().sendPacket(new BTGWPacketConfigRequest());
		
		System.out.println("Request sent.");
	}
	
	public synchronized void submitConfiguration() {		
		if(BTGateway.getInstance() == null) {
			System.out.println("Not connected yet, not sending configuration.");
			return;
		}
		
		System.out.println("Sending new config to robot");

		// for each table and each value, submit a config SET request
		for(int i = 0; i < myIntegerTableModel.getRowCount(); i++) {
			String key = (String)myIntegerTableModel.getValueAt(i, 0);
			Integer value = Integer.parseInt((String)myIntegerTableModel.getValueAt(i, 1));
			
			
			System.out.println("Sending Integer " + key + " = "+ value);
			BTGateway.getInstance().sendPacket(new BTGWPacketConfigInteger(key, value.intValue()));
		}
		
		for(int i = 0; i < myFloatTableModel.getRowCount(); i++) {
			String key = (String)myFloatTableModel.getValueAt(i, 0);
			Float value = Float.parseFloat((String)myFloatTableModel.getValueAt(i, 1));
			
			System.out.println("Sending Float " + key + " = "+ value);
			BTGateway.getInstance().sendPacket(new BTGWPacketConfigFloat(key, value.floatValue()));
		}

		for(int i = 0; i < myBooleanTableModel.getRowCount(); i++) {
			String key = (String)myBooleanTableModel.getValueAt(i, 0);
			Boolean value = Boolean.parseBoolean((String)myBooleanTableModel.getValueAt(i, 1));
			
			System.out.println("Sending Boolean " + key + " = "+ value);
			BTGateway.getInstance().sendPacket(new BTGWPacketConfigBoolean(key, value.booleanValue()));
		}
		
		System.out.println("Config sent.");
	}

	@Override
	public synchronized void handlePacket(BTGWPacket packet) {
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGBOOLEAN) {
			BTGWPacketConfigBoolean p = (BTGWPacketConfigBoolean) packet;
			configBooleanValues.put(p.getKey(), Boolean.toString(p.getValue()));			
			System.out.println("Received boolean config "+p.getKey()+ " = " +p.getValue());
			
			updateGUITable();
			return;
		}
		
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGFLOAT) {
			BTGWPacketConfigFloat p = (BTGWPacketConfigFloat) packet;
			configFloatValues.put(p.getKey(), Float.toString(p.getValue()));
			System.out.println("Received float config "+p.getKey()+ " = " +p.getValue());
			
			updateGUITable();
			return;
		}
		
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGINTEGER) {
			BTGWPacketConfigInteger p = (BTGWPacketConfigInteger) packet;
			configIntegerValues.put(p.getKey(), Integer.toString(p.getValue()));
			System.out.println("Received integer config "+p.getKey()+ " = " +p.getValue());
			
			updateGUITable();
			return;
		}

		//not sure if a robot will ever do it, but meh :)
		if(packet.getCommandCode() == BTGWPacket.CMD_CONFIGREQUEST) {
			submitConfiguration();
			return;
		}
	}

	@Override
	public synchronized void repaint() {
		super.repaint();
	}
	
	
}
