package graphics1;

import java.awt.Color;

import hsa2.GraphicsConsole;

public class Fading {

	public static void main(String[] args) {
			new Fading();
	}
	
	GraphicsConsole gc = new GraphicsConsole (800,600, "Spots");

	Fading(){
		//setup
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null); //centre window
//		gc.setBackgroundColor(Color.DARK_GRAY);
		gc.setBackgroundColor(new Color(120,120,120,10));
		gc.clear();
		gc.setColor(Color.RED);
		
		//variables:
		int size = 40;
		int sleepTime = 5;
		
		//main loop
		while(true) {
			gc.clear();
			
			//make random number
			int rx = (int) ((Math.random()* 700)+50);
			int ry = (int) ((Math.random()* 500)+50);
			gc.fillOval(rx,ry,size,size);
			
			//make random colour for next ball
			int r = (int) (Math.random()* 256);
			int g = (int) (Math.random()* 256);
			int b = (int) (Math.random()* 256);
			gc.setColor(new Color(r,g,b));
			
			gc.sleep(sleepTime);
		}
	}
}
