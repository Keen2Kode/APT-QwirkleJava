import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Board {
	
	public Board()
	{		
		tiles = new Tile[MAX][MAX];
	}
	
	static String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
	"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	static final int MAX = Board.alphabet.length;
	
	/* 	2d array for efficiency (parsing adjacent tiles)
	 *	no need of vector of vectors since the tiles are static*/
	Tile[][] tiles;
	private static final int NORTH = 0;
	private static final int SOUTH = 1;
	private static final int EAST = 2;
	private static final int WEST = 3;
	
	
	void set(int x, int y, Tile tile)
	{
		tiles[y][x] = tile;
	}
	
	Tile get(int x, int y)
	{
		return get(0, x, y, NORTH);
	}
	
	// gets the tile relative to the requested tile location
	Tile get(int a, int x, int y, int direction)
	{
		Tile tile = null;
		try
		{
			switch (direction)
			{
				case NORTH : tile = tiles[y+a][x];	break;
				case SOUTH : tile = tiles[y-a][x];	break;
				case EAST  : tile = tiles[y][x+a];	break;
				case WEST  : tile = tiles[y][x-a];  break;
			}
		}
		catch (ArrayIndexOutOfBoundsException e){};
		return tile;
	}
	
	// used by isValidBoard() isValid() noTilesPlaceable()
	// an alternative to checking firstTurn
	int count()
	{
		int tileCount = 0;
		for (int y=0; y<Board.MAX; y++)
		for (int x=0; x<Board.MAX; x++ )
			if (tiles[y][x] != null)
				tileCount++;
		return tileCount;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//toString for reusability between print and store methods
	// always prints a square board
	private String toString(int minRow, int maxRow, int minCol, int maxCol)
	{
		// print column indexes
		String xIndexes = "   ";
		for (int col=minCol; col<=maxCol; col++)
			xIndexes += String.format(" %-2s",col);
		
		xIndexes += "\n   ";
		for (int col=minCol; col<=maxCol; col++)
			xIndexes += "---";
		
		// print row indexes and all nodes
		String rows = "\n";
		for (int row=minRow; row<=maxRow; row++)
		{
			rows += String.format(" %s ", alphabet[row]);
			for (int col=minCol; col<=maxCol; col++) 
				rows += String.format("|%s", tiles[row][col]!=null ? tiles[row][col].toString():"  ");
			rows += String.format("|\n");
		}
		return xIndexes + rows;
	}
//	
//	public String toString()
//	{
//		int minRow=0, maxRow=MAX-1, minCol=0, maxCol=MAX-1;
//		boolean minRowFound=false, maxRowFound=false, minColFound=false, maxColFound=false;
//		
//		// shrink the grid to its minimum size
//		for (int a=0; a<MAX; a++)
//		{
//			int b = MAX-1-a;
//			for (int scan=0; scan<MAX; scan++)
//			{
//				if (tiles[a][scan] != null)	minRowFound = true;
//				if (tiles[b][scan] != null)	maxRowFound = true;
//				if (tiles[scan][a] != null)	minColFound = true;
//				if (tiles[scan][b] != null)	maxColFound = true;
//			}	
//			// stop when max and min are too close
//			if (!minRowFound && maxRow>minRow+2) minRow++;
//			if (!maxRowFound && maxRow>minRow+2) maxRow--;
//			if (!minColFound && maxCol>minCol+2) minCol++;
//			if (!maxColFound && maxCol>minCol+2) maxCol--;
//		}
//		// add padding
//		if (minRow>0)			minRow--;
//		if (maxRow<MAX-1)	maxRow++;
//		if (minCol>0)			minCol--;
//		if (maxCol<MAX-1)	maxCol++;
//		
//		// grow the grid to square, avoiding edges
//		for (int i=0; maxRow-minRow != maxCol-minCol; i++)
//		{
//			int rows = maxRow - minRow;
//			int cols = maxCol - minCol;
//			if (rows<cols && i%2==0 && maxRow<MAX-1) 	maxRow++;
//			if (rows<cols && i%2!=0 && minRow>0) 			minRow--;
//			if (cols<rows && i%2==0 && maxCol<MAX-1) 	maxCol++;
//			if (cols<rows && i%2!=0 && minCol>0)  			minCol--;
//		}
//		return toString(minRow, maxRow, minCol, maxCol);
//	}
//	
	
	public String toString()
	{
		int minRow = MAX-1, minCol = MAX-1;
		int maxRow = 0, maxCol = 0;
		
		for (int row=0; row<MAX; row++)
		for (int col=0; col<MAX; col++)
		{
			if (tiles[row][col] != null)
			{ 
				minRow = Math.min(row, minRow);
				maxRow = Math.max(row, maxRow);
				minCol = Math.min(col, minCol);
				maxCol = Math.max(col, maxCol);
			}
		}
		
		boolean isEmpty = minRow>maxRow;
		if (isEmpty)
		{
			minRow = MAX/2;
			maxRow = MAX/2;
			minRow = MAX/2;
			minRow = MAX/2;
		}
		// add padding
		if (minRow>0)			minRow--;
		if (maxRow<MAX-1)	maxRow++;
		if (minCol>0)			minCol--;
		if (maxCol<MAX-1)	maxCol++;
		
		// grow the grid to square, avoiding edges
		for (int i=0; maxRow-minRow != maxCol-minCol; i++)
		{
			int rows = maxRow - minRow;
			int cols = maxCol - minCol;
			if (rows<cols && i%2==0 && maxRow<MAX-1) 	maxRow++;
			if (rows<cols && i%2!=0 && minRow>0) 		minRow--;
			if (cols<rows && i%2==0 && maxCol<MAX-1) 	maxCol++;
			if (cols<rows && i%2!=0 && minCol>0)  		minCol--;
		}
		return toString(minRow, maxRow, minCol, maxCol);
	}
	
}
