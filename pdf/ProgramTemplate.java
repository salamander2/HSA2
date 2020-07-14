/* HSA2 Graphics Template program 
 *
 * This is a template for you to copy and use in your programs. You copy the file to a new
 * file name, rename a few things below, and then add your code and change these comments.
 * 
 * @author M. Harwood
 */

package YOUR_PACKAGE_NAME_HERE; // CHANGE THIS to match the name of the package (folder) that code is in

import hsa2.GraphicsConsole;     // this imports the code from the hsa2 graphics library
import java.awt.Color;   	 // this imports standard Java code for using colors and fonts
import java.awt.Font;

public class ProgramTemplate	//there are three places where THIS name NEEDS TO BE CHANGED when you make a new class. (1)
{
  public static void main(String[] args) {
    new ProgramTemplate(); // this is the name of your class. CHANGE HERE (2)
  }

  //Global variables can go here:
  GraphicsConsole gc = new GraphicsConsole(800, 600, "Drawings");

  ProgramTemplate() { //This is the constructor. Name of class. CHANGE HERE (3)
    //all drawing goes here
	gc.setBackgroundColor(Color.YELLOW);
	gc.clear();
	gc.drawLine(50,50,300,100); //will draw in black since we haven't changed the foreground colour
    //.....
  }
  
}
