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

public class DummyPanel extends JPanel 
{

	final int BRICK_WIDTH = 70;
	final int BRICK_HEIGHT = 25;



	JButton btnCont;
	

	// For drawing images offScreen (prevents Flicker)
	// These variables keep track of an off screen image object and
	// its corresponding graphics object
	Image offScreenImage;
	Graphics offScreenBuffer;

	boolean gameStarted = false;

	public DummyPanel ()
	{

		setBackground (Color.black);
		setLayout(null);

		btnCont = new JButton ("Continue");
		add(btnCont);
		btnCont.setBounds(600, 400, 100, 50);

	} 
	// start menu (mode #1)
	public void paintComponent(Graphics g) {
		Color darkBlue = new Color (0,0,50);
	g.setColor(darkBlue);
		g.fillRect(0, 0, 18 * BRICK_WIDTH, 26 * BRICK_HEIGHT+ 75);
		g.setColor(Color.WHITE);
		g.setFont(new Font("sansserif", Font.PLAIN, 50));
		g.drawString("How to play", 50, 50);
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("monospaced", Font.PLAIN, 16));
		g.drawString("The objective is to destroy all the bricks above your paddle.", 50, 100);
		g.drawString("Use the ball located above your paddle to hit the bricks.", 50, 150);
		g.drawString("Use your paddle to bounce the ball towards the bricks", 50, 200);
		g.drawString("Move your mouse from left to right to control your paddle", 50, 250);
	}


	

	// Constructor
	
	public void addContinueButtonListener (ActionListener btnListener) {
		btnCont.addActionListener(btnListener);
	}

} 
