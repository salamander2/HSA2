package hsa_ufa;

/**
 * Internal to hsa package.
 * <p>
 * The CloseableDialog class is the base class for Dialog objects
 * created by the hsa package.  Dialogs based on this class will
 * close when the dialog's close box is clicked.
 * <p>
 * Full documentation for the classes in the hsa package available at:
 * <br>
 *                      http://www.holtsoft.com/java/hsa_package.html
 * <p>
 * @author Tom West
 * @version 2.0 99/02/01
 */
class CloseableDialog extends java.awt.Dialog
  implements java.awt.event.ActionListener,
  java.awt.event.WindowListener
{
  /**
   * Creates a closeable dialog with a close box that closes the dialog.
   * This constructor allows parent to be null if desired.
   *
   * @param parent The parent of the dialog
   * @param title The window title of the dialog
   */
  public CloseableDialog (java.awt.Frame parent, String title)
  {
    super (parent == null ? new java.awt.Frame ():
             parent, title, true);
    setResizable (false);
    addWindowListener (this);
  } // Constructor - CloseableDialog (Frame, String)
  
  
  /**
   * Closes the dialog. Used as the default action for an OK button.
   *
   * @param e The action event
   */
  public void actionPerformed (java.awt.event.ActionEvent e)
  {
    dispose ();
  } // actionPerformed (ActionEvent)
  
  
  /**
   * Places the dialog in the centre of the window passed in, or in
   * the centre of the screen if no window is provided.
   *
   * @param window The window to centre the dialog on
   */
  public void positionDialog (java.awt.Window window)
  {
    java.awt.Dimension dlgSize = getSize ();
    java.awt.Point loc;
    if (window == null)
    {
      // Place frame in middle of the screen
      java.awt.Dimension screenSize = getToolkit ().getScreenSize ();
      loc = new java.awt.Point (
                                (screenSize.width - dlgSize.width) / 2,
                                (screenSize.height - dlgSize.height) / 3);
    }
    else
    {
      java.awt.Dimension windowSize = window.getSize ();
      java.awt.Point windowLoc = window.getLocation ();
      loc = new java.awt.Point (
                                windowLoc.x + (windowSize.width - dlgSize.width) / 2,
                                windowLoc.y + (windowSize.height - dlgSize.height) / 3);
    }
    setLocation (loc);
  } // positionDialog (Window)
  
  
  /**
   * Does nothing. Called by the system after the dialog has been activated.
   *
   * @param e The window event
   */
  public void windowActivated (java.awt.event.WindowEvent e)
  {
    // Event not handled.
  } // windowActivated (WindowEvent)
  
  
  /**
   * Does nothing. Called by the system after the dialog has been closed
   * (dispose () has been called).
   *
   * @param e The window event
   */
  public void windowClosed (java.awt.event.WindowEvent e)
  {
    // Event not handled.
  } // windowClosed (WindowEvent)
  
  
  /**
   * Closes the dialog. Called by the system when the close box of the
   * dialog has been clicked.
   *
   * @param e The window event
   */
  public void windowClosing (java.awt.event.WindowEvent e)
  {
    dispose ();
  } // windowClosing (WindowEvent)
  
  
  /**
   * Does nothing. Called by the system after the dialog has been deactivated.
   *
   * @param e The window event
   */
  public void windowDeactivated (java.awt.event.WindowEvent e)
  {
    // Event not handled.
  } // windowDeactivated (WindowEvent)
  
  
  /**
   * Does nothing. Called by the system after the dialog has been deminimized.
   *
   * @param e The window event
   */
  public void windowDeiconified (java.awt.event.WindowEvent e)
  {
    // Event not handled.
  } // windowDeiconified (WindowEvent)
  
  
  /**
   * Does nothing. Called by the system after the dialog has been minimized.
   *
   * @param e The window event
   */
  public void windowIconified (java.awt.event.WindowEvent e)
  {
    // Event not handled.
  } // windowIconified (WindowEvent)
  
  
  /**
   * Does nothing. Called by the system after the dialog has been opened
   * for the first time.
   *
   * @param e The window event
   */
  public void windowOpened (java.awt.event.WindowEvent e)
  {
    // Event not handled.
  } // windowOpened (WindowEvent)
} /* CloseableDialog class */
