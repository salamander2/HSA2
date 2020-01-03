/**********************************************************************************
 * Sydney S
 * November 3, 2017. ICS3U1
 * Purpose: Create a game in which the user must survive in a maze for 15 seconds
 * without hitting a moving ball or the yellow maze walls
 * Game play: click to start. Move using the mouse.
 **********************************************************************************/
// Copyright 2018. You are not permitted to copy or to use this program without the permission of the author. 
// The program is being published for illustrative purposes only.

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import ca.quarkphysics.hsa2.GraphicsConsole;

public class MazeCraze implements ActionListener {
	 class BallMaze extends Rectangle {
			
			//this inherits the rectangle variables : x,y,width,height
			
			int vx = -7;
			int vy = 5;	//positive means that it starts by going down
			
			Color clr = Color.CYAN;
			
			BallMaze(int x, int y){
				this.x = y; 
				this.y = y;
				width = height = 40;
			}
			
	}

	public static void main(String[] args) {
		new MazeCraze();
	}

	//global variables
	GraphicsConsole gc = new GraphicsConsole(SCRW,SCRH);

	//screen size variables
	final static int SCRW=800;
	final static int SCRH=600;
	final static int SLEEP = 5;
	final static int TIMERSPEED = 1000;



	Rectangle r1 = new Rectangle(0, 0, 500, 100);
	Rectangle r2 = new Rectangle(500, 0, 100, 300);
	Rectangle r3 = new Rectangle(100, 300, 200, 200);
	Rectangle r4 = new Rectangle(SCRW-100, SCRH/2-200, SCRW, SCRH);
	BallMaze ball = new BallMaze(200,100);
	Rectangle mouse = new Rectangle(SCRW/2, SCRH,15, 20);
	
	Timer timer = new Timer(TIMERSPEED, this);
	int seconds = 0;



	MazeCraze() {

		setup();

		drawGraphics();
		gc.setTitle("Click to Start! (Stay on the black)");
		//click to start
		while (gc.getMouseClick() == 0 ) {
			gc.sleep(100);
		}
		gc.setTitle("Playing! (Don't let the ball hit you)");

		int n=0;
		while(true) {


			//move ball
			moveBall();

			randomSpeedChange();
			
			//update mouse
			mouse.x = gc.getMouseX();
			mouse.y = gc.getMouseY();

			//see if mouse hits rectangle
			if (mouse.intersects(r1)) {
				break;
			}

			if (mouse.intersects(r2)) {
				break;
			}

			if (mouse.intersects(r3)) {
				break;
			}

			if (mouse.intersects(r4)) {
				break;
			}

			if (mouse.intersects(ball)) {
				break;
			}

			//ball bouncing on maze walls

			ballHitRectangle(ball,r1);
			ballHitRectangle(ball,r2);
			ballHitRectangle(ball,r3);
			ballHitRectangle(ball,r4);


			drawGraphics (); 
			gc.sleep(SLEEP);
		}
		//game over text
		Font myFont = new Font("Monospaced", Font.ITALIC, 50);
		gc.setFont(myFont);
		gc.drawString("Game Over", SCRW/3, SCRH/2);
	}

	//setup
	void setup() { 
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null);
		gc.setBackgroundColor(Color.BLACK);
		gc.enableMouse();
		gc.enableMouseMotion();
		timer.start();
	}
	
	void randomSpeedChange() {
		if (seconds%3 == 0 ) {
			ball.vx = (int) (ball.vx + Math.random()*4);
			gc.sleep(40);
		}
	}
	
	
	//graphics
	void drawGraphics() {
		synchronized(gc) {
			gc.clear();
			gc.setColor(Color.YELLOW);

			//draw rectangles
			//gc.fillRect(0, 0, 500, 100);
			gc.fillRect(r1.x, r1.y, r1.width, r1.height);
			gc.fillRect(r2.x, r2.y, r2.width, r2.height);
			gc.fillRect(r3.x, r3.y, r3.width, r3.height);
			gc.fillRect(r4.x, r4.y, r4.width, r4.height);

			//draw ball
			gc.setColor(Color.cyan);
			gc.fillOval(ball.x, ball.y, ball.width, ball.height);

			//draw mouse
			gc.setColor(Color.RED);
			gc.drawRect(mouse.x, mouse.y, mouse.width, mouse.height);
			
			//draw time
			gc.drawString("Time=" +seconds, 100, 10);
		}		
	}
	//move ball
	void moveBall() {
		ball.x = ball.x + ball.vx;
		ball.y = ball.y + ball.vy;
		//bounce off screen
		if (ball.x < 0) ball.vx = -ball.vx;
		if (ball.y < 0) ball.vy = -ball.vy;
		if (ball.x > SCRW-ball.width) ball.vx = -ball.vx;
		if (ball.y > SCRH-ball.height) ball.vy = -ball.vy;



	}

	void ballHitRectangle (BallMaze b, Rectangle r ) {

		//here we treat the ball as a rectangle 
		if (b.intersects(r)) {

			//top bounce

			/* this just sees if part of the ball touches the rect. 
			 The whole ball could be below, as when it is moving down and to the left and hits the middle of the left side	 */
			//if (b.y + b.height > r.y && b.vy > 0) {

			if (b.y + b.height > r.y && b.y < r.y && b.vy > 0) {
				//is the whole ball between the left and right sides of the box?
				if (b.x > r.x && b.x+b.width < r.x+r.width) {

					b.x -= b.vx;	//undo last move
					b.y -= b.vy;

					b.vy = -b.vy;					
					return;
				}
			}

			//hit from left (left side of block)
			if (b.x + b.width > r.x && b.vx > 0) {	//moving right 
				if (b.y > r.y && b.y + b.height < r.y + r.height) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					

					b.vx = -b.vx;					
					return;							
				}
			}
			//hit bottxom (moving upwards)
			if (b.y < r.y+r.height && b.vy < 0){ 				
				if (b.x > r.x && b.x+b.width < r.x+r.width) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					

					b.vy = -b.vy;					
					return;
				}
			}
			//hitting right side, moving from left
			if (b.x < r.x+r.width && b.vx < 0){
				if (b.y > r.y && b.y + b.height < r.y + r.height) {
					b.x -= b.vx;	//undo last move
					b.y -= b.vy;					

					b.vx = -b.vx;					
					return;
				}

			}


			/* ****************************
			 * Cases for hitting corners: *
			 ******************************/

			//undo last move
			b.x -= b.vx;	
			b.y -= b.vy;

			//Find centre of rect:
			int crX = r.x + r.width/2;
			int crY = r.y + r.height/2;

			/* Find which corner ball is bouncing off of, then bounce the ball away from the corner */
			//top left corner
			if (b.x+b.width < crX && b.y + b.height < crY) {	
				b.vx = - Math.abs(b.vx);
				b.vy = - Math.abs(b.vy);
				return;
			}

			//top right corner
			if (b.x > crX && b.y + b.height < crY) {	
				b.vx = + Math.abs(b.vx);
				b.vy = - Math.abs(b.vy);
				return;
			}

			//bottom left corner
			if (b.x + b.width < crX && b.y > crY) {	
				b.vx = - Math.abs(b.vx);
				b.vy = + Math.abs(b.vy);
				return;
			}

			//bottom right corner
			if (b.x > crX && b.y > crY) {	
				b.vx = + Math.abs(b.vx);
				b.vy = + Math.abs(b.vy);
				return;
			}
		}
	}
	
	@Override 
	public void actionPerformed(ActionEvent ev) {
		seconds++;
	}




} //end of class

/* Marking:

 + commment: author
 + lots comments in code (beyond what the teacher wrote) 

 X unclear what to do. Have some instructions on the screen (I've added them to the title)

 + method names make sense
 + variable names all make sense -- except for BallMaze - this sounds like it's a maze for a ball.
 + capitalization  correct
 + indenting good. 
 + Vertical spacing good

whate about making the font for the Time bigger too

Awesome idea. It's not clear if there is time limit or not.
*/
