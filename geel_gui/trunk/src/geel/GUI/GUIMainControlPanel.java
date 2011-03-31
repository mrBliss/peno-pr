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
import geel.BTGW.packets.BTGWPacketDie;
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

public class GUIMainControlPanel extends JPanel {
	private static final long serialVersionUID = 963121970814789181L;

	public GUIMainControlPanel() {
        super(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        rightSplitPane.add(new GUISteerPanel(), JSplitPane.TOP); // should be behaviour panel FIXME
        rightSplitPane.add(new GUILogPanel(), JSplitPane.BOTTOM);
        //rightSplitPane.add(new GUISteerPanel(), BorderLayout.SOUTH); // should be log panel FIXME
        rightSplitPane.setDividerLocation(0.1);
                
        
        splitPane.add(new GUISensorPanel(), JSplitPane.LEFT);
        splitPane.add(rightSplitPane, JSplitPane.RIGHT);
        splitPane.setDividerLocation(0.1);
        
        add(splitPane, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		new GUIStandAloneFrame(new GUIMainControlPanel(), "test");
	}	
}
