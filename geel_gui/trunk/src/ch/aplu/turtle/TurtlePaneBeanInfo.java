// TurtlePaneBeanInfo.java
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

import java.beans.*;

/**
 * A bean info class derived from SimpleBeanInfo in order to restrict the
 * visible properties of bean class TurtlePane.
 */
public class TurtlePaneBeanInfo extends SimpleBeanInfo
{
  /**
   * Return the descriptor of visible properties.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    PropertyDescriptor pds[] = null;
    try
    {
      pds = new PropertyDescriptor[]
      {
        new PropertyDescriptor("backgroundColor", TurtlePane.class),
        new PropertyDescriptor("enableFocus", TurtlePane.class),
      };
    }
    catch (IntrospectionException e)
    {
      e.printStackTrace();
    }
    return pds;
  }
}