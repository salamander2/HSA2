/**
 * This is a re-implementation of the old hsa console by Holt Software Associates.
 * Re-done from scratch in Swing with much code imported from the old hsa console.
 * The main goals were to reduce screen flicker during animations and eliminate a
 * couple of small bugs in the input routines. April 30, 2010.
 *
 * See Console.java for differences between this version and the old one.
 *
 * Update August 2012: Changed synchronization to synchronize on the associated
 * Console object. Now application writers can also synchronize on the Console object
 * to kill the last remaining cases of screen flicker.
 * 
 * Update September 2014: Fixed bug in getRow() and getColumn(); they now report
 * the current cursor position without requiring a print() first.
 * 
 * @author Sam Scott
 * @author Josh Gray (getRow()/getColumn() bug fix)
 * @author Tom West (old hsa code)
 */
package hsa_ufa;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;
import javax.swing.Timer;

public class ConsoleCanvas extends JPanel implements ActionListener, KeyListener {

	/** Window title **/
	private String title;
	/** Container of this object **/
	private hsa_ufa.Console container;

	// ***** Screen variables *****
	
	/** Off screen buffer **/
	private final BufferedImage buffer;
	/** Foreground color **/
	private Color foregroundColor = Color.black;
	/** Background color **/
	private Color backgroundColor = Color.white;
	/** Screen size **/
	private int width, height;
	/** Screen drawing mode **/
	private boolean xorMode = false;
	/** Color for xor mode **/
	private Color xorColor = backgroundColor;
	/** Font for drawString **/
	private Font drawStringFont;
	/** Refresh speed **/
	private static final int framesPerSecond = 60;
	/** Timer object for redrawing screen **/
	private Timer timer;

	// ***** Text input/output variables *****
	private Font textFont;
	private int fontHeight, fontBase, fontWidth;
	private int cursorRow = 0, cursorCol = 0;
	private boolean cursorFlashing = false;
	private int flashSpeed = 20; // speed is in frames (see framesPerSecond above)
	private int flashCount = 0;
	private boolean cursorVisible = false;

	// ***** Text output variables - adapted from original hsa package *****
	private static final int MARGIN = 3;
	private int currentRow = 0, currentCol = 0;
	private int actualRow = 0, actualCol = 0;
	private int startCol = 0, startRow = 0;
	private int maxRow = 0, maxCol = 0;
	private final static int TAB_SIZE = 8;

	// ***** Keyboard Buffer & Input Variables - adapted from original hsa package *****
	private static final int BUFFER_SIZE = 2048;
	private static final int EMPTY_BUFFER = -1;
	private char[] kbdBuffer = new char [BUFFER_SIZE];
	private int kbdBufferHead = 0, kbdBufferTail = 0;
	private char[] lineBuffer = new char [BUFFER_SIZE];
	private int lineBufferHead = 0, lineBufferTail = 0;
	protected int ungotChar = EMPTY_BUFFER;
	private boolean echoOn = true;
	//private boolean clearToEOL = true;
	
	// New Keyboard variables
	/** Code for key currently held down **/
	private int currentKeyCode = Console.VK_UNDEFINED;
	/** Code for last key pressed **/
	private int lastKeyCode = currentKeyCode;
	/** Character currently held down **/
	private char currentKeyChar = (char) Console.VK_UNDEFINED;
	/** Last character pressed **/
	private char lastKeyChar = currentKeyChar;
	/** Size of keysDown array **/
	private final int numKeyCodes = 256;
	/** Array of booleans representing characters currently held down **/
	private boolean[] keysDown = new boolean [numKeyCodes];

	// ****************
	// *** CONSTRUCTORS
	// ****************

	public ConsoleCanvas(int width, int height, int fontSize, String title, hsa_ufa.Console console)
	{
		this.container = console;
		this.title = title;

		// Sizing
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		this.height = height;
		this.width = width;
		// Adapted from old hsa code
		textFont = new Font("monospaced", Font.PLAIN, fontSize);
		FontMetrics fm = getFontMetrics(textFont);
		fontHeight = fm.getHeight() + fm.getLeading();
		fontBase = fm.getDescent();
		fontWidth = 0;
		for (int ch = 32; ch < 127; ch++) {
			fontWidth = Math.max(fontWidth, fm.charWidth(ch));
		}

		// set the number of rows and columns
		maxCol = (width - 2*MARGIN) / fontWidth - 1;
		maxRow = (height - 2*MARGIN) / fontHeight - 1;

		clear();
		//new Thread(this).start();
		timer = new Timer(1000/framesPerSecond, this);
		timer.start();
	}

	// ************
	// *** GRAPHICS
	// ************
	protected void clear()
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(backgroundColor);
		if(xorMode)
			g.setPaintMode();
		g.fillRect(0, 0, width, height);
		setCursor(0,0);
		if(xorMode)
			g.setXORMode(xorColor);
	}
	protected void clearRect(int x, int y, int width, int height)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(backgroundColor);
		if(xorMode)
			g.setPaintMode();
		g.fillRect(x, y, width, height);
		if(xorMode)
			g.setXORMode(xorColor);
	}
	protected void copyArea(int x, int y, int width, int height, int dx, int dy)
	{
		Graphics g = getOffscreenGraphics();
		g.copyArea(x, y, width, height, dx, dy);
	}
	protected void setColor(Color c)
	{
		foregroundColor = c;
	}
	protected void setBackgroundColor(Color c)
	{
		backgroundColor = c;
	}
	protected void setPaintMode()
	{
		xorMode = false;
	}
	protected void setXORMode(Color c)
	{
		xorMode = true;
		xorColor = c;
	}
	protected void fillRect(int x, int y, int width, int height)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.fillRect(x, y, width, height);
	}
	protected void drawRect(int x, int y, int width, int height)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.drawRect(x, y, width, height);
	}
	protected void fillOval(int x, int y, int width, int height)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.fillOval(x, y, width, height);
	}
	protected void drawOval(int x, int y, int width, int height)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.drawOval(x, y, width, height);
	}
	protected void drawLine(int x1, int y1, int x2, int y2)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.drawLine(x1, y1, x2, y2);
	}
	protected void drawPolygon(int[] x, int[] y, int n)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.drawPolygon(x, y, n);
	}
	protected void fillPolygon(int[] x, int[] y, int n)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.fillPolygon(x, y, n);
	}
	protected void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.drawArc(x, y, width, height, startAngle, arcAngle);
	}
	protected void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.fillArc(x, y, width, height, startAngle, arcAngle);
	}
	protected void drawRoundRect(int x, int y, int width, int height, int xRadius, int yRadius)	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.drawRoundRect(x, y, width, height, xRadius, yRadius);
	}
	protected void fillRoundRect(int x, int y, int width, int height, int xRadius, int yRadius) {
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.fillRoundRect(x, y, width, height, xRadius, yRadius);
	}
	protected void draw3DRect(int x, int y, int width, int height, boolean raised)
	{
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.draw3DRect(x, y, width, height, raised);
	}
	protected void fill3DRect(int x, int y, int width, int height, boolean raised) {
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.fill3DRect(x, y, width, height, raised);
	}
	protected void drawString(String str, int x, int y) {
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		if (drawStringFont != null)
			g.setFont(drawStringFont);
		g.drawString(str, x, y);
	}
	public void setFont(Font f) {
		super.setFont(f);
		drawStringFont = f;
	}
	protected void drawImage(Image img, int x, int y) {
		boolean success = false;
		Graphics g = getOffscreenGraphics();
		success = g.drawImage (img, x, y, null); 
		// loop to timeout if image not drawn properly
		for (int i = 0 ; i < 1000 & !success ; i++) {
			success = g.drawImage (img, x, y, null); 
			try
			{
				Thread.sleep (1);
			}
			catch (InterruptedException e)
			{
			}
		}
		if (!success)
			throw new RuntimeException ("Image not loaded.");
	}
	protected void drawImage(Image img, int x, int y, int width, int height) {
		boolean success = false;
			Graphics g = getOffscreenGraphics();
			success = g.drawImage (img, x, y, width, height, null);
			// loop to timeout if image not drawn properly
			for (int i = 0 ; i < 1000 & !success ; i++)
			{
				success = g.drawImage (img, x, y, width, height, null);
				try {
					Thread.sleep (1);
				} catch (InterruptedException e) {}
			}
			if (!success)
				throw new RuntimeException ("Image not loaded.");
	}


	// ********
	// *** TEXT
	// ********

	public void setCursor(int row, int col)
	{
		//if (cursorFlashing)
		//	cursorOff();
		currentRow = row;
		currentCol = col;
		actualRow = row;
		actualCol = col;
		//if (cursorFlashing)
		//	cursorOn();
		setCursorPos(row, col);
	}
	public int getCurrentColumn()
	{
		return cursorCol;
	}
	public int getCurrentRow()
	{
		return cursorRow;
	}
	public int getNumColumns()
	{
		return maxCol+1;
	}
	public int getNumRows()
	{
		return maxRow+1;
	}
	/**
	 * Sets the cursor to the specified row and column.Adapted from hsa.
	 */
	private void setCursorPos (int row, int col)
	{
		if (cursorFlashing)
			cursorOff();
		cursorRow = row;
		cursorCol = col;
		if (cursorFlashing)
			cursorOn();
	}
	/**
	 * Write a string to the Console. Adapted from hsa.
	 *
	 * @param text
	 *            The string to be written to the Console
	 */
	public void print (String text)
	{
		// Convert the printing of null to a printable string.
		if (text == null)
		{
			text = "<null>";
		}

		int index = 0;
		int len = text.length ();
		int start = 0;

		while (true)
		{
			index = start;
			if (index == len)
			{
				setCursorPos (actualRow, actualCol);
				return;
			}

			while ((index < len) && (text.charAt (index) != '\n')
					&& (text.charAt (index) != '\t')
					&& (index - start < maxCol - currentCol))
			{
				index++;
			}
			if (start != index)
			{
				// Draw what we have so far
				drawText (currentRow, currentCol, text.substring (start, index));
				currentCol += index - start;
				actualCol = currentCol;
			}
			if (index == len)
			{
				setCursorPos (actualRow, actualCol);
				return;
			}
			if (text.charAt (index) == '\n')
			{
				if ((currentRow <= maxRow) && (currentCol <= maxCol))
				{
					clearToEOL (currentRow, currentCol);
				}
				if (currentRow < maxRow)
				{
					currentCol = 0;
					currentRow++;
					actualCol = currentCol;
					actualRow = currentRow;
				}
				else
				{
					scrollUpALine ();
					startRow--;
					currentCol = 0;
					actualCol = currentCol;
				}
			}
			else if (text.charAt (index) == '\t')
			{
				int numSpaces = TAB_SIZE - ((currentCol - 1) % TAB_SIZE);
				// If the next tab position is off the end of the screen,
				// scroll down a line and place the cursor at the beginning
				// of the line.
				if (currentCol + numSpaces > maxCol)
				{
					print ("\n");
				}
				else
				{
					print ("        ".substring (0, numSpaces));
				}
			}
			else
			{
				if (currentCol <= maxCol)
				{
					drawText (currentRow, currentCol, text.substring (index, index + 1));
					if (currentCol < maxCol)
					{
						currentCol++;
						actualCol = currentCol;
					}
					else
					{
						if (currentRow < maxRow)
						{
							currentCol=0; // converted from ++ by sam
							actualCol = 0;
							actualRow++;
							currentRow++; // added by sam
						}
						else
						{
							currentCol++;
						}
					}
				}
				else
				{
					if (currentRow < maxRow)
					{
						currentRow++;
					}
					else
					{
						scrollUpALine ();
						startRow--;
					}
					drawText (currentRow, 1, text.substring (index, index + 1));
					currentCol = 0;
					actualCol = currentCol;
					actualRow = currentRow;
					index--; //kludge
				}
			}
			start = index + 1;
		}
	}
	// *********
	// *** INPUT
	// *********

	/**
	 * Returns the next character entered on the keyboard. Ignores characters
	 * currently in the line buffer.
	 *
	 * @return The next character entered on the keyboard.
	 */
	public synchronized char getChar (boolean cursor)
	{
		while (kbdBufferHead == kbdBufferTail)
		{
			try
			{
				container.setTitle (title + " - Waiting for input");
				if (cursor)
					cursorOn();
				else
					cursorOff();
				synchronized (this)
				{
					wait ();
				}
				if (cursor)
					cursorOff();
				container.setTitle (title + " - Running");
			}
			catch (InterruptedException e)
			{
			}
		}

		char ch = kbdBuffer [kbdBufferTail];
		kbdBufferTail = (kbdBufferTail + 1) % kbdBuffer.length;

		return ch;
	}

	/**
	 * Reads a single character from the Console. Note that this discards any
	 * whitespace. If you want to get every character on the line, use the
	 * readLine () method.
	 *
	 * @return The character read from the Console
	 */
	public synchronized char readChar ()
	{
		char result, ch;

		if (ungotChar != EMPTY_BUFFER)
		{
			result = (char) ungotChar;
			ungotChar = EMPTY_BUFFER;
			return (result);
		}

		if (lineBufferHead != lineBufferTail)
		{
			result = lineBuffer [lineBufferTail];
			lineBufferTail = (lineBufferTail + 1) % lineBuffer.length;
			return (result);
		}

		startRow = currentRow;
		startCol = currentCol;
		if (currentRow > maxRow)
		{
			startRow++;
			currentCol = 0;
		}

		// Turn cursor on if necessary
		//cursorOn ();

		// Wait for a character to be entered
		while (true)
		{
			ch = getChar (true);
			if (ch == '\n')
			{
				//clearToEOL = false;
				if (echoOn)
					print ("\n");
				//clearToEOL = true;
				lineBuffer [lineBufferHead] = '\n';
				lineBufferHead = (lineBufferHead + 1) % lineBuffer.length;
				break;
			}
			if (ch == '\b')
			{
				if (lineBufferHead == lineBufferTail)
				{
					invertScreen ();
				}
				else
				{
					int chToErase;

					lineBufferHead = (lineBufferHead + lineBuffer.length - 1)
							% lineBuffer.length;
					chToErase = lineBuffer [lineBufferHead];
					if (echoOn)
					{
						if (chToErase != '\t')
						{
							erasePreviousChar ();
						}
						else
						{
							int cnt;
							eraseLineOfInput ();
							cnt = lineBufferTail;
							while (cnt != lineBufferHead)
							{
								container.print (lineBuffer [cnt]);
								cnt = (cnt + 1) % lineBuffer.length;
							}
						}
					}
				}
			} // if backspace
			else if (ch == '\025')
			{
				if (echoOn)
				{
					eraseLineOfInput ();
				}
				lineBufferHead = lineBufferTail;
			}
			else
			{
				if (echoOn)
				{
					container.print (ch);
					//System.out.println(currentCol+" "+actualCol+" "+cursorCol);
				}
				lineBuffer [lineBufferHead] = ch;
				lineBufferHead = (lineBufferHead + 1) % lineBuffer.length;
			}
		}

		result = lineBuffer [lineBufferTail];
		lineBufferTail = (lineBufferTail + 1) % lineBuffer.length;

		// Turn cursor off if necessary
		//cursorOff ();

		return (result);
	}
	/**
	 * Returns the code for the key currently held down.
	 **/
	public synchronized int getKeyCode ()
	{
		return currentKeyCode;
	}
	/**
	 * Returns the char for the key currently held down.
	 **/
	public synchronized char getKeyChar ()
	{
		return currentKeyChar;
	}
	/**
	 *Returns the code for the last key pressed.
	 **/
	public synchronized int getLastKeyCode ()
	{
		return lastKeyCode;
	}
	/**
	 * Returns the char for the last key pressed.
	 **/
	public synchronized char getLastKeyChar ()
	{
		return lastKeyChar;
	}
	public synchronized boolean isKeyDown(int key)
	{
		if ((key >=0) & (key < numKeyCodes))
			return keysDown[key];
		return false;
	}
	public synchronized boolean isKeyDown(char key)
	{
		if ((key >=0) & (key < numKeyCodes))
			return keysDown[(int)key];
		return false;
	}

	// **********************
	// *** UTILITY METHODS
	// **********************
	public void killThread()
	{
		timer.stop();
	}
	// **********************
	// *** NON-PUBLIC METHODS
	// **********************

	/**
	 * Places a keystroke in the keyboard buffer. It is synchronized so that
	 * there can't be a problem with input being taken off the keyboard buffer
	 * and placed on the keyboard buffer at the same time. Adapted from hsa.
	 * Modified by Sam to record the current key held down.
	 */
	public synchronized void keyPressed (KeyEvent e)
	{
		// This is a workaround for a bug where the canvas isn't given
		// focus back!  The frame appears to have it, however.
		//if (!hasFocus)
		//{
		// focusGained (null);
		//}
		// Modified to record the current & last key press - SAM
		currentKeyCode = e.getKeyCode ();
		currentKeyChar = e.getKeyChar ();
		lastKeyCode = currentKeyCode;
		lastKeyChar = currentKeyChar;

		if ((currentKeyCode >= 0) & (currentKeyCode < numKeyCodes))
			keysDown [currentKeyCode] = true;

		char ch = e.getKeyChar ();
		// Handle standard keystrokes including backspace, newline and
		// Ctrl+U to delete a line of input.
		if (((' ' <= ch) && (ch <= '~')) || (ch == '\b') ||
				(ch == '\t') || (ch == '\n') || (ch == '\025'))
		{
			// Place the keystroke into the keyboard buffer.
			kbdBuffer [kbdBufferHead] = e.getKeyChar ();
			kbdBufferHead = (kbdBufferHead + 1) % kbdBuffer.length;

			// The following statements wakes up any processes that are
			// sleeping while waiting for keyboard input.
			synchronized (this)
			{
				notify ();
			}
		}

		// Handle Ctrl+V to paste.
		else if (ch == '\026')
		{
			Transferable clipData =
					getToolkit ().getSystemClipboard ().getContents (this);

			try
			{
				String s = (String) (clipData.getTransferData (DataFlavor.stringFlavor));
				int bufferUsed = (kbdBufferHead - kbdBufferTail + kbdBuffer.length) % kbdBuffer.length;
				if (s.length () > kbdBuffer.length - bufferUsed)
				{
					// Current keyboard buffer isn't big enough.
					invertScreen ();
				}
				else
				{
					for (int cnt = 0 ; cnt < s.length () ; cnt++)
					{
						// Place the keystroke into the keyboard buffer.
						ch = s.charAt (cnt);

						// Some systems seem to mix up CR and LF.
						if (((' ' <= ch) && (ch <= '~')) || (ch == '\n'))
						{
							kbdBuffer [kbdBufferHead] = ch;
							kbdBufferHead = (kbdBufferHead + 1) % kbdBuffer.length;
						}
					}
					synchronized (this)
					{
						notify ();
					}
				}
			}
			catch (Exception exception)
			{
				invertScreen ();
			}
		}

		// To stop tabs from changing the focus.
		e.consume ();
	}
	/**
	 * Set current key to the null code
	 */
	public void keyReleased (KeyEvent e)
	{
		currentKeyCode = Console.VK_UNDEFINED;
		currentKeyChar = (char) Console.VK_UNDEFINED;
		if ((e.getKeyCode () >= 0) & (e.getKeyCode () < numKeyCodes))
			keysDown [e.getKeyCode()] = false;
	}
	/**
	 * Does nothing.  Called by the system when a key is typed.
	 */
	public void keyTyped (KeyEvent e)
	{
		// This event not handled.
	}
	private void cursorOff()
	{
		synchronized(container)
		{
			cursorFlashing = false;
			if (cursorVisible)
				toggleVisibleCursor();
		}
	}
	private void cursorOn()
	{
		synchronized(container)
		{
			cursorFlashing = true;
		}
	}
	private void toggleVisibleCursor()
	{
			if (xorMode)
				drawRect(actualCol*fontWidth+MARGIN, actualRow*fontHeight+MARGIN, fontWidth, fontHeight);
			else
			{
				setXORMode(backgroundColor);
				drawRect(actualCol*fontWidth+MARGIN, actualRow*fontHeight+MARGIN, fontWidth, fontHeight);
				setPaintMode();
			}
			if (cursorVisible)
				cursorVisible = false;
			else
				cursorVisible = true;
	}
	/**
	 * Draws the specified text to the screen at the specified row and column
	 * using the specified foreground and background colours. Adapted from hsa.
	 */
	private void drawText (int row, int col, String text)
	{
		int x = (col) * fontWidth;
		int y = (row) * fontHeight;
			Graphics g = buffer.getGraphics ();

			// Erase the area that the image will appear on.
			g.setColor (backgroundColor);
			g.fillRect (x+MARGIN, y+MARGIN, fontWidth * text.length (), fontHeight);

			// Draw the text
			g.setColor (foregroundColor);
			g.setFont (textFont);
			g.drawString (text, x+MARGIN, y+MARGIN + fontHeight - fontBase);
	}
	/**
	 * Clears a rectangle on console canvas from the specified row and column to
	 * the end of line. Adapted from hsa.
	 */
	private void clearToEOL (int row, int col)
	{
		int x = (col) * fontWidth;
		int y = (row) * fontHeight;
		int len = width - x;
			Graphics g = buffer.getGraphics ();

			// First clear the rectangle on the offscreen image.
			g.setColor (backgroundColor);
			g.fillRect (x+MARGIN, y+MARGIN, len, fontHeight);
	}
	/**
	 * Scrolls up the entire ConsoleCanvas a single line. The blank space at the
	 * bottom is filled in the specified colour. Adapted from hsa.
	 */
	private void scrollUpALine ()
	{
		synchronized(container)
		{
			Graphics g = buffer.getGraphics ();
			// Scroll the screen up
			g.copyArea (0, fontHeight, width, height - fontHeight, 0, -fontHeight);
			// Erase the last line
			g.setColor (backgroundColor);
			g.fillRect (0, height - fontHeight, width, fontHeight);
		}
	}
	public void paintComponent(Graphics g)
	{	  
		synchronized(container)
		{
			g.drawImage(buffer, 0, 0, width, height, this);
		}
	}
	public void actionPerformed (ActionEvent e)
	{
		if (cursorFlashing)
		{
			flashCount = (flashCount+1)%flashSpeed;
			if (flashCount == 0) {
				synchronized(container) {
					toggleVisibleCursor();
				}
			}
		}
		repaint();
	}
	private Graphics getOffscreenGraphics()
	{
		Graphics g = buffer.getGraphics();
		if (xorMode)
			g.setXORMode(xorColor);
		else
			g.setPaintMode();
		return g;
	}
	private synchronized void invertScreen() {
		//TODO - fill this in, maybe
		/* Graphics g = getGraphics();
		 *
     g.translate(MARGIN, MARGIN);
     g.setColor(Color.white);
     g.setXORMode(Color.black);

     // Invert the screen
     g.fillRect(0, 0, numXPixels, numYPixels);
     Toolkit.getDefaultToolkit().sync();

     // Wait 50 milliseconds
     try {
     Thread.sleep(50);
     } catch (Exception e) {
     }

     // Restore the screen
     g.fillRect(0, 0, numXPixels, numYPixels);
     Toolkit.getDefaultToolkit().sync();

     g.setPaintMode();*/
	}
	/**
	 * Erases the previous character in a line of input. Called when the user
	 * presses backspace when typing. Adapted from hsa.
	 */
	private void erasePreviousChar ()
	{
		if (currentCol > 0)
		{
			currentCol--;
		}
		else
		{
			if (currentRow > 0)
			{
				currentRow--;
				currentCol = maxCol;
			}
		}
		actualRow = currentRow;
		actualCol = currentCol;

		drawText (currentRow, currentCol, " ");
		setCursorPos (currentRow, currentCol);

		if ((currentCol == -1) && (currentRow != startRow))
		{
			currentCol = maxCol + 1;
			currentRow--;
		}
	}
	/**
	 * Erases the entire line of input. Called when the user presses Ctrl+U when
	 * typing. Adapted from hsa.
	 */
	private void eraseLineOfInput ()
	{
		int numChars, cnt;

		numChars = (actualCol - startCol) + maxCol * (actualRow - startRow);
		currentRow = startRow;
		currentCol = startCol;
		actualRow = startRow;
		actualCol = startCol;
		for (cnt = 0 ; cnt < numChars ; cnt++)
			print (" ");
		currentRow = startRow;
		currentCol = startCol;
		actualRow = startRow;
		actualCol = startCol;
		setCursorPos (currentRow, currentCol);
	} // eraseLineOfInput (void)
}
