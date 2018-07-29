## Animation Tutorial

This is a tutorial to demonstrate how to make a moderately good animation game using HSA2 graphics.  
We'll be making a block breaking game that's flexible enough to be extended as you wish.
 

## STEP 4 : Paddle collision

Well, that was easy! Just use the Rectangle.intersects() method.

Note that this does not really do collisions properly:
The ball is treated as a rectangle, so the corners of the rectangle are quite far from the circle denoting the ball. 
I've drawn a square around the ball so you can see how this works.
    	
Also: there is no complex bouncing coded. 
For example, if you hit the ball at the ends of the paddle it should bounce differently from if you hit it with the middle of the paddle. 

