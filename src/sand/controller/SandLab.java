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
  public static final int CLEAR = 5;
  
  // particle colours
  private Color sandColor = new Color(184, 184, 152);
  private Color chlorineColor = new Color(98, 240, 133, 10);
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
    names = new String[6];
    // Each value needs a name for the button
    names[EMPTY] = "Air (empty)";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[CHLORINE_GAS] = "Chlorine Gas";
    names[CLEAR] = "Clear";
    
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
    else if (grid[randomRow][randomCol] == CLEAR)
    {
    	clear();
    }
    
  }
  
  private void updateSand(int currentRow, int currentCol)
  {
	if (currentRow + 1 < grid.length)
	{
		if (grid[currentRow + 1][currentCol] == EMPTY)
		{
			swapParticles(currentRow, currentCol, currentRow + 1, currentCol);
		}
		else if (grid[currentRow + 1][currentCol] == WATER)
		{
			grid[currentRow + 1][currentCol] = WATER;
			swapParticles(currentRow, currentCol, currentRow + 1, currentCol);
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
	else if (currentRow + 1 < grid.length && grid[currentRow + 1][currentCol] == WATER)
	{
		distributeFluids(currentRow, currentCol, randomDirection, DOWN, grid[currentRow][currentCol]);
	}
  }
  
  private void updateGas(int currentRow, int currentCol)
  {
	  int randomDirection = (int) (Math.random() * 2) + 2;
	  
	  if (currentRow - 1 > -1)
	  {
		  if (grid[currentRow - 1][currentCol] == EMPTY)
		  {
			  swapParticles(currentRow, currentCol, currentRow - 1, currentCol);
		  }
		  else if (grid[currentRow - 1][currentCol] == WATER)
		  {
			  swapParticles(currentRow, currentCol, currentRow - 1, currentCol);
		  }
		  else if (grid[currentRow - 1][currentCol] == CHLORINE_GAS)
		  {
			  distributeFluids(currentRow, currentCol, randomDirection, UP, grid[currentRow][currentCol]);
		  }
	  }
  }
  
  private void distributeFluids(int currentRow, int currentCol, int direction, int flowDirection, int particle)
  {
	  int shift = 1;
	  boolean flowed = false;
	  
	  if (direction == LEFT)
	  {
		  if (flowDirection == DOWN)
		  {
			  if (currentRow + 1 < grid.length)
			  {
				  while (currentCol - shift > -1 && !flowed)
				  {
					  if(density(grid[currentRow + 1][currentCol - shift]) < density(particle))
					  {
						  swapParticles(currentRow, currentCol, currentRow + 1, currentCol - shift);
						  shift = 1;
						  flowed = true;
					  }
					  else
					  {
						  shift++;
					  }
				  }
			  }
		  }
		  else if (flowDirection == UP)
		  {
			  if (currentRow - 1 > -1)
			  {
				  while (currentCol - shift > -1 && !flowed)
				  {
					  if(density(grid[currentRow - 1][currentCol - shift]) > density(particle))
					  {
						  swapParticles(currentRow, currentCol, currentRow - 1, currentCol - shift);
						  shift = 1;
						  flowed = true;
					  }
					  else
					  {
						  shift++;
					  }
				  }
			  }
		  }
	  }
	  else if (direction == RIGHT)
	  {
		  if (flowDirection == DOWN)
		  {
			  if (currentRow + 1 < grid.length)
			  {
				  flowed = false;
				  while (currentCol + shift < grid[0].length && !flowed)
				  {
					  if (density(grid[currentRow + 1][currentCol + shift]) < density(particle))
					  {
						  swapParticles(currentRow, currentCol, currentRow + 1, currentCol + shift);
						  shift = 1;
						  flowed = true;
					  }
					  else
					  {
						  shift++;
					  }
				  }
			  }
		  }
		  else if (flowDirection == UP)
		  {
			  if (currentRow - 1 > -1)
			  {
				  flowed = false;
				  while (currentCol + shift < grid[0].length && !flowed)
				  {
					  if (density(grid[currentRow - 1][currentCol + shift]) > density(particle))
					  {
						  swapParticles(currentRow, currentCol, currentRow - 1, currentCol + shift);
						  shift = 1;
						  flowed = true;
					  }
					  else
					  {
						  shift++;
					  }
				  }
			  }
		  }
	  }
	  else
	  {
		  return;
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
	  else if (particle == SAND)
	  {
		  density = 3;
	  }
	  else if (particle == METAL)
	  {
		  density = 4;
	  }
	  
	  return density;
  }
  
  private void swapParticles(int rowOne, int colOne, int rowTwo, int colTwo)
  {
	  int tool = grid[rowTwo][colTwo];
	  grid[rowTwo][colTwo] = grid[rowOne][colOne];
	  grid[rowOne][colOne] = tool;
  }
  
  public void clear()
  {
	  for (int outerIndex = 0; outerIndex < grid.length; outerIndex++)
	  {
		  for (int innerIndex = 0; innerIndex < grid[0].length; innerIndex++)
		  {
			  grid[innerIndex][outerIndex] = EMPTY;
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
