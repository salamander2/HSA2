package animation;

/***** STEP 5 : Ball class ******/

import java.awt.Color;
import java.awt.Rectangle;

public class Block extends Rectangle {
	
	//This can be used for setting various types of blocks: unbreakable ones, ones that make the paddle smaller or larger, ones that give an extra ball...) 
	//They could have different colours too.
	int type = 1;
	Color colour = new Color(255,222,111);
	
	//is the block displayed on the screen?
	boolean isVisible = true;	
	
	//constructor. Set parameters for all blocks
	Block() {
		width = 100;
		height = 20;
	}
	
}
