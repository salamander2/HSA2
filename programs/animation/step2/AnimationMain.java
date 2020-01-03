package animation;

/* Tutorial: making a block breaking game using HSA2 library for Java  */

/***** STEP 2 ------ Making a ball move. Making it bounce off the edges of the screen. -------------- 
    The ball is a separate object (with a separate class Ball.java). 
    This is because the ball has properties that it should maintain and that we want to change. 
    We can also create multiple balls this way.

    Notice some new global variables: lives, isPlaying (this will be used to end the game)

    Notice some constants. These make it easy to change numbers in the code without having to do search and replace.
	Get used to using constants for these sort of things. I can change my screen size and EVERYTHING still works.

    A game loop has been added. This is a standard way of doing things.

    The sleep() function is ESSENTIAL. This allows the computer to do other things. It can be used to vary the speed of the game
    ball.xspeed and ball.yspeed are how many pixels the ball moves each time. If this number is larger, the ball moves faster. However, it also becomes more jerky because it actually jumps this number of pixels.  So keep the xspeed small and vary the game speed by changing SLEEPTIME. It is in milliseconds

	SYNCHRONIZED has been added to drawGraphics(). This reduces flickering during animation a lot (and is one reason why all drawing needs to be in only one place)

    Interesting, both a capital or lowercase Q will end the game.

	I've also made it so that when the ball hits the bottom of the screen, you lose a life and the ball changes colour.

Next time:

    1. I'm going to make a nicer font in the next stage so that the text looks better. 
    2. Notice how the ball actually goes off the screen on the right and bottom. This needs fixing. 
    3. Let's draw a rectangle to make sure that the ball actually starts in the place that we want it to.
**************************************************************************************************************/


//Always check to make sure that Eclipse hasn't added in some weird import. It tends to do that when you cut and paste code.
import ca.quarkphysics.hsa2.GraphicsConsole;
import java.awt.Color;

public class AnimationMain {

	public static void main(String[] args) {
		new AnimationMain();
	}

	/***** Constants *****/
	static final int SLEEPTIME = 10;
	static final int GRWIDTH = 800;
	static final int GRHEIGHT = 600;

	/***** Global (instance) Variables ******/
	GraphicsConsole gc = new GraphicsConsole (GRWIDTH, GRHEIGHT);
	Ball ball;
	int lives = 4;
	boolean isPlaying = true;

	/****** Constructor ********/
	AnimationMain() {
		initialize();

		//MAIN GAME LOOP
		while (gc.getKeyCode()  != 'Q' && isPlaying) {		//press q to quit

			moveBall();
			/* TODO: later we'll add in these methods */
			//movePaddle();	
			//checkCollisions();

			drawGraphics();

			gc.sleep(SLEEPTIME);

			if (lives <= 0) isPlaying = false;
		}
	
		//Ending the game
		gc.drawString("GAME OVER", 30, 30);
		gc.sleep(2000);
		gc.close(); //close the window and end everything
	}

	/****** Methods for game *******/	
	void initialize() {

		gc.setTitle("Brick breaker game");
		gc.setAntiAlias(true);
		gc.setBackgroundColor(Color.BLACK);

		ball = new Ball(GRWIDTH, 100); //the ball will start somewhere in this section of the screen (but at least 75 pixels from the edges of this region)
	}

	void moveBall() {
		ball.x += ball.xspeed;
		ball.y += ball.yspeed;
		
		//bounce off bottom of screen		
		if (ball.y > gc.getDrawHeight()) {
			ball.yspeed *=-1;
			lives--;
			ball.colour = new Color(Color.HSBtoRGB((float)Math.random(), 1.0f, 1.0f));
		}
		
		//right side of screen
		if (ball.x > gc.getDrawWidth()) {
			ball.xspeed *=-1;
		}
		//top of screen
		if (ball.y < 0) {
			ball.yspeed *=-1;
			ball.yspeed++;			
		}
		//left side of screen
		if (ball.x < 0) {
			ball.xspeed *=-1;
		}
	}
	
	/* Method: drawGraphics()
     * ALL graphics drawing must be done here. 
     * Do not draw on gc anywhere else in your program (if you want it to work properly)
     */
	void drawGraphics() {

		synchronized(gc) {
			gc.clear();		 //clear screen and then redraw everything
			gc.setColor(Color.WHITE);
			gc.drawString("LIVES = " + lives, 30, 70);
			gc.setColor(ball.colour);
			gc.fillOval(ball.x, ball.y, ball.width, ball.height);
		}
	}
}
