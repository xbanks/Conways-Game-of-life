// Starts the game of life. Defaults to 100 Cycles

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main 
{
	
	public static void main (String[] args)
	{
		int num_cycles = 100;
		
		JFrame frame = new JFrame("GOL");
		frame.setSize(500, 400);
		
		LifePanel life = new LifePanel(frame.getWidth(), frame.getHeight());
		frame.add(life);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		frame.setVisible(true);
		frame.setResizable(false);
		life.go(num_cycles);

	}
	
}
