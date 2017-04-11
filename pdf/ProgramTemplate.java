/* HSA2 Graphics Template program 
 *
 * This is a template for you to copy and use in your programs. You copy the file to a new
 * file name, rename a few things below, and then add your code and change these comments.
 * 
 * @author M. Harwood
 */

package YOUR_PACKAGE_NAME_HERE; // change this to match the name of the package (folder) that code is in

import hsa2.GraphicsConsole;     // this imports the code from the hsa2 new package
import java.awt.Color;   // this imports code for using colors and fonts
import java.awt.Font;

public class ProgramTemplate	//there are three places where this name needs to be changed when you make a new class. (1)
{
  public static void main(String[] args)
  {
    new ProgramTemplate(); // this is the name of your class (2)
  }

  //Global variables here:
  GraphicsConsole gc = new GraphicsConsole(800, 600, "Drawings");

  ProgramTemplate(){ //constructor. Name of class. (3)
    //all drawing goes here

	gc.setBackgroundColor(Color.BLACK);
    gc.clear();
    //.....
  }
  
}
