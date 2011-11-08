// Turtle.java
// Modifications by Aegidius Pluess

// Copyright 2002-2003 Regula Hoefer-Isenegger (Version 1.0)
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

/* ================ UML diagram =======================================
 -----------              --------------------------       ------------
|JPanel     |            |interface                 |     |JFrame      |
|-----------|            |TurtleContainer           |     |----------- |
|           |            |--------------------------|     |            |
|           |            |Playground getPlayground()|     |            |
 -----------              --------------------------       ------------
     ^                               ^                           ^
     | is-a                          |                           |
     |                               |                           |
 -----------                         |                           |
|class      |                        |                           | is-a
|Playground |                        |implements                 |
|-----------|                        |                           |
|           |                        |                           |
|           |                        |              -------------
 -----------                         |             |
    ^ o                   --------------------------------
    | |                  |class                           |
    | |     has-a        |TurtleFrame                     |
    | |    (creates)     |------------------------------- |
    |  ----------------- |TurtleFrame(...new Playground())|
    | is-a               |Playground playground           |
    |                     --------------------------------
  ----------                            o
 |class     |                           | has-a (creates)
 |TurtlePane|                           |
 |----------|                           |
 |          |             --------------------------------
 |          |            |class                           |
  ----------             |Turtle                          |
                         |------------------------------- |
                         |TurtleFrame turtleFrame         |
                         |                                |
                          --------------------------------
==========================================================================*/

/*
Screen coordinate system: 0..400 pixels in both directions corresponds to turtle
turtle coordinate system -200..200, so only translation is necessary (no scaling).

This avoids rounding errors.
*/

package ch.aplu.turtle;

import ch.aplu.turtle.*;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.sound.sampled.*;

/**
   The core class for turtles.

   For a simple example on how to use <code>Turtle</code>, cf. the
   <a href="package-summary.html#example"> Java Turtle Package description</a>.
   @author <a href="mailto:regula@hoefer.ch">Regula Hoefer-Isenegger</a>
   @version 0.1.1
*/
public class Turtle implements Cloneable
{
  private double angle;
  private Point2D.Double position;
  private Playground playground;
  private JMenuBar menuBar;
  private int framesPerSecond;
  private double speed;           // Pixel/sec
  private double angleSpeed;      // Radian/sec
  private TurtleRenderer turtleRenderer;
  private int angleResolution;
  private LineRenderer lineRenderer;
  private static TurtleFactory turtleFactory;
  private boolean penUp;
  private boolean showTurtle;
  private boolean initialVisibility = true;
  private Pen pen;
  private Color color;
  private int edgeBehaviour;
  // new instance by Pluess (May 2003)
  private TurtleFrame turtleFrame;
  private static JMenuBar nullMenuBar = null;
  private boolean isTurtleArea = false;

  /** Mode attribute for turtle applets in standalone window
  */
  public static int APPLETFRAME = 1;

  /** Mode attribute for normal turtle application (default)
  */
  public static int STANDARDFRAME = 0;

  /** Convert from screen coordinates to turtle coordinates
  */
  public Point2D.Double toTurtlePos(Point p) {
    return playground.toTurtleCoords((double)p.x, (double)p.y);
  }

  /** Convert from screen coordinates to turtle coordinates
  */
  public Point2D.Double toTurtlePos(int x, int y) {
    return playground.toTurtleCoords((double)x, (double)y);
  }

  /** Convert from screen coordinates to turtle coordinates
  */
  public double toTurtleX(int x) {
    return (playground.toTurtleCoords((double)x, 0)).x;
  }

  /** Convert from screen coordinates to turtle coordinates
  */
  public double toTurtleY(int y) {
    return (playground.toTurtleCoords(0, (double)y)).y;
  }

  /** Get the TurtleFrame (derivated from JFrame)
  */
  public TurtleFrame getFrame() {
    return turtleFrame;
  }

  /** Add the specified mouse listener to receive mouse events.
   */
  public void addMouseListener(MouseListener l) {
    playground.addMouseListener(l);
  }

  /** Add the specified mouse motion listener to receive mouse motion events.
   */
  public void addMouseMotionListener(MouseMotionListener l) {
    playground.addMouseMotionListener(l);
  }

  /** Add the specified key listener to receive key events.
   * (Not allowed for embedded applets.)
   */
  public void addKeyListener(KeyListener l) {
    if (turtleFrame == null)
      throw new RuntimeException("KeyListener not allowed for embedded applets");
    turtleFrame.addKeyListener(l);
  }

  /** Add the specified window listener to receive window events.
   * (Not allowed for embedded applets.)
  */
  public void addWindowListener(WindowListener l) {
    if (turtleFrame == null)
      throw new RuntimeException("WindowListener not allowed for embedded applets");
    turtleFrame.addWindowListener(l);
  }

  /** Add the specified window focus listener to receive window focus events.
   * (Not allowed for embedded applets.)
  */
  public void addWindowFocusListener(WindowFocusListener l) {
    if (turtleFrame == null)
      throw new RuntimeException("WindowFocusListener not allowed for embedded applets");
    turtleFrame.addWindowFocusListener(l);
  }

  /** Add the specified component listener to receive component events.
   * (Not allowed for embedded applets.)
  */
  public void addComponentListener(ComponentListener l) {
    if (turtleFrame == null)
      throw new RuntimeException("ComponentListener not allowed for embedded applets");
    turtleFrame.addComponentListener(l);
  }

  /** Add the specified focus listener to receive focus events.
   * (Not allowed for embedded applets.)
  */
  public void addFocusListener(FocusListener l) {
    if (turtleFrame == null)
      throw new RuntimeException("FocusListener not allowed for embedded applets");
    turtleFrame.addFocusListener(l);
  }

  /**
   * Emit a beep. Fails if no standard speaker available. Try to use bong() instead.
     @return a reference to the <code>Turtle</code> to allow chaining.
   * @see #bong()
  */
  public Turtle beep() {
    Toolkit.getDefaultToolkit().beep();
    return this;
  }

  /**
   * Emit a 'bong' via the sound card. Useful on computers without standard
   * speaker.
     @return a reference to the <code>Turtle</code> to allow chaining.
   * @see #beep()
  */
  public Turtle bong()
  {
    InputStream is = getClass().getResourceAsStream("res/bong.wav");
    StreamingPlayer player = new StreamingPlayer(is);
    try
    {
      player.start(false);
    }
    catch (LineUnavailableException ex)
    {
      System.out.println("Error in bong(). Sound card unavailable");
    }
    return this;
  }


  /** Represent clip mode.
  @see #WRAP
  @see #clip()
  */
  protected final static int CLIP = 0;
  /** Represent wrap mode.

  @see #CLIP
  @see #wrap()
  */
  protected final static int WRAP = 1;
  /** Represent the default edge behaviour (i.e. CLIP or WRAP).

  @see #CLIP
  @see #WRAP
  @see #clip()
  @see #wrap()
  */
  protected static int DEFAULT_EDGE_BEHAVIOUR = CLIP;
  /** Represent the default speed (velocity).

  @see #speed(double)
  */
  protected static double DEFAULT_SPEED = 200;

  /** Represent the default angle resolution.

  It specifies how many different turtle pictures
  are generated.
  */
  protected static int DEFAULT_ANGLE_RESOLUTION = 72;

  /** Specify how many frames per second are used for
      turtle animation.
  */
  protected static int DEFAULT_FRAMES_PER_SECOND = 10;

  /** Specify the default turtle color.

  @see #setColor(java.awt.Color)
  */
  protected static Color DEFAULT_TURTLE_COLOR = Color.cyan;

  /** Specify the default pen color.

  @see #setPenColor(java.awt.Color)
  */
  protected static Color DEFAULT_PEN_COLOR = Color.blue;

  // ------------ Start of ctors ---------------------------

  /** Create a new <code>Turtle</code> in its own new window.
   */
  public Turtle()
  {
    this(nullMenuBar);
  }

  /** Create a new <code>Turtle</code> in its own new window
   * and the given menu.
   */
  Turtle(final JMenuBar menuBar)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(menuBar);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(menuBar);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(JMenuBar menuBar)
  {
    turtleFrame = new TurtleFrame(menuBar);
    init(turtleFrame, DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }

  /** Create a new <code>Turtle</code> with specified visibility
   * in its own new window.
   */
  public Turtle(boolean show)
  {
    this(nullMenuBar, show);
  }

  /** Create a new <code>Turtle</code> with specified visibility
   * in its own new window and the given menu.
   */
  Turtle(final JMenuBar menuBar, final boolean show)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(menuBar, show);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(menuBar, show);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(JMenuBar menuBar, boolean show)
  {
    initialVisibility = show;
    turtleFrame = new TurtleFrame(menuBar);
    init(turtleFrame, DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }

  /** Create a new <code>Turtle</code> with specified
   *  <code>color</code> in its own new window.
   */
  public Turtle(Color color)
  {
    this(nullMenuBar, color);
  }

  /** Create a new <code>Turtle</code> with specified
   *  <code>color</code> in its own new window
   * and the given menu.
   */
  public Turtle(final JMenuBar menuBar, final Color color)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(menuBar, color);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(menuBar, color);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(JMenuBar menuBar, Color color)
  {
    turtleFrame = new TurtleFrame(menuBar);
    init(turtleFrame, color);
    getPlayground().paintTurtles();
  }

  /** Create a new <code>Turtle</code> in the given <code>TurtleContainer</code>.
  */
  public Turtle(final TurtleContainer turtleContainer)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(turtleContainer);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(turtleContainer);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(TurtleContainer turtleContainer)
  {
    if (turtleContainer instanceof TurtleArea)
      isTurtleArea = true;
    else
      turtleFrame = (TurtleFrame)turtleContainer;  // turtleFrame not initialized for embedded applets
    init(turtleContainer, DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }

  /** Create a new <code>Turtle</code> in the given <code>TurtlePane</code>.
  */
  public Turtle(final TurtlePane turtlePane)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(turtlePane);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(turtlePane);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(TurtlePane turtlePane)
  {
    playground = turtlePane;

    if (playground.turtleG2D != null)  // TurtlePane already initialized, put
                                       // new turtle in same playground
    {
      init(getPlayground(), DEFAULT_TURTLE_COLOR);
      getPlayground().paintTurtles();
    }
    else
    {
      playground.isBean = false; // inhibit special section in Playground.paintComponent()

      // deferred init:
      if (playground.traceG2D != null)
        playground.traceG2D.dispose();
      Dimension dim = turtlePane.getSize();
      Color bkColor = turtlePane.getBackgroundColor();
      getPlayground().init(dim, bkColor);

      init(turtlePane.getPlayground(), DEFAULT_TURTLE_COLOR);
      getPlayground().paintTurtles();
    }
  }

  /** Create a new <code>Turtle</code> with specified visiblity
   * in the given <code>TurtleContainer</code>.
   */
  public Turtle(final TurtleContainer turtleContainer, final boolean show)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(turtleContainer, show);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(turtleContainer, show);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(TurtleContainer turtleContainer, boolean show)
  {
    if (turtleContainer instanceof TurtleArea || turtleContainer instanceof TurtlePane) {
            isTurtleArea = true;
        }
    else {
            turtleFrame = (TurtleFrame) turtleContainer;
        }  // ditto
    initialVisibility = show;
    init(turtleContainer, DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }

  /** Create a new <code>Turtle</code> with specified
      <code>color</code> in the given <code>TurtleContainer</code>.
  */
  public Turtle(final TurtleContainer turtleContainer, final Color color)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(turtleContainer, color);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(turtleContainer, color);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(TurtleContainer turtleContainer, Color color)
  {
    if (turtleContainer instanceof TurtleArea)
      isTurtleArea = true;
    else
      turtleFrame = (TurtleFrame)turtleContainer;  // ditto
    init(turtleContainer, color);
    getPlayground().paintTurtles();
  }

  /** Create a new <code>Turtle</code> in the same
      <code>TurtleContainer</code> (window) as
      <code>otherTurtle</code>.
  */
  public Turtle(final Turtle otherTurtle)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(otherTurtle);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(otherTurtle);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(Turtle otherTurtle)
  {
    turtleFrame = otherTurtle.getFrame();
    init(otherTurtle.getPlayground(), DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }

   /** Create a new <code>Turtle</code> in the same
       <code>TurtleContainer</code> (window) as
       <code>otherTurtle</code> with specified visibility.
   */
  public Turtle(final Turtle otherTurtle, final boolean show)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(otherTurtle, show);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(otherTurtle, show);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

 private void createTurtle(Turtle otherTurtle, boolean show)
 {
   initialVisibility = show;
   turtleFrame = otherTurtle.getFrame();
   init(otherTurtle.getPlayground(), DEFAULT_TURTLE_COLOR);
   getPlayground().paintTurtles();
 }

  /** Create a new <code>Turtle</code> with the specified
      <code>color</code> in the same
      <code>TurtleContainer</code> (window) as <code>otherTurtle</code>.
  */
  public Turtle(final Turtle otherTurtle, final Color color)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(otherTurtle, color);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(otherTurtle, color);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(Turtle otherTurtle, Color color)
  {
    turtleFrame = otherTurtle.getFrame();
    init(otherTurtle.getPlayground(), color);
    getPlayground().paintTurtles();
  }


  private void init(TurtleContainer turtleContainer, Color color)
  {
    init(turtleContainer.getPlayground(), color);
  }

  /** Create a new <code>Turtle</code> with a special mode.
   * Call with mode Turtle.APPLETFRAME for a standalone applet window.
   * Instance count for multiple windows is disabled.
   * If more features are needed, use TurtleFrame(int mode, ...);
   */
  Turtle(final int mode)
  {
    if (EventQueue.isDispatchThread())
      createTurtle(mode);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
           createTurtle(mode);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createTurtle(int mode)
  {
    turtleFrame = new TurtleFrame(mode);
    init(turtleFrame, DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }

  // ------------ End of ctors ---------------------------

  private void init(Playground playground, Color color) {
    angle = 0;
    position = new Point2D.Double(0, 0);
    setPlayground(playground);
    framesPerSecond = DEFAULT_FRAMES_PER_SECOND;
    if (initialVisibility)
      speed(DEFAULT_SPEED);
    else
      speed(-1);
    if (isTurtleArea)
      initialVisibility = false;
    showTurtle = initialVisibility;
    setAngleResolution(DEFAULT_ANGLE_RESOLUTION);
    angleSpeed = getSpeed() * Math.PI * 2 / DEFAULT_SPEED;
    pen = new Pen(DEFAULT_PEN_COLOR);
    if(getTurtleFactory() == null){
      turtleFactory = createTurtleFactory();
    }
    internalSetColor(color);
    lineRenderer = createLineRenderer();
    getTurtleRenderer().setAngle(getAngle());
  }

  /** Set the angle resolution for the <code>Turtle</code>'s pictures.

  It specifies how many pictures are used. e.g. an angle resolution
  of 90 means that you get one picture for every 4 degrees
  (= 360/90 degrees).
  */
  public Turtle setAngleResolution(int newResolution) {
    synchronized(playground) {
      angleResolution = newResolution;
      return this;
    }
  }

  /** Return the <code>TurtleFactory</code> of this turtle.

  @see ch.aplu.turtle.TurtleFactory
  */
  public TurtleFactory getTurtleFactory() {
    return this.turtleFactory;
  }

  /** Create a <code>LineRenderer</code> which is responsible
      for the correct drawing of the lines.

      @return the new <code>LineRenderer</code>
  */

  protected LineRenderer createLineRenderer() {
    return new LineRenderer(this);
  }

  /**  Create a <code>TurtleRenderer</code> which is responsible
       for the correct drawing of the <code>Turtle</code>.

       @return the new <code>TurtleRenderer</code>
  */
  protected TurtleRenderer createTurtleRenderer() {
    return new TurtleRenderer(this);
  }

  /** Create a <code>TurtleFactory</code> which provides for
      the Turtle pictures.

      @return the new <code>TurtleFactory</code>
  */

  protected TurtleFactory createTurtleFactory() {
    return new TurtleFactory();
  }
  /** Get the angle resolution.

  @see #setAngleResolution(int)
  */

  protected int getAngleResolution() {
    return angleResolution;
  }

  /** Get the <code>TurtleRenderer</code>.
   */
  TurtleRenderer getTurtleRenderer() {
    return turtleRenderer;
  }

  /** Get the <code>LineRenderer</code>.
   */
  private LineRenderer getLineRenderer() {
    return lineRenderer;
  }

  /** Get the <code>Playground</code>.
   */
  public Playground getPlayground() {
    return playground;
  }

  /** Set the <code>Playground</code> to the specified
      <code>playground</code>.

      The <code>Turtle</code> is removed from the old
      <code>Playground</code> and set to the new one.
  */
  private void setPlayground(Playground playground) {
    Playground pg = getPlayground();
    // 	if (playground!=null) {
    // 	    System.out.println("arg: "+playground.hashCode());
    // 	}
    if(pg != null){
      // 	    System.out.println("old: "+pg.hashCode());
      pg.clearTurtle(this);
      pg.remove(this);
      pg.paintTurtles();
    }
    this.playground = playground;
    // 	if (this.playground!=null) {
    // 	    System.out.println("new: "+this.playground.hashCode());
    // 	}
    playground.add(this);
    playground.paintTurtles(this);
  }

  /** Initialize the menu bar instance.
   */
  private void setMenuBar(JMenuBar menuBar) {
    this.menuBar = menuBar;
  }

  /** Return the menu bar instance.
   */
  private JMenuBar getMenuBar() {
    return menuBar;
  }

  /** Get the angle speed.

  (I.e. how fast the <code>Turtle</code> rotation
  animation is performed.)
  */
  private double getAngleSpeed() {
    return angleSpeed;
  }

  /** Set the angle speed.

  @see #getAngleSpeed()
  */
  private void setAngleSpeed(double newAngleSpeed) {
    this.angleSpeed = newAngleSpeed;
  }

  /** Get the current angle (heading) of the
      <code>Turtle</code>.
  */
  private double getAngle() {
    return angle; // in radians
  }

  /** Get the current speed.
   */
  public double getSpeed() {
    return speed;
  }

  /** Query the <code>Turtle</code>'s x-position.
   * Coordinate are not bound to playground even when
   * wrapping is on.
   */
  public double _getX() {
    synchronized (playground) {
      return position.getX();
    }
  }

  /** Query the <code>Turtle</code>'s y-position without wrapping.
   * Coordinate are not bound to playground even when
   * wrapping is on.
   */
  public double _getY() {
    synchronized (playground) {
      return position.getY();
    }
  }

  /** Query the <code>Turtle</code>'s x-position.
   * If turtle is outside playground and wrapping is on
   * return the coordinate in range -200..200.
   * */

  public double getX() {
    synchronized (playground) {
      double xPos = _getX();
      if (isWrap()) {
        if (_getX() > 200)
          xPos = (_getX() + 200) % 400 - 200;
        if (_getX() < -200)
          xPos = (_getX() - 200) % 400 + 200;
      }
      return xPos;
    }
  }

  /** Query the <code>Turtle</code>'s x-position.
   * If turtle is outside playground and wrapping is on
   * return the coordinate in range -200..200.
   */
  public double getY() {
    synchronized (playground) {
      double yPos = _getY();
      if (isWrap()) {
        if (_getY() > 200)
          yPos = (_getY() + 200) % 400 - 200;
        if (_getY() < -200)
          yPos = (_getY() - 200) % 400 + 200;
      }
      return yPos;
    }
  }


  /** Query the <code>Turtle</code>'s position without wrapping.
   * Coordinate are not bound to playground even when
   * wrapping is on.
   */
  public Point2D.Double _getPos() {
    return position;
  }

  /** Query the <code>Turtle</code>'s position */
  public Point2D.Double getPos() {
    return new Point2D.Double(getX(), getY());
  }

  /** Put the <code>Turtle</code> to a new position with the given x-coordinates.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setX(double x) {
    synchronized (playground) {
      getPlayground().clearTurtle(this);
      internalSetX(x);
      getPlayground().paintTurtles(this);
      return this;
    }
  }

  /** Put the <code>Turtle</code> to a new position with the given screen x-coordinates.

  @return a reference to  the<code>Turtle</code> to allow chaining.
  */
  public Turtle setScreenX(int x) {
    synchronized (playground) {
      return setX(toTurtleX(x));
    }
  }

  /** Put the <code>Turtle</code> to a new position with the given y-coordinates.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setY(double y) {
    synchronized (playground) {
      getPlayground().clearTurtle(this);
      internalSetY(y);
      getPlayground().paintTurtles(this);
      return this;
    }
  }

  /** Put the <code>Turtle</code> to a new position with the given screen y-coordinates.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setScreenY(int y) {
    synchronized (playground) {
      return setY(toTurtleY(y));
    }
  }

  /** Set the <code>Turtle</code>'s x-Coordinate.
 */
  protected void internalSetX(double x) {
    synchronized (playground) {
      position.setLocation(x, _getY());
    }
  }

  /** Set the <code>Turtle</code>'s y-Coordinate.
  */
  protected void internalSetY(double y) {
    position.setLocation(_getX(), y);
  }

  /** Set the <code>Turtle</code>'s Position.
  */
  protected void internalSetPos(double x, double y) {
    position.setLocation(x, y);
  }

  /** Hide the <code>Turtle</code>.

  @return a reference to the <code>Turtle</code> to allow chaining.
  @see #st()
  If there is only one turte, the speed is set to -1
  so there is no Turtle animation at all.
  Hiding the <code>Turtle</code> speeds up the graphics enormously.
  */
  public Turtle ht() {
    synchronized (playground) {
      this.internalHide();
      if (getPlayground().countTurtles() == 1)
        speed(-1);
      return this;
    }
  }

  /** Hide the <code>Turtle</code>.

  This is the same as ht().
  @return a reference to the <code>Turtle</code> to allow chaining.
  @see #st()
  */
  public Turtle hideTurtle()
  {
    return ht();
  }

  /** This is the method called by the public methods ht() and hideTurtle().

  Here the actual hiding takes place.
  @see #ht()
  @see #hideTurtle()
  */
  protected void internalHide() {
    getPlayground().clearTurtle(this);
    showTurtle = false;
    if (getPlayground().getPrinterG2D() == null)
      getPlayground().repaint();
  }

  /** Set the <code>Turtle</code> to show mode.

  That means that the <code>Turtle</code> will be drawn.
  @return a reference to the <code>Turtle</code> to allow chaining.
  @see #ht()
  */
  public Turtle st() {
    synchronized (playground) {
      if (getPlayground().getPrinterG2D() != null)
        return this;  // Don't show the turtle during printing
      getPlayground().paintTurtle(this);
      showTurtle = true;
      getPlayground().repaint();
      return this;
    }
  }

  /** The same as st().

  @see #st()
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle showTurtle() {
    return st();
  }

  /** Tell whether the <code>Turtle</code> is hidden or not.

  @return <code>true</code> if the <code>Turtle</code> is hidden,
  <code>false</code> otherwise.
  */
  public boolean isHidden() {
    return !showTurtle;
  }

  private int getFramesPerSecond() {
    return this.framesPerSecond;
  }

  /** Only set the angle attribute. This method does not
      invoke any re-painting.
  */
  private void setAngle(double radians) {
    this.angle = radians;
  }

  /** This is the same as setH(double degrees).

  @see #setH(double)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setHeading(double degrees) {
    synchronized (playground) {
      setAngle(Math.toRadians(degrees));
      getTurtleRenderer().setAngle(Math.toRadians(degrees));
      getPlayground().clearTurtle(this);
      getPlayground().paintTurtles(this);
      return this;
    }
  }

  /** Set the <code>Turtle</code>'s heading.

  0 means facing NORTH.<br>
  the angles are measured clockwise.
  @return a reference to the <code>Turtle</code> to allow chaining.
  @see #setHeading(double)
  */
  public Turtle setH(double degrees) {
    return setHeading(degrees);
  }
  /** Query the <code>Turtle</code>'s heading.

  @see #setH(double)
  */
  public double heading() {
    synchronized (playground) {
      return Math.toDegrees(getAngle());
    }
  }
  /** Set the <code>Turtle</code>'s heading to the new value.

  @return the old (previous) value.
  @see #setH(double)
  */
  public double heading(double degrees) {
    synchronized (playground) {
      double tmp = Math.toDegrees(getAngle());
      setHeading(degrees);
      return tmp;
    }
  }

  /** Set the <code>Turtle</code>'s speed.

  If you try to set the speed to 0, it will be set to 1 (very slow).
  A negative speed means that moving the <code>Turtle</code> (fd, bk)
  will not be animated.<br>
  The unit is pixels per second (up to certain bounds depending on the CPU etc.).<br>
  <i>Remark: Dashed lines will only be painted as you expect it with speed set
  to <code>-1</code>.</i>

  @return a reference to the <code>Turtle</code> reference to allow chaining.
  @see #fd(double)
  @see #bk(double)
  */
  public Turtle speed(double speed) {
    if (speed == 0)
      this.speed = 1;
    else
      this.speed = speed;
    return this;
  }


  /** This method is responsible for the rotation animation.
   */
  private void internalRotate(double angle) {
    // angle in radians
    if(isHidden()){
      synchronized(playground)
      {
        setAngle(getAngle()+angle);
        if (getTurtleRenderer().imageChanged(getAngle())) {
          getTurtleRenderer().setAngle(getAngle());
        }
      }
      return;
    }
    if (angle != 0) {
      int iterations = getAngleIterations(angle);

      double sign = angle/Math.abs(angle);
      double increment = sign*getAngleSpeed()/(double)getFramesPerSecond();
      double startAngle = getAngle();

      for (int index = 0; index < iterations; index++) {
        long timeStamp = System.currentTimeMillis();

        synchronized(playground) {
          getPlayground().clearTurtle(this);

          if (index < iterations-1) {
            setAngle(getAngle()+increment);
          }
          else {
            setAngle(startAngle+angle);
          }

          if (getTurtleRenderer().imageChanged(getAngle())) {
            getTurtleRenderer().setAngle(getAngle());
            getPlayground().paintTurtles(this);
          }
        }


      }
    }
    getPlayground().paintTurtles(this);
  }

  /** This method is responsible for the moving animation.
   */
  private void internalMove(double length) {
    if (getSpeed()>0){
      if (length != 0) {
        int iterations = getPathIterations(length);
        // an angle of 0 means: facing NORTH
        double startX = _getX();
        double startY = _getY();
        getLineRenderer().init(startX, startY);
        double dx = length * Math.sin(getAngle());
        double dy = length * Math.cos(getAngle());
        double incrementX = dx / iterations;
        double incrementY = dy / iterations;
        for (int index = 0; index < iterations; index++) {
          long timeStamp = System.currentTimeMillis();
          int nX = (int)_getX();
          int nY = (int)_getY();

          synchronized(playground) {
            getPlayground().clearTurtle(this);

            if (index < iterations-1) {
              internalSetX(_getX()+incrementX);
              internalSetY(_getY()+incrementY);
            }
            else { // last step: Calc the "exact" value
              internalSetX(startX + dx);
              internalSetY(startY + dy);
            }
            if (nX != (int)_getX()
                || nY != - (int)_getY()
                || index == iterations-1)
              {
                if (!isPenUp()) {
                  getLineRenderer().lineTo(_getX(), _getY());
                }
                getPlayground().paintTurtles(this);
            }
          }
          Double frames = new Double(1000./getFramesPerSecond());
        }
      }
    }
    else { // Speed < 0, i.e. no animation
      double startX = _getX();
      double startY = _getY();
      getLineRenderer().init(startX, startY);
      double dx = length * Math.sin(getAngle());
      double dy = length * Math.cos(getAngle());
      getPlayground().clearTurtle(this);
      internalSetX(startX + dx);
      internalSetY(startY + dy);
      if (!isPenUp()) {
        getLineRenderer().lineTo(_getX(), _getY());
      }
          getPlayground().paintTurtles(this);
    }
  }

  /** Turn the <code>Turtle</code> the given angle (in degrees) to the left

  @return a reference to the <code>Turtle</code> to allow chaining.
  @see #rt(double)
  */
  public Turtle lt(double degrees) {
    return left(degrees);
  }

  /**	Same as lt(double degrees)

  @see #lt(double)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle left(double degrees) {
    internalRotate(-Math.toRadians(degrees));
    return this;
  }

  /** Turn the <code>Turtle</code>  the given angle (in degrees) to the right.

  @return a reference to the <code>Turtle</code> to allow chaining.
  @see #rt(double)
  */
  public Turtle rt(double degrees) {
    return right(degrees);
  }

  /** Same as rt(double degrees).

  @see #rt(double)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle right(double degrees) {
    internalRotate(Math.toRadians(degrees));
    return this;
  }

  /** Same as fd(double distance)

  @see #fd(double)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle forward(double distance) {
    internalMove(distance);
    return this;
  }

  /** Move the <code>Turtle</code> forwards.

  Negative values for <code>distance</code> are
  allowed. In that case, the <code>Turtle</code>
  will move backwards.

  @see #bk(double)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle fd(double distance) {
    return forward(distance);
  }

  /** Same as bk(double distance).

  @see #bk(double)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle back(double distance) {
    internalMove(-distance);
    return this;
  }

  /** Move the <code>Turtle</code> backwards.

  Negative values for <code>distance</code> are
  allowed. In that case, the <code>Turtle</code>
  will move forwards.

  @see #fd(double)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle bk(double distance) {
    return back(distance);
  }

/*
  // not bound to playground, even when wrapping is on
  protected Point2D.Double getPosition() {
    return position;
  }

  // bound to playground, when wrapping is on
  protected Point2D.Double getPositionWrapped() {
    return new Point2D.Double(getX(), getY());
  }
*/

  /** Query the distance from the current location
      to the given one.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public double distance(double x, double y) {
    synchronized (playground) {
      return this.getPos().distance(x,y);
    }
  }

  /** Query the distance from the current location
      to the given one.
  *   Actually a polygon with 36 sides is drawn.
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public double distance(Point2D.Double p) {
    synchronized (playground) {
      return this.getPos().distance(p);
    }
  }

  /** Draw a circle to the left from the current position with
   *  turtle's heading tangent direction and given radius.
  *   Actually a polygon with 36 sides is drawn.
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle leftCircle(double radius)
  {
    return circle(radius, true);
  }

  /** Draw a circle to the right from the current position with
   *  turtle's heading tangent direction and given radius.
  *   Actually a polygon with 36 sides is drawn.
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle rightCircle(double radius)
  {
    return circle(radius, false);
  }

  private Turtle circle(double radius, boolean left)
  {
    double nbSteps = 36;  // 36 side polygon
    double angle = 2 * Math.PI / nbSteps;
    double step = 2 * radius * Math.sin(angle/2);
    for (int i = 0; i < 36; i++) {
      if (left)
        if (i == 0)
          lt(180.0 / nbSteps);
        else
          lt(360.0 / nbSteps);
      else
        if (i == 0)
          rt(180.0 / nbSteps);
        else
          rt(360.0 / nbSteps);
      fd(step);
    }
    return this;
  }

  /** Calculate the number of iterations when animating left or right (rotation).
  */
  private int getAngleIterations(double dAngle) {
    if(getAngleSpeed()<0){
      return 1;
    }
    if(getAngleSpeed()==0){
      setAngleSpeed(1);
    }
    double dAbsAngle = Math.abs(dAngle);
    Double dValue = new Double(Math.ceil(dAbsAngle/getAngleSpeed()*getFramesPerSecond()));
    return dValue.intValue();
  }

  /** Calculate the number of iterations when animating forwards or backwards.
  */
  private int getPathIterations(double length) {
    if(speed < 0)
      return 1;

    if(speed == 0)
      speed(1);

    double dAbsLength = Math.abs(length);
    Double dValue = new Double(Math.ceil(dAbsLength/getSpeed()*getFramesPerSecond()));
    return dValue.intValue();
  }

  /**	Lift the <code>Turtle</code>'s pen up so it
        won't draw a line anymore.

        This is the same as pu().

        @see #pu()
        @see #penDown()
        @see #pd()
        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle penUp() {
    synchronized (playground) {
      this.penUp = true;
      return this;
    }
  }

  /**	Lift the <code>Turtle</code>'s pen up so it
        won't draw a line anymore.

        This is the same as penUp().

        @see #penUp()
        @see #penDown()
        @see #pd()
        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle pu() {
    return this.penUp();
  }

  /**	Lower the <code>Turtle</code>'s <code>Pen</code> down  so it
        will draw a line when moving.

        This is the same as pd().

        @see #pd()
        @see #penUp()
        @see #pu()
        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle penDown() {
    synchronized (playground) {
      this.penUp = false;
      return this;
    }
  }

  /**	Lowers the <code>Turtle</code>'s pen down so it
        will draw a line when moving.

        This is the same as penDown().

        @see #penDown()
        @see #penUp()
        @see #pu()
        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle pd() {
    return this.penDown();
  }

  /** Query the <code>Pen</code>'s state (up or down).

  @return <code>true</code> if the <code>Pen</code> is
  up, <code>false</code> otherwise.
  @see #pu()
  @see #pd()
  */
  public boolean isPenUp() {
    return this.penUp;
  }

  /** Return the bounds of this <code>Turtle</code>. This is required
      by the methods that (return-)paint the Turtles.
  */
  Rectangle getBounds() {
    Rectangle rect = new Rectangle();

    Image img = getTurtleRenderer().currentImage();
    int nWidth = img.getWidth(getTurtleRenderer());
    int nHeight = img.getHeight(getTurtleRenderer());
    double x = (_getX()<0)?Math.floor(_getX()):Math.ceil(_getX());
    double y = (_getY()<0)?Math.floor(_getY()):Math.ceil(_getY());
    rect.setBounds((int)x-nWidth/2, (int)y + nHeight/2, nWidth, nHeight);
    return rect;
  }

  /** Get the <code>Turtle</code>'s <code>Pen</code>.

  You need it if you want to change end caps etc.
  @see Pen
  */
  public Pen getPen() {
    return pen;
  }
  /** Set the line thickness.

  This works only neatly in clip mode (yet).
  @see #clip()
  @see #wrap()
  */
  public Turtle setLineWidth(double lineWidth){
    synchronized (playground) {
      setLineWidth((float)lineWidth);
      return this;
    }
  }

  /** Set the line thickness.

  This works only neatly in clip mode (yet).
  @see #clip()
  @see #wrap()
  */
  public Turtle setLineWidth(float lineWidth) {
    synchronized (playground) {
      getPen().setLineWidth(lineWidth);
      return this;
    }
  }

  /** Set the <code>Turtle</code>'s color to the specified one.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setColor(Color color) {
    synchronized (playground) {
      internalSetColor(color);
      getPlayground().paintTurtles();
      return this;
    }
  }

  private void internalSetColor(Color color) {
    this.color = color;
    if(getTurtleRenderer()==null){
      turtleRenderer = createTurtleRenderer();
      getTurtleRenderer().init(getTurtleFactory(), getAngleResolution());
    }
    else{
      getTurtleRenderer().init(new TurtleFactory(),
                               this.angleResolution);
    }
  }

  /** Set the color to the specified one.

  @see #fill()
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setFillColor(Color color) {
    synchronized (playground) {
      getPen().setFillColor(color);
      return this;
    }
  }

  /** Query the <code>Turtle</code>'s current color.
   */
  public Color getColor(){
    return color;
  }

  /**Set the <code>Turtle</code>'s pen color.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setPenColor(Color color) {
    synchronized (playground) {
      getPen().setColor(color);
      return this;
    }
  }

  /**	Move the Turtle back "home", i.e. set its position
        to the origin, facing NORTH.

        Color, PenColor etc. remain the same.
        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle home() {
    // first : clean the <code>Turtle</code>!
    synchronized (playground) {
      getPlayground().clearTurtle(this);
      position = new Point2D.Double(0,0);
      setHeading(0);

      return this;
    }
  }

  /**	The <code>Turtle</code>'s <code>Pen</code> is
        changed to an eraser (which is in fact a pen with
        background color).

        This is the same as pe()
        @see #pe()
        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle penErase() {
    synchronized (playground) {
      this.internalPenErase();
      return this;
    }
  }

  /**	The <code>Turtle</code>'s <code>Pen</code> is
        changed to an eraser (which is in fact a pen with
        background color).

        This is the same as penErase()
        @see #penErase()
        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle pe() {
    return penErase();
  }

  protected void internalPenErase(){
    this.setPenColor(getPlayground().getBackground());
  }

  /**  Put the <code>Turtle</code> to a new position with specified
        x- and y-coordinates.

        @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setPos(double x, double y) {
    synchronized (playground) {
      getPlayground().clearTurtle(this);
      internalSetPos(x, y);
      getPlayground().paintTurtles();
      return this;
    }
  }

  /** Put the <code>Turtle</code> to a new position.

  @return the a reference to <code>Turtle</code> to allow chaining.
  */
  public Turtle setPos(Point2D.Double p) {
    return setPos(p.x, p.y);
  }

  /** Put the <code>Turtle</code> to a new screen position.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setScreenPos(Point p) {
    return setPos(toTurtlePos(p));
  }


  /**	Put a Turtle image at the current position.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle stampTurtle() {
    synchronized (playground) {
      this.getPlayground().stampTurtle(this);
      return this;
    }
  }

  /** Calculate the direction to a given point.

  @return the direction from the current turtle position
  towards the given point, measured in degrees and clockwise
  from the vertical upwards position.
  */
  public double towards(double x, double y) {
    synchronized (playground) {
      double dx = x - getX();
      double dy = y - getY();
      double result = Math.toDegrees(Math.atan2(dx, dy));
      return (result < 0) ? result+360 : result;
    }
  }

  /** Calculate the direction to a given point.

  @return the direction from the current turtle position
  towards the given point, measured in degrees and clockwise
  from the vertical upwards position.
  */
  public double towards(Point2D.Double p) {
    return towards(p.getX(), p.getY());
  }

  /** Put the <code>Turtle</code> to the top (i.e. above any other
      turtle).

      Just invokes the toTop-Method in Playground.
      @see Playground#toTop
      @see #internalToBottom
  */
  void internalToTop() {
    this.getPlayground().toTop(this);
  }
  /** Put the <code>Turtle</code> to the bottom (i.e. under any other
      turtle).

      Just invokes the toBottom-Method in Playground.
      @see Playground#toBottom
      @see #internalToTop
  */
  void internalToBottom() {
    this.getPlayground().toBottom(this);
  }

  /** Put this turtle to the bottom.

  So any other turtle in the same Playground will be drawn over it.
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle toBottom() {
    //  	this.getPlayground().toBottom(this);
    synchronized (playground) {
      internalToBottom();
      getPlayground().paintTurtles();
      return this;
    }
  }

  /** Put this turtle to the top.

  So it will be drawn over any other turtle in the same Playground.
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle toTop() {
    //  	this.getPlayground().toTop(this);
    synchronized (playground) {
      this.getPlayground().paintTurtles(this);
      return this;
    }
  }

  /** Set the pen width.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle penWidth(int newWidth) {
    synchronized (playground) {
      return penWidth((float)newWidth);
    }
  }

  /** Internal Method for setting the penWidth.
   */
  private Turtle penWidth(float newWidth) {
    setLineWidth(newWidth);
    return this;
  }

  /** Query the pen width.
   */
  public int penWidth(){
    return (int)this.getPen().getLineWidth();
  }

  /** Returns the current edge behaviour.

  @see #CLIP
  @see #WRAP
  */
  protected int getEdgeBehaviour() {
    return edgeBehaviour;
  }

  /** Sets the edge behaviour to the specified value;

  @see #CLIP
  @see #WRAP
  */
  protected void setEdgeBehaviour(int edgeBehaviour) {
    synchronized (playground) {
      this.edgeBehaviour = edgeBehaviour;
    }
  }

  /** Set the <code>Turtle</code> to clip-mode.
  @see #wrap()
  @see #clip()
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle clip() {
    synchronized (playground) {
      setEdgeBehaviour(CLIP);
      return this;
    }
  }

  /** Cause the <code>Turtle</code> to wrap around the playground borders.

  e.g when as the <code>Turtle</code> leaves the Window on one side,
  it reappears on the opposite side.

  @return a reference to the <code>Turtle</code> to allow chaining.
  @see #clip()
  */
  public Turtle wrap() {
    synchronized (playground) {
      setEdgeBehaviour(WRAP);
      return this;
    }
  }

  /** Tell whether the <code>Turtle</code> is in clip mode.

  @return true if in clip mode, false otherwise.
  @see #clip()
  @see #wrap()
  */
  public boolean isClip() {
    synchronized (playground) {
      return (getEdgeBehaviour() == CLIP);
    }
  }
  /** Tell wheter the <code>Turtle</code> is in wrap mode.

  @return true if in wrap mode, false otherwise.
  @see #wrap()
  @see #clip()
  */
  public boolean isWrap() {
    synchronized (playground) {
      return (getEdgeBehaviour() == WRAP);
    }
  }

  /** Fill the region the Turtle is in.<br>

  A region is bounded by lines
  of any color different to the pixel color at the current turtle position
  and by the border of the window. <br>
  (The pen of the Turtle must be down.)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle fill() {
    synchronized (playground) {
      getPlayground().fill(this, getPlayground().getPixelColor(this));
      return this;
    }
  }

  /** Fill the region as if the Turtle where at coordinates <code>x</code> and <code>y</code>.<br>
  A region is bounded by lines
  of any color different to the pixel color at the given position and by the border of
  the window. <br>
  (The pen of the Turtle must be down.)
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle fill(double x, double y) {
    synchronized (playground) {
      double oldX = getX();
      double oldY = getY();
      boolean hidden = isHidden();
      ht().setPos(x,y);
      getPlayground().fill(this, getPlayground().getPixelColor(this));
      setPos(oldX, oldY);
      if(!hidden){
        st();
      }
      return this;
    }
  }

  /**  Erase all traces and text painted by the turtles, but let all
   * turtles where they are.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle clean() {
    synchronized (playground) {
      getPlayground().clean();
      return this;
    }
  }

  /** Paint the specified text at the current turtle position.

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle label(String text) {
    synchronized (playground) {
      if (text == null || text.length()== 0)
        return this;
      getPlayground().label(text, this);
      return this;
    }
  }

  /** Set the current font as specified.

  @see java.awt.Font
  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setFont(Font font) {
    synchronized (playground) {
      getPen().setFont(font);
      return this;
    }
  }

  /** Change the current font to the specified one. If you want to know what fonts are available on your system,
      call #getAvailableFontFamilies() .

      @see #getAvailableFontFamilies
      @see java.awt.Font  more information about fontName, style and size.
  */
  public Turtle setFont(String fontName, int style, int size) {
    synchronized (playground) {
      getPen().setFont(new Font(fontName, style, size));
      return this;
    }
  }

  /** Set the font size.

      @see #setFontStyle  changing  the font style
      @see #setFont  changing  the whole font.
      @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setFontSize(int size) {
    synchronized (playground) {
      getPen().setFontSize(size);
      return this;
    }
  }

  /** Set the font style.

      This is either <code>java.awt.Font.PLAIN, java.awt.Font.BOLD, java.awt.Font.ITALIC</code> or <code>java.awt.Font.BOLD+java.awt.Font.ITALIC</code>
      @see #setFontSize  changing  the font size
      @see #setFont  changing  the whole font.
      @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle setFontStyle(int style) {
    synchronized (playground) {
      getPen().setFontStyle(style);
      return this;
    }
  }

  /** Provide information about all font families (e.g. roman) currently available on your system.
      Each font name is a string packed into a array of strings.
      (This might be useful when you want to change the font.)
      @see #setFont(java.awt.Font)
      @see #setFont(java.lang.String, int, int)
  */
   public static String[] getAvailableFontFamilies() {
     return Pen.getAvailableFontFamilies();
   }

  /** Return the current font.

      @see #setFontSize  changing  the font size
      @see #setFontStyle  changing  the font style
      @see #setFont  changing  the whole font.
  */
  public Font getFont() {
    return getPen().getFont();
  }

  /** Creates a clone of the Turtle in it's Playground.
      Color, position and heading are the same.
      (It's visible and pen is down.)
      Return the cloned object reference as a Object type, because
      it overrides Object.clone().
      (You may cast it to a Turtle.)
  */
  public Object clone()
  {
    synchronized (playground) {
      Turtle t = new Turtle(this, false);
      t.setColor(getColor());
      t.setPos(getX(), getY());
      t.heading(heading());
      t.showTurtle();
      return t;
    }
  }

  /**  Set antialiasing on or off for the turtle trace buffer
   * This may result in an better image quality, especially
   * for filling operations (platform dependant).

  @return a reference to the <code>Turtle</code> to allow chaining.
  */
  public Turtle antiAliasing(boolean on) {
    synchronized (playground) {
      getPlayground().setAntiAliasing(on);
      return this;
    }
  }


  /**
   * Print the graphics context to an attached printer with
   * the given magnification scale factor.
   * scale = 1 will print on standard A4 format paper.<br>
   *
   * The given tp must implement the GPrintable interface,
   * e.g. the single method void draw(), where all the
   * drawing into the GPanel must occur.
   *
   * Be aware the turtle(s) state (position, direction, etc.)
   * must be reinitialized, because draw() is called several
   * times by the printing system.
   *
   * A standard printer dialog is shown before printing is
   * started. Only turte traces are printed.
   * <br>
   * Example:<br>
   * <code>
import ch.aplu.turtle.*;<br>
<br>
public class PrintTest implements TPrintable<br>
{<br>
  private Turtle t = new Turtle();<br>
<br>
  public PrintTest()<br>
  {<br>
    draw();           // Draw on screen<br>
    t.print(this);    // Draw on printer<br>
  }<br>
<br>
  public void draw()<br>
  {<br>
    t.home();   // Needed for initialization<br>
    for (int i = 0; i < 5; i++)<br>
      t.fd(20).rt(90).fd(20).lt(90);<br>
  }<br>
<br>
  public static void main(String[] args)<br>
  {<br>
    new PrintTest();<br>
  }<br>
}<br>
   </code>
   */
  public boolean print(TPrintable tp, double scale)
  {
    return getPlayground().print(tp, scale);
  }

  /**
   * Same as print(tp, scale) with scale = 1.
   */
  public boolean print(TPrintable tp)
  {
    return print(tp, 1);
  }

  /**
   * Print the Turtle's current playground with given scale.
   */
  public boolean printScreen(double scale)
  {
    return print(null, scale);
  }

  /**
   * Same as printScreen(scale) with scale = 1.
   */
  public boolean printScreen()
  {
    return printScreen(1);
  }

  /**
   * Clear the Turtle's playground. All turtle images and traces are erased,
   * but the turtles remain (invisible) at their positions.
   */
  public void clear()
  {
    synchronized (playground) {
      getPlayground().clear();
    }
  }

 /**
  * Delay execution for the given amount of time ( in ms ).
  */
  public static void sleep(int time)
  {
    /*try
    {
      Thread.currentThread().sleep(time);
    }
    catch (Exception e) {}*/
  }

  /** Set the title of Turtle's playground
   */
  public void setTitle(final String text) {
    if (EventQueue.isDispatchThread())
      turtleFrame.setTitle(text);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
            turtleFrame.setTitle(text);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  /**
   * Return version information
   */
  public String version()
  {
    return SharedConstants.VERSION;
  }

  /**
   * Enable/disable automatic repainting.
   * If disabled, call repaint() to perform repainting.
   * Disabling automatic repainting and hiding the
   * <code>Turtle</code> speeds up the graphics enormously.
   */
  public void enableRepaint(boolean b)
  {
    getPlayground().enableRepaint(b);
  }

  /**
   * Perform manual repainting when automatic repainting is
   * disabled.
   */
  public void repaint()
  {
    getPlayground().repaint();
  }

  /**
   * Return the color of the pixel at the current turtle position.
   */
  public Color getPixelColor()
  {
    synchronized (playground) {
      return getPlayground().getPixelColor(this);
    }
  }

}



















