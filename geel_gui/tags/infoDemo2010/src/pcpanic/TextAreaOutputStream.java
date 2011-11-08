/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pcpanic;


/*
 * @(#) TextAreaOutputStream.java
 *
 */
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.AttributeSet;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext.SmallAttributeSet;

/**
 * An output stream that writes its output to a javax.swing.JTextArea
 * control.
 *
 * @author Ranganath Kini
 * @see javax.swing.JTextArea
 */
public class TextAreaOutputStream extends OutputStream {

    private JTextPane textControl;
    private SimpleAttributeSet attr;

    /**
     * Creates a new instance of TextAreaOutputStream which writes
     * to the specified instance of javax.swing.JTextArea control.
     *
     * @param control A reference to the javax.swing.JTextArea
     * control to which the output must be redirected
     * to.
     * @param c 
     */
    public TextAreaOutputStream(JTextPane control, Color c) {
        textControl = control;
        attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, c);
        
        
    }

    public TextAreaOutputStream(JTextPane control) {
        this(control,Color.BLACK);
    }



    /**
     * Writes the specified byte as a character to the
     * javax.swing.JTextArea.
     *
     * @param b The byte to be written as character to the
     * JTextArea.
     */
    public void write(int b) throws IOException {
        try {
            // append the data as character to the JTextArea control
            textControl.getDocument().insertString(textControl.getDocument().getLength(), String.valueOf((char) b), attr);
            textControl.setCaretPosition(textControl.getDocument().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(TextAreaOutputStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
