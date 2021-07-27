package hsa2;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.ImageObserver;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is a re-implementation of the old hsa console by Holt Software Associates.
 * Re-done from scratch in Swing with much code imported from the old hsa console.
 * The main goals were to reduce screen flicker during animations and eliminate a 
 * couple of small bugs in the input routines. April 30, 2010.
 * <p>
 * Differences from hsa console:
 *   - Row &amp; Column for text start at 0, 0
 *   - When creating console, specify width, height in pixels (not rows, columns)
 *   - setColor sets the drawing color for print and println as well as graphics
 *   - setTextBackgroundColor no longer works. Use setBackgroundColor instead.
 *   - use getDrawHeight() and getDrawWidth() for screen size in pixels
 *   - no more readString. Use readToken or readLine instead.
 *   - no more readChar. Use getChar instead.
 *   - methods added to poll the keyboard state without pausing the program (ketKeyChar, 
 *   getKeyCode, getLastKeyChar, etc.). Good for live-action games.
 *   - dropped support for print, quit, save buttons.
 *   - fixed drawImage to be more reliable
 *   - added mouse listener code
 * <p>
 * @author Tom West (old hsa code)
 * @author Sam Scott
 * @author Josh Gray (mouse code) 
 * @author Michael Harwood (setStroke, antiAlias, updated dialogs to JOptionPane, drawimage can do sprites)
 * @version 4.5
 */
public class GraphicsConsole extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {

	// Constants for setting up the window 
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_HEIGHT = 500;
	private static final int DEFAULT_WIDTH = 650;
	private static final int DEFAULT_FONTSIZE = 12;
	private static final String DEFAULT_NAME = "HSA2 Graphics Console";

	// The main drawing surface
	private ConsoleCanvas canvas;

	// Constants for the getKey methods
	/** Code for the ALT key **/
	public static final int VK_ALT = KeyEvent.VK_ALT;
	/** Code for the BACKSPACE key **/
	public static final int VK_BACK_SPACE = KeyEvent.VK_BACK_SPACE;
	/** Code for the CAPSLOCK key **/
	public static final int VK_CAPS_LOCK = KeyEvent.VK_CAPS_LOCK;
	/** Code for the CTRL key **/
	public static final int VK_CONTROL = KeyEvent.VK_CONTROL;
	/** Code for the DELETE key **/
	public static final int VK_DELETE = KeyEvent.VK_DELETE;
	/** Code for the DOWN arrow **/
	public static final int VK_DOWN = KeyEvent.VK_DOWN;
	/** Code for the END key **/
	public static final int VK_END = KeyEvent.VK_END;
	/** Code for the ENTER key **/
	public static final int VK_ENTER = KeyEvent.VK_ENTER;
	/** Code for the ESCAPE key **/
	public static final int VK_ESCAPE = KeyEvent.VK_ESCAPE;
	/** Code for the F1 key **/
	public static final int VK_F1 = KeyEvent.VK_F1;
	/** Code for the F2 key **/
	public static final int VK_F2 = KeyEvent.VK_F2;
	/** Code for the F3 key **/
	public static final int VK_F3 = KeyEvent.VK_F3;
	/** Code for the F4 key **/
	public static final int VK_F4 = KeyEvent.VK_F4;
	/** Code for the F5 key **/
	public static final int VK_F5 = KeyEvent.VK_F5;
	/** Code for the F6 key **/
	public static final int VK_F6 = KeyEvent.VK_F6;
	/** Code for the F7 key **/
	public static final int VK_F7 = KeyEvent.VK_F7;
	/** Code for the F8 key **/
	public static final int VK_F8 = KeyEvent.VK_F8;
	/** Code for the F9 key **/
	public static final int VK_F9 = KeyEvent.VK_F9;
	/** Code for the F10 key **/
	public static final int VK_F10 = KeyEvent.VK_F10;
	/** Code for the F11 key **/
	public static final int VK_F11 = KeyEvent.VK_F11;
	/** Code for the F12 key **/
	public static final int VK_F12 = KeyEvent.VK_F12;
	/** Code for the HOME key **/
	public static final int VK_HOME = KeyEvent.VK_HOME;
	/** Code for the LEFT arrow **/
	public static final int VK_LEFT = KeyEvent.VK_LEFT;
	/** Code for the NUMLOCK key **/
	public static final int VK_NUM_LOCK = KeyEvent.VK_NUM_LOCK;
	/** Code for the NUMPAD 0 key **/
	public static final int VK_NUMPAD0 = KeyEvent.VK_NUMPAD0;
	/** Code for the NUMPAD 1 key **/
	public static final int VK_NUMPAD1 = KeyEvent.VK_NUMPAD1;
	/** Code for the NUMPAD 2 key **/
	public static final int VK_NUMPAD2 = KeyEvent.VK_NUMPAD2;
	/** Code for the NUMPAD 3 key **/
	public static final int VK_NUMPAD3 = KeyEvent.VK_NUMPAD3;
	/** Code for the NUMPAD 4 key **/
	public static final int VK_NUMPAD4 = KeyEvent.VK_NUMPAD4;
	/** Code for the NUMPAD 5 key **/
	public static final int VK_NUMPAD5 = KeyEvent.VK_NUMPAD5;
	/** Code for the NUMPAD 6 key **/
	public static final int VK_NUMPAD6 = KeyEvent.VK_NUMPAD6;
	/** Code for the NUMPAD 7 key **/
	public static final int VK_NUMPAD7 = KeyEvent.VK_NUMPAD7;
	/** Code for the NUMPAD 8 key **/
	public static final int VK_NUMPAD8 = KeyEvent.VK_NUMPAD8;
	/** Code for the NUMPAD 9 key **/
	public static final int VK_NUMPAD9 = KeyEvent.VK_NUMPAD9;
	/** Code for the PAGEDOWN key **/
	public static final int VK_PAGE_DOWN = KeyEvent.VK_PAGE_DOWN;
	/** Code for the PAGEUP key **/
	public static final int VK_PAGE_UP = KeyEvent.VK_PAGE_UP;
	/** Code for the PAUSE key **/
	public static final int VK_PAUSE = KeyEvent.VK_PAUSE;
	/** Code for the PRINTSCREEN key **/
	public static final int VK_PRINTSCREEN = KeyEvent.VK_PRINTSCREEN;
	/** Code for the RIGHT arrow **/
	public static final int VK_RIGHT = KeyEvent.VK_RIGHT;
	/** Code for the SHIFT key **/
	public static final int VK_SHIFT = KeyEvent.VK_SHIFT;
	/** Code for the TAB key **/
	public static final int VK_TAB = KeyEvent.VK_TAB;
	/** Special code for no key (e.g. getKeyCode returns this if no key currently held down) **/
	public static final int VK_UNDEFINED = KeyEvent.VK_UNDEFINED;
	/** Code for the UP arrow **/
	public static final int VK_UP = KeyEvent.VK_UP;

	// Mouse Variables

	private boolean mouseButton[] = { false, false, false };
	private int mouseX = 0, mouseY = 0, mouseClick = 0, mouseWheelRotation = 0, mouseWheelUnitsToScroll = 0;
	private boolean mouseDrag = false;
	private Point startDrag, endDrag;

	// ****************
	// *** CONSTRUCTORS
	// ****************

	/** Creates default GraphicsConsole **/
	public GraphicsConsole() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FONTSIZE, DEFAULT_NAME);
	}
	/** Creates GraphicsConsole with specified font size
	 * @param fontSize Default font size for println and print **/
	public GraphicsConsole(int fontSize) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fontSize, DEFAULT_NAME);
	}
	/** Creates GraphicsConsole with specified window name 
	 * @param name GraphicsConsole window name **/
	public GraphicsConsole(String name) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FONTSIZE, name);
	}
	/**  Creates GraphicsConsole with specified window width and height
	 * @param width GraphicsConsole width in pixels
	 * @param height GraphicsConsole height in pixels
	 */
	public GraphicsConsole(int width, int height) {
		this(width, height, DEFAULT_FONTSIZE, DEFAULT_NAME);
	}
	/** Creates GraphicsConsole with specified window width, height, and font size
	 * @param width GraphicsConsole width in pixels
	 * @param height GraphicsConsole height in pixels
	 * @param fontSize Default font size for println and print 
	 */
	public GraphicsConsole(int width, int height, int fontSize) {
		this(width, height, fontSize, DEFAULT_NAME);
	}
	/** Creates GraphicsConsole with specified window width, height, and name
	 * @param width GraphicsConsole width in pixels
	 * @param height GraphicsConsole height in pixels
	 * @param name GraphicsConsole window name
	 */
	public GraphicsConsole(int width, int height, String name) {
		this(width, height, DEFAULT_FONTSIZE, name);
	}

	/** Creates GraphicsConsole with specified window width, height, font size, and name (title)
	 * invokeAnd Wait() is used to make HSA2 thread safe, since Timers can be used to update graphics.
	 * and invokeLater() does not return fast enough, and so causes null pointer errors for gc.methods
	 * 
	 * @param width GraphicsConsole width in pixels
	 * @param height GraphicsConsole height in pixels
	 * @param fontSize Default font size for println and print 
	 * @param name GraphicsConsole window name
	 */
	public GraphicsConsole(int width, int height, int fontSize, String name) {		 
		super(name);
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					makeGUI(width, height, fontSize, name);
				}
			});
		} catch (Exception e) {
/* this only works in Java 8++
		} catch (InvocationTargetException | InterruptedException e) {
*/
			e.printStackTrace();
		}	
	}

	// *************************************
	// **** PUBLIC GRAPHIC OUTPUT METHODS
	// *************************************
	/**
	 * @return Width of drawing area in pixels
	 */
	public int getDrawWidth() {
		return canvas.getWidth(); //JComponent.getWidth()
	}
	/**
	 * @return Height of drawing area in pixels
	 */
	public int getDrawHeight() { 
		return canvas.getHeight(); //JComponent.getWidth()
	}
	/**
	 * Clears the drawing area to the current background color
	 */
	public void clear() {
		canvas.clear();
	}
	/**
	 * Clears a rectangle on the screen
	 * @param x Top left corner x coordinate
	 * @param y Top left corner y coordinate
	 * @param width Width of area to clear
	 * @param height Height of area to clear
	 */
	public void clearRect (int x, int y, int width, int height) {
		canvas.clearRect (x, y, width, height);
	} 
	/**
	 * Use this method to copy a rectangular area of the screen to a new location
	 * @param x Top left corner x coordinate
	 * @param y Top left corner y coordinate
	 * @param width Width of area to copy
	 * @param height Height of area to copy
	 * @param delta_x Will be copied at x = x + delta_x
	 * @param delta_y Will be copied at y = y + delta_y
	 */
	public void copyArea (int x, int y, int width, int height, int delta_x, int delta_y) {
		canvas.copyArea (x, y, width, height, delta_x, delta_y);
	} 
	/** Set the graphics (foreground) colour
	 * @param c New drawing color for graphics and text
	 */
	public void setColor(Color c) {
		canvas.setColor(c);
	}
	/**
	 * NOTE: This command only sets the background color. Nothing will change on the drawing
	 * surface until you use clearRect() or clear()
	 * @param c New background color
	 */
	public void setBackgroundColor(Color c) {
		canvas.setBackgroundColor(c);
	}
	/**
	 * Sets default painting mode (see setXORMode)
	 */
	public void setPaintMode() {
		canvas.setPaintMode();
	}
	/**
	 * Sets XOR painting mode. In this mode, every pixel drawn to the screen will be combined
	 * with the pixel already on the screen to create an "outline" effect of what was beneath
	 * it. In this mode, if you draw the same object twice in the same location, it will appear 
	 * and then disappear. See also setPaintMode().
	 * 
	 * @param c Color to use as the background color for computation of new color
	 */
	public void setXORMode(Color c) {
		canvas.setXORMode(c);
	}
	/**
	 * Draws a filled rectangle on the drawing area.
	 * @param x Top left X coordinate
	 * @param y Top left Y coordinate
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void fillRect(int x, int y, int width, int height)  {
		canvas.fillRect(x, y, width, height);
	}
	/**
	 * Draws a rectangle outline on the drawing area.
	 * Stroke width can be set using setStroke().
	 * @param x Top left X coordinate
	 * @param y Top left Y coordinate
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void drawRect(int x, int y, int width, int height) {
		canvas.drawRect(x, y, width, height);
	}
	/**
	 * Draws a filled in oval on the drawing area, inscribed within a rectangle.
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void fillOval(int x, int y, int width, int height) {
		canvas.fillOval(x, y, width, height);
	}
	/**
	 * Draws an oval outline on the drawing area, inscribed within a rectangle.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void drawOval(int x, int y, int width, int height) {
		canvas.drawOval(x, y, width, height);
	}
	/**
	 * Draws a straight line on the drawing area.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x1 Starting x coordinate
	 * @param y1 Starting y coordinate
	 * @param x2 Ending x coordinate
	 * @param y2 Ending y coordinate
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		canvas.drawLine(x1, y1, x2, y2);
	} 

	/**
	 * Draws a polygon outline on the drawing area.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x An array of x coordinates for the corner points of the polygon (related to array y)
	 * @param y An array of y coordinates for the corner points of the polygon (related to array x)
	 * @param n Number of points in the polygon
	 */
	public void drawPolygon(int[] x, int[] y, int n) {
		canvas.drawPolygon(x, y, n);
	}
	/**
	 * Draws a polygon outline on the drawing area.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param p The Polygon to draw.
	 */
	public void drawPolygon(Polygon p) {
		canvas.drawPolygon(p);
	}

	/**
	 * Draws a filled in polygon on the drawing area.
	 * Stroke width can be set using setStroke().
	 * @param x An array of x coordinates for the corner points of the polygon (related to array y)
	 * @param y An array of y coordinates for the corner points of the polygon (related to array x)
	 * @param n Number of points in the polygon
	 */
	public void fillPolygon(int[] x, int[] y, int n) {
		canvas.fillPolygon(x, y, n);
	}
	/**
	 * Draws a filled in polygon on the drawing area.
	 * Stroke width can be set using setStroke().
	 * @param p The Polygon to draw.
	 */
	public void fillPolygon(Polygon p) {
		canvas.fillPolygon(p);
	}

	/**
	 * Draws an arc outline on the drawing area. The arc will be inscribed within a rectangle, just
	 * like drawOval(), but only part of the oval will be drawn.
	 * Draws an oval outline on the drawing area, inscribed within a rectangle.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width of rectangle in pixels
	 * @param height Height of rectangle in pixels
	 * @param startAngle Start angle of arc in degrees (0 = right middle, 90 = top middle, etc.)
	 * @param arcAngle Number of degrees of arc to draw
	 */
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		canvas.drawArc(x, y, width, height, startAngle, arcAngle);
	}
	/**
	 * Draws a filled in arc outline on the drawing area (looks like a pie slice). The arc will be 
	 * inscribed within a rectangle, just like drawOval(), but only part of the oval will be drawn.
	 * Draws an oval outline on the drawing area, inscribed within a rectangle.
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width of rectangle in pixels
	 * @param height Height of rectangle in pixels
	 * @param startAngle Start angle of arc in degrees (0 = right middle, 90 = top middle, etc.)
	 * @param arcAngle Number of degrees of arc to draw
	 */
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		canvas.fillArc(x, y, width, height, startAngle, arcAngle);
	} 
	/**
	 * Draws a rectangle outline on the drawing area with rounded corners.
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param xRadius Horizontal radius of arc for corners
	 * @param yRadius Vertical radius of arc for corners
	 */
	public void drawRoundRect(int x, int y, int width, int height, int xRadius, int yRadius) {
		canvas.drawRoundRect(x, y, width, height, xRadius, yRadius);
	}
	/**
	 * Draws a filled in rectangle on the drawing area with rounded corners.
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param xRadius Horizontal radius of arc for corners
	 * @param yRadius Vertical radius of arc for corners
	 */
	public void fillRoundRect(int x, int y, int width, int height, int xRadius, int yRadius) {
		canvas.fillRoundRect(x, y, width, height, xRadius, yRadius);
	}  
	/**
	 * Draws a rectangle outline on the drawing area with a 3D effect.
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param raised Draw rectangle as raised or sunk in
	 */
	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		canvas.draw3DRect(x, y, width, height, raised);
	}
	/**
	 * Draws a filled in rectangle on the drawing area with a 3D effect.
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param raised Draw rectangle as raised or sunk in
	 */
	public void fill3DRect(int x, int y, int width, int height, boolean raised) {
		canvas.fill3DRect(x, y, width, height, raised);
	}
	/**
	 * Draws a string on the drawing area using the current font and color.
	 * Note that the coordinates specify the bottom left corner rather than the
	 * top left corner for drawing.
	 * AntiAliasing can be set using setAntiAlias()
	 * @param str The string to draw. 
	 * @param x Bottom left X coordinate
	 * @param y Bottom left Y coordinate
	 */
	public void drawString(String str, int x, int y) {
		canvas.drawString(str, x, y);
	}
	/**
	 * Draws specified image on the drawing area. Note that if the image takes a while
	 * to load, this method will delay until it is loaded, timing out after 1000 ms.
	 * @param img The image to draw
	 * @param x Top left x coordinate
	 * @param y Top left y coordinate
	 */
	public void drawImage(Image img, int x, int y) {
		canvas.drawImage(img, x, y);
	}
	/**
	 * Draws specified image on the drawing area. Note that if the image takes a while
	 * to load, this method will delay until it is loaded, timing out after 1000 ms.
	 * @param img The image to draw
	 * @param x Top left x coordinate
	 * @param y Top left y coordinate
	 * @param width Compress or stretch image to this width
	 * @param height Compress or stretch image to this width
	 */
	public void drawImage(Image img, int x, int y, int width, int height) {
		canvas.drawImage(img, x, y, width, height);
	}

    /**
	 * Draws specified image on the drawing area. Note that if the image takes a while
	 * to load, this method will delay until it is loaded, timing out after 1000 ms.
	 * The subimage is scaled and flipped as needed according to the s and d parameters below
     * @param       img the specified image to be drawn. This method does nothing if <code>img</code> is null.
     * @param       dx1 the <i>x</i> coordinate of the first corner of the destination rectangle.
     * @param       dy1 the <i>y</i> coordinate of the first corner of the destination rectangle.
     * @param       dx2 the <i>x</i> coordinate of the second corner of the destination rectangle.
     * @param       dy2 the <i>y</i> coordinate of the second corner of the destination rectangle.
     * @param       sx1 the <i>x</i> coordinate of the first corner of the source rectangle.
     * @param       sy1 the <i>y</i> coordinate of the first corner of the source rectangle.
     * @param       sx2 the <i>x</i> coordinate of the second corner of the source rectangle.
     * @param       sy2 the <i>y</i> coordinate of the second corner of the source rectangle.
     * @param       observer object. It is STRONGLY recommended to set this to NULL when you call this method.
     * 					  There is no guarantee that the image observer will work as intended. It has not been tested with HSA2.
     * @return   <code>false</code> if the image pixels are still changing;
     *           <code>true</code> otherwise.
	 */
	public void drawImage(Image img,
		int dx1,int dy1,int dx2,int dy2,
		int sx1,int sy1,int sx2,int sy2,ImageObserver observer) {
			canvas.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);		
	}

	/**
	 * Sets the font for drawString (not for print or println)
	 * v4.3 Only sets the font if the font has changed (because setting fonts slows graphics down a lot).
	 * @param f The new font
	 */
	public void setFont(Font f) {
		Font oldFont = canvas.getFont();
		if (oldFont.equals(f)) return;
		
		//MH: Is this necessary?
		//super.setFont(f);	//set the JFrame font
				
		canvas.setFont(f);	//set the JPanel font
		
	}
	/**
	 * This sets the stroke size for drawLine ONLY
	 * @param strokeSize in pixels 
	 */
	public void setStroke(int strokeSize) {
		canvas.setStroke(strokeSize);
	}
	/**
	 * This turns antialiasing on or off.
     * Antialiasing makes likes look a lot smoother, so one normally wants this turned on.
     * Example: gc.setAntiAlias(true);
	 * @param onOff set to TRUE or FALSE 
	 */
	public void setAntiAlias(boolean onOff) {
		canvas.setAntiAlias(onOff);
	}
	/**
	 * This allows the GraphicsConsole JFrame to be resized
	 * @param resizeOn set to TRUE or FALSE 
	 * There is a bug in Java that often adds a 10px border to the right and bottom sides when JFrame.setResizable() is run
	 * see http://stackoverflow.com/questions/11225144/java-setresizablefalse-changes-the-window-size-swing
	 * So try to avoid running it unncessarily. */
	public void setResizable(boolean resizeOn) {
		if (resizeOn) {
			super.setResizable(true);
			canvas.addComponentListener(this);
		} else {
			if (super.isResizable()) super.setResizable(false);
			//canvas.removeComponentListener(this); //NO. Null pointer error!
		}
	}

	/**
	 * Draws a star outline on the screen from (x, y) to (x + width, y + width). Adapted from hsa.
	 *
	 * @param x
	 *            The x coordinate of the top left corner of the rectangle that the star is inscribed in.
	 * @param y
	 *            The y coordinate of the top left corner of the rectangle that the star is inscribed in.
	 * @param width
	 *            The width of the rectangle that the star is inscribed in.
	 * @param height
	 *            The height of the rectangle that the star is inscribed in.
	 */
	public void drawStar (int x, int y, int width, int height) {
		int[] xPoints, yPoints;
		float rx, ry, xc, yc;

		rx = width;
		ry = height;
		xc = x + rx / 2;
		yc = y + height;

		xPoints = new int [11];
		yPoints = new int [11];
		xPoints [0] = (int) xc;
		yPoints [0] = (int) (yc - ry);
		xPoints [1] = (int) (xc + rx * 0.118034);
		yPoints [1] = (int) (yc - ry * 0.618560);
		xPoints [2] = (int) (xc + rx * 0.500000);
		yPoints [2] = yPoints [1];
		xPoints [3] = (int) (xc + rx * 0.190983);
		yPoints [3] = (int) (yc - ry * 0.381759);
		xPoints [4] = (int) (xc + rx * 0.309017);
		yPoints [4] = (int) yc;
		xPoints [5] = (int) xc;
		yPoints [5] = (int) (yc - ry * 0.236068);
		xPoints [6] = (int) (xc - rx * 0.309017);
		yPoints [6] = yPoints [4];
		xPoints [7] = (int) (xc - rx * 0.190983);
		yPoints [7] = yPoints [3];
		xPoints [8] = (int) (xc - rx * 0.500000);
		yPoints [8] = yPoints [2];
		xPoints [9] = (int) (xc - rx * 0.118034);
		yPoints [9] = yPoints [1];
		xPoints [10] = xPoints [0];
		yPoints [10] = yPoints [0];
		canvas.drawPolygon (xPoints, yPoints, 11);
	} 
	/**
	 * Draws a filled star on the screen from (x, y) to (x + width, y + width). Adapted from hsa.
	 *
	 * @param x
	 *            The x coordinate of the top left corner of the rectangle that the star is inscribed in.
	 * @param y
	 *            The y coordinate of the top left corner of the rectangle that the star is inscribed in.
	 * @param width
	 *            The width of the rectangle that the star is inscribed in.
	 * @param height
	 *            The height of the rectangle that the star is inscribed in.
	 */
	public void fillStar (int x, int y, int width, int height) {
		int[] xPoints, yPoints;
		float rx, ry, xc, yc;

		rx = width;
		ry = height;
		xc = x + rx / 2;
		yc = y + height;

		xPoints = new int [11];
		yPoints = new int [11];
		xPoints [0] = (int) xc;
		yPoints [0] = (int) (yc - ry);
		xPoints [1] = (int) (xc + rx * 0.118034);
		yPoints [1] = (int) (yc - ry * 0.618560);
		xPoints [2] = (int) (xc + rx * 0.500000);
		yPoints [2] = yPoints [1];
		xPoints [3] = (int) (xc + rx * 0.190983);
		yPoints [3] = (int) (yc - ry * 0.381759);
		xPoints [4] = (int) (xc + rx * 0.309017);
		yPoints [4] = (int) yc;
		xPoints [5] = (int) xc;
		yPoints [5] = (int) (yc - ry * 0.236068);
		xPoints [6] = (int) (xc - rx * 0.309017);
		yPoints [6] = yPoints [4];
		xPoints [7] = (int) (xc - rx * 0.190983);
		yPoints [7] = yPoints [3];
		xPoints [8] = (int) (xc - rx * 0.500000);
		yPoints [8] = yPoints [2];
		xPoints [9] = (int) (xc - rx * 0.118034);
		yPoints [9] = yPoints [1];
		xPoints [10] = xPoints [0];
		yPoints [10] = yPoints [0];
		canvas.fillPolygon (xPoints, yPoints, 11);
	}

	/**
	 * Draws a maple leaf outline on the screen from (x, y) to (x + width, y + width).
	 * Adapted from hsa.
	 *
	 * @param x
	 *            The x coordinate of the top left corner of the rectangle that
	 *            the maple leaf is inscribed in.
	 * @param y
	 *            The y coordinate of the top left corner of the rectangle that
	 *            the maple leaf is inscribed in.
	 * @param width
	 *            The width of the rectangle that the maple leaf is inscribed
	 *            in.
	 * @param height
	 *            The height of the rectangle that the maple leaf is inscribed
	 *            in.
	 */
	public void drawMapleLeaf (int x, int y, int width, int height) {
		int[] xPoints, yPoints;
		float rx, ry, xc, yc;

		rx = width;
		ry = height;
		xc = x + rx / 2;
		yc = y + height;

		xPoints = new int [26];
		yPoints = new int [26];
		xPoints [0] = (int) (xc + rx * 0.021423);
		yPoints [0] = (int) (yc - ry * 0.215686);
		xPoints [1] = (int) (xc + rx * 0.270780);
		yPoints [1] = (int) (yc - ry * 0.203804);
		xPoints [2] = (int) (xc + rx * 0.271820);
		yPoints [2] = (int) (yc - ry * 0.295752);
		xPoints [3] = (int) (xc + rx * 0.482015);
		yPoints [3] = (int) (yc - ry * 0.411765);
		xPoints [4] = (int) (xc + rx * 0.443046);
		yPoints [4] = (int) (yc - ry * 0.483267);
		xPoints [5] = (int) (xc + rx * 0.500000);
		yPoints [5] = (int) (yc - ry * 0.587435);
		xPoints [6] = (int) (xc + rx * 0.363353);
		yPoints [6] = (int) (yc - ry * 0.619576);
		xPoints [7] = (int) (xc + rx * 0.342287);
		yPoints [7] = (int) (yc - ry * 0.693849);
		xPoints [8] = (int) (xc + rx * 0.153596);
		yPoints [8] = (int) (yc - ry * 0.612537);
		xPoints [9] = (int) (xc + rx * 0.201601);
		yPoints [9] = (int) (yc - ry * 0.918462);
		xPoints [10] = (int) (xc + rx * 0.093001);
		yPoints [10] = (int) (yc - ry * 0.894514);
		xPoints [11] = (int) xc;
		yPoints [11] = (int) (yc - ry);
		xPoints [12] = (int) (xc - rx * 0.093001);
		yPoints [12] = yPoints [10];
		xPoints [13] = (int) (xc - rx * 0.201601);
		yPoints [13] = yPoints [9];
		xPoints [14] = (int) (xc - rx * 0.153596);
		yPoints [14] = yPoints [8];
		xPoints [15] = (int) (xc - rx * 0.342287);
		yPoints [15] = yPoints [7];
		xPoints [16] = (int) (xc - rx * 0.363353);
		yPoints [16] = yPoints [6];
		xPoints [17] = (int) (xc - rx * 0.500000);
		yPoints [17] = yPoints [5];
		xPoints [18] = (int) (xc - rx * 0.443046);
		yPoints [18] = yPoints [4];
		xPoints [19] = (int) (xc - rx * 0.482015);
		yPoints [19] = yPoints [3];
		xPoints [20] = (int) (xc - rx * 0.271820);
		yPoints [20] = yPoints [2];
		xPoints [21] = (int) (xc - rx * .2707796);
		yPoints [21] = yPoints [1];
		xPoints [22] = (int) (xc - rx * 0.021423);
		yPoints [22] = yPoints [0];
		xPoints [23] = xPoints [22];
		yPoints [23] = (int) yc;
		xPoints [24] = xPoints [0];
		yPoints [24] = yPoints [23];
		xPoints [25] = xPoints [0];
		yPoints [25] = yPoints [0];
		canvas.drawPolygon (xPoints, yPoints, 26);
	} 

	/**
	 * Draws a filled maple leaf on the screen from (x, y) to (x + width, y +
	 * width). Adapted from hsa.
	 *
	 * @param x
	 *            int The x coordinate of the top left corner of the rectangle
	 *            that the maple leaf is inscribed in.
	 * @param y
	 *            int The y coordinate of the top left corner of the rectangle
	 *            that the maple leaf is inscribed in.
	 * @param width
	 *            int The width of the rectangle that the maple leaf is
	 *            inscribed in.
	 * @param height
	 *            int The height of the rectangle that the maple leaf is
	 *            inscribed in.
	 */
	public void fillMapleLeaf (int x, int y, int width, int height) {
		int[] xPoints, yPoints;
		float rx, ry, xc, yc;

		rx = width;
		ry = height;
		xc = x + rx / 2;
		yc = y + height;

		xPoints = new int [26];
		yPoints = new int [26];
		xPoints [0] = (int) (xc + rx * 0.021423);
		yPoints [0] = (int) (yc - ry * 0.215686);
		xPoints [1] = (int) (xc + rx * 0.270780);
		yPoints [1] = (int) (yc - ry * 0.203804);
		xPoints [2] = (int) (xc + rx * 0.271820);
		yPoints [2] = (int) (yc - ry * 0.295752);
		xPoints [3] = (int) (xc + rx * 0.482015);
		yPoints [3] = (int) (yc - ry * 0.411765);
		xPoints [4] = (int) (xc + rx * 0.443046);
		yPoints [4] = (int) (yc - ry * 0.483267);
		xPoints [5] = (int) (xc + rx * 0.500000);
		yPoints [5] = (int) (yc - ry * 0.587435);
		xPoints [6] = (int) (xc + rx * 0.363353);
		yPoints [6] = (int) (yc - ry * 0.619576);
		xPoints [7] = (int) (xc + rx * 0.342287);
		yPoints [7] = (int) (yc - ry * 0.693849);
		xPoints [8] = (int) (xc + rx * 0.153596);
		yPoints [8] = (int) (yc - ry * 0.612537);
		xPoints [9] = (int) (xc + rx * 0.201601);
		yPoints [9] = (int) (yc - ry * 0.918462);
		xPoints [10] = (int) (xc + rx * 0.093001);
		yPoints [10] = (int) (yc - ry * 0.894514);
		xPoints [11] = (int) xc;
		yPoints [11] = (int) (yc - ry);
		xPoints [12] = (int) (xc - rx * 0.093001);
		yPoints [12] = yPoints [10];
		xPoints [13] = (int) (xc - rx * 0.201601);
		yPoints [13] = yPoints [9];
		xPoints [14] = (int) (xc - rx * 0.153596);
		yPoints [14] = yPoints [8];
		xPoints [15] = (int) (xc - rx * 0.342287);
		yPoints [15] = yPoints [7];
		xPoints [16] = (int) (xc - rx * 0.363353);
		yPoints [16] = yPoints [6];
		xPoints [17] = (int) (xc - rx * 0.500000);
		yPoints [17] = yPoints [5];
		xPoints [18] = (int) (xc - rx * 0.443046);
		yPoints [18] = yPoints [4];
		xPoints [19] = (int) (xc - rx * 0.482015);
		yPoints [19] = yPoints [3];
		xPoints [20] = (int) (xc - rx * 0.271820);
		yPoints [20] = yPoints [2];
		xPoints [21] = (int) (xc - rx * .2707796);
		yPoints [21] = yPoints [1];
		xPoints [22] = (int) (xc - rx * 0.021423);
		yPoints [22] = yPoints [0];
		xPoints [23] = xPoints [22];
		yPoints [23] = (int) yc;
		xPoints [24] = xPoints [0];
		yPoints [24] = yPoints [23];
		xPoints [25] = xPoints [0];
		yPoints [25] = yPoints [0];
		canvas.fillPolygon (xPoints, yPoints, 26);
	} 

	// *****************************
	// *** TEXT OUTPUT METHODS
	// *****************************

	/**
	 * Places the cursor for input methods, print, and println.
	 * @param row Row number (starts at 0)
	 * @param col Column number (starts at 0)
	 */
	public void setCursor(int row, int col) {
		canvas.setCursor(row, col);
	}
	/**
	 * @return The current cursor column
	 */
	public int getColumn() {
		return canvas.getCurrentColumn();
	}
	/**
	 * @return The current cursor row
	 */
	public int getRow() {
		return canvas.getCurrentRow();
	}
	/**
	 * @return The total number of columns for print and println
	 */
	public int getNumColumns() {
		return canvas.getNumColumns();
	}
	/**
	 * @return The total number of rows for print and println
	 */
	public int getNumRows() {
		return canvas.getNumRows();
	}
	/**
	 * Writes a newline to the GraphicsConsole. Adapted from hsa.
	 */
	public void println () {
		print ("\n");
	} 
	/**
	 * Writes the text representation of an 8-bit integer (a "byte") to
	 * the GraphicsConsole followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void println (byte number) {
		print (number);
		print ("\n");
	} 
	/**
	 * Writes the text representation of an 8-bit integer (a "byte")
	 * to the GraphicsConsole with a specified field size followed by a newline.
	 * Adapted from hsa.
	 * 
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (byte number, int fieldSize) {
		print (number, fieldSize);
		print ("\n");
	} 
	/**
	 * Writes a character to the GraphicsConsole followed by a newline. Adapted from hsa.
	 *
	 * @param ch The character to be written to the GraphicsConsole.
	 */
	public void println (char ch) {
		print (ch);
		print ("\n");
	}
	/**
	 * Writes a character to the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param ch The character to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the character is to be written in.
	 */
	public void println (char ch, int fieldSize) {
		print (ch, fieldSize);
		print ("\n");
	}
	/**
	 * Writes a double precision floating point number (a "double") to
	 * the GraphicsConsole followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void println (double number) {
		print (number);
		print ("\n");
	}
	/**
	 * Writes a double precision floating point number (a "double") to
	 * the GraphicsConsole with a specified field size followed by a newline.
	 * Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (double number, int fieldSize) {
		print (number, fieldSize);
		print ("\n");
	}
	/**
	 * Writes a double precision floating point number (a "double") to
	 * the GraphicsConsole with a specified field size and a specified number of
	 * decimal places followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void println (double number, int fieldSize, int decimalPlaces) {
		print (number, fieldSize, decimalPlaces);
		print ("\n");
	}
	/**
	 * Writes a floating point number (a "float") to the GraphicsConsole followed by
	 * a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void println (float number) {
		print (number);
		print ("\n");
	}
	/**
	 * Writes a floating point number (a "float") to the GraphicsConsole with a
	 * specified field size followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (float number, int fieldSize) {
		print (number, fieldSize);
		print ("\n");
	}
	/**
	 * Writes a floating point number (a "double") to the GraphicsConsole with a
	 * specified field size and a specified number of decimal places
	 * followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void println (float number, int fieldSize, int decimalPlaces) {
		print (number, fieldSize, decimalPlaces);
		print ("\n");
	}
	/**
	 * Writes the text representation of an 32-bit integer (an "int") to
	 * the GraphicsConsole followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void println (int number) {
		print (number);
		print ("\n");
	}
	/**
	 * Writes the text representation of an 32-bit integer (an "int")
	 * to the GraphicsConsole with a specified field size followed by a newline.
	 * Adapted from hsa.
	 * 
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (int number, int fieldSize) {
		print (number, fieldSize);
		print ("\n");
	}
	/**
	 * Writes the text representation of an 64-bit integer (a "long") to
	 * the GraphicsConsole followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void println (long number) {
		print (number);
		print ("\n");
	}
	/**
	 * Writes the text representation of an 8-bit integer (a "byte") to
	 * the GraphicsConsole. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void print (byte number) {
		print ((int) number);
	}
	/**
	 * Writes the text representation of an 8-bit integer (a "byte")
	 * to the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (byte number, int fieldSize) {
		print ((int) number, fieldSize);
	}
	/**
	 * Writes a character to the GraphicsConsole. Adapted from hsa.
	 *
	 * @param ch The character to be written to the GraphicsConsole.
	 */
	public void print (char ch) {
		print (String.valueOf (ch));
	}
	/**
	 * Writes a character to the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param ch The character to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the character is to be written in.
	 */
	public void print (char ch, int fieldSize) {
		String charStr = String.valueOf (ch);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - charStr.length () ; cnt++)   {
			padding.append (' ');
		}
		print (charStr + padding);
	}
	/**
	 * Writes a double precision floating point number (a "double") to
	 * the GraphicsConsole. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void print (double number) {
		print (String.valueOf (number));
	}
	/**
	 * Writes a double precision floating point number (a "double") to
	 * the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (double number, int fieldSize) {
		double posValue = Math.abs (number);
		int placesRemaining = fieldSize;
		String format = null, numStr;
		StringBuffer padding = new StringBuffer ();

		if (number < 0)
			placesRemaining--;                 // Space for the minus sign
		if (posValue < 10.0)
			format = "0";
		else if (posValue < 100.0)
			format = "00";
		else if (posValue < 1000.0)
			format = "000";
		else if (posValue < 10000.0)
			format = "0000";
		else if (posValue < 100000.0)
			format = "00000";
		else if (posValue < 1000000.0)
			format = "000000";
		else if (posValue < 10000000.0)
			format = "0000000";
		else if (posValue < 100000000.0)
			format = "00000000";

		if (format == null)   {
			// We're using scientific notation
			numStr = String.valueOf (number);
		} else {
			// Add a decimal point, if there's room
			placesRemaining -= format.length ();
			if (placesRemaining > 0) {
				format = format + ".";
				placesRemaining--;
			}

			// For any addition room, add decimal places
			for (int cnt = 0 ; cnt < placesRemaining ; cnt++) {
				format = format + "#";
			}

			// Convert the number
			NumberFormat form = new DecimalFormat (format);
			numStr = form.format (number);
		}

		// If the number is not long enough, pad with spaces
		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++) {
			padding.append (' ');
		}
		print (padding + numStr);
	}
	/**
	 * Writes a double precision floating point number (a "double") to
	 * the GraphicsConsole with a specified field size and a specified number of
	 * decimal places. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void print (double number, int fieldSize, int decimalPlaces) {
		double posValue = Math.abs (number);
		//int placesRemaining = fieldSize;
		String format = null, numStr;
		StringBuffer padding = new StringBuffer ();

		//if (number < 0)
		// placesRemaining--;                 // Space for the minus sign
		if (posValue < 10.0)
			format = "0";
		else if (posValue < 100.0)
			format = "00";
		else if (posValue < 1000.0)
			format = "000";
		else if (posValue < 10000.0)
			format = "0000";
		else if (posValue < 100000.0)
			format = "00000";
		else if (posValue < 1000000.0)
			format = "000000";
		else if (posValue < 10000000.0)
			format = "0000000";
		else if (posValue < 100000000.0)
			format = "00000000";

		if (Math.abs (number) >= 100000000.0)
		{
			// We're using scientific notation
			numStr = String.valueOf (number);
		}
		else
		{
			format = "0.";

			// For any addition room, add decimal places
			for (int cnt = 0 ; cnt < decimalPlaces ; cnt++)
			{
				format = format + "0";
			}

			// Convert the number
			NumberFormat form = new DecimalFormat (format);
			form.setMinimumIntegerDigits (1);
			numStr = form.format (number);
		}

		// If the number is not long enough, pad with spaces
		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++)
		{
			padding.append (' ');
		}
		print (padding + numStr);
	}
	/**
	 * Writes a floating point number (a "float") to the GraphicsConsole. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void print (float number) {
		print (String.valueOf (number));
	}
	/**
	 * Writes a floating point number (a "float") to
	 * the GraphicsConsole with a specified field size.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (float number, int fieldSize) {
		print ((double) number, fieldSize);
	}
	/**
	 * Writes a floating point number (a "double") to the GraphicsConsole with a
	 * specified field size and a specified number of decimal places.
	 * Adapted from hsa.
	 * 
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void print (float number, int fieldSize, int decimalPlaces) {
		print ((double) number, fieldSize, decimalPlaces);
	}
	/**
	 * Writes the text representation of an 32-bit integer (an "int") to
	 * the GraphicsConsole. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void print (int number) {
		print (String.valueOf (number));
	}
	/**
	 * Writes the text representation of an 32-bit integer (an "int")
	 * to the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (int number, int fieldSize) {
		String numStr = String.valueOf (number);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++) {
			padding.append (' ');
		}
		print (padding + numStr);
	}
	/**
	 * Writes the text representation of an 64-bit integer (a "long") to
	 * the GraphicsConsole. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void print (long number) {
		print (String.valueOf (number));
	}
	/**
	 * Writes the text representation of an 64-bit integer (a "long")
	 * to the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (long number, int fieldSize) {
		String numStr = String.valueOf (number);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++) {
			padding.append (' ');
		}
		print (padding + numStr);
	}
	/**
	 * Writes a string to the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param text The string to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the string is to be written in.
	 */
	public void print (String text, int fieldSize) {
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - text.length () ; cnt++) {
			padding.append (' ');
		}
		print (text + padding);
	}
	/**
	 * Writes the text representation of an 16-bit integer (a "short") to
	 * the GraphicsConsole. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void print (short number) {
		print ((int) number);
	}
	/**
	 * Writes the text representation of an 16-bit integer (a "short")
	 * to the GraphicsConsole with a specified field size. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (short number, int fieldSize) {
		print ((int) number, fieldSize);
	}
	/**
	 * Writes the text representation of a boolean to the GraphicsConsole. Adapted from hsa.
	 *
	 * @param value The boolean to be written to the GraphicsConsole.
	 */
	public void print (boolean value) {
		print (String.valueOf (value));
	}
	/**
	 * Writes the text representation of a boolean to the GraphicsConsole with a
	 * specified field size. Adapted from hsa.
	 *
	 * @param value The boolean to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the boolean is to be written in.
	 */
	public void print (boolean value, int fieldSize) {
		String boolStr = String.valueOf (value);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - boolStr.length () ; cnt++) {
			padding.append (' ');
		}
		print (boolStr + padding);
	}
	/**
	 * Write a string to the GraphicsConsole. Adapted from hsa.
	 *
	 * @param text
	 *            The string to be written to the GraphicsConsole
	 */
	public void print (String text) {
		canvas.print(text);
	}
	/**
	 * Writes the text representation of an 64-bit integer (a "long")
	 * to the GraphicsConsole with a specified field size followed by a newline.
	 * Adapted from hsa.
	 * 
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (long number, int fieldSize) {
		print (number, fieldSize);
		print ("\n");
	}
	/**
	 * Writes a string to the GraphicsConsole followed by a newline. Adapted from hsa.
	 *
	 * @param text The string to be written to the GraphicsConsole.
	 */
	public void println (String text) {
		print (text);
		print ("\n");
	}
	/**
	 * Writes a string to the GraphicsConsole with a specified field size followed by
	 * a newline. Adapted from hsa.
	 *
	 * @param text The string to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the string is to be written in.
	 */
	public void println (String text, int fieldSize) {
		print (text, fieldSize);
		print ("\n");
	}
	/**
	 * Writes the text representation of an 16-bit integer (a "short") to
	 * the GraphicsConsole followed by a newline. Adapted from hsa.
	 *
	 * @param number The number to be written to the GraphicsConsole.
	 */
	public void println (short number) {
		print (number);
		print ("\n");
	}
	/**
	 * Writes the text representation of an 16-bit integer (a "short")
	 * to the GraphicsConsole with a specified field size followed by a newline.
	 * Adapted from hsa.
	 * 
	 * @param number The number to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (short number, int fieldSize) {
		print (number, fieldSize);
		print ("\n");
	}
	/**
	 * Writes the text representation of a boolean to the GraphicsConsole followed
	 * by a newline. Adapted from hsa.
	 *
	 * @param value The boolean to be written to the GraphicsConsole.
	 */
	public void println (boolean value) {
		print (value);
		print ("\n");
	}
	/**
	 * Writes the text representation of a boolean to the GraphicsConsole with a
	 * specified field size followed by a newline. Adapted from hsa.
	 *
	 * @param value The boolean to be written to the GraphicsConsole.
	 * @param fieldSize The field width that the boolean is to be written in.
	 */
	public void println (boolean value, int fieldSize) {
		print (value, fieldSize);
		print ("\n");
	}

	// ************************
	// *** TEXT INPUT METHODS
	// ************************

	/** reads a character from the GraphicsConsole.
	 * 
	 * @return the character read.
	 */
	public char getChar() {
		return canvas.getChar(false);
	}
	/**
	 * Reads a boolean from the GraphicsConsole.
	 * The actual text in the GraphicsConsole must be either "true" or "false"
	 * although case is irrelvant.
	 *
	 * @return The boolean value read from the GraphicsConsole.
	 */
	public boolean readBoolean () {
		String s;

		s = readToken ().toLowerCase ();
		if (s.equals ("true"))
			return (true);
		else if (s.equals ("false"))
			return (false);
		else
			new FatalError ("Unable to convert \"" + s + "\" to a boolean", this);
		return (false);
	}
	/**
	 * Reads an 8-bit integer (a "byte") from the GraphicsConsole.
	 * The actual text in the GraphicsConsole must be a number from -128 to 127.
	 *
	 * @return The byte value read from the GraphicsConsole.
	 */
	public byte readByte () {
		String s = readToken ();

		try {
			return (Byte.parseByte (s));
		} catch (NumberFormatException e) {
			new FatalError ("Unable to convert \"" + s + "\" to a byte", this);
			// Never reaches here
		}
		return (0);
	}
	/**
	 * Reads a double precision floating point number (a "double") from
	 * the GraphicsConsole.
	 *
	 * @return The double value read from the GraphicsConsole.
	 */
	public double readDouble () {
		Double d;
		String s;

		s = readToken ();
		try {
			d = Double.valueOf (s);
			return (d.doubleValue ());
		} catch (NumberFormatException e) {
			new FatalError ("Unable to convert \"" + s + "\" to a double", this);
			// Never reaches here
		}
		return (0.0);
	}
	/**
	 * Reads a floating point number (a "float") from the GraphicsConsole.
	 *
	 * @return The float value read from the GraphicsConsole.
	 */
	public float readFloat () {
		Float f;
		String s;

		s = readToken ();
		try {
			f = Float.valueOf (s);
			return (f.floatValue ());
		} catch (NumberFormatException e) {
			new FatalError ("Unable to convert \"" + s + "\" to a float", this);
			// Never reaches here
		}
		return ((float) 0.0);
	}
	/**
	 * Reads a 32-bit integer (an "int") from the GraphicsConsole.
	 * The actual text in the GraphicsConsole must be a number from
	 * -2147483648 to 2147483647.
	 *
	 * @return The int value read from the GraphicsConsole.
	 */
	public int readInt () {
		String s = readToken ();

		try {
			return (Integer.parseInt (s));
		} catch (NumberFormatException e) {
			new FatalError ("Unable to convert \"" + s + "\" to a int", this);
			// Never reaches here
		}
		return (0);
	}
	/**
	 * Reads a full line of text from the GraphicsConsole.
	 *
	 * @return The line of text read from the GraphicsConsole.
	 */
	public String readLine () {
		char ch;                                // The character being read in
		String s = "";          // The string typed in

		// Skip whitespace up tio the first newline
		do 
			ch = canvas.readChar ();
		while (ch == ' ');

		if (ch == '\n')
			ch = canvas.readChar ();

		while (ch != '\n') {
			s = s + ch;
			ch = canvas.readChar ();
		}

		return (s);
	}
	/**
	 * Reads a 64-bit integer (a "long") from the GraphicsConsole.
	 *
	 * @return The long value read from the GraphicsConsole.
	 */
	public long readLong () {
		String s = readToken ();                        // The token read in

		try {
			return (Long.parseLong (s));
		} catch (NumberFormatException e) {
			new FatalError ("Unable to convert \"" + s + "\" to a long", this);
			// Never reaches here
		}
		return (0);
	}
	/**
	 * Reads a 16-bit integer (a "short") from the GraphicsConsole.
	 * The actual text in the GraphicsConsole must be a number from -32768 to 32767.
	 *
	 * @return The short value read from the GraphicsConsole.
	 */
	public short readShort () {
		String s = readToken ();

		try {
			return (Short.parseShort (s));
		} catch (NumberFormatException e) {
			new FatalError ("Unable to convert \"" + s + "\" to a short", this);
			// Never reaches here
		}
		return (0);
	}
	/**
	 * Reads in input from the keyboard buffer until it hits a
	 * whitespace, which indicates the end of a token.
	 * @return the string read.
	 */
	public String readToken () {
		char ch;

		StringBuffer sb = new StringBuffer ();

		// Skip white space
		do
			ch = canvas.readChar ();
		while ((ch == ' ') || (ch == '\n') || (ch == '\t'));

		if (ch == '"') {
			// Read until close quote
			ch = canvas.readChar ();
			while (ch != '"') {
				sb.append (ch);
				ch = canvas.readChar ();
				if (ch == '\n') {
					new FatalError ("No terminating quote for quoted string");
					// Never reaches here.
				}
			}

			// Read the character following the close quote.
			ch = canvas.readChar ();
		} else {
			do {
				sb.append (ch);
				ch = canvas.readChar ();
			} while ((ch != ' ') && (ch != '\n') && (ch != '\t'));
		}

		// Lastly, skip any whitespace until the end of line
		while ((ch == ' ') || (ch == '\t'))
			ch = canvas.readChar ();

		if (ch != '\n')
			canvas.ungotChar = (int) ch;

		return (new String (sb));
	} 
	/**
	 * @return the code for the key currently held down.
	 **/
	public int getKeyCode () {
		return canvas.getKeyCode();
	}
	/**
	 * @return the char for the key currently held down.
	 **/
	public char getKeyChar () {
		return canvas.getKeyChar();
	}
	/**
	 * @return the code for the last key pressed.
	 **/
	public  int getLastKeyCode () {
		return canvas.getLastKeyCode();
	}
	/**
	 * @return the char for the last key pressed.
	 **/
	public char getLastKeyChar () {
		return canvas.getLastKeyChar();
	}
	/**
	 * @param key The key code to check
	 * @return True if the specified key is held down right now. False otherwise.
	 */
	public boolean isKeyDown(int key) {
		return canvas.isKeyDown(key);
	}

	// ************************
	// *** OTHER PUBLIC METHODS
	// ************************

	/**
	 * Closes the GraphicsConsole window.
	 */
	public void close () {
		canvas.killThread ();
		this.dispose ();
	} 
	
	/** 
	 * A simpler sleep function
	 * handles the try/catch or "throws InterruptedException" 
	 * that Thread.sleep() produces.
	 * @param milliSeconds  the time to sleep in ms.
	 */
	public void sleep(long milliSeconds) {		
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * This uses Swing JOptionPane for dialogs and so removes the need for the Message class.
	 * It does not display an icon
	 * @param message to display
	 * @param title for the popup message box
	 */
	public void showDialog(String message, String title) {
		//MH. June 2017
                //clear all keys that are being pressed down
                canvas.clearKeysDown();
		JOptionPane.showMessageDialog(null, message, title , JOptionPane.PLAIN_MESSAGE);	
	}
	/**
	 * This uses Swing JOptionPane to get text from the user.
	 * The text can be from one letter, or a word, to a whole line. The text is terminated with Enter.
	 * It displays the Question icon.
	 * 
	 * Sample code:
<pre>
	name = c.showInputDialog("What is your name?", "");
	//handle CANCEL option
	if(name == null){
		System.out.println("Cancel pressed");
		System.exit(0); //or do something else
	}
	//handle OK option with no text
	if (name.equals("")) {
		name = "No Name";
	}</pre>
	 * 
	 * @param message to display
	 * @param title for the popup message box
	 * @return The string typed in. If the Cancel button is pressed, the return value is equal to null.
	 * If OK is pressed without anything typed in, the return value is a zero length string.
	 */
	public String showInputDialog(String message, String title) {
		//MH. June 2017
                //clear all keys that are being pressed down
                canvas.clearKeysDown();
		return  JOptionPane.showInputDialog(null, message, title ,JOptionPane.QUESTION_MESSAGE);
	}
	

	// **********************
	// *** MOUSE METHODS
	// **********************

	/*
	 * The enableMouse... methods are included so that the overhead of
	 * listening for mouse events is not a factor if the programmer doesn't
	 * want to use the mouse.
	 * 
	 * You MUST invoke the appropriate enableMouse... methods() first, if you
	 * want to use the mouse.
	 * 
	 * enableMouse(): Listens for mouse button events, and also saves the
	 * coordinates of the mouse when a mouse button event occurs, so that
	 * getMouseX() and getMouseY() return the coordinates of the mouse as of
	 * the last time a button was clicked/pressed/released.
	 * 
	 *  - getMouseButton() and getMouseClick() are enabled.
	 * 
	 * enableMouseMotion(): Listens for mouse move and drag events, thereby
	 * allowing getMouseX() and getMouseY() to return the current coordinates
	 * of the mouse, regardless of button or wheel events.
	 * 
	 * enableMouseWheel(): Listens for mouse wheel events, and also saves the
	 * coordinates of the mouse when a mouse wheel event occurs, so that
	 * getMouseX() and getMouseY() return the coordinates of the mouse as of
	 * the last time the mouse wheel was scrolled.
	 * 
	 *  - getMouseWheelRotation() and getMouseWheelUnitsToScroll() are enabled.
	 * 
	 * NOTE: if enableMouseMotion() has been invoked, then getMouseX() and
	 * getMouseY() will return the current coordinates of the mouse, rather
	 * than the coordinates of the mouse as of the last time a button was
	 * clicked/pressed/released or wheel was scrolled.
	 * 
	 * enableMouseMotion() is by far the most demanding in terms of overhead,
	 * so don't invoke this method unless it is absolutely necessary.
	 * 
	 * disableMouse... methods are provided to avoid mouse event overhead
	 * when they are no longer needed.
	 */
	
	public void disableMouse() {
		canvas.removeMouseListener(this);
	}

	public void disableMouseMotion() {
		canvas.removeMouseMotionListener(this);
	}

	public void disableMouseWheel() {
		canvas.removeMouseWheelListener(this);
	}
	/**
	 * Adds MouseListener to the GraphicsConsole
	 */
	public void enableMouse() {
		canvas.addMouseListener(this);
	}

	/**
	 * Adds MouseMotionListener to the GraphicsConsole
	 */
	public void enableMouseMotion() {
		canvas.addMouseMotionListener(this);
	}

	/**
	 * Adds MouseWheelListener to the GraphicsConsole
	 */
	public void enableMouseWheel() {
		canvas.addMouseWheelListener(this);
	}

	/**
	 * Returns true if the specified button is pressed, false otherwise.
	 * 
	 * Buttons are numbered 0, 1 or 2.
	 * @param buttonNum mouse button number (0,1,2)
	 * @return T/F if that button has been pressed.
	 */
	public boolean getMouseButton(int buttonNum) {

		if (( buttonNum >= 0)&& (buttonNum < mouseButton.length))
			return mouseButton[ buttonNum ];
		else
			return false;
	}

	/**
	 * Returns non-zero if the mouse has been clicked since the last time
	 * the click was queried, zero if the mouse was not clicked.
	 * 
	 * 1 = single click
	 * 2 = double click
	 * 3 = triple click
	 * etc.
	 * @return number of clicks
	 */
	public int getMouseClick() {
		int toReturn = mouseClick;
		mouseClick = 0;
		return toReturn;
	}

	/**
	 * Returns the rotation of the mouse wheel since the last time the wheel
	 * was queried.
	 * Positive or negative depending on the direction.
	 * @return number of "clicks" that mouse wheel was rotated. 
	 */
	public int getMouseWheelRotation() {
		int toReturn = mouseWheelRotation;
		mouseWheelRotation = 0;
		return toReturn;
	}

	/**
	 * Returns the number of units the mouse wheel has been scrolled since
	 * the last time the wheel was queried.
	 * @return the number of units to scroll based on the direction and amount 
	 * of mouse wheel rotation, and on the wheel scrolling settings of the native platform
	 */
	public int getMouseWheelUnitsToScroll() {
		int toReturn = mouseWheelUnitsToScroll;
		mouseWheelUnitsToScroll = 0;
		return toReturn;
	}

	/**
	 * Returns the X coordinate of the mouse pointer position within the drawing
	 * area.
	 * @return x coordinate of mouse pointer position
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * Returns the Y coordinate of the mouse pointer position within the drawing
	 * area.
	 * @return y coordinate of mouse pointer position
	 */
	public int getMouseY() {
		return mouseY;
	}

	/**
	* Returns true of mouse is being dragged (button down and mouse moving triggers a drag event)
	*/ 
	public boolean isMouseDragged() {
		return mouseDrag;
	}
	
	/**
	* Returns the distance dragged in the x-axis (positive or negative)
	* @return distance in pixels dragged in x-direction
	*/
	public int getMouseDX() {
		if (endDrag == null || startDrag == null) return 0;
		return endDrag.x - startDrag.x;		
	}
	
	/**
	* Returns the distance dragged in the y-axis (positive or negative)
	* @return distance in pixels dragged in y-direction
	*/
	public int getMouseDY() {
		if (endDrag == null || startDrag == null) return 0;
		return endDrag.y - startDrag.y;
	}
	/* Sample code for dragging event (rectangle r1)
	 * Note that oldx and oldy are global instance variables to keep track of the position of the rectangle before it started dragging.
 	 * The speed at which something can be dragged is a bit slow. 

		if (r1.contains(gc.getMouseX(), gc.getMouseY())) {
			if (gc.isMouseDragged()) {		
				r1.x = oldx + gc.getMouseDX();
				r1.y = oldy + gc.getMouseDY();
			} else {			
				oldx = r1.x;
				oldy = r1.y;
			}
		}
	*/

	/* *********************************************
	 * MOUSE LISTENER EVENTS
	 * "Background" mouse methods 
	 * (i.e., don't try to invoke these directly!)
	 * *********************************************/
	@Override
	public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		//mouseClick = e.getClickCount(); //moved to mouseReleased for faster response
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		endDrag = new Point(e.getX(), e.getY());
		mouseDrag = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (e.getButton() == MouseEvent.BUTTON1)
			mouseButton[ 0 ] = true;

		else if (e.getButton() == MouseEvent.BUTTON2)
			mouseButton[ 1 ] = true;

		else if (e.getButton() == MouseEvent.BUTTON3)
			mouseButton[ 2 ] = true;
		
		startDrag = new Point(e.getX(), e.getY());
		endDrag = startDrag;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		mouseClick = e.getClickCount(); //for better response when using gc.getMouseClick()

		if (e.getButton() == MouseEvent.BUTTON1)
			mouseButton[ 0 ] = false;

		else if (e.getButton() == MouseEvent.BUTTON2)
			mouseButton[ 1 ] = false;

		else if (e.getButton() == MouseEvent.BUTTON3)
			mouseButton[ 2 ] = false;
	
		mouseDrag = false;
		startDrag = endDrag = null;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

		mouseWheelRotation += e.getWheelRotation();
		mouseWheelUnitsToScroll += e.getUnitsToScroll();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		canvas.doResizing();
	}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentHidden(ComponentEvent e) {}

	// **********************
	// *** NON-PUBLIC METHODS
	// **********************

	private void makeGUI (int width, int height, int fontSize, String title) { 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);		

		JPanel newPanel = new JPanel ();
		newPanel.setLayout (new GridLayout(1,1));

		canvas = new ConsoleCanvas(width, height, fontSize, title, this);
		newPanel.add(canvas);
		this.addKeyListener(canvas);
		
		this.setContentPane(newPanel);
//		this.setLocationRelativeTo(null); //centre the window in the display
		this.pack();
		this.setVisible(true);
	}
	
}

