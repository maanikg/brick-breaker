# brick-breaker
# brick-breaker
This was not an individual project. I completed this project as part of a team assignment (team of 2).

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
