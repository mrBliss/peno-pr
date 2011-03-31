package geel.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
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

public class GUIConnectPanel extends JPanel {
	private static final long serialVersionUID = -2601617076907696631L;
	
	private NXTInfo[] robotInfoList;
	
	private JTextArea output;
	private JButton scanButton;
	private JButton connectButton;
	private JList listPanel; 
	private DefaultListModel listModel = new DefaultListModel();

	public int selectedRobotIndex;

	protected boolean connected = false;
	
	public GUIConnectPanel() {
		
        super(new BorderLayout());

        listPanel = new JList(listModel);
        
        final ListSelectionModel listSelectionModel = listPanel.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        JScrollPane listPane = new JScrollPane(listPanel);
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel controlPane = new JPanel();

        //Build output area.
        output = new JTextArea(1, 10);
        output.setEditable(false);
        JScrollPane outputPane = new JScrollPane(output,
                         ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                         ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //Do the layout.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(splitPane, BorderLayout.CENTER);

        JPanel topHalf = new JPanel();
        topHalf.setLayout(new BoxLayout(topHalf, BoxLayout.LINE_AXIS));
        JPanel listContainer = new JPanel(new GridLayout(1,1));
        listContainer.setBorder(BorderFactory.createTitledBorder("Robots"));
        listContainer.add(listPane);
        
        
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    buttonPane.add(Box.createHorizontalGlue());
	    
	    scanButton = new JButton("Scan");
	    scanButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				scanButton.setLabel("Scanning...");
				output.append("Scanning...\n");
				repaint();
				updateRobotList();
				output.append("Done scanning.\n");
				scanButton.setLabel("Scan");
			}
		});
	    buttonPane.add(scanButton);
	    
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    
	    connectButton = new JButton("Connect");
	    connectButton.setEnabled(false);
	    connectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!connected) {
					connectButton.setLabel("Connecting...");
					output.append("Connecting...\n");
					repaint();
					connected = connectToRobot();
					if(connected) {
						connectButton.setLabel("Disconnect");
					} else {
						connectButton.setLabel("Connect");
					}
				} else {
					// break down connection gently
					try {
						BTGateway.getInstance().close();
						connected = false;
						connectButton.setLabel("Connect");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	    buttonPane.add(connectButton);	   
	    
	    
        
        
        topHalf.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
        
        listContainer.setMinimumSize(new Dimension(200, 200));
        listContainer.setPreferredSize(new Dimension(200, 200));
        
        topHalf.add(listContainer);
        topHalf.add(buttonPane);
        //topHalf.add(tableContainer);

        topHalf.setMinimumSize(new Dimension(600, 400));
        topHalf.setPreferredSize(new Dimension(600, 400));
        splitPane.add(topHalf);

        JPanel bottomHalf = new JPanel(new BorderLayout());
        bottomHalf.add(controlPane, BorderLayout.PAGE_START);
        bottomHalf.add(outputPane, BorderLayout.CENTER);
        //XXX: next line needed if bottomHalf is a scroll pane:
        //bottomHalf.setMinimumSize(new Dimension(400, 50));
        bottomHalf.setPreferredSize(new Dimension(600, 135));
        splitPane.add(bottomHalf);

        
        
        
		
		//updateRobotList();
		
//		System.exit(0);
//		
//    	BTGWConnection btgwconn = new BTGWRealConnectionMaker("WallE", "00:16:53:02:F7:9D");
//
//        /* connect to it */
//        btgwconn.connect();
//        /* wait untill we are connected */
//        while(btgwconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
//                //System.out.println("Not connected. Sleeping...");
//                try {
//                        Thread.sleep(200);
//                } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                }
//
//        }
//
//        
//        
//        System.out.println("connected");
//        /* create a gateway */
//        BTGateway btgw = new BTGateway(btgwconn);
//        BTGateway.addInstance(btgw);
//        
//        new GUIStandAloneFrame(new GUISensorPanel(), "Robot sensor data");
//        new GUIStandAloneFrame(new GUIConfigurationPanel(), "Configuration values");
//        new GUIStandAloneFrame(new GUIColorConfigurationPanel(), "Lightsensor calibrator");
	}
	
	protected boolean connectToRobot() {
		String name = robotInfoList[selectedRobotIndex].name;
		String address = robotInfoList[selectedRobotIndex].deviceAddress;
		
		output.append("Connecting to robot "+name +" @ "+address+"\n");
		
    	BTGWConnection btgwconn = new BTGWRealConnectionMaker(name, address);
		
        /* connect to it */
        btgwconn.connect();
        long timeout = 10; // seconds
        long timeNow = System.currentTimeMillis();
        
        /* wait untill we are connected */
        while(System.currentTimeMillis() - timeNow < timeout * 1000 && btgwconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
        		output.append("Not yet connected to robot "+name+", waiting...\n");
                //System.out.println("Not connected. Sleeping...");
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
        
        if(btgwconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
        	output.append("Connection did not complete within "+timeout+" seconds, giving up :(\n");
        	return false;
        }
        
        output.append("Connected!\n");
        /* create a gateway */
        BTGateway btgw = new BTGateway(btgwconn);
        BTGateway.addInstance(btgw);
        
        new GUIStandAloneFrame(new GUISensorPanel(), "Robot sensor data");
        new GUIStandAloneFrame(new GUIConfigurationPanel(), "Configuration values");
        new GUIStandAloneFrame(new GUIColorConfigurationPanel(), "Lightsensor calibrator");

        return true;
	}

	public void updateRobotList() {
		try {
			NXTComm BTComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			robotInfoList = BTComm.search(null, NXTCommFactory.BLUETOOTH);
			
			listModel.clear();
			selectedRobotIndex = -1;
			connectButton.setEnabled(false);
			
			for(NXTInfo robot: robotInfoList) {
				System.out.println("Robot "+robot.name + " at "+ robot.deviceAddress);
				listModel.addElement(robot.deviceAddress+" ("+robot.name+")");
			}
			
			repaint();
		
		} catch (NXTCommException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GUIStandAloneFrame(new GUIConnectPanel(), "P&O Demo Connect Panel");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) { 
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            boolean isAdjusting = e.getValueIsAdjusting(); 

            if(lsm.isSelectionEmpty()) return; // FIXME
            int index = lsm.getMinSelectionIndex();

            if(!isAdjusting) {
            	output.append("Selected robot "+robotInfoList[index].name + " @ "+ robotInfoList[index].deviceAddress + "\n");
            	selectedRobotIndex = index;
            	connectButton.setEnabled(true);
            }
        }
    }

	
	
	
}
