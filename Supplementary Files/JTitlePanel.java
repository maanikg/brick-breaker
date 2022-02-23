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

public class JTitlePanel extends JPanel 
{
	final int BRICK_WIDTH = 70;
	final int BRICK_HEIGHT = 25;


	JButton btnStart, btnRules;
	Image movingBar;
	Timer timer;

	// For drawing images offScreen (prevents Flicker)
	// These variables keep track of an off screen image object and
	// its corresponding graphics object
	Image offScreenImage;
	Graphics offScreenBuffer;

	boolean gameStarted = false;

	public JTitlePanel ()
	{
		setBackground (Color.black);
//		setLayout (new BoxLayout (this, BoxLayout.PAGE_AXIS));


		setLayout(null);
		btnStart = new JButton ("Start game");
		add(btnStart);
		btnStart.setBounds( 600, 400, 100,50);
		btnRules = new JButton("How to Play");
		add(btnRules);
		btnRules.setBounds( 600, 450, 100,50);




	} 
	// start menu (mode #1)
	public void paintComponent(Graphics g) {
		g.fillRect(0, 0, 18 * BRICK_WIDTH, 26 * BRICK_HEIGHT+ 50);
		g.setFont(new Font ("TimesRoman", Font.BOLD, 90));
		g.setColor(Color.WHITE);
		g.drawString("BRICK BREAKER", 290  , (26 * BRICK_HEIGHT+ 50)/2);
		repaint();
	}


	// need to add key listeniner cuz in the case of pressed, 2 buttons will move out, the title will move up)
	// two buttons are rule screen(3) and start game(2) 

	// Constructor
	
	public void addStartButtonActionListener (ActionListener btnListener) {
		btnStart.addActionListener(btnListener);
		btnRules.addActionListener(btnListener);
	}

} 
