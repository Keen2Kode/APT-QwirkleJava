import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Qwirkle {
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		Engine engine = new Engine();
		
		boolean runningStatus = true;
	    while (runningStatus)
	    {
	    	System.out.println("Welcome to Qwirkle!\n-------------------");
	        System.out.print("1. New Game\n2. Load Game\n3. Show student information\n4. Quit\n> ");
	        int mainInput = sc.nextInt();

		    /*gameStart(gameEngine, runningStatus); - stored within newGame so that unnecessary eof's arent called/repeating code*/
	        if 		(mainInput == 1)	runningStatus = newGameMenu(engine);
	        else if (mainInput == 2)    {}
	        else if (mainInput == 3)    runningStatus = true;//replace with member print method that returns true
	        else if (mainInput == 4)    runningStatus = false;
	    }
	    System.out.println("Good bye");
		
	}
	
	static boolean newGameMenu(Engine engine) 
	{
		System.out.println("\n\nStarting new game: ");
		engine.addPlayer("straight");
		engine.addPlayer("fire");
		engine.addPlayer("homy");
		return oneTurn(engine);
	}
	
	static boolean oneTurn(Engine engine) {

		boolean runningStatus = !engine.isGameOver();
		if (runningStatus)	System.out.println("\n\n" + engine.getSelected().name + ", it's your turn.");
		else				System.out.println("Game over! Final scores: ");
		
		for (Player player : engine.players)
			System.out.println("Score for " + player.name + ": " + player.points);
		System.out.println(engine.toString());
		
		if (runningStatus)
		{
			System.out.println("Your hand is: " + engine.getSelected().hand + "\n> ");
			runningStatus = aiOptions(engine); //playerOptions(engine);
		}
	    return runningStatus ?  oneTurn(engine) : false;
	}
	
	private static boolean aiOptions(Engine engine)
	{
		Tile toPlace = engine.tilesPlaceable(engine.getSelected().hand); 
		Tile toReplace = engine.getSelected().hand.getFirst();
		
		if (toPlace != null)	engine.place(toPlace);
		else					engine.transaction(toReplace, false);
		try	{Thread.sleep(100);}	catch (Exception e) {}
		return true;
	}
	
	private static boolean playerOptions(Engine engine)
	{
		Scanner sc = new Scanner(System.in);
		String[] command = sc.nextLine().split(" ");
		
		boolean validCommand = false; 
		boolean runningStatus = !command[0].equals("quit");
		
		if (runningStatus)
		{
			// assume validated/correct format
			if 		(command[0].equals("place")) 	validCommand = place(command, engine);
			else if (command[0].equals("replace")) 	validCommand = replace(command, engine); 
			else if (command[0].equals("save"))		validCommand = save(command, engine);
			runningStatus = validCommand ? true : playerOptions(engine);
		}
		return runningStatus;
	}
	
	
	private static boolean place(String[] command, Engine engine)
	{
		Color color = Color.getColor(command[1].substring(0,1));
		Shape shape = Shape.getShape(command[1].substring(1));
		Tile tile = new Tile(color, shape);
		
		// correct formatting?
		int y = command[3].charAt(0) - 'A';
		int x =  Integer.parseInt(command[3].substring(1));
		
	
		if (engine.isValid(tile, x, y))
		{
			engine.place(tile);
			if (engine.qwirkles() == 1)	System.out.println(" Yaay QWIRRKAAAA");
			if (engine.qwirkles() == 2) System.out.println("boiii");
			return true;
		}
		else 
		{
			System.out.println("Invalid move. Try again.");
			return false;
		}
	}
	
	private static boolean replace(String[] command, Engine engine)
	{
		Color color = Color.getColor(command[1].substring(0,1));
		Shape shape = Shape.getShape(command[1].substring(1));
		Tile tile = new Tile(color, shape);
		// correct formatting?
		
		if (engine.canReplace(tile))
			engine.transaction(tile, false);
		return true;
		
	}
	
	private static boolean save(String[] command, Engine engine)
	{
		System.out.println("Saving to file " + command[1]);
		PrintWriter pw = null;
		File file = new File(command[1] + ".txt");
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Player p : engine.players)
			pw.write(p.toString());
		pw.write(engine.toString());
		pw.write(engine.getSelected().toString());
		return true;
	}
}
