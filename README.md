# brick-breaker
I completed this project as part of a grade 11 final team project (team of 2).
The files in the Main Project folder are of a better quality and are more recent than those in the Supplementary Files, but have less functionality(such as having no title screen). I was responsible for coding the visual graphics of the game including the motion of the ball and bouncer and the entire brick layout (including varying degrees of thickness), collision detection, and support for mouse and key controls.

This project is the brick-breaker game coded with mouse and key controls, sound integration, and a comprehensive visual user interface.

Bugs:

Depending on the collision, the ball may bounce off course(it is assumed this is due to the ball intersecting with multiple bricks at the same time). 

Features Missing/Added:
 - missing power ups
 - added sound effects

Instructions:
When the game first starts, move your mouse to decide the trajectory of the ball. Click to start the game.
The bouncer can be controlled by moving the mouse or using the left or right arrows.

When the ball hits the bouncer, there is a slight randomization in the direction of the bounce. If the ball is coming from the left and hits the left side of the bouncer, the ball will go off the bouncer in a random direction. Otherwise, if the ball bounces off the right side of the bouncer, the ball will bounce off regularly. This puts a bit of strategy into the game.

When the ball hits a brick that requires multiple hits to be broken, the score will not be updated. The score will only be updated when a brick breaks.Each time your score hits a multiple of a 100, a congratulating sound will be played.
Press p to pause the game(meant to move to another frame).

When all the bricks are broken, the game is over. This turns a gameOver variable true, and is meant to switch frames. The same applies if all lives are lost - a variable is triggered that is meant to switch frames.
