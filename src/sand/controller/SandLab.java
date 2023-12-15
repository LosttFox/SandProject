package sand.controller;

import java.awt.*;
import java.util.*;

import sand.view.SandDisplay;

public class SandLab
{
  //Step 4,6
  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;
  public static final int CHLORINE_GAS = 4;
  public static final int LAVA = 5;
  public static final int OBSIDIAN = 6;
  public static final int FILL = 7;
  public static final int GLASS = 8;
  
  // particle colours
  private Color sandColor = new Color(184, 184, 152);
  private Color chlorineColor = new Color(98, 240, 133, 10);
  private Color obsidianColor = new Color(38, 27, 61);
  private Color glassColor = new Color(255, 255, 255);
  //direction constants
  private static final int UP = 0;
  private static final int DOWN = 1;
  private static final int LEFT = 2;
  private static final int RIGHT = 3;
  
  
  //do not add any more fields below
  private int[][] grid;
  private SandDisplay display;
  
  
  
  /**
   * Constructor for SandLab
   * @param numRows The number of rows to start with
   * @param numCols The number of columns to start with;
   */
  public SandLab(int numRows, int numCols)
  {
    String[] names;
    // Change this value to add more buttons
    //Step 4,6
    names = new String[8];
    // Each value needs a name for the button
    names[EMPTY] = "Air (empty)";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[CHLORINE_GAS] = "Chlorine Gas";
    names[LAVA] = "Lava";
    names[OBSIDIAN] = "Obsidian";
    names[FILL] = "Fill";
    
    //1. Add code to initialize the data member grid with same dimensions
    this.grid = new int [numRows] [numCols];
    
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    //2. Assign the values associated with the parameters to the grid
    grid[row][col] = tool;
  }

  //copies each element of grid into the display
  public void updateDisplay()
  {
      //Step 3
   //Hint - use a nested for loop
	  for (int row = 0; row < grid.length; row++)
	    {
	    	for (int col = 0; col < grid[0].length; col++)
	    	{
	    		if (grid[row][col] == EMPTY)
	    		{
	    			display.setColor(row, col, Color.black);
	    		}
	    		else if (grid[row][col] == METAL)
	    		{
	    			display.setColor(row, col, Color.gray);
	    		}
	    		else if (grid[row][col] == SAND)
	    		{
	    			display.setColor(row, col, sandColor);
	    		}
	    		else if (grid[row][col] == WATER)
	    		{
	    			display.setColor(row, col, Color.blue);
	    		}
	    		else if (grid[row][col] == CHLORINE_GAS)
	    		{
	    			display.setColor(row, col, chlorineColor);
	    		}
	    		else if (grid[row][col] == LAVA)
	    		{
	    			display.setColor(row, col, Color.orange);
	    		}
	    		else if (grid[row][col] == OBSIDIAN)
	    		{
	    			display.setColor(row, col, obsidianColor);
	    		}
	    		else if (grid[row][col] == GLASS)
	    		{
	    			display.setColor(row, col, glassColor);
	    		}
	    	}
	    }
  }

  //Step 5,7
  //called repeatedly.
  //causes one random particle in grid to maybe do something.
  public void step()
  {
    //Remember, you need to access both row and column to specify a spot in the array
    //The scalar refers to how big the value could be
    //int someRandom = (int) (Math.random() * scalar)
    //remember that you need to watch for the edges of the array
    int randomRow = (int) (Math.random() * grid.length);
    int randomCol = (int) (Math.random() * grid[0].length);
    
    if (grid[randomRow][randomCol] == SAND)
    {
    	updateSand(randomRow, randomCol);
    }
    else if (grid[randomRow][randomCol] == WATER)
    {
    	updateWater(randomRow, randomCol);
    }
    else if (grid[randomRow][randomCol] == CHLORINE_GAS)
    {
    	updateGas(randomRow, randomCol);
    }
    else if (grid[randomRow][randomCol] == LAVA)
    {
    	updateLava(randomRow, randomCol);
    }
    else if (grid[randomRow][randomCol] == FILL)
    {
    	fill(display.getTool());
    }
    
  }
  
  private void updateSand(int currentRow, int currentCol)
  {
	int randomDirection = (int) (Math.random() * 2) + 2;
	  
   	if (currentRow + 1 < grid.length)
   	{
   		if (grid[currentRow + 1][currentCol] == EMPTY)
   	{
   			swapParticles(currentRow, currentCol, currentRow + 1, currentCol);
   		}
   		else if (grid[currentRow + 1][currentCol] == WATER)
   		{
   			swapParticles(currentRow, currentCol, currentRow + 1, currentCol);
   		}
   		else if (grid[currentRow + 1][currentCol] == SAND)
   		{
   			distributeFluids(currentRow, currentCol, randomDirection, DOWN, grid[currentRow][currentCol]);
   		}
   		else if (grid[currentRow + 1][currentCol] == LAVA)
   		{
   			grid[currentRow][currentCol] = GLASS;
   		}
   	}
  }
  
  private void updateWater(int currentRow, int currentCol)
  {
	int randomDirection = (int) (Math.random() * 2) + 2;
	  
	if (currentRow + 1 < grid.length && (grid[currentRow + 1][currentCol] == EMPTY || grid[currentRow + 1][currentCol] == CHLORINE_GAS))
  	{
  		swapParticles(currentRow, currentCol, currentRow + 1, currentCol);
  	}
	else if (currentRow + 1 < grid.length && grid[currentRow + 1][currentCol] == LAVA)
	{
		grid [currentRow + 1][currentCol] = OBSIDIAN;
		grid[currentRow][currentCol] = EMPTY;
	}
	else if (currentRow + 1 < grid.length && (grid[currentRow + 1][currentCol] == METAL || grid[currentRow + 1][currentCol] == SAND || grid[currentRow + 1][currentCol] == WATER))
	{
		distributeFluids(currentRow, currentCol, randomDirection, DOWN, grid[currentRow][currentCol]);
	}
  }
  
  private void updateGas(int currentRow, int currentCol)
  {
	  int randomDirection = (int) (Math.random() * 2) + 2;
	  
	  if (currentRow - 1 > -1)
	  {
		  if (grid[currentRow - 1][currentCol] == EMPTY || grid[currentRow - 1][currentCol] == WATER || grid[currentRow - 1][currentCol] == LAVA || grid[currentRow - 1][currentCol] == SAND)
		  {
			  swapParticles(currentRow, currentCol, currentRow - 1, currentCol);
		  }
		  else if (grid[currentRow - 1][currentCol] == METAL)
		  {
			  distributeFluids(currentRow, currentCol, randomDirection, UP, grid[currentRow][currentCol]);
		  }
		  else if (grid[currentRow - 1][currentCol] == CHLORINE_GAS)
		  {
			  distributeFluids(currentRow, currentCol, randomDirection, UP, grid[currentRow][currentCol]);
		  }
	  }
  }
  
  private void updateLava(int currentRow, int currentCol)
  {
	  int randomDirection = (int) (Math.random() * 2) + 2;
	  
	  if (currentRow + 1 < grid.length)
	  {
		  int tileBelow = grid[currentRow + 1][currentCol];
		  
		  if (tileBelow == EMPTY || tileBelow == CHLORINE_GAS)
		  {
			  swapParticles(currentRow, currentCol, currentRow + 1, currentCol);
		  }
		  else if (tileBelow == METAL)
		  {
			  grid[currentRow + 1][currentCol] = LAVA;
		  }
		  else if (tileBelow == WATER)
		  {
			  grid[currentRow + 1][currentCol] = OBSIDIAN;
			  grid[currentRow][currentCol] = EMPTY;
		  }
		  else if (tileBelow == SAND)
		  {
			  grid[currentRow + 1][currentCol] = GLASS;
		  }
		  else if (tileBelow == LAVA || tileBelow == OBSIDIAN || tileBelow == GLASS)
		  {
			  distributeFluids(currentRow, currentCol, randomDirection, DOWN, grid[currentRow][currentCol]);
		  }
	  }
  }
  
  private void distributeFluids(int currentRow, int currentCol, int direction, int flowDirection, int particle) 
  {
	    boolean isFluid = isFluid(particle);
	  	int shift = 1;
	    boolean flowed = false;

	    while ((currentCol - shift >= 0 || currentCol + shift < grid[0].length) && !flowed) 
	    {
	        // Making targetRow shifted up / down 1 based on flowDirection, also makes sure it doesn't go out of bounds
	    	int targetRow = currentRow;
	        
	        if (flowDirection == UP && currentRow - 1 >= 0)
	        {
	        	targetRow = currentRow - 1;
	        }
	        else if (flowDirection == DOWN && currentRow + 1 < grid.length)
	        {
	        	targetRow = currentRow + 1;
	        }
	        
	        // Making targetCol shifted left / right [shift] amount based on direction, also makes sure it doesn't go out of bounds
	        int targetCol = currentCol;

	        if (direction == LEFT && currentCol - shift >= 0)
	        {
	        	targetCol = currentCol - shift;
	        }
	        else if (direction == RIGHT && currentCol + shift < grid[0].length)
	        {
	        	targetCol = currentCol + shift;
	        }
	        
	        
	        
	        if (grid[targetRow][targetCol] == EMPTY) 
	        {
	            swapParticles(currentRow, currentCol, targetRow, targetCol);
	            flowed = true;
	        } 
			else if (grid[targetRow][targetCol] == METAL || grid[targetRow][targetCol] == OBSIDIAN || grid[targetRow][targetCol] == GLASS || grid[targetRow][targetCol] == SAND) 
	        {
				flowed = true;  
	        }
			else if (grid[targetRow][targetCol] == particle) 
	        {
	            shift++;  
	        } 
			else if ((isFluid && (density(grid[targetRow][targetCol]) > density(particle) 
				  || (density(particle) < density(EMPTY)) && grid[targetRow][targetCol] < density(particle)))
				  || (!isFluid && (density(grid[targetRow][targetCol]) < density(particle))))
	        {
	            
	            swapParticles(currentRow, currentCol, targetRow, targetCol);
	            flowed = true;
	        } 
			else 
	        {
	            flowed = true;  
	        }
	    }
	}

  
  private int density(int particle)
  {
	  int density = -1;
	  
	  if (particle == CHLORINE_GAS)
	  {
		  density = 0;
	  }
	  else if (particle == EMPTY)
	  {
		  density = 1;
	  }
	  else if (particle == WATER)
	  {
		  density = 2;
	  }
	  else if (particle == LAVA)
	  {
		  density = 3;
	  }
	  else if (particle == SAND || particle == GLASS)
	  {
		  density = 4;
	  }
	  else if (particle == METAL)
	  {
		  density = 5;
	  }
	  else if (particle == OBSIDIAN)
	  {
		  density = 6;
	  }
	  
	  return density;
  }
  
  private boolean isFluid(int particle)
  {
	  if (particle == EMPTY || particle == WATER || particle == CHLORINE_GAS || particle == LAVA)
	  {
		  return true;
	  }
	  return false;
  }
  
  private void swapParticles(int rowOne, int colOne, int rowTwo, int colTwo)
  {
	  int tool = grid[rowTwo][colTwo];
	  grid[rowTwo][colTwo] = grid[rowOne][colOne];
	  grid[rowOne][colOne] = tool;
  }
  
  public void fill(int particle)
  {
	  for (int outerIndex = 0; outerIndex < grid.length; outerIndex++)
	  {
		  for (int innerIndex = 0; innerIndex < grid[0].length; innerIndex++)
		  {
			  grid[innerIndex][outerIndex] = particle;
		  }
	  }
  }
  
  //do not modify this method!
  public void run()
  {
    while (true) // infinite loop
    {
      for (int i = 0; i < display.getSpeed(); i++)
      {
        step();
      }
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
      {
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
      }
    }
  }
}
