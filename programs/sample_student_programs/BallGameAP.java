/*** 
Game: move the ball using the mouse. Catch the small yellow ball 
	  and do not let the bigger balls hit you as they follow you.

Author: Aiden P., ICS3U1, February 2018

Copyright 2018. You are not permitted to copy or to use this program without the permission of the author. 
The program is being published for illustrative purposes only.
***/


import hsa2.GraphicsConsole;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

public class BallGameAP {

	public static void main(String[] args) {
		new BallGameAP();
	}
	//Variables
	final static int SCRW=800;
	final static int SCRH=600;
	GraphicsConsole gc=new GraphicsConsole(SCRW,SCRH);
	Ball b1,b2;
	MouseBall m;
	ScoreBall s;
	int upSpeed=0;
	int score=1;	//Score has to be 1 or else the enemy balls will constantly speed up
	int mouseX;
	int mouseY;
	int scoreX;
	int scoreY;
	final static int SLEEPTIME=6;

	BallGameAP() {
		setup();
		
		//wait for mouse click
		while (gc.getMouseClick() == 0) {
			gc.sleep(100);
		}

		//main loop
		while(gc.getKeyCode()!= 'Q') {
			follow();
			moveBalls(b1);
			moveBalls(b2);
			intersections();
			getScore();
			drawGraphics();
			if (b1.intersects(m) || b2.intersects(m)) {	//When you lose
				gc.setColor(Color.BLACK);
				gc.setFont(new Font("Serif",Font.BOLD,22));
				gc.drawString("GAME OVER",400,50);
				gc.sleep(3000);
				System.exit(0);
			}
			gc.sleep(SLEEPTIME);
		}

	}
	void setup() {	//The Setup
		gc.setAntiAlias(true);
		gc.setTitle("Ball Collision");
		gc.setLocationRelativeTo(null);
		gc.enableMouse();
		gc.enableMouseMotion();
		b1=new Ball(SCRW);
		b2=new Ball(SCRH);
		m=new MouseBall();
		s=new ScoreBall();
		b2.clr=Color.GREEN.darker();
		b2.height=30;
		b2.width=30;
		b1.vx=2;
		b1.vy=1;
		scoreX=(int)(Math.random()*780)+1;	//The x and y for the score ball
		scoreY=(int)(Math.random()*580)+1;
	}
	void drawGraphics() {	//Where the graphics are drawn
		synchronized(gc) {
			gc.clear();
			gc.setColor(b1.clr);
			gc.fillOval(b1.x,b1.y,b1.width,b1.height);	//Draw ball 1
			gc.setColor(b2.clr);
			gc.fillOval(b2.x,b2.y,b2.width,b2.height);	//Draw ball 2
			gc.setColor(m.clr);
			gc.fillOval(mouseX,mouseY,m.width,m.height);	//Draw MouseBall
			gc.setColor(s.clr);
			gc.fillOval(scoreX,scoreY,s.width,s.height);	//Draw ScoreBall
			gc.setColor(Color.BLACK);
			gc.setFont(new Font("Serif",Font.BOLD,22));
			gc.drawString("Score: "+(score-1),50,50);	//Write out score
		}
	}
	void moveBalls(Ball b) {	//Ball movement code
		s.x=scoreX;
		s.y=scoreY;
		b.x=b.x+b.vx;
		b.y=b.y+b.vy;
		m.x=mouseX;
		m.y=mouseY;
		if (b.x>SCRW-40) b.vx=-b.vx;	//Wall bounce code
		if (b.y>SCRH-40) b.vy=-b.vy;
		if (b.x<0) b.vx=-b.vx;
		if (b.y<0) b.vy=-b.vy;
		mouseX=gc.getMouseX();
		mouseY=gc.getMouseY();
	}
	void intersections() {	//Intersection code that doesn't work
		if (b1.intersects(b2)) {
			b1.vx=b2.vx;
			b2.vx=b1.vx;
			b1.vy=b2.vy;
			b2.vy=b1.vy;
		}
	}
	void follow() {	//The code that makes the enemy balls follow the mouse
		if (mouseX>b1.x+(b1.height/2)) {	//Ball 1
			b1.vx=2+upSpeed;
		}
		if (mouseX<b1.x+(b1.height/2)) {
			b1.vx=-2-upSpeed;
		}
		if (mouseY>b1.y+(b1.height/2)) {
			b1.vy=1+upSpeed;
		}
		if (mouseY<b1.y+(b1.height/2)) {
			b1.vy=-1-upSpeed;
		}
		if (mouseX>b2.x+(b2.height/2)) {	//Ball 2
			b2.vx=3+upSpeed;
		}
		if (mouseX<b2.x+(b2.height/2)) {
			b2.vx=-3-upSpeed;
		}
		if (mouseY>b2.y+(b2.height/2)) {
			b2.vy=2+upSpeed;
		}
		if (mouseY<b2.y+(b2.height/2)) {
			b2.vy=-2-upSpeed;
		}
	}
	void getScore() {	//Where we check if a score ball has been taken and if we should speed up the enemy balls
		if (m.intersects(s)) {
			score+=1;
			scoreX=(int)(Math.random()*780)+1;
			scoreY=(int)(Math.random()*580)+1;
			if ((score-1)%10==0) {
				upSpeed+=1;
			}
		}
	}


	class Ball extends Rectangle{
		int vx=4;
		int vy=3;

		Color clr=Color.RED;

		Ball(int w) { //w is screen width
			x=(int)(Math.random()*w)+50;
			y=(int)(Math.random()*100)+1;
			width=40;
			height=40;
		}
	}
	class MouseBall extends Rectangle {
		Color clr=new Color(133,0,133);
		MouseBall() {
			height=20;
			width=20;
		}
	}
	class ScoreBall extends Rectangle{
		Color clr=Color.YELLOW.darker();
		ScoreBall(){
			height=15;
			width=15;
		}
	}
}

/* Marking  90%

 X no comment: author etc.
 X no explanation of the program's purpose
 + a few extra comments in code  

 + method names make sense
 + variable names all make sense
 + capitalization correct
 + indenting correct.
 X vertical spaceing in code needs improving

Excellent concept.
  X no comments for variables. Why do you need scoreball as well as scoreX and scoreY?

 */
