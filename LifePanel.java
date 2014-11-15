import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import javax.swing.JPanel;


public class LifePanel extends JPanel
{
	
	private boolean[][] cell_array;
	private Dimension cell_dim;
	private int cell_array_width;
	private int cell_array_height;
	private int border_width_buffer;
	private int border_height_buffer;
	
	private final Color alive_color = Color.black;
	private final Color dead_color = Color.blue; 
	
	private final double percent_alive = .25;
	private final int sleep_time = 100;
	
	public LifePanel(int Width, int Height)
	{
		cell_dim = new Dimension(5, 5);
		border_width_buffer = (Width % cell_dim.width) / 2;
		border_height_buffer = (Height % cell_dim.height) / 2;
		cell_array_width = Width / 5;
		cell_array_height = Height / 5;
		
		cell_array = new boolean[cell_array_width][cell_array_height];
		
		System.out.printf("BWB: %d\nCAW: %d", border_width_buffer, cell_array_width);
		
		resetCellArray();
	}
	
	//	Starts the cycling of generations	
	public void go(int cycles)
	{
		while(cycles > 0)
		{
			try {
				Thread.sleep(sleep_time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nextCycle();
			this.repaint();
			cycles--;
		}
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.setBackground(Color.blue);
		
		
		for(int i = 0; i < cell_array_width; i++)
		{
			for(int j = 0; j < cell_array_height; j++)
			{
				
				Color cell_color = (cell_array[i][j]) ? alive_color : dead_color;
				g2d.setColor(cell_color);
				int x = border_width_buffer + i * cell_dim.width;
				int y = border_height_buffer + j * cell_dim.height;
				g2d.fillRect(x, y, cell_dim.width, cell_dim.height);
				g2d.setColor(dead_color);
//				System.out.printf("[%s]", cell_array[i][j]);	// DEBUG
			}
//			System.out.println();	//	DEBUG
		}
	}
	
	//	Creates a new array with new values 	
	public void resetCellArray()
	{
		clearCellArray();
		Random rand = new Random();
		int block_width = 5;
		int block_height = 4;
		int num_horiz_blocks = cell_array_width / block_width;
		int num_vert_blocks = cell_array_height / block_height;
		
		//	DEBUG		
		System.out.println("horiz blocks: " + num_horiz_blocks);
		System.out.println("vert blocks: " + num_vert_blocks);
		
		for(int i = 0; i < num_horiz_blocks; i++)
			for(int j = 0; j < num_vert_blocks; j++)
			{
				int num_alive = rand.nextInt( (int)(block_width * block_height * percent_alive) ) + 1;
				
				while(num_alive > 0)
				{
					int x = rand.nextInt(block_width);
					int y = rand.nextInt(block_height);
					
					if(!cell_array[ i * block_width + x ]  [ j * block_height + y ])
						cell_array[ i * block_width + x ]  [ j * block_height + y ] = true;
					
					num_alive--;
				}
				
			}
	}
	
	//	Clears the array.	
	private void clearCellArray()
	{
		for(int i = 0; i < cell_array_width; i++)
			for(int j = 0; j < cell_array_height; j++)
				cell_array[i][j] = false;
	}
	
	//	Inverts the values in the array	
	private void invertCellArray()
	{
		for(int i = 0; i < cell_array_width; i++)
			for(int j = 0; j < cell_array_height; j++)
				cell_array[i][j] = !cell_array[i][j];
	}
	
	//	Returns the amount of neighbors for a certain cell	
	private int getNeighbors(int x, int y)
	{
		int neighbors = 0;
		
		Point[] surounding = new Point[8];
		
		for(float theta = 0; theta < 2 * Math.PI-.1; theta += Math.PI/4)
		{
			int x_offset = x + (int) Math.round( Math.cos(theta) );
			int y_offset = y - (int) Math.round( Math.sin(theta) );
			
			if(isValidIndex(x_offset, y_offset) && cell_array[x_offset][y_offset])
				neighbors++;
		}
		
		return neighbors;
	}
	
	//	Checks if the given index isn't out of bounds, eg. (-1, 2)	
	private boolean isValidIndex(int x, int y)
	{
		if(x < 0 || y < 0 || x >= cell_array_width || y >= cell_array_height)
			return false;
		return true;
	}
	
	//	Generates the next cycle of life based on the 4 rules	
	private void nextCycle()
	{
		boolean next_cycle[][] = new boolean[cell_array_width][cell_array_height];
		
		for(int i = 0; i < cell_array_width; i ++)
			for(int j = 0; j < cell_array_height; j++)
			{
				int neighbors = getNeighbors(i, j);
				if(cell_array[i][j])
				{
					if(neighbors < 2)
						next_cycle[i][j] = false;
					else if(neighbors < 4)
						next_cycle[i][j] = true;
					else
						next_cycle[i][j] = false;
				}
				else if(neighbors == 3)
					next_cycle[i][j] = true;
			}
		
		cell_array = next_cycle;
	}
	
	
}
