import java.util.LinkedList;
import java.util.List;

public class Player {

	public Player (String name, LinkedList<Tile> hand)
	{
		this.name = name;
		this.hand = hand;
	}
	
	String name;
	LinkedList<Tile> hand;
	int points;
	
	public String toString()
	{
		return String.format("%s\n%d\n%s\n", name, points, hand.toString());
	}

}
