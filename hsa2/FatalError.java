package hsa2;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The FatalError class displays an error message and then terminates the
 * programs execution.
 * <p>
 * Full documentation for the classes in the hsa package available at:
 * <br>
 *   https://github.com/salamander2/HSA2
 * <p>
 * @author Tom West
 * @author Michael Harwood
 * @version 4.5
 * Changes: switch to using JOptionPane and remove CloseableDialog.java
 * 			change accessibility from public to default so that it can only be called by other HSA2 classes.
 */

class FatalError
{
	/**
	 * Contructor - FatalError to be displayed on centre of screen.
	 *
	 * @param message The message to be displayed in the FatalError dialog box.
	 */
	public FatalError (String message)
	{
		this (message, null);
	}
	/**
	 * Contructor - FatalError to be displayed on centre of a specified Frame.
	 *
	 * @param message The message to be displayed in the FatalError dialog box.
	 * @param frame The Frame that the dialog box should be centred on.
	 */
	public FatalError (String message, JFrame frame)
	{
		JOptionPane.showMessageDialog(null, message,"Fatal Error", JOptionPane.ERROR_MESSAGE + JOptionPane.OK_OPTION);
		System.exit (0);
	}
} /* FatalError class */
