//BUGS WHEN COLLISION DETECTED

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import javax.swing.*;

public class FinalProject_Maanik extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener
{
	static JFrame frame;
	final int BRICK_WIDTH = 70;
	final int BRICK_HEIGHT = 25;
	final int TOP_OFFSET = 42;
	final int BORDER_SIZE = 4;
	final int bouncerLength = 150;
	final int bouncerHeight = 10;
	final int bouncerYLocation = 640;
	final int movingBallDiameter = 17;
	final int lifeIconLength = 22;

	Color pink = new Color(255,210,211);
	Color darkerPink = new Color(255, 162, 165);
	Color darkestPink = new Color(255,113,117);
	Color lightRed = new Color(255,50,62);
	Color red = new Color(255,0,0);
	Color darkerRed = new Color(233,0,0);
	Color darkerDarkerRed = new Color(187,0,0);
	Color darkestRed = new Color(141,0,0);

	int[] [] brickArea = new int[28][18];  //STATIC?
	int[] [] brickValues = new int[28][18];
	boolean bouncerMoveRight = false, bouncerMoveLeft = false;
	double verticalDistance, horizontalDistance;
	double horizontalSpeed, verticalSpeed;
	double xValueIntersection;
	double angle;
	int mouse_XValue, mouse_YValue;
	int bouncerXLocation = 559;
	int lastRowPos;
	int numLives = 3;
	int score = 0;
	int movingBallXLocation = bouncerXLocation + bouncerLength/2 - movingBallDiameter/2;//(int)(bouncerXLocation + 0.5*bouncerLength-0.5*movingBallDiameter);
	int movingBallYLocation = bouncerYLocation - movingBallDiameter;
	boolean straightLineShot;//, locateMouse = false;
	boolean gameStarted = false, gamePaused = false, gameOver = false;
	boolean lostLife = false;
	boolean increaseXIntersection = false, decreaseXIntersection = false;
	final Point START_POINT = new Point(movingBallXLocation, movingBallYLocation);
	Point endPoint = new Point((int)xValueIntersection, lastRowPos);
	AudioClip brickCollisionSound, lostLifeSound, levelFailedSound, checkpointSound, soundtrack;

	Ellipse2D movingBall = new Ellipse2D.Double(movingBallXLocation, movingBallYLocation, movingBallDiameter, movingBallDiameter);
	Rectangle movingBouncer = new Rectangle(bouncerXLocation, bouncerYLocation, bouncerLength, bouncerHeight);
	Rectangle movingBallOutline = new Rectangle(movingBallXLocation, movingBallYLocation, movingBallDiameter, movingBallDiameter);

	Robot robot;
	Image offScreenImage;
	Graphics offScreenBuffer;
	Image lifeIcon = Toolkit.getDefaultToolkit().getImage("MC_Heart.png");
	Image firstLife = lifeIcon, secondLife = lifeIcon, thirdLife = lifeIcon;
	Image emptyHeart = Toolkit.getDefaultToolkit().getImage("Empty_Heart.png");

	BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	Cursor defaultCursor =	Cursor.getDefaultCursor();
	Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

	// For drawing images offScreen (prevents Flicker)
	// These variables keep track of an off screen image object and
	// its corresponding graphics object

	public FinalProject_Maanik (){
		//		frame.setPreferredSize (new Dimension (20 * BRICK_WIDTH + 2 * BORDER_SIZE + 1, (10 + 1) * SQUARE_SIZE + TOP_OFFSET + BORDER_SIZE + 1));
		frame.setPreferredSize (new Dimension (18 * BRICK_WIDTH, 26 * BRICK_HEIGHT+ 50));
		frame.setLocation (45, 10);
		frame.setBackground (Color.black);
		setLayout (new BoxLayout (this, BoxLayout.PAGE_AXIS));

		brickCollisionSound = Applet.newAudioClip (getCompleteURL ("beep-07.wav"));
		lostLifeSound = Applet.newAudioClip (getCompleteURL ("life_lost.wav"));
		levelFailedSound = Applet.newAudioClip (getCompleteURL ("game-over.wav"));
		checkpointSound = Applet.newAudioClip (getCompleteURL ("success.wav"));
		soundtrack = Applet.newAudioClip (getCompleteURL ("soundtrack.wav"));
		
		JMenu gameMenu = new JMenu ("Menu");
		JMenuItem newOption, exitOption;
		newOption = new JMenuItem ("New Game");
		exitOption = new JMenuItem ("Exit");

		gameMenu.add (newOption);
		gameMenu.addSeparator ();
		gameMenu.add (exitOption);
		gameMenu.setOpaque(true);
		gameMenu.setBackground(Color.DARK_GRAY);

		JMenuBar mainMenu = new JMenuBar ();
		mainMenu.add (gameMenu);
		frame.setJMenuBar (mainMenu);
		newGame ();

		newOption.setActionCommand ("New");
		newOption.addActionListener (this);
		exitOption.setActionCommand ("Exit");
		exitOption.addActionListener (this);

		setFocusable (true); 
		addKeyListener (this);
		addMouseListener (this);
		addMouseMotionListener(this);
	}
	
	public URL getCompleteURL (String fileName){
		try{
			return new URL ("file:" + System.getProperty ("user.dir") + "/" + fileName);
		}catch (MalformedURLException e){
			System.err.println (e.getMessage ());
		}
		return null;
	}

	public void actionPerformed (ActionEvent event){
		String eventName = event.getActionCommand ();
		if (eventName.equals ("New")) {
			newGame ();
			gameStarted = false;
		}else if (eventName.equals ("Exit"))
			System.exit (0);
	}

	public void newGame (){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
//		soundtrack.play();
//		soundtrack.loop();
		if (!gameStarted) 
			for (int count = 0; count < brickArea.length; count++) 
				for (int countTwo = 0; countTwo < brickArea[count].length; countTwo++)
					brickArea[count][countTwo] = 0;
		if (lostLife) { 
			numLives--;
			if (score > 500) 
				score /=2;
			else if (score > 300)
				score = score/3;
			else if (score > 100) 
				score = score/4;
			else if (score < 0) {
				score -=50;
			}else
				score = -50;
			/*
			 * 
			 * DOESN't SHOW IMMEDIATELY - HAVE TO MOVE MOUSE TO SHOW
			 */
			if (numLives == 2) {
				lostLifeSound.play();
				offScreenBuffer.drawImage(emptyHeart, (int)frame.getBounds().getWidth() - 75, 1, lifeIconLength, lifeIconLength, null);
				offScreenBuffer.drawImage(secondLife, (int)frame.getBounds().getWidth() - 50, 1, lifeIconLength, lifeIconLength, null);
				offScreenBuffer.drawImage(thirdLife, (int)frame.getBounds().getWidth() - 25, 1, lifeIconLength, lifeIconLength, null);
			}else if (numLives == 1) {
				lostLifeSound.play();
				offScreenBuffer.drawImage(emptyHeart, (int)frame.getBounds().getWidth() - 75, 1, lifeIconLength, lifeIconLength, null);
				offScreenBuffer.drawImage(emptyHeart, (int)frame.getBounds().getWidth() - 50, 1, lifeIconLength, lifeIconLength, null);
				offScreenBuffer.drawImage(thirdLife, (int)frame.getBounds().getWidth() - 25, 1, lifeIconLength, lifeIconLength, null);
			}else if (numLives == 0) {
				gameOver = true;
				levelFailedSound.play();
			}
		}

		robot.mouseMove(679, (int)frame.getBounds().getHeight()/2);
		frame.getContentPane().setCursor(blankCursor);
		bouncerXLocation = 559;
		movingBouncer.setLocation(bouncerXLocation, bouncerYLocation);
		movingBallXLocation = bouncerXLocation + bouncerLength/2 - movingBallDiameter/2;
		movingBallYLocation = bouncerYLocation - movingBallDiameter;
		movingBall = new Ellipse2D.Double(movingBallXLocation, movingBallYLocation, movingBallDiameter, movingBallDiameter);
		horizontalSpeed = 0;
		verticalSpeed = 0;
		repaint ();
	}

	public void checkCollision() {
		if (!gamePaused) {
			Rectangle movingBallPath = new Rectangle(movingBallOutline.x, movingBallOutline.y, movingBallDiameter, movingBallDiameter);
			movingBallOutline.setLocation(movingBallXLocation, movingBallYLocation);

			delay(5);
			repaint();
			/*
			 * WALL COLLISION
			 * 
			 */
			if (movingBallXLocation + horizontalSpeed < 0) {
				movingBallXLocation = 0;
				horizontalSpeed*=-1;
			}else if (movingBallXLocation + horizontalSpeed+movingBallDiameter >= frame.getBounds().getWidth()) {
				movingBallXLocation = (int)frame.getBounds().getWidth()-movingBallDiameter;
				horizontalSpeed*=-1;
			}else if (movingBallYLocation + verticalSpeed < BRICK_HEIGHT) {
				movingBallYLocation = BRICK_HEIGHT;
				verticalSpeed*=-1;
			}

			movingBallOutline.setLocation(movingBallXLocation, movingBallYLocation);

			if (movingBallYLocation+verticalSpeed +movingBallDiameter >= bouncerYLocation) {
				if (movingBallOutline.intersects(bouncerXLocation, bouncerYLocation-50, bouncerLength, 50)) {
//					otherCollisionSound.play();
					//			if (movingBallXLocation + movingBallDiameter + horizontalSpeed > bouncerXLocation) {
					//				movingBallYLocation = bouncerYLocation - bouncerHeight -movingBallDiameter;
					//				verticalSpeed*=-1;
					//			}else if (movingBallXLocation + horizontalSpeed < bouncerXLocation+bouncerLength) {
					movingBallYLocation = bouncerYLocation - bouncerHeight -movingBallDiameter;
					verticalSpeed*=-1;

					boolean ballComingRight = false, ballComingLeft = false;
					if ((horizontalSpeed > 0) && (movingBallYLocation+0.5*movingBallDiameter)<(bouncerXLocation+0.5*bouncerLength)) 
						ballComingRight = true;
					else if ((horizontalSpeed < 0) && (movingBallYLocation+0.5*movingBallDiameter)>(bouncerXLocation+0.5*bouncerLength))
						ballComingLeft = true;
					else if (horizontalSpeed == 0)
						straightLineShot = true;

					if (straightLineShot||ballComingRight||ballComingLeft) {
						Random randBool = new Random();
						boolean ballReturnsSameSide = randBool.nextBoolean();
						double randomEndPointX = 0;

						if (straightLineShot) {
							randomEndPointX = (Math.random()*51) + (bouncerXLocation+0.5*bouncerLength-50); 
							straightLineShot = false;
						}else if (ballComingRight && ballReturnsSameSide) {
							randomEndPointX = (Math.random()*25)+movingBallXLocation+0.5*movingBallDiameter;
							ballComingRight = false;
						}else if (ballComingLeft && ballReturnsSameSide) { 
							randomEndPointX = (Math.random()*25)+movingBallXLocation+0.5*movingBallDiameter-25;
							ballComingLeft = false;
						}

						randomEndPointX = (int)randomEndPointX;
						angle = Math.atan2(lastRowPos - movingBallYLocation, randomEndPointX - movingBallXLocation);
						horizontalSpeed = (int)(Math.cos(angle)*15);
						verticalSpeed = (int)(Math.sin(angle)*15);
					}
				}
				else if (movingBallYLocation+movingBallDiameter>bouncerYLocation){
					lostLife = true;
					newGame();
					return;
				}
			}
			movingBallOutline.setLocation(movingBallXLocation, movingBallYLocation);
			movingBallPath.setLocation(movingBallOutline.x+(int)horizontalSpeed, movingBallOutline.y+(int)verticalSpeed);

			for (int row = 0 ; row <= 19; row++) 
				for (int column = 0 ; column <= 17 ; column++) {
					if (movingBallPath.getBounds2D().intersects(column*BRICK_WIDTH, row*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT) && brickArea[row][column]!=0) {
						int ballTop = movingBallPath.y;
						int ballRight = movingBallPath.x+ movingBallDiameter;
						int ballBottom = movingBallPath.y+ movingBallDiameter;
						int ballLeft = movingBallPath.x;
						int wallLeft = column*BRICK_WIDTH;
						int wallRight = column*BRICK_WIDTH+BRICK_WIDTH;
						int wallTop = row*BRICK_HEIGHT;
						int wallBottom = row*BRICK_HEIGHT+BRICK_HEIGHT;

						///ERROR SOMETIMES
						if(ballRight > wallLeft && ballLeft < wallLeft && 
								ballRight - wallLeft < ballBottom - wallTop && 
								ballRight - wallLeft < wallBottom - ballTop)
						{
							System.out.println("Left");
							movingBallXLocation = wallLeft - movingBallDiameter;
							horizontalSpeed*=-1;
						}else if(ballLeft < wallRight &&
								ballRight > wallRight && 
								wallRight - ballLeft < ballBottom - wallTop && 
								wallRight - ballLeft < wallBottom - ballTop)
						{
							System.out.println("Right");
							movingBallXLocation = wallLeft + BRICK_WIDTH;
							horizontalSpeed*=-1;
						}else if(ballBottom > wallTop && ballTop < wallTop){
							System.out.println("Top");
							movingBallYLocation = wallTop - movingBallDiameter;
							verticalSpeed*=-1;
						}else if(ballTop < wallBottom && ballBottom > wallBottom){
							System.out.println("Bottom");
							movingBallYLocation = wallBottom;
							verticalSpeed*=-1;
						}

						brickArea[row][column]--;
						if (brickArea[row][column] == 0) {
							int origScore = score;
							brickCollisionSound.play();
							if (brickValues[row][column] == 1) 
								score+=10;
							else if (brickValues[row][column] == 2) 
								score+=20;
							else if (brickValues[row][column] == 3) 
								score+=30;
							else if (brickValues[row][column] == 4) 
								score+=40;
							int endScore = score;
							if (origScore > 0 && endScore > 0 && ((int)(endScore/100) > (int)(origScore/100))) {
								checkpointSound.play();
							}
						}

						int xPos = column * BRICK_WIDTH;
						int yPos = row * BRICK_HEIGHT;

						if (brickArea[row][column] == 0)
							offScreenBuffer.setColor(Color.black);
						else if (brickArea[row][column] == 1) 
							offScreenBuffer.setColor(pink);
						else if (brickArea[row][column] == 2) 
							offScreenBuffer.setColor(darkerPink);
						else if (brickArea[row][column] == 3) 
							offScreenBuffer.setColor(darkestPink);
						else if (brickArea[row][column] == 4) 
							offScreenBuffer.setColor(lightRed);
						else if (brickArea[row][column] == 5) 
							offScreenBuffer.setColor(red);
						else if (brickArea[row][column] == 6) 
							offScreenBuffer.setColor(darkerRed);
						else if (brickArea[row][column] == 7) 
							offScreenBuffer.setColor(darkerDarkerRed);
						else if (brickArea[row][column] == 8) 
							offScreenBuffer.setColor(darkestRed);
						offScreenBuffer.fillRect(xPos+1, yPos+1, BRICK_WIDTH-2, BRICK_HEIGHT-2);
						offScreenBuffer.setColor(Color.black);
						repaint();
						break; // Break?
					}
				}
			movingBallXLocation+=horizontalSpeed;
			movingBallYLocation+=verticalSpeed;	 
			movingBallOutline.setLocation(movingBallXLocation, movingBallYLocation);
			repaint();
		}
	}

	public void mouseClicked (MouseEvent e){
		/*
		 *  
		 * NEED TO ALLOW COMPUTER TO BE CONTROLLED BY ECLIPSE
		 * 
		 */
		if (!gameStarted || lostLife) {
			gameStarted = true;
			lostLife = false;
			endPoint = new Point((int)xValueIntersection, lastRowPos);

			angle = Math.atan2(endPoint.y - START_POINT.y, endPoint.x - START_POINT.x);
			horizontalSpeed = (int)(Math.cos(angle)*15);
			verticalSpeed = (int)(Math.sin(angle)*15);

			if (horizontalSpeed == 0)
				straightLineShot = true;

			frame.getContentPane().setCursor(blankCursor);
			//			frame.getContentPane().setCursor(defaultCursor);
			checkCollision();
		}
		//		else 		
		//			locateMouse = true;


		repaint();
	}

	public void mouseReleased (MouseEvent e){
	}

	public void mouseEntered (MouseEvent e){
	}

	public void mouseExited (MouseEvent e){
	}

	public void mousePressed (MouseEvent e){
	}

	public void mouseMoved(MouseEvent e) {
		mouse_XValue = e.getX();
		mouse_YValue = e.getY();
		if (gameStarted && !lostLife && !gamePaused) 
			if (mouse_XValue - bouncerLength/2 > 0 && mouse_XValue + bouncerLength/2 < frame.getBounds().getWidth())
				bouncerXLocation = mouse_XValue - bouncerLength/2;
			else
				if (mouse_XValue - bouncerLength/2 <= 0)
					bouncerXLocation = 0;
				else if (mouse_XValue + bouncerLength/2 >= frame.getBounds().getWidth())
					bouncerXLocation = (int)frame.getBounds().getWidth() - bouncerLength;
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void keyPressed (KeyEvent kp){
		if (gameStarted && !lostLife) {
			if (kp.getKeyCode () == KeyEvent.VK_RIGHT) {
				bouncerMoveRight = true;
				bouncerMoveLeft = false;
			}else if (kp.getKeyCode () == KeyEvent.VK_LEFT){
				bouncerMoveLeft = true;
				bouncerMoveRight = false;
			}
		}else 
			//			if (kp.getKeyCode() == KeyEvent.VK_RIGHT || kp.getKeyCode() == KeyEvent.VK_LEFT) 
			//				JOptionPane.showConfirmDialog(frame, "Please move the mouse to determine the angle of your shot.\nThe left and right keys, used to control the bouncer, will not work until the ball is in motion.", "WARNING", JOptionPane.OK_CANCEL_OPTION);
			if (kp.getKeyCode() == KeyEvent.VK_RIGHT) {
				increaseXIntersection = true;
				decreaseXIntersection = false;
				drawDashedLine(offScreenBuffer);
			}else if (kp.getKeyCode() == KeyEvent.VK_LEFT) {
				increaseXIntersection = false;
				decreaseXIntersection = true;
				drawDashedLine(offScreenBuffer);
			}

		if (kp.getKeyCode() == KeyEvent.VK_P && gameStarted && !lostLife) {
			if (!gamePaused) {
				gamePaused = true;
				frame.getContentPane().setCursor(defaultCursor);
			}
			else {
				delay(50);
				gamePaused = false;
				frame.getContentPane().setCursor(blankCursor);
			}
		}
		//			else if (kp.getKeyCode() == KeyEvent.VK_ENTER || kp.getKeyCode() == KeyEvent.VK_SPACE) {
		//				gameStarted = true;
		//				repaint();
		//			}
	}

	public void keyPressedMoveBouncer() {
		delay(5);
		repaint();
		if (bouncerMoveRight && !bouncerMoveLeft) {
			if (bouncerXLocation + bouncerLength < frame.getBounds().getWidth())
				if (bouncerXLocation + bouncerLength + 20 < frame.getBounds().getWidth()) 
					bouncerXLocation+=20;
				else
					bouncerXLocation = (int)frame.getBounds().getWidth() - bouncerLength;
		}else if (bouncerMoveLeft && !bouncerMoveRight){	
			if (bouncerXLocation > 0)
				if (bouncerXLocation - 20 > 0) 
					bouncerXLocation-=20;
				else
					bouncerXLocation = 0;
		}
		repaint ();
	}

	public void keyReleased (KeyEvent e){
		bouncerMoveLeft = false;
		bouncerMoveRight = false;
		increaseXIntersection = false;
		decreaseXIntersection = false;
	}

	public void keyTyped (KeyEvent e){
	}

	public void update (Graphics g){
		paint (g);
	}

	public void drawDashedLine(Graphics g){
		final float[] straightPattern = {100, 0};
		Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		Stroke straight = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0 , straightPattern, 0);

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Graphics2D lineMaker = (Graphics2D) offScreenBuffer;
		lineMaker.setColor(Color.yellow.darker());
		lineMaker.setColor(new Color((lineMaker.getColor().getRed()), lineMaker.getColor().getGreen(), lineMaker.getColor().getBlue(), 50));
		lineMaker.setStroke(dashed);

		lastRowPos = 19*BRICK_HEIGHT+BRICK_HEIGHT;

//		int mousePos = mouse_XValue;
//		if (increaseXIntersection && !decreaseXIntersection) {
//			if (mousePos+20 >= frame.getBounds().width) {
//				mousePos = frame.getBounds().width-1;
//			}else
//				mousePos+=20;
//			System.out.println(mousePos);
//			robot.mouseMove(mousePos, bouncerYLocation-movingBallDiameter);
//			System.out.println("Here");
//		}else if (!increaseXIntersection && decreaseXIntersection) {
//			if (mousePos-20 <= frame.getLocation().x) {
//				mousePos = 1;
//			}else
//				mousePos-=20;
//			//			robot.mouseMove(bouncerXLocation,bouncerYLocation - 100);
//			mousePos-=20;
//			robot.mouseMove(mousePos, bouncerYLocation-movingBallDiameter);
//		}

		double currentLineSlope = (double)(-START_POINT.y+0.5*movingBallDiameter + mouse_YValue) / (double)(START_POINT.x+0.5*movingBallDiameter - mouse_XValue);
		double currentLineB_Value = -mouse_YValue - currentLineSlope*mouse_XValue;

		xValueIntersection = (-lastRowPos-currentLineB_Value)/currentLineSlope;



		if (Double.isNaN(xValueIntersection))
			xValueIntersection = mouse_XValue;
		else if (xValueIntersection < 0)
			xValueIntersection = 0;
		else if (xValueIntersection > frame.getBounds().getWidth())
			xValueIntersection = frame.getBounds().getWidth();
		lineMaker.drawLine((int)(START_POINT.x+0.5*movingBallDiameter),(int)(START_POINT.y+0.5*movingBallDiameter), (int)xValueIntersection,lastRowPos);
		lineMaker.setStroke(straight);
	}

	public void levelOne(Graphics g) {
		for (int count = 0; count < brickArea[16].length; count++) 
			brickArea[16][count] = 1;
		for (int count = 0; count < brickArea[14].length; count++)
			brickArea[14][count] = 2;
		for (int count = 0; count < brickArea[12].length; count++)
			brickArea[12][count] = 2;
		//		for (int count = 0; count < brickArea[10].length; count++)
		//			brickArea[10][count] = 4;
		for (int count = 0; count < brickArea[10].length; count++)
			brickArea[10][count] = 0;
		for (int count = 0; count < brickArea[8].length; count++)
			brickArea[8][count] = 1;
		//		for (int count = 0; count < brickArea[4].length; count++)
		//			brickArea[4][count] = 1;

		brickArea[1][1] = 1;
		brickArea[1][2] = 2;
		brickArea[1][3] = 3;
		brickArea[1][4] = 4;
		brickArea[1][5] = 5;
		brickArea[1][6] = 6;
		brickArea[1][7] = 7;
		brickArea[1][8] = 8;
	}

	public void paintComponent (Graphics g){
		if (offScreenBuffer == null){
			offScreenImage = createImage (this.getWidth (), this.getHeight ());
			offScreenBuffer = offScreenImage.getGraphics ();
		}

		offScreenBuffer.clearRect (0, 0, this.getWidth (), this.getHeight ());

		offScreenBuffer.setColor(Color.DARK_GRAY);
		offScreenBuffer.fillRect(0, 0, frame.getBounds().width, BRICK_HEIGHT);

		offScreenBuffer.setColor(Color.DARK_GRAY);
		offScreenBuffer.fillRect(bouncerXLocation,bouncerYLocation,bouncerLength,bouncerHeight);

		if (numLives == 3) {
			offScreenBuffer.drawImage(firstLife, (int)frame.getBounds().getWidth() - 75, 1, lifeIconLength, lifeIconLength, null);
			offScreenBuffer.drawImage(secondLife, (int)frame.getBounds().getWidth() - 50, 1, lifeIconLength, lifeIconLength, null);
			offScreenBuffer.drawImage(thirdLife, (int)frame.getBounds().getWidth() - 25, 1, lifeIconLength, lifeIconLength, null);
		}else if (numLives == 2) {
			offScreenBuffer.drawImage(emptyHeart, (int)frame.getBounds().getWidth() - 75, 1, lifeIconLength, lifeIconLength, null);
			offScreenBuffer.drawImage(secondLife, (int)frame.getBounds().getWidth() - 50, 1, lifeIconLength, lifeIconLength, null);
			offScreenBuffer.drawImage(thirdLife, (int)frame.getBounds().getWidth() - 25, 1, lifeIconLength, lifeIconLength, null);
		}else if (numLives == 1) {
			offScreenBuffer.drawImage(emptyHeart, (int)frame.getBounds().getWidth() - 75, 1, lifeIconLength, lifeIconLength, null);
			offScreenBuffer.drawImage(emptyHeart, (int)frame.getBounds().getWidth() - 50, 1, lifeIconLength, lifeIconLength, null);
			offScreenBuffer.drawImage(thirdLife, (int)frame.getBounds().getWidth() - 25, 1, lifeIconLength, lifeIconLength, null);
		}
		
		offScreenBuffer.setColor(Color.white);
		offScreenBuffer.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		offScreenBuffer.drawString("Score: ", 2, 20);
		if (score < 0)
			offScreenBuffer.setColor(Color.red);
		else if (score > 0)
			offScreenBuffer.setColor(Color.green);
		else if (score == 0)
			offScreenBuffer.setColor(Color.white);
		offScreenBuffer.drawString("" + score, 75, 20);

		for (int row = 1 ; row <= 18; row++)
			for (int column = 0 ; column <= 17 ; column++){
				int xPos = column * BRICK_WIDTH;
				int yPos = row * BRICK_HEIGHT; //TOP_OFFSET + 
				// Find the x and y positions for each row and column
				//				int xPos = (column - 1) * BRICK_WIDTH + BORDER_SIZE;
				offScreenBuffer.setColor (Color.black);
				offScreenBuffer.drawRect (xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT);

				//				offScreenBuffer.setColor(Color.red);
				//				offScreenBuffer.drawRect(START_POINT.x, START_POINT.y, 5, 5);
				//				offScreenBuffer.drawRect(endPoint.x, endPoint.y, 5, 5);

				if (row == 20 && column == 17) 
					offScreenBuffer.drawRect(column*BRICK_WIDTH, row*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);

				offScreenBuffer.setColor(Color.cyan.darker());
				offScreenBuffer.fillOval(movingBallXLocation, movingBallYLocation, movingBallDiameter, movingBallDiameter);

				if (brickArea[row][column] !=0) {
					if (brickArea[row][column] == 1) 
						offScreenBuffer.setColor(pink);
					else if (brickArea[row][column] == 2) 
						offScreenBuffer.setColor(darkerPink);
					else if (brickArea[row][column] == 3) 
						offScreenBuffer.setColor(darkestPink);
					else if (brickArea[row][column] == 4) 
						offScreenBuffer.setColor(lightRed);
					else if (brickArea[row][column] == 5) 
						offScreenBuffer.setColor(red);
					else if (brickArea[row][column] == 6) 
						offScreenBuffer.setColor(darkerRed);
					else if (brickArea[row][column] == 7) 
						offScreenBuffer.setColor(darkerDarkerRed);
					else if (brickArea[row][column] == 8) 
						offScreenBuffer.setColor(darkestRed);
					offScreenBuffer.fillRect(xPos+1, yPos+1, BRICK_WIDTH-2, BRICK_HEIGHT-2);
					offScreenBuffer.setColor(Color.black);
				}
			}
		if (lostLife || !gameStarted) {
			
			drawDashedLine(offScreenBuffer);
			if (!gameStarted) {
				levelOne(offScreenBuffer);
				for (int row = 0; row < brickArea.length; row++) 
					for (int column = 0; column < brickArea[row].length; column++) 
						brickValues[row][column] = brickArea[row][column];
			}
		}else {
			keyPressedMoveBouncer();
			checkCollision();
		}
		g.drawImage (offScreenImage, 0, 0, this);
	}

	private void delay (int milliSec) {
		try{
			Thread.sleep (milliSec);
		}catch (InterruptedException e){
		}
	}

	public static void main (String[] args){
		frame = new JFrame ("Brick Breaker");
		FinalProject_Maanik myPanel = new FinalProject_Maanik ();
		frame.add (myPanel);
		frame.pack ();
		frame.setVisible (true);

	}
} 