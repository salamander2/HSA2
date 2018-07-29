## Animation Tutorial

This is a tutorial to demonstrate how to make a moderately good animation game using HSA2 graphics.  
We'll be making a block breaking game that's flexible enough to be extended as you wish.
 

## STEP 3 : Adding a paddle, moving it, collisions

:heavy_check_mark: Font: fixed up 

:heavy_check_mark: Ball bouncing off screen fixed. You need to figure out why this was happening and why the fix worked.

:heavy_check_mark: The ball has a random x direction when it starts again.

### The Paddle

* The simplest way to see if things collide is if they are both rectangles.
* Notice that the Ball still has its own class, but the paddle doesn't. They are both rectangles, but the paddle doesn't have any special properties, whereas the ball does. 
	Furthermore, we're never having more than one paddle. If we were, it would be most convenient if we made the paddle in to a class.
* Read the comments about the initialize() method in the code.
* Paddle is set up to move by using the mouse OR using the keyboard. Just uncomment whichever method you want to use. 
* Note that the keyboard moving still needs a bit of fixing up: you can move the paddle off of the right side of the screen!
