package animation;

/***** STEP 5 ------Draw Blocks -------------- 

Again there are 3 steps: 
	(i)   create the block objects, 
	(ii)  draw them on the screen, 
	(iii) then see if they get hit by the ball. (and see if you have won the game).

I've only drawn 5 blocks. You should make a grid of them. 
You can use modulus (%) and integer division (/) to find the row and columns. 
e.g. if you had 10 blocks in each row, then for block #43, row = n / 10 (which returns 4) and col = n%10 (which returns 3). 
The column is used to set the X values, the row is used to set the Y values.

When a block is hit, there are various things that can be done:

   A. If all of the blocks are stored in an ArrayList, you can just delete the block.
      You can't do this with Arrays though.
   B. If you store the blocks in an array, you can do two things: 
		(i) set isVisible to false 
		(ii) set the y coordinate of the block to -100. This moves the block up off of the top of the screen.

		Method (ii) is better because the block will never collide with the ball since the ball never goes off screen. 
		This saves time because you don't have to worry about colliding with invisible blocks. 
		I am still using isVisible, but I don't really need to. I could replace it with if (ball.y < 0) ...

Winning and losing. I've more or less got this working, but if you type "Q" it says that you win.
DO NOT MAKE THINGS PUBLIC UNLESS YOU HAVE TO. Always restrict the scope of variables and methods as much as possible.

So, I've made as many things as possible private. 
I can't make the variables in the Ball and Block class private unless I write getter and setter methods, 
which is quite easy, but I'll leave this as an excercise for the student.
 *********************************************************/

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
	static final Color PADDLECOLOUR = Color.YELLOW;
	static final int NUMBLOCKS = 5;			//set the number of blocks here

	/***** Global (instance) Variables ******/
	private GraphicsConsole gc = new GraphicsConsole (GRWIDTH, GRHEIGHT);
	private Ball ball = new Ball(GRWIDTH, 200);
	private Rectangle paddle = new Rectangle(0,0,100,16);	//set width and height here
	private Block[] blocks = new Block[NUMBLOCKS];		//this just makes the array, it doesn't create the blocks!
	private int lives;
	private boolean isPlaying = true;

	/****** Constructor ********/
	private AnimationMain() {
		initialize();

		//MAIN GAME LOOP
		while (gc.getKeyCode()  != 'Q' && isPlaying) {		//press q to quit

			//movePaddle_mouse();
			movePaddle_keys();

			moveBall();

			drawGraphics();

			gc.sleep(SLEEPTIME);

			if (lives <= 0) isPlaying = false;

			//check if all of the blocks are gone.
			boolean win = true;
			for (int i=0; i < NUMBLOCKS; i++) {	
				if (blocks[i].isVisible) win = false;  
			}
			if (win) isPlaying = false;
		}

		//Ending the game		
		if (lives > 0)
			gc.drawString("GAME OVER, You win!", 30, 30);
		else 
			gc.drawString("GAME OVER, You lost.", 30, 30);

		gc.sleep(3000);
		gc.close(); //close the window and end everything

	}

	/****** Methods for game *******/
	
	private void initialize() {
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
		paddle.x = GRWIDTH/2;
		paddle.y = GRHEIGHT - 100;
		ball.resetXY(); // This is totally unnecessary unless you restart and need to reset the ball position and speed.

		//make all blocks. ** I"m only making one row of 6 blocks. You can figure out how to make more.
		for (int i=0; i < NUMBLOCKS; i++) {		//instead of NUMBLOCKS I could use blocks.length
			blocks[i] = new Block();
			if (i < 6) {
				blocks[i].x = 120*i+30;
				blocks[i].y = 60;			
			}
		}

		gc.sleep(500); // allow a bit of time for the user to move the mouse to the correct position in the game screen
	}

	/**
	 * This method moves the ball and handles all collisions where the ball hits something.
	 * Don't make a separate method to see if the paddle hits the ball.
	 */
	private void moveBall() {
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
			ball.yspeed++; //speed up the ball each time it hits the top

		}
		//left side of screen
		if (ball.x < 0) {
			ball.xspeed *=-1;
		}

		//check if ball hits paddle
		if (ball.intersects(paddle)) {
			if (ball.yspeed > 0 ) {			//the ball must be moving downwards, not upwards
				ball.yspeed *=-1;
			}
		}	

		//see if ball hits block
		for (int i=0; i < blocks.length; i++) {
			if (ball.intersects(blocks[i])) {
				blocks[i].isVisible = false;	//don't bother drawing it on the screen
				blocks[i].y = -100;			//move it off the screen
				ball.yspeed *= -1;
			}
		}


	}	

	private void movePaddle_mouse(){
		paddle.x = gc.getMouseX() - paddle.width/2;
	}

	private void movePaddle_keys(){
		int moveAmount = 7;
		//37 and 39 are the keyboard codes for the left and right arrow keys.
		if (gc.getKeyCode() == 37) paddle.x -= moveAmount;
		if (gc.getKeyCode() == 39) paddle.x += moveAmount;

		//check to prevent moving the paddle off the screen
		if (paddle.x < 0) paddle.x = 0;
		//now you need to figure out how to to the same for the right side of the screen (I did the easy one!)
		//...
	}

	private void drawGraphics() {

		synchronized(gc) {
			gc.clear();	

			gc.setColor(Color.WHITE);
			gc.drawString("LIVES = " + lives, 500, 30);
			gc.setColor(ball.colour);
			gc.fillOval(ball.x, ball.y, ball.width, ball.height);

			//draw the blocks
			for (int i=0; i < blocks.length; i++) {
				if (blocks[i].isVisible) {
					gc.setColor(blocks[i].colour);
					gc.fillRect(blocks[i].x, blocks[i].y, blocks[i].width, blocks[i].height);
				}
			}

			gc.setColor(PADDLECOLOUR);
			gc.fillRoundRect(paddle.x, paddle.y, paddle.width, paddle.height, 10,10);
		}
	}
}
