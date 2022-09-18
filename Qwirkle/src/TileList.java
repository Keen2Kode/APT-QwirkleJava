import java.util.LinkedList;
import java.util.List;

public class TileList extends LinkedList<Tile> {

	public TileList(Tile starter)
	{
		add(starter);
	}
	
	public TileList()
	{
	}
	
//	boolean isValidTile(Tile tile)
//	{
//		
//		/*does not account for if unselected tiles match*/
//		if (isEmpty() || contains(tile))
//			return false;
//		
//		boolean isCommonColor = true;
//		boolean isCommonShape = true;
//		for (Tile t : this)
//		{
//			if (t.color != tile.color) isCommonColor = false;
//			if (t.shape != tile.shape) isCommonShape = false;
//		}
//		return isCommonColor || isCommonShape;
//	}
//	/* null not allowed*/
//	boolean contains(Tile tile)
//	{
//		for (Tile t : this)
//			if (tile.equals(t))
//				return true;
//		return false;
//	}
	
	boolean isValid()
	{
		// don't perform size checks as it's validity depends on the other tileList
		// EMPTY LISTS ARE VALID
		
		// find if there's a copy
		for (int i=0;   i<size(); i++)
		for (int j=i+1; j<size(); j++)
			if (!get(i).fits(get(j)))
				return false;
		return true;
	}
	
	
	

	
}

