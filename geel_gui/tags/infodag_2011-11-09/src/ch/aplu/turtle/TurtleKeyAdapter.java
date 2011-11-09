// TurtleKeyAdapter
// Added by Aegidius Pluess

// Copyright 2002 Regula Hoefer-Isenegger
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

import java.awt.event.*;

/**
 * Class that overides KeyAdapter.keyPressed() in order to get
 * key events. You must register an instance with Turtle.addKeyListener().
 * @see ch.aplu.turtle.Turtle#addKeyListener
 */
public class TurtleKeyAdapter extends KeyAdapter
{
  private static int keyCode;
  private static Object monitor = new Object();

 /**
  * For internal use only.
  */
  public void keyPressed(KeyEvent evt)
  {
     keyCode = evt.getKeyCode();
     synchronized(monitor)
     {
       monitor.notify();
     }
  }

  /**
   * Wait for a keystroke and return the keycode.
   */
  public static int getKeyCodeWait()
  {
    synchronized(monitor)
    {
      try
      {
        monitor.wait();
      }
      catch (InterruptedException ex) {}
    }
    return keyCode;
  }
}