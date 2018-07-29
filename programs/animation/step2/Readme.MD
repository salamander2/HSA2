## Animation Tutorial

This is a tutorial to demonstrate how to make a moderately good animation game using HSA2 graphics.  
We'll be making a block breaking game that's flexible enough to be extended as you wish.
 

## STEP 2 : Making a ball move. Making it bounce off the edges of the screen.

* The ball is a separate object (with a separate class Ball.java). This is because the ball has properties that it should maintain and that we want to change. We can also create multiple balls this way.
* Notice some new global variables: lives, isPlaying (this will be used to end the game)
* Notice some constants. These make it easy to change numbers in the code without having to do search and replace. Get used to using constants for these sort of things. I can change my screen size and EVERYTHING still works.
* A game loop has been added. This is a standard way of doing things.

The sleep() function is ESSENTIAL. This allows the computer to do other things. It can be used to vary the speed of the game.
ball.xspeed and ball.yspeed are how many pixels the ball moves each time - you could also use this to vary the speed of the game,
since if this number is larger, the ball moves faster. 
HOWEVER, it also becomes more jerky because it actually jumps this number of pixels.  So keep the xspeed small and vary the game speed by changing SLEEPTIME. It is in milliseconds

SYNCHRONIZED has been added to drawGraphics(). This reduces flickering during animation a lot (and is one reason why all drawing needs to be in only one place)

Interesting, both a capital or lowercase Q will end the game.

I've also made it so that when the ball hits the bottom of the screen, you lose a life and the ball changes colour.

### Next:
1. I'm going to make a nicer font in the next stage so that the text looks better. 
2. Notice how the ball actually goes off the screen on the right and bottom. This needs fixing. 
3. Let's draw a rectangle to make sure that the ball actually starts in the place that we want it to.
