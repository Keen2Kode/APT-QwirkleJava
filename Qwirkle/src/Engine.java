import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Engine {
	
	public Engine()
	{		
		createTileBag();
		players = new ArrayList<>();
		selected = 0;
		board = new Board();
		horizontal = new LinkedList<Tile>();
		vertical = new LinkedList<Tile>();
	}
	
	void createTileBag()
	{
		tileBag = new LinkedList<Tile>();
		for (int i=0; i<30; i++)							/*types changed*/
			for (Color c : Color.values())
			for (Shape s : Shape.values())
				tileBag.add(new Tile(c,s));
		Collections.shuffle(tileBag);
	}
	
	void addPlayer(String name)
	{
		LinkedList<Tile> hand = new LinkedList<>();
		for (int i=0; i<6; i++)
			hand.add(tileBag.removeFirst());
		players.add(new Player(name, hand));
	}
	
	
	/* 	2d array for efficiency (parsing adjacent tiles)
	 *	no need of vector of vectors since the tiles are static*/
	Board board;
	LinkedList<Tile> tileBag;
	
	List<Player> players;
	private int selected;
	// store these because used in multiple methods
	// avoid over-parametrizing functions
	private int x;
	private int y;
	// store these for code modularity between isValid(tile) and place(tile)
	private LinkedList<Tile> horizontal;
	private LinkedList<Tile> vertical;
	// alternative to changing lot of Qwirkle and GameEngine code
//	private boolean isFirstTurn;
	private static final int NORTH = 0;
	private static final int SOUTH = 1;
	private static final int EAST = 2;
	private static final int WEST = 3;
	
	
	
	
	


	
	
	
	
	
	
	
	
	
	boolean canReplace(Tile tile)
	{
		return players.get(selected).hand.contains(tile) && !tileBag.isEmpty();
	}
	
	// when called during regular gameplay
	boolean isValid(Tile tile, int x, int y)
	{	
		boolean playerHasTile =	true;								// players.get(selected).hand.contains(tile);
		return playerHasTile && isValid(x, y, false) && isValid(tile);
	}
	
	// this can be called by Qwirkle during regular gameplay
	// OR by isGameOver() to check if tiles are still placeable
	// OR by readBoard() when loading the file
	
	// first check if the coordinates are valid
	// this also serves as an initializer for the valid check
	// since this is very resource heavy
	private boolean isValid(int x, int y, boolean loadCheck)
	{
		boolean validCoordinate = false;
		
		if (x >= 0 && x < Board.MAX
		&& 	y >= 0 && y < Board.MAX
		&&	(board.get(x, y) == null) ^ loadCheck)
		{
			this.x = x;
			this.y = y;
			
			// choice of vectors over individual tile checking for reusability
			// eg: to avoid recalculations of points, no repeating tiles checking etc.
			vertical.clear();
			horizontal.clear();
			buildTileList(1, NORTH);
			buildTileList(1, EAST);
			// add the tiles later for efficiency
			// to avoid recreating the lists each time
			validCoordinate = true;
		}
		return validCoordinate;
	}
	
	// CONTRACT: isValid(x,y) has been called first
	private boolean isValid(Tile tile)
	{
		boolean validTile = false;
		vertical.add(tile);
		horizontal.add(tile);
		if ((vertical.size()>1  || horizontal.size()>1 || board.count()==0)
		&&	isValid(vertical) && isValid(horizontal))
			validTile = true;
		return validTile;
	}
	
	private boolean isValid(List<Tile> tl)
	{
		// don't perform size checks as it's validity depends on the other LinkedList<Tile>
		// EMPTY LISTS ARE VALID
		
		// find if there's a copy
		for (int i=0;   i<tl.size(); i++)
		for (int j=i+1; j<tl.size(); j++)
			if (!tl.get(i).fits(tl.get(j)))
				return false;
		return true;
	}
	
	// builds the tile list with tiles in the chosen direction
	// needs initial direction to be NORTH/EAST
	private void buildTileList(int distance, int direction)
	{
		Tile newTile = board.get(distance, x, y, direction);
		boolean condition = false;
		if (newTile != null)
		{
			(direction<2 ? vertical : horizontal).add(newTile);
			distance++;
			condition = true;
		}
		// cover NORTH+SOUTH OR EAST+WEST
		else if (direction%2 == 0)
		{
			distance = 1;
			direction++;
			condition = true;
		}
		if (condition)
			buildTileList(distance, direction);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	void transaction(Tile tile, boolean placeOrReplace)
	{
		/*remove first find instance in c++*/
		players.get(selected).hand.remove(tile);
		
		// if replacing place in bag or placing on board
		if (placeOrReplace) board.set(x,y, tile);
		else 				tileBag.add(tile);				
		
		if (!tileBag.isEmpty())
			players.get(selected).hand.add(tileBag.removeFirst());
		// always switch players after a successful transaction
		switchPlayer();
	}
	
	void place(Tile tile)
	{
		int vertPoints = vertical.size()>1   ? vertical.size()   : 0;
		int horiPoints = horizontal.size()>1 ? horizontal.size() : 0;
		// first move
		int singlePoints = board.count()==0 ? 1 : 0;
		players.get(selected).points += singlePoints + vertPoints + horiPoints;
		
		// give new Tile to player IF tiles left	
		transaction(tile, true);
	}
	
	void switchPlayer()
	{
		selected = (selected+1) % players.size();
	}
	
	public int qwirkles()
	{
		int qwirkles = 0;
		if (vertical.size() == 6) 	qwirkles++;
		if (horizontal.size() == 6) qwirkles++;
		return qwirkles;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	Player getSelected()
	{
		return players.get(selected);
	}
	
	List<Player> getPlayers()
	{
		return players;
	}
	
	boolean isGameOver()
	{
		boolean emptyHandedPlayer = false;
		for (Player player : players)
			if (player.hand.isEmpty())
			{
				System.out.println("empty player");
				emptyHandedPlayer = true;
			}
		
		List<Tile> tiles = new LinkedList<>(tileBag);
		for (Player player : players)
			tiles.addAll(player.hand);
		return tilesPlaceable(tiles) == null || emptyHandedPlayer;
		
	}
	
	// return null if no tile can be placed ANYWHERE on the board
	Tile tilesPlaceable(List<Tile> tilesToPlace)
	{
		
		// iterate through coordinates randomly
		List<Integer> coordinates = new LinkedList<>();
		for (int i=0; i<Board.MAX*Board.MAX; i++)
			coordinates.add(i);
		Collections.shuffle(coordinates);
		
		// search each coordinate (randomly) if a tile can be placed there
		for (int a : coordinates)
		{
			int x = a%Board.MAX;
			int y = a/Board.MAX;
			if (isValid(x, y, false))
				for (Tile tile : tilesToPlace)
				{
					// reuse the valid Tile method
					if (isValid(tile))
						// the coordinates already set
						return tile;
					// for efficiency: replace each tile 
					// without RE-building vertical/horizontal vects
					vertical.remove(tile);
					horizontal.remove(tile);
				}
		}
		return null;
	}
	
	
	private boolean isValidBoard()
	{
		// search each coordinate's position
		for (int y=0; y<Board.MAX; y++)
		for (int x=0; x<Board.MAX; x++)
			if (isValid(x,y, true) && isValid(board.get(x, y)) == false)
				return false;
		return true;
	}
	
	
	
	public String toString()
	{
		return board.toString();
	}
	
}
