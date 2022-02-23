import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.net.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BrickBreakerFrame extends JFrame implements ActionListener
{

	final int BRICK_WIDTH = 70;
	final int BRICK_HEIGHT = 25;

	
	JTitlePanel titlePanel;
	DummyPanel dummyPanel;
	GamePanel gamePanel;
	GameOverPanel gameOverPanel;

	
	int screen = 1; 


	Timer timer;


	boolean gameStarted = false;

	public BrickBreakerFrame (String title)
	{
		
		// Setting the defaults for the panel
		//		frame.setPreferredSize (new Dimension (20 * BRICK_WIDTH + 2 * BORDER_SIZE + 1, (10 + 1) * SQUARE_SIZE + TOP_OFFSET + BORDER_SIZE + 1));
		super(title);
		setPreferredSize (new Dimension (18 * BRICK_WIDTH, 26 * BRICK_HEIGHT+ 50));
		setLocation (45, 10);
		setBackground (Color.black);


		
		
		// Set up the Menu
		// Set up the Game MenuItems
		JMenuItem exitOption;
		
		exitOption = new JMenuItem ("Exit");

		// Set up the Game Menu
		JMenu gameMenu = new JMenu ("Menu");

		// Add each MenuItem to the Game Menu (with a separator)
		
		gameMenu.addSeparator ();
		gameMenu.add (exitOption);
		gameMenu.setOpaque(true);
		gameMenu.setBackground(Color.DARK_GRAY);

		JMenuBar mainMenu = new JMenuBar ();
		mainMenu.add (gameMenu);
		setJMenuBar (mainMenu);
		
		titlePanel = new JTitlePanel();
		titlePanel.addStartButtonActionListener(this);
		dummyPanel = new DummyPanel();
		dummyPanel.addContinueButtonListener(this);
		gamePanel = new GamePanel();
		gameOverPanel = new GameOverPanel();
		gameOverPanel.addNewGameButtonListener(this);


		exitOption.setActionCommand ("Exit");
		exitOption.addActionListener (this);
		
		

		//setFocusable (true); // Need this to set the focus to the panel in order to add the keyListener
switchPanel(1);

	} 
	
	
	private void switchPanel(int screen) {
		if (screen == 1) {
			add(titlePanel);
			remove(dummyPanel);
			remove(gamePanel);
			remove(gameOverPanel);
		}
		else if (screen == 2){
			remove(titlePanel);
			add(dummyPanel);
			remove(gamePanel);
			remove(gameOverPanel);
		}else if (screen == 3) {
			remove(titlePanel);
			remove(dummyPanel);
			add(gamePanel);
			remove(gameOverPanel);
		}else if (screen == 4) {
			remove(titlePanel);
			remove(dummyPanel);
			remove(gamePanel);
			add(gameOverPanel);
		}
		pack();
	}
	

	public void actionPerformed (ActionEvent event){

		String eventName = event.getActionCommand ();
		System.out.println("action performed " + eventName);
		if (eventName.equals("Exit")) {
			System.exit(0);
		}else 
		if (eventName.equals ("Start game")) {
			switchPanel (3);
		}else if (eventName.equals("How to Play")) {
			switchPanel(2);
		}else if (eventName.equals("Continue")) {
			switchPanel(4);
		}else if (eventName.equals("Play Again")) {
			switchPanel(1);
		}
		pack();
		setVisible(true);
		

	}




	public static void main (String[] args)
	{
		BrickBreakerFrame frame = new BrickBreakerFrame("Brick Breaker");
		
		frame.pack ();
		frame.setVisible (true);

	}
} 
