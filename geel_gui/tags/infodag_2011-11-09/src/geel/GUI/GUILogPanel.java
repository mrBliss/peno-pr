package geel.GUI;

import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketMessage;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class GUILogPanel extends JScrollPane implements IBTGWCommandListener {	
	private JTextArea output = new JTextArea(1,10);
	
	public GUILogPanel() {        
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setViewportView(output);
        output.setEditable(false);
        
        if(BTGateway.getInstance() != null)
        	BTGateway.getInstance().addListener(BTGWPacket.CMD_MESSAGE, this);
        
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
