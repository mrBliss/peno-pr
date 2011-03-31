package geel.GUI;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GUIStandAloneFrame extends JFrame {
	public GUIStandAloneFrame(JComponent child, String title) {
		setTitle(title);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        add(child);
        setMinimumSize(new Dimension(600, 400));
        
        pack();
		setVisible(true);
	}
}
