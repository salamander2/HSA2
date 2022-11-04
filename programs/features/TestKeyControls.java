package testing;
/* This program is to show the new methods in GraphicsConsole that track the last
 * WASD key and the last Arrow key pressed: getLastWASD() and getLastArrow().
 * 
 * This allows responsive motion by using isKeyDown(), but if two keys are held down,
 * instead of moving diagonally, the most recent key takes precedence.
 * 
 * There is still one PROBLEM:
 *  If you hold S(down) and then press D(right) and then release D, the ball stops.
 *  This is because while isKeyDown('S') is still true, it is no longer the lastWASD key.
 *  'D' was, but now 'D' is no longer held down. 
 *  SOLUTION: I have to add code to ConsoleCanvas to track the order in which keys are released. 			
 */


import java.awt.Color;
import java.awt.Rectangle;

import hsa2.GraphicsConsole;

public class TestKeyControls {

	public static void main(String[] args) {
		new TestKeyControls();
	}

	static final int WINW = 900;
	static final int WINH = 700;
	GraphicsConsole gc = new GraphicsConsole(WINW, WINH);

	TestBall ball1 = new TestBall(100,100,40,40);
	TestBall ball2 = new TestBall(300,500,60,60);

	TestKeyControls() {
		setupGraphics();
		gc.clear();

		/* There are various moveBall methods that you can examine and try.
		 *   moveBall(Ball b) is the normal one to move the ball and make it bounce
		 *   moveBall1_keys() uses isKeyDown() by itself. 
		 *   	This allows diagonal movement, but can't detect which key was the last one pressed.
		 *   moveBall1_WASD() uses getLastWASD() and isKeyDown() to move ball 1 - only in x and y directions.
		 *   moveBall1_Arrows() is the same, but with arrow keys.
		 */
		
		while(true) {
			//			moveBall1_keys();
			moveBall1_WASD();
			moveBall2_Arrows();
			//			moveBall(ball1);
			//			moveBall(ball2);
			drawGraphics();
			gc.sleep(5);
		}
	}

	void setupGraphics() {
		gc.setTitle("Test WASD and Arrow keys");
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null);
		gc.setBackgroundColor(Color.BLACK);
	}

	void moveBall1_keys() {
		gc.setTitle("" + gc.getKeyCode());

		//Problem: while this is very responsive it ALWAYS allows diagonal movement. Something can't allow that (e.g. Snake game)
		if (gc.isKeyDown('A') && ball1.x > 0) ball1.x -= ball1.xspeed;
		if (gc.isKeyDown('D') && ball1.x + ball1.width < WINW) ball1.x += ball1.xspeed;
		if (gc.isKeyDown('W') && ball1.y > 0) ball1.y -= ball1.yspeed;
		if (gc.isKeyDown('S') && ball1.y + ball1.height < WINH) ball1.y += ball1.yspeed;
	}
	
	//As long as a WASD key is down ball1 will move in that direction. 
	//It will only move in the direction of the most recent WASD key that is held down.
	void moveBall1_WASD() {
		switch (gc.getLastWASD()){
		case 'W':
			if (gc.isKeyDown('W') && ball1.y > 0) ball1.y -= ball1.yspeed;
			break;
		case 'A':
			if (gc.isKeyDown('A') && ball1.x > 0) ball1.x -= ball1.xspeed;
			break;
		case 'S':
			if (gc.isKeyDown('S') && ball1.y + ball1.height < WINH) ball1.y += ball1.yspeed;
			break;
		case 'D':
			if (gc.isKeyDown('D') && ball1.x + ball1.width < WINW) ball1.x += ball1.xspeed;
			break;
		}

	}

	//As long as an arrow key is down ball2 will move in that direction. 
	//It will only move in the direction of the most recent arrow key that is held down.
	void moveBall2_Arrows() {		
		switch (gc.getLastArrow()){
		case 38:
			if (gc.isKeyDown(38) && ball2.y > 0) ball2.y -= ball2.yspeed;
			break;
		case 37:
			if (gc.isKeyDown(37) && ball2.x > 0) ball2.x -= ball2.xspeed;
			break;
		case 40:
			if (gc.isKeyDown(40) && ball2.y + ball2.height < WINH) ball2.y += ball2.yspeed;
			break;
		case 39:
			if (gc.isKeyDown(39) && ball2.x + ball2.width < WINW) ball2.x += ball2.xspeed;
			break;
		}	
	}

	/* This is the original code that will bounce balls around the screen. */
	void moveBall(TestBall b) {
		b.x += b.xspeed;
		b.y += b.yspeed;


		if(b.x < 0 && b.xspeed < 0) {
			b.xspeed *= -1;
		}

		if(b.x + b.height > WINW && b.xspeed > 0) {
			b.xspeed *= -1;
		}

		if(b.y < 0 && b.yspeed < 0) {
			b.yspeed *= -1;
		}

		if(b.y + b.height > WINH && b.yspeed > 0) {
			b.yspeed *= -1;
		}

	}

	void drawGraphics() {
		synchronized(gc) {
			gc.clear();
			gc.setColor(Color.RED);
			gc.fillOval(ball1.x, ball1.y, ball1.width, ball1.height);
			gc.setColor(Color.YELLOW);
			gc.fillOval(ball2.x, ball2.y, ball2.width, ball2.height);
		} 
	}

	//cannot be accessed from an object outside the class
	private class TestBall extends Rectangle {
		int xspeed = 3;
		int yspeed = 3;

		TestBall(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			width=w;
			height = h;
		}
	}
}