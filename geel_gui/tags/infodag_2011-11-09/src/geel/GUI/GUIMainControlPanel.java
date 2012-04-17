package geel.GUI;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
