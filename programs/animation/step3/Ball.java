package animation;

/***** STEP 3 : Ball class ******/

import java.awt.Color;
import java.awt.Rectangle;


public class Ball extends Rectangle
{
	//Do not put superclass variables here: x,y,width,height belong to the Rectangle parent class
	int xspeed = 3;	
	int yspeed = 3;
	int diameter = 40;
	Color colour = Color.RED;
	//int x,y,width,height;  <<< EXTREMELY IMPORTANT: This line MUST now be deleted. Rectangles come with their own copy of these 4 variables, so don't recreate them.
	
	//this is to save the screen dimensions so that we can reset the ball
	private int savX,savY;
	
	/**
	 * Ball constructor:
	 * The ball will be placed randomly within the rectangle specified by the parameters below.
	 * The ball will be at least 75 pixels from the edge of this rectangle.
	 * @param maxX The range of X values for the ball to be placed
	 * @param maxY The range of Y values for the ball to be placed
	 */
	Ball(int maxX, int maxY) {
		
		width=height=diameter;

		x = (int)(Math.random() * (maxX-150))+75;
		y = (int)(Math.random() * (maxY-150))+75;
		
		// uncomment the following line if you want each ball to have a different random colour
		//colour = new Color(Color.HSBtoRGB((float)Math.random(), 1.0f, 1.0f)); 
		savX = maxX; savY = maxY;
	}

	/** This method repositions the ball so that it starts at a random place near the top of the 
	 * screen, just as it did when it was made in the constructor.
     * The speed is reset to the original speed too (but the xspeed can go in either direction).
     * This method is called everytime that the player loses a life.
     */
	void resetXY() {
		x = (int)(Math.random() * (savX - 150))+75;
		y = (int)(Math.random() * (savY - 150))+75;
		xspeed = yspeed = 3;	
		if (Math.random() < 0.5) xspeed *= -1;	
	}
}
