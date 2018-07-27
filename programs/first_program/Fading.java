package graphics1;


import hsa2.GraphicsConsole;
import java.awt.Color;

public class Fading {

	public static void main(String[] args) {
			new Fading();
	}
	
	GraphicsConsole gc = new GraphicsConsole (800,600, "Spots");

	Fading(){
		//Setup
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null); //centre window
//		gc.setBackgroundColor(Color.DARK_GRAY);
		gc.setBackgroundColor(new Color(120,120,120,10));
		gc.clear();
		gc.setColor(Color.RED);
		
		//variables:
		int size = 40;
		int sleepTime = 5; // in milliseconds
		
		//main loop
		while(true) {
			gc.clear();
			
			//make random numbers for location (based on 800x600)
			int rx = (int) ((Math.random()* 700)+50);
			int ry = (int) ((Math.random()* 500)+50);

			gc.fillOval(rx,ry,size,size);

			/*			
			//make random colour for next ball
			int r = (int) (Math.random()* 256);
			int g = (int) (Math.random()* 256);
			int b = (int) (Math.random()* 256);
			gc.setColor(new Color(r,g,b));
			*/

			// Here’s a special way to make brighter random colours:
			gc.setColor(new Color(Color.HSBtoRGB((float)Math.random(), 1.0f, 1.0f)));

			gc.sleep(sleepTime); //you must have a sleep to allow the graphics to be updated
		}
	}
} //end of class
