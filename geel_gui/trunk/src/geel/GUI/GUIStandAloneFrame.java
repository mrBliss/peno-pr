package geel.GUI;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GUIStandAloneFrame extends JFrame {
	public GUIStandAloneFrame(JComponent child, String title) {
		setTitle(title);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        add(child);
        setPreferredSize(new Dimension(300, 300));
        
        pack();
		setVisible(true);
	}
}
