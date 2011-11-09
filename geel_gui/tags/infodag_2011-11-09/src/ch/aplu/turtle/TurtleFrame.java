// TurtleFrame.java

// Copyright 2002 Regula Hoefer-Isenegger
// Modificatons by Aegidius Pluess
//
// This file is part of The Java Turtle (TJT) package
//
// TJT is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// TJT is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with The Java Turtle Package; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package ch.aplu.turtle;

import ch.aplu.turtle.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;

/** This class is used for a Turtle application. It contains a <code>Playground</code>
    where the <code>Turtle</code>s live.
 */
public class TurtleFrame extends JFrame
                         implements TurtleContainer, FocusListener
{
    private Playground playground;
    private String DEFAULT_TITLE = "Java Turtle Playground";
    private int mode = Turtle.STANDARDFRAME;
    private boolean isAppletFirst = true;

   // Changed by Pluess (June 2003): instanceCount
    static int instanceCount = 0;
    static int leftBorderSize = 0;
    static TurtleFrame firstFrame = null;
    static JMenuBar nullMenuBar = null;

    /** Create a window with default title.
    */
     public TurtleFrame() {
      init(DEFAULT_TITLE, nullMenuBar, -1, -1, new Playground());
    }

    /** Create a window with given title.
    */
     public TurtleFrame(String title) {
      init(title, nullMenuBar, -1, -1, new Playground());
    }

    /** Create a window with default title and given JMenuBar.
     */
    public TurtleFrame(JMenuBar menuBar) {
      init(DEFAULT_TITLE, menuBar, -1, -1, new Playground());
    }

    /** Create a window with given title and JMenuBar.
     */
    public TurtleFrame(String title, JMenuBar menuBar) {
      init(title, menuBar, -1, -1, new Playground());
    }

    /** Create a window with default title and give background color.
    */
     public TurtleFrame(Color bkColor) {
       init(DEFAULT_TITLE, nullMenuBar, -1, -1, new Playground(bkColor));
    }

    /** Create a window with given title and background color.
    */
     public TurtleFrame(String title, Color bkColor) {
       init(title, nullMenuBar, -1, -1, new Playground(bkColor));
    }

    /** Create a window with given title, JMenuBar and background color.
    */
     public TurtleFrame(String title, JMenuBar menuBar, Color bkColor) {
      init(title, menuBar, -1, -1, new Playground(bkColor));
    }

    /** Create a window with given title, width and height.
    */
    public TurtleFrame(String title, int width, int height) {
        Dimension size = new Dimension(width, height);
        init(title, nullMenuBar, -1, -1, new Playground(size));
    }

    /** Create a window with given title, width, height and background color.
    */
    public TurtleFrame(String title, int width, int height, Color bkColor) {
        Dimension size = new Dimension(width, height);
        init(title, nullMenuBar, -1, -1, new Playground(size, bkColor));
    }

    /** Create a window with given title, JMenuBar, width and height.
     */
    public TurtleFrame(String title, JMenuBar menuBar, int width, int height) {
        Dimension size = new Dimension(width, height);
        init(title, menuBar, -1, -1, new Playground(size));
    }

    /** Create a window with given title, JMenuBar, width, height and background color.
     */
    public TurtleFrame(String title, JMenuBar menuBar, int width, int height, Color bkColor) {
        Dimension size = new Dimension(width, height);
        init(title, menuBar, -1, -1, new Playground(size, bkColor));
    }

    /** Create a window with given ulx, uly and title.
    */
    public TurtleFrame(int ulx, int uly, String title) {
        init(title, nullMenuBar, ulx, uly, new Playground());
    }

    /** Create a window with given ulx, uly, title, width and height.
    */
    public TurtleFrame(int ulx, int uly, String title, int width, int height) {
        Dimension size = new Dimension(width, height);
        init(title, nullMenuBar, ulx, uly, new Playground(size));
    }

    /** Create a window with given ulx, uly, title, width, height and background color.
    */
    public TurtleFrame(int ulx, int uly, String title, int width, int height, Color bkColor) {
        Dimension size = new Dimension(width, height);
        init(title, nullMenuBar, ulx, uly, new Playground(size, bkColor));
    }

    /** Create a window with given ulx, uly, title, JMenuBar, width, height and background color.
     */
    public TurtleFrame(int ulx, int uly, String title, JMenuBar menuBar, int width, int height, Color bkColor) {
        Dimension size = new Dimension(width, height);
        init(title, menuBar, ulx, uly, new Playground(size, bkColor));
    }

    /** Create a window with given mode.
     *  Used for an applet with a standalone window (mode: Turtle.APPLETFRAME).
     *  (Instance count for multiple windows is disabled.)
     */
    public TurtleFrame(int mode) {
      this.mode = mode;
      init(DEFAULT_TITLE, nullMenuBar, -1, -1, new Playground());
    }

    /** Create a window with given mode, title, width, height and background color.
     *  Used for an applet with a standalone window (mode: Turtle.APPLETFRAME).
     *  (Instance count for multiple windows is disabled.)
     */
    public TurtleFrame(int mode, String title, int width, int height, Color bkColor) {
      this.mode = mode;
      Dimension size = new Dimension(width, height);
      init(title, nullMenuBar, -1, -1, new Playground(size, bkColor));
    }

    // Changed by Pluess (June 2003): instanceCounter
    private void init(String title, JMenuBar menuBar, int ulx, int uly, Playground playground) {
        this.playground = playground;
        addFocusListener(this);
        String s;
        if (instanceCount > 0)
        {
          s = title + "   # " + (instanceCount+1);
          if (instanceCount == 1)
          {
            firstFrame.setTitle(title + "   # 1");
          }
        }
        else
          s = title;
        setTitle(s);
        getContentPane().add(playground);
        setResizable(false);
        if (ulx != -1)
          setLocation(ulx, uly);
        else
          setLocation();
        if (mode == Turtle.STANDARDFRAME)
          setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (menuBar != nullMenuBar)
          this.setJMenuBar(menuBar);
        pack();
        setVisible(true);
        if (instanceCount == 0)
        {
          leftBorderSize = this.getInsets().left;
          firstFrame = this;
        }
        if (mode == Turtle.STANDARDFRAME)
          instanceCount++;
    }

    /** If you don't want the window to appear at the center of the screen,
     override this method.
    */
    // Changed by Pluess (June 2003): instanceCounter
    protected void setLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension appSize = this.getPreferredSize();
        int xBorder = screenSize.width / 10;
        int yBorder = screenSize.height / 10;
        int xStep = (8 * screenSize.width / 10 - appSize.width) / 9;
        int yStep = (8 * screenSize.height / 10 - appSize.width) / 9;

        int locX = 0;
        int locY = 0;
        if (instanceCount % 2 == 0)
        {
          locX = xBorder + ((instanceCount/2) % 10) * xStep;
          locY = yBorder + ((instanceCount/2) % 10) * yStep;
        }
        else
        {
          locX = appSize.width + 2*leftBorderSize + xBorder + ((instanceCount/2) % 10) * xStep;
          locY = yBorder + ((instanceCount/2) % 10) * yStep;
        }
        setLocation(locX, locY);
    }

    /** Returns the playground of this TurtleFrame.
     */
    public Playground getPlayground() {
        return playground;
    }

    /** For standalone applets we must put the applet window in front of the browser.
     */
    public void focusLost(FocusEvent evt)
    {
      if (mode == Turtle.APPLETFRAME && isAppletFirst)
      {
        toFront();
        isAppletFirst = false;
      }
    }

    public void focusGained(FocusEvent evt)
    {}

}













