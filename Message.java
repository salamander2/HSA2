package hsa_ufa;

/**
 * The Message class displays a dialog box with a message in it until the
 * user clicks the OK button.  Do not instantiate a Message from an
 * action or paint method, as the constructor does not return until
 * until the OK button is pressed (or the window is closed).
 * <p>
 * Full documentation for the classes in the hsa package available at:
 * <br>
 *                      http://www.holtsoft.com/java/hsa_package.html
 * <p>
 * @author Tom West
 * @version 2.0 99/02/01
 */

import java.awt.*;

public class Message extends CloseableDialog
{
  private static int MINIMUM_WIDTH = 200;
  
  private Button okButton;
  
  
  /**
   * Contructor - Message to be displayed on centre of screen.
   *
   * @param message The message to be displayed in the Message dialog box.
   */
  public Message (String message)
  {
    this (message, "", null);
  } // Constructor - Message (String)
  
  
  /**
   * Contructor - Message to be displayed on centre of a specified Frame.
   *
   * @param message The message to be displayed in the Message dialog box.
   * @param frame The Frame that the dialog box should be centred on.
   */
  public Message (String message, Frame frame)
  {
    this (message, "", frame);
  } // Constructor - Message (String, Frame)
  
  
  /**
   * Contructor - Message to be displayed on centre of screen with a specified title.
   *
   * @param message The message to be displayed in the Message dialog box.
   * @param title The title of the Message dialog box.
   */
  public Message (String message, String title)
  {
    this (message, title, null);
  } // Constructor - Message (String, String)
  
  
  /**
   * Contructor - Message to be displayed on centre of a specified Frame.
   *
   * @param message The message to be displayed in the Message dialog box.
   * @param frame The Frame that the dialog box should be centred on.
   * @param title The title of the Message dialog box.
   */
  public Message (String message, String title, Frame frame)
  {
    // Create the dialog.
    super (frame, title);
    
    setBackground (Color.lightGray);
    
    // Put the message at the top
    this.add ("Center", new Label ("  " + message + "  ", Label.CENTER));
    
    // Put OK button at bottom
    okButton = new Button ("OK");
    okButton.addActionListener (this);
    Panel p = new Panel ();
    p.setLayout (new FlowLayout (FlowLayout.CENTER, 0, 0));
    p.add (okButton);
    add ("South", p);
    
    pack ();
    
    positionDialog (frame);
    
    Message.beep ();
    
    setVisible (true);
  } // Constructor - Message (String, String, Frame)
  
  
  /**
   * Causes the speaker to beep.
   */
  public static void beep ()
  {
    Toolkit.getDefaultToolkit ().beep ();
  } // beep (void)
  
  
  /**
   * Specifies the preferred size of the dialog.
   */
  public Dimension getPreferredSize ()
  {
    Dimension d = super.getPreferredSize ();
    d.width = Math.max (MINIMUM_WIDTH, d.width);
    return d;
  } // Dimension preferredSize (void)
} /* Message class */
