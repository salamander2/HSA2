/*** 
Game: move the ball from the bottom to the top without getting hit by the white rectangles
	  use the cursor keys to move.

Author: James P., ICS3U1, February 2018

Copyright 2018. You are not permitted to copy or to use this program without the permission of the author. 
The program is being published for illustrative purposes only.
 ***/


import java.awt.Color;
import java.awt.Rectangle;

import hsa2.GraphicsConsole;

public class BallCross {

	public static void main(String[] args) {
		new BallCross();
	}

	final static int SCRW=800;
	final static int SCRH=900;
	final static int SLEEP = 5;
	GraphicsConsole gc = new GraphicsConsole(SCRW,SCRH);

	Ball b;
	Obstacle l1,l2,l3,l4,o1,o2,o3,o4,o5,o6;

	BallCross(){
		setup();

		// main loop
		while(true) {
			moveBall();
			drawGraphics();
			gc.sleep(SLEEP);
			moveO1();
			moveO2();
			moveO3();
			moveO4();
			moveO5();
			moveO6();
			checkCollide();
			// if number returned is 1, breaks loop and proceed to show dialog
			if (conditionWin() > 0) break;
		}

		// dialog box popup when win
		gc.showDialog("You win", "Game End");
		gc.close();
	}

	void setup(){
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null);	
		gc.setTitle("Ball Cross");
		gc.setBackgroundColor(Color.BLACK);
		b = new Ball();
		l1 = new Obstacle();
		l1.width = 370;
		l1.height = 1;
		l1.x = 0;
		l1.y = 100;
		l2 = new Obstacle();
		l2.width = 430;
		l2.height = 1;
		l2.x = 800-370;
		l2.y = 100;
		l3 = new Obstacle();
		l3.width = 370;
		l3.height = 1;
		l3.x = 0;
		l3.y = 750;
		l4 = new Obstacle();
		l4.width = 430;
		l4.height = 1;
		l4.x = 800-370;
		l4.y = 750;
		o1 = new Obstacle();
		o1.height = 200;
		o1.x = 100;
		o1.y = 100;
		o2 = new Obstacle();
		o2.x = 500;
		o2.y = 500;
		o3 = new Obstacle();
		o3.width = 100;
		o3.height = 25;
		o3.x = 700;
		o3.y = 300;
		o4 = new Obstacle();
		o4.width = 250;
		o4.height = 25;
		o4.x = 500;
		o4.y = 700;
		o5 = new Obstacle();
		o5.width = 300;
		o5.height = 25;
		o5.x = 130;
		o5.y = 170;
		o6 = new Obstacle();
		o6.width = 25;
		o6.height = 400;
		o6.x = 700;
		o6.y = 300;

	}

	// arrows keys to move ball
	void moveBall() {
		if(gc.isKeyDown(37)) b.x -= b.speed; //left
		if(gc.isKeyDown(39)) b.x += b.speed; //right
		if(gc.isKeyDown(38)) b.y -= b.speed; //up
		if(gc.isKeyDown(40)) b.y += b.speed; //down

		if (b.x < 0) b.x = 0;
		if (b.x > gc.getDrawWidth() - b.width) b.x = gc.getDrawWidth() - b.width;
		if (b.y < 0) b.y = 0;
		if (b.y > gc.getDrawHeight() - b.height) b.y = gc.getDrawHeight() - b.height;
	}

	// obstacle movement 1 - 6
	void moveO1() {
		o1.y += o1.speed;

		if (o1.y < 100) o1.speed = -o1.speed;
		if (o1.y > 750-o1.height) o1.speed = -o1.speed;
	}

	void moveO2() {
		o2.y += o2.speed;

		if (o2.y <= 100) o2.speed = -o2.speed;
		if (o2.y >= 750-o2.height) o2.speed = -o2.speed;
	}

	void moveO3() {
		o3.x -= o3.speed;

		if (o3.x < 0) o3.speed = -o3.speed;
		if (o3.x > SCRW-o3.width) o3.speed = -o3.speed;
	}

	void moveO4() {
		o4.x -= o4.speed;

		if (o4.x < 0) o4.speed = -o4.speed;
		if (o4.x > SCRW-o4.width) o4.speed = -o4.speed;
	}

	void moveO5() {
		o5.y += o5.speed;

		if (o5.y <= 100) o5.speed = -o5.speed;
		if (o5.y >= 750-o5.height) o5.speed = -o5.speed;
	}

	void moveO6() {
		o6.y += o6.speed;

		if (o6.y < 100) o6.speed = -o6.speed;
		if (o6.y > 750-o6.height) o6.speed = -o6.speed;
	}
	// end of obstacle movement

	// ball hit obstacle
	void checkCollide() {
		if (b.intersects(l1)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(l2)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(l3)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(l4)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(o1)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(o2)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(o3)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(o4)) {
			b.x = 385;
			b.y = 850;
		}
		if (b.intersects(o5)) {
			b.x = 385;
			b.y = 850;
		}

		if (b.intersects(o6)) {
			b.x = 385;
			b.y = 850;
		}
	}

	// condition to win
	int conditionWin() {
		if (b.y == 40) return 1; // player wins
		return 0; // ongoing game
	}

	// draw graphics
	void drawGraphics(){
		// stop flickering
		synchronized(gc){
			gc.clear();
			gc.setColor(Color.WHITE);
			// ball
			gc.setColor(Color.YELLOW);
			gc.fillOval(b.x, b.y, b.width, b.height);
			// moving obstacles
			gc.setColor(Color.WHITE);
			gc.fillRect(o1.x, o1.y, o1.width, o1.height);
			gc.fillRect(o2.x, o2.y, o2.width, o2.height);
			gc.fillRect(o3.x, o3.y, o3.width, o3.height);
			gc.fillRect(o4.x, o4.y, o4.width, o4.height);
			gc.fillRect(o5.x, o5.y, o5.width, o5.height);
			gc.fillRect(o6.x, o6.y, o6.width, o6.height);
			// spawn point and win point
			gc.setColor(Color.RED);
			gc.drawOval(385-10, 850-10, 50, 50);
			gc.setColor(Color.GREEN);
			gc.drawOval(385-10, 25, 50, 50);
			gc.setColor(Color.WHITE);
			// still lines
			gc.fillRect(l1.x, l1.y, l1.width, l1.height);
			gc.fillRect(l2.x, l2.y, l2.width, l2.height);
			gc.fillRect(l3.x, l3.y, l3.width, l3.height);
			gc.fillRect(l4.x, l4.y, l4.width, l4.height);
		}
	}


	class Ball extends Rectangle {

		int speed = 5;

		Ball() {
			x = 385;
			y = 850;
			width = 30;
			height = 30;
		}
	}
	class Obstacle extends Rectangle {

		int speed = (int)(Math.random() * 10) + 5;

		Obstacle() {
			width = 25;
			height = 100;
		}
	}
}

/* Marking: 100%

 + comments: author etc.
 X NO instructions - what keys to use, etc. What the purpose is
 ... some comments in code (beyond what the teacher wrote). More would be better

 + method names make sense
 + variable names all make sense
 + capitalization correct
 + indenting correct 

Very imaginative and clever.
 */
