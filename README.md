# HSA2

## Holt Software Associates simple graphics for Java &mdash; updated for Java 7 and Java 8.

*The intent of this software is to allow new Java programmers to start using graphics without having to learn Swing first.*

### Features
* provides a JPanel inside a JFrame to draw graphics on (called "GraphicsConsole"). 
* hides all of the event listeners for the mouse and keyboard and implements _polling_ instead. 
This is so students can start using keyboard and mouse input immediately without having to learn about events and event listeners
* uses all of the Swing drawing commands on the Graphics object (e.g. drawString(), fillOval()).
* works with Images, ImageIcons, and BufferedImages
* provides a simple implementation of JOPtionPanes for dialog boxes

### Limitations:
* All keyboard input is handled through the Swing KeyListener. 
* There is no error handling for text input: the program displays a message and then ends immediately.
* There is some flickering still with animated images. Using the `syncrhonized` keyword helps. 
* No JButtons, JLabels, or other Swing components can be added to the JPanel, but as one can see from the examples, they're not really necessary.
* Many advanced Graphics2D functions are not implemented. Antialiasing and rotate both work.
* Only one JPanel is created per JFrame (GraphicsConsole). However, you can have multiple GraphicsConsoles.

These limitations will not be fixed. The solution is to begin programming in Swing or JavaFX.

### The PDF files contain useful documentation.

### There are sample programs and tutorials for you to work through. See the folder "programs"
 
### :movie_camera: I've just made a YouTube channel with some useful videos. 
:boom: URL: https://www.youtube.com/channel/UCvdhhpYkAm7UBFUWOIj3mzw (Youtube channel "Salamander2")

----

To install the HSA2 library into Ecplise:

* download the zip file from Github
* unzip it
* drag the whole hsa2 folder into the SRC folder of whatever project you're working on in Eclipse

----

See "HSA Change log.txt" for list of changes (and also look at the changes using Git).

### :boom: Please let me know of any bugs or enhancements (feel free to submit code).

~~~~~~
This repository is at https://github.com/salamander2/HSA2
