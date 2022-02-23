import javax.swing.*;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

// Text fields help get input from the user
public class TitlePanel extends JPanel implements ActionListener{
	
	JFrame frame;
	JPanel myPanel;
	JButton start,rule;
	
	public TitlePanel () {
		
	
		setLayout (new GridLayout (0,2,10,5));
		setBorder (BorderFactory.createEmptyBorder (50,50,50,50));		
		
		
		
		start = new JButton ("Start Game");
		start.setActionCommand ("Start");
		start.addActionListener (this);
		
		rule = new JButton("How to Play");
		rule.setActionCommand("rules");
		rule.addActionListener(this);
		
		
		add(Box.createRigidArea(new Dimension(20,0))); // adding space in between
		add(Box.createRigidArea(new Dimension(20,0)));
		
		add (start);
		add(rule);
		
	
		
		setVisible (true);
		
	}
	
	public void actionPerformed (ActionEvent event) {
		String eventName = event.getActionCommand();
		if (eventName.equals ("Start")) {
			
		}
	}
	
	public static void main (String [] args){
				new TitlePanel ();
	}
}