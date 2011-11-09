// AppletFrame.java
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

import ch.aplu.turtle.*;
import java.awt.*;
import javax.swing.*;

/** This class is used for a Turtle applet. It contains a Playground
    where the Turtles live.
 */
public class TurtleArea implements TurtleContainer
{
  private Playground playground;

  /** Create an applet window with given applet container.
   */
  public TurtleArea(JApplet jApplet)
  {
    init(jApplet, new Playground());
  }

  /** Create an applet window with given applet container and background color.
   */
  public TurtleArea(JApplet jApplet, Color bkColor)
  {
    init(jApplet, new Playground(bkColor));
  }

  /** Create an applet window with given applet container, width and height.
   */
  public TurtleArea(JApplet jApplet, int width, int height)
  {
    Dimension size = new Dimension(width, height);
    init(jApplet,  new Playground(size));
  }

  /** Create an applet window with given applet container, width, height and background color.
   */
  public TurtleArea(JApplet jApplet, int width, int height, Color bkColor)
  {
    Dimension size = new Dimension(width, height);
    init(jApplet,  new Playground(size, bkColor));
  }

  private void init(JApplet ja, Playground pg)
  {
    playground = pg;
    ja.getContentPane().add(playground);
  }

 /** Returns the playground of this AppletFrame.
  */
  public Playground getPlayground()
  {
    return playground;
  }
}
