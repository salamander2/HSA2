package animation;

/* This is a tutorial to demonstrate how to make a moderately good animation game using HSA2 graphics. 
 * We'll be making a block breaking game that's flexible enough to be extended as you wish.
 */

/***** STEP 1 ------- Drawing a circle, using variables for its location. ****************** 
    This is pretty much straight forward.
    I've put the program into two separate methods because we'll be using this way of organizing our logic later.
*******************************************************************************************/

//Always check to make sure that Eclipse hasn't added in some weird import. It tends to do that when you cut and paste code.
import hsa2.GraphicsConsole;
import java.awt.Color;

public class AnimationMain {

	public static void main(String[] args) {
		new AnimationMain();
	}
	
	/***** Global (instance) Variables ******/
	GraphicsConsole gc = new GraphicsConsole (800,600);
	int ballx, bally;
	int ballSize = 30;
	
	/****** Constructor ********/
	AnimationMain() {
		initialize();	//ie. setup.
		drawGraphics();
	}
	
	/****** Methods for game *******/	

	/* Method: initialize()
	 * These are things that are only done once. 
	 * They should not be done over and over in a loop as they will either slow the program down or screw it up.
	 * Putting all of the initialization in a separate method is useful because then it is really easy to restart the game.
	 */
	void initialize() {
		ballx = gc.getDrawWidth()/2;
		bally = 100;
		gc.setTitle("Brick breaker game");
		gc.setAntiAlias(true);
		gc.setBackgroundColor(Color.BLACK);
		gc.clear();	//this must be done for the background colour to take effect
	}
	
	/* Method: drawGraphics()
     * ALL graphics drawing must be done here. 
     * Do not draw on gc anywhere else in your program (if you want it to work properly)
     */
	void drawGraphics() {
		gc.setColor(Color.RED);
		gc.fillOval(ballx, bally, ballSize, ballSize);
	}
}

