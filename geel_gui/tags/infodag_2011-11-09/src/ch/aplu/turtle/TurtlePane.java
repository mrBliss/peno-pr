// TurtlePane.java
// Added by Aegidius Pluess

// Copyright 2002 Regula Hoefer-Isenegger
//
// This file is part of The Java Turtle package (TJT)
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

import java.awt.*;

/**
 * A bean class derived from Playground that can be used as component in
 * a Gui builder.
 */
public class TurtlePane extends Playground
  implements TurtleContainer
{

  /**
   * Property for bean support.
   */
  public Color backgroundColor = Playground.DEFAULT_BACKGROUND_COLOR;

  /**
   * Property for bean support.
   */
  public boolean enableFocus = false;

  /**
   * Parameterless bean constructor.
   */
  public TurtlePane()
  {
    super(true);
    setFocusable(enableFocus);
  }

  /**
   * Property setter.
   */
  public void setBackgroundColor(Color value)
  {
    backgroundColor = value;
    beanBkColor = value;
}

  /**
   * Property getter.
   */
  public Color getBackgroundColor()
  {
    return backgroundColor;
  }

  /**
   * Property setter.
   */
  public void setEnableFocus(boolean value)
  {
    enableFocus = value;
    setFocusable(enableFocus);
  }

  /**
   * Property getter.
   */
  public boolean getEnableFocus()
  {
    return enableFocus;
  }

  /**
   * Return current instance reference.
   */
  public Playground getPlayground()
  {
    return this;
  }


}
