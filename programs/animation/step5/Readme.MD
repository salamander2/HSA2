## Animation Tutorial

This is a tutorial to demonstrate how to make a moderately good animation game using HSA2 graphics.  
We'll be making a block breaking game that's flexible enough to be extended as you wish.
 

## STEP 5 : Draw Blocks

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
		    (ii) set the y coordinate of the block to -100. 
             This moves the block up off of the top of the screen.
        
Method (ii) is better because the block will never collide with the ball since the ball never goes off screen. 
This saves time because you don't have to worry about colliding with invisible blocks. 
I am still using isVisible, but I don't really need to. I could replace it with if (ball.y < 0) ...

Winning and losing. I've more or less got this working, but if you type "Q" it says that you win.

DO NOT MAKE THINGS PUBLIC UNLESS YOU HAVE TO. Always restrict the scope of variables and methods as much as possible.
So, I've made as many things as possible private. I can't make the variables in the Ball and Block class private unless I write getter and setter methods, which is quite easy, but I'll leave this as an excercise for the student.

## TODO:
* fix paddle movement (you can move the paddle off of the right side of the screen - using the keyboard)
* add complex bouncing coded. If you hit the ball at the ends of the paddle it should bounce differently from if you hit it with the middle of the paddle.
* make 36 blocks
* add getter and setter methods for Ball and Block class

 
