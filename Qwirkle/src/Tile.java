
public class Tile {

	public Tile(Color c, Shape s)
	{
		color = c;
		shape = s;
	}
	
	private Color color;
	private Shape shape;
	
	
	
	
	boolean fits(Tile tile)
	{
		boolean isCommonColor = tile.color == color;
		boolean isCommonShape = tile.shape == shape;
		return isCommonColor ^ isCommonShape;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		boolean isCommonColor = ((Tile) obj).color == color;
		boolean isCommonShape = ((Tile) obj).shape == shape;
		return isCommonColor && isCommonShape;
	}

	public String toString()
	{
		return //isFilled ? 
				color.key + shape.key;
	}
}








enum Color
{
	RED("R"), BLUE("B"), GREEN("G"), YELLOW("Y"), ORANGE("O"), PURPLE("P");
	
	Color(String key)
	{
		this.key = key;
	}
	String key;
	
	
	public static Color getColor(String key)
	{
		for (Color color : values())
			if (color.key.equals(key.toUpperCase()))
				return color;
		return null;
	}
	
	public String toString()
	{
		return name();
	}
}

enum Shape
{
	CIRCLE(1), FOUR_STAR(2), DIAMOND(3), SQUARE(4), SIX_STAR(5), CLOVER(6);
	
	Shape(int key)
	{
		this.key = key;
	}
	int key;
	
	
	public static Shape getShape(String key)
	{
		for (Shape shape : values())
			if (shape.key == Integer.parseInt(key))
				return shape;
		return null;
	}
	
	public String toString()
	{
		return name();
	}
	
}

