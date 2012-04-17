package geel.GUI;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class GUIMainConfigPanel extends JPanel {
	private static final long serialVersionUID = 963121970814789181L;

	public GUIMainConfigPanel() {
        super(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(splitPane, BorderLayout.CENTER);
        splitPane.setDividerLocation(0.1);

        splitPane.add(new GUIConfigurationPanel());
        splitPane.add(new GUIColorConfigurationPanel());
	}

	public static void main(String[] args) {
		new GUIStandAloneFrame(new GUIMainConfigPanel(), "test");
	}	
}
