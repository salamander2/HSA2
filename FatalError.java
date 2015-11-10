package hsa2;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;

/**
 * The FatalError class displays an error message and then terminates the
 * programs execution.
 * <p>
 * Full documentation for the classes in the hsa package available at:
 * <br>
 *   http://www.holtsoft.com/java/hsa_package.html
 * <p>
 * @author Tom West
 * @version 2.0 99/02/01
 */
public class FatalError extends CloseableDialog 
{
	private java.awt.Button quitButton;
	/**
	 * Contructor - FatalError to be displayed on centre of screen.
	 *
	 * @param message The message to be displayed in the FatalError dialog box.
	 */
	public FatalError (String message)
	{
		this (message, null);
	} // Constructor - FatalError (String)
	/**
	 * Contructor - FatalError to be displayed on centre of a specified Frame.
	 *
	 * @param message The message to be displayed in the FatalError dialog box.
	 * @param frame The Frame that the dialog box should be centred on.
	 */
	public FatalError (String message, Frame frame)
	{
		// Create the dialog.
		super (frame, "Fatal Error");

		setBackground (Color.lightGray);

		// Put the message at the top
		add ("Center", new Label ("  " + message + "  ", Label.CENTER));

		// Put OK button at bottom
		quitButton = new Button ("Quit");
		quitButton.addActionListener (this);
		Panel p = new Panel ();
		p.setLayout (new FlowLayout (FlowLayout.CENTER, 0, 0));
		p.add (quitButton);
		add ("South", p);

		pack ();

		positionDialog (frame);

		Message.beep ();

		setVisible (true);

		System.exit (0);
	} // Constructor - FatalError (String, Frame)
} /* FatalError class */