package animation;

/***** STEP 3 ------  Adding a paddle, moving it, collisions ---------

    Font: fixed up
    Ball bouncing off screen fixed. You need to figure out why this was happening and why the fix worked.
    The ball has a random x direction when it starts again.

	The Paddle

	The simplest way to see if things collide is if they are both rectangles.
	Notice that the Ball still has its own class, but the paddle doesn't. 
	They are both rectangles, but the paddle doesn't have any special properties, whereas the ball does. 
	Furthermore, we're never having more than one paddle. 
	If we were, it would be most convenient if we made the paddle in to a class.

Read the comments about the initialize() method in the code.

Paddle is set up to move by using the mouse or using the keyboard. 
Just uncomment whichever method you want to use. 
Note that the keyboard moving still needs a bit of fixing up: you can move the paddle off of the right side of the screen!

********************************************************************************************/

import ca.quarkphysics.hsa2.GraphicsConsole;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

public class AnimationMain {

	public static void main(String[] args) {
		new AnimationMain();
	}

	/***** Constants *****/
	static final int SLEEPTIME = 10;
	static final int GRWIDTH = 800;
	static final int GRHEIGHT = 600;
	static final Color PADDLECOLOUR = Color.YELLOW;	//so it's really easy to find and change when needed 

	/***** Global (instance) Variables ******/
	GraphicsConsole gc = new GraphicsConsole (GRWIDTH, GRHEIGHT);
	Ball ball = new Ball(GRWIDTH, 100);
	Rectangle paddle = new Rectangle(0,0,100,16);	//set width and height here
	int lives;
	boolean isPlaying = true;

	/****** Constructor ********/
	AnimationMain() {
		initialize();

		//MAIN GAME LOOP
		while (gc.getKeyCode()  != 'Q' && isPlaying) {		//press q to quit

			//movePaddle_mouse();
			movePaddle_keys();
			
			moveBall();
			
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
		//set up gc
		gc.setFont(new Font("Georgia", Font.PLAIN, 25));
		gc.setTitle("Brick breaker game");
		gc.setAntiAlias(true);
		gc.setBackgroundColor(Color.BLACK);
		gc.enableMouseMotion(); //only needed for mouse (obviously)
		gc.clear();
		
		//set up variables
		lives = 4;
		isPlaying = true;
		
		//set up objects
		//set paddle x and y here (or could be in the Rectangle constructor above)
		paddle.x = GRWIDTH/2; 	//or paddle.x = gc.getDrawWidth()/2;
		paddle.y = GRHEIGHT - 80;
				
		gc.sleep(500); // allow a bit of time for the user to move the mouse to the correct position in the game screen
	}

	/**
	 * This method moves the ball AND handles all collisions where the ball hits something.
	 * Don't make a separate method to see if the paddle hits the ball.
	 */
	void moveBall() {
		ball.x += ball.xspeed;
		ball.y += ball.yspeed;
		
		//bounce off bottom of screen		
		if ((ball.y + ball.diameter) > gc.getDrawHeight()) {
			ball.yspeed *=-1;
			lives--;
			ball.colour = new Color(Color.HSBtoRGB((float)Math.random(), 1.0f, 1.0f));
		}
		
		//right side of screen
		if ((ball.x + ball.diameter) > gc.getDrawWidth()) {
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
	

	void movePaddle_mouse(){
		paddle.x = gc.getMouseX() - paddle.width/2;
	}
	
	void movePaddle_keys(){
		int moveAmount = 7;
		if (gc.getKeyCode() == 37) paddle.x -= moveAmount;
		if (gc.getKeyCode() == 39) paddle.x += moveAmount;
		
		//check to prevent moving the paddle off the screen
		if (paddle.x < 0) paddle.x = 0;
		//now you need to figure out how to to the same for the right side of the screen (I did the easy one!)
	}
	
	/* Method: drawGraphics()
     * ALL graphics drawing must be done here. 
     * Do not draw on gc anywhere else in your program (if you want it to work properly)
     */
	void drawGraphics() {

		synchronized(gc) {
			gc.clear();	
			
			gc.setColor(Color.WHITE);
			gc.drawString("LIVES = " + lives, 30, 70);
			gc.setColor(ball.colour);
			gc.fillOval(ball.x, ball.y, ball.width, ball.height);
			gc.setColor(PADDLECOLOUR);
			gc.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
		}
	}
}

