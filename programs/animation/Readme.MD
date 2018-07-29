

#HOW TO WRITE AN ANIMATION GAME

This is a tutorial to demonstrate how to make a moderately good animation game using HSA2 graphics so that you don't have to learn Java Swing Graphics first.
We'll be making a block breaking game that's flexible enough to be extended as you wish.

Controls: the keyboard arrow keys will be controlling the paddle.



Basic program & structure
 
## Step 1. Drawing a circle, using variables for its location.

    This is pretty much straight forward.
    I've put the program into two separate methods because we'll be using this way of organizing our logic later.


##2. Making a ball move. Making it bounce off the edges of the screen

    The ball is a separate object (with a separate class Ball.java). This is because the ball has properties that it should maintain and that we want to change. We can also create multiple balls this way.

    Notice some new global variables: lives, isPlaying (this will be used to end the game)

    Notice some constants. These make it easy to change numbers in the code without having to do search and replace.

    A game loop has been added. This is a standard way of doing things.

    The sleep() function is ESSENTIAL. This allows the computer to do other things. It can be used to vary the speed of the game
        ball.xspeed and ball.yspeed are how many pixels the ball moves each time. If this number is larger, the ball moves faster. However, it also becomes more jerky because it actually jumps this number of pixels.  So keep the xspeed small and vary the game speed by changing SLEEPTIME. It is in milliseconds

    Interesting, both a capital or lowercase Q will end the game.

    I've also made it so that when the ball hits the bottom of the screen, you lose a life and the ball changes colour.

##Next time:

one I'm going to make a nicer font in the next release so that the text looks better. two Notice how the ball actually goes off the screen on the right and bottom. This needs fixing. three Let's draw a rectangle to make sure that the ball actually starts in the place that we want it to.

##3. Adding a paddle, moving it, collisions

    Font: fixed up
    Ball bouncing off screen fixed. You need to figure out why this was happening and why the fix worked.
    Ball starting location: meh... I'm not going to bother doing that.

Paddle

The simplest way to see if things collide is if they are both rectangles. Notice that the Ball still has its own class, but the paddle doesn't. They are both rectangles, but the paddle doesn't have any special properties, whereas the ball does. Furthermore, we're never having more than one paddle. If we were, it would be most convenient if we made the paddle in to a class.

Read the comments about the initialize() method in the code. You can also use GitHub to look at the differences between the commits to see what has changed.

Paddle is set up to move by using the mouse or using the keyboard. Just uncomment whichever method you want to use. Note that the keyboard moving still needs a bit of fixing up: you can move the paddle off of the right side of the screen!

##4. Paddle collision

Well, that was easy! Just use the Rectangle.intersects() method.

Note that this does not really do collisions properly.

    The ball is treated as a rectangle, so the corners of the rectangle are quite far from the circle denoting the ball. I've drawn a square around the ball so you can see how this works.
    There is no complex bouncing coded. For example, if you hit the ball at the ends of the paddle it should bounce differently from if you hit it with the middle of the paddle. I may try and code this just for fun.

##5. Draw Blocks ...

Again there are 3 steps: create the block objects, draw them on the screen, then see if they get hit by the ball. (and see if you have won the game).

I've only drawn 5 blocks. You should make a grid of them. You can use modulus (%) and integer division (/) to find the row and columns. e.g. if you had 10 blocks in each row, then for block #43, row = n / 10 (which returns 4) and col = n%10 (which returns 3). The column is used to set the X values, the row is used to set the Y values.

When a block is hit, there are various things that can be done:

    If all of the blocks are stored in an ArrayList, you can just delete the block.
    You can't do this with Arrays though.
    If you store the blocks in an array, you can do two things: (i) set isVisible to false (ii) set the y coordinate of the block to -100. This moves the block up off of the top of the screen.

Method (ii) is better because the block will never collide with the ball since the ball never goes off screen. This saves time because you don't have to worry about colliding with invisible blocks. I am still using isVisible, but I don't really need to. I could replace it with if (ball.y < 0) ...

Winning and losing. I've more or less got this working, but if you type "Q" it says that you win.
DO NOT MAKE THINGS PUBLIC UNLESS YOU HAVE TO. Always restrict the scope of variables and methods as much as possible.

I just caught an error because, thankfully, I didn't make the Block() constructor public. It was trying to use an older version of Block() in a completely different package.

So, I've made as many things as possible private. I can't make the variables in the Ball and Block class private unless I write getter and setter methods, which is quite easy, but I'll leave this as an excercise for the student.

DONE! 

Things for you to do:

There are lots of possibilities for you to expand on this.

The simplest thing is to make more blocks, have them colour coded so that the different blocks do different things. Or you could make a totally different game. Pong is remarkably simple, so try something a bit more complex.
