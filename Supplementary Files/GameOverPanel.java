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

public class GameOverPanel extends JPanel 
{

	final int BRICK_WIDTH = 70;
	final int BRICK_HEIGHT = 25;



	JButton btnNew;
	

	// For drawing images offScreen (prevents Flicker)
	// These variables keep track of an off screen image object and
	// its corresponding graphics object
	Image offScreenImage;
	Graphics offScreenBuffer;


	public GameOverPanel ()
	{

		setBackground (Color.black);
		setLayout(null);


		btnNew = new JButton ("Play Again");
		add(btnNew);
		btnNew.setBounds(600, 400, 100, 50);



	} 
	// start menu (mode #1)
	public void paintComponent(Graphics g) {
		Color reds = new Color (225, 0 , 0);
		Color blues = new Color (51,204,255 );
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, 18 * BRICK_WIDTH, 26 * BRICK_HEIGHT+ 75);
		g.setColor(reds);
		g.setFont(new Font("symbol", Font.BOLD, 80));
		g.drawString("GAME OVER", 400,(26 * BRICK_HEIGHT+ 50)/2 );
	}

	// need to add key listeniner cuz in the case of pressed, 2 buttons will move out, the title will move up)
	// two buttons are rule screen(3) and start game(2) 

	// Constructor
	
	public void addNewGameButtonListener (ActionListener btnListener) {
		btnNew.addActionListener(btnListener);
	}

} 
