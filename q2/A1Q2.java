import java.io.*;
import java.util.*;

public class A1Q2
{
  /*
   enum Direction {
   UP(0), DOWN(2), RIGHT(1), LEFT(3);
   
   public Direction(int value)
   {
   this.value = value;
   }
   
   private int value;
   public int value()
   {
   return value;
   }
   }*/
  
  public static void main(String[] args) 
  {
    try
    {
      String inputLine;                    //store the first line of the file
      String[] tokens;             //the tokenized contents of 
      String file = "q2input.txt";
      
      BufferedReader fileIn = new BufferedReader(new FileReader(file)); //access the file
      inputLine = fileIn.readLine(); //get the first line of the file
      tokens = inputLine.trim().split("\\s"); // tokenize the words in each input line
      
      int[][] inputMaze = new int[ Integer.parseInt(tokens[0]) ][ Integer.parseInt(tokens[1] )];
      
      inputLine = fileIn.readLine();//get first line of maze
      int height = 0;
      int value;
      while (inputLine != null)      //read all lines of file
      {
        tokens = inputLine.trim().split("");
        for (int width = 0; width < tokens.length; width++)        //loop through all the words in the tokenized line
        {
          if ( tokens[width].charAt(0) == '#')
          {
            inputMaze[height][width] = Maze.Tile.WALL.getValue();
          }
          else if ( tokens[width].charAt(0) == '!')
          {
            inputMaze[height][width] = Maze.Tile.FLAG.getValue();
          }
          else if ( tokens[width].charAt(0) == '.')
          {
            inputMaze[height][width] = Maze.Tile.OPEN.getValue();
          }
          else if ( tokens[width].charAt(0) == 'h' )
          {
            inputMaze[height][width] = Maze.Tile.HERO.getValue();
          }
        }
        inputLine = fileIn.readLine(); //get the next line
        height++;
      }
      
      
      Maze maze = new Maze(inputMaze);
      Maze result = AStarHelper(maze);
      System.out.println("\n\n****SOLUTION:");
      result.print();
      //testCompareMaze(maze);
      /*
       * ArrayList<Maze> closed = new ArrayList<Maze>();
       * 
       for (int i = 0; i< 10; i++)
       {
       maze.moveHero(1);
       //maze.print();
       closed.add(maze); //adding the new move to the list of mazes already done
       }
       
       ArrayList<Maze> children = testChildrenGeneration(maze);
       
       
       //ArrayList<Maze> solnPath = AStarHelper(maze);
       */
    }
    
    catch (IOException e) 
    {
      System.out.println(e.getMessage());
    }
  }
  
  public static void testCompareMaze(Maze maze)
  {
    Loc flag = new Loc(10, 2); //create a new flag to find the distance to
    Maze maze2 = maze.clone(); //clone the first maze
    for (int i = 0; i< 10; i++)
    {
      maze2.moveHero(1); //move the person right a bunch
    }
    maze.print();
    maze2.print();
  }
  
  public static void testMove(Maze maze)
  {
    //move the hero to the right
    for (int i = 0; i< 10; i++)
    {
      maze.moveHero(1);
      maze.print();
    }
    
    //move the hero up!
    for (int i = 0; i< 10; i++)
    {
      maze.moveHero(0);
      maze.print();
    }
  }
  
  public static ArrayList<Maze> testChildrenGeneration(Maze maze)
  {
    ArrayList<Maze> children = maze.generateChildren();
    
    System.out.println("\n*************TESTING CHILDREN GENERATION***************");
    System.out.println(children.toString());
    
    return children;
  }
  
  
  public static void testEucDist()
  {
    Loc d1 = new Loc(0, 0);
    Loc d2 = new Loc(1, 1);
    Loc d3 = new Loc(6, 9);
    
    System.out.println("d1-d1: "+d1.euclideanDistance(d1));
    System.out.println("d1-d2: "+d1.euclideanDistance(d2));
    System.out.println("d1-d3: "+d1.euclideanDistance(d3));
    System.out.println("d2-d1: "+d2.euclideanDistance(d1));
    System.out.println("d2-d2: "+d2.euclideanDistance(d2));
    System.out.println("d2-d3: "+d2.euclideanDistance(d3));
    System.out.println("d3-d1: "+d3.euclideanDistance(d1));
    System.out.println("d3-d2: "+d3.euclideanDistance(d2));
    System.out.println("d3-d3: "+d3.euclideanDistance(d3));
  }
  
  public static Maze AStarHelper(Maze maze)
  {
    //calculate distance to each of the flags from heroLoc
    //perform AStar search from hero to closest flag
    //if null delete that flag (unreachable)
    Maze solution = maze;

    while (maze.flagsLeft() != 0)   //Keep using the A star algorithm while there are flags to find
    {
      int closestFlagPos = maze.getClosestFlagLoc();
      Loc closestFlag = maze.getFlag(closestFlagPos);
      System.out.println("Closest flag to hero: "+closestFlagPos+" Actual loc: "+closestFlag);
      
      maze = AStar(maze, closestFlag);    //do the search and return a maze thats the same when failed or a new maze state


      maze.deleteFlag(closestFlagPos);    //delete the closest flag


    }
  
    
    
    return solution;
  }
  
  
  public static Maze AStar(Maze startState, Loc goal)
  {
    //Comparator<Loc> comparator = new FlagDistanceComparator();
    ArrayList<Maze> open = new ArrayList<Maze>();
    ArrayList<Maze> closed = new ArrayList<Maze>();
    //ArrayList<Maze> solution = new ArrayList<Maze>();
    ArrayList<Maze> children;
    Maze current, solution = startState;
    int currentLoc;

    open.add(startState);

    while (open.size() != 0)
    {
      currentLoc = findLowestCost(open, goal);
      current = open.get(currentLoc);   //get the lowest costing state (priority queue hack)

      if (current.getHeroLoc().compare(goal) == true)
      {
        return current;
      }
      
      //generate child states, check if not in closed or open
      children = current.generateChildren();
      for (int i = 0; i< children.size(); i++)
      {
        System.out.println("\n\n\n**********************CHILDREN:");
        children.get(i).print();
      }

      closed.add(current);
      open.remove(currentLoc);
    }
    //this should return something
    return solution;
  }


  //Adds the heuristic value to the current cost to reach that state to determine cost
  public static int findLowestCost(ArrayList<Maze> list, Loc goal)
  {
    Maze lowestCost = null;
    double currLowest;
    double bestLowest = 9999;
    int bestLocation = 0;

    System.out.println("List size: "+list.size());
    for (int i = 0; i< list.size(); i++)
    {
      currLowest = list.get(i).getCost() + list.get(i).heuristicValue(goal); //goal being the flag location
      //System.out.println("Curr lowest: "+currLowest);

      if (currLowest < bestLowest)
      {
        bestLocation = i;
      }
    }

    return bestLocation;
  } 
  
} //END MAIN



class Loc
{
  private int x;
  private int y;
  
  public Loc(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public String toString()
  {
    return "("+x+", "+y+")";
  }
  
  public int getX() {return x;}
  public int getY() {return y;}
  public void setY(int y) {this.y = y;}
  public void setX(int x) {this.x = x;}
  
  public boolean compare(Loc a)
  {
    boolean result = false;
    if (a.getX() == this.x && a.getY() == this.y)
    {
      result = true;
    }
    return result;
  }
  
  public double euclideanDistance(Loc other)
  {
    double dY = y - other.getY();
    double dX = x - other.getX();
    double result = Math.sqrt(dX*dX + dY*dY);
    return result;
  }
  
  public Loc clone()
  {
    Loc cloneLoc = new Loc(this.x, this.y);
    return cloneLoc;
  }
}





class Maze // implements Comparator<Integer>
{ 
  private ArrayList<Maze> children;
  private int moves;
  public int[][] maze;
  private int height;
  private int width;
  private Loc heroLoc;
  private int cost;
  private ArrayList<Loc> flags;
  
  public enum Tile
  {
    OPEN(0), WALL(-1), FLAG(-2), HERO(-3);
    
    private int value;
    private Tile(int value) 
    {
      this.value = value;
    }
    public int getValue()
    {
      return value;
    }
  }
  
  public Maze(int[][] newMaze)
  {
    this.maze = newMaze;
    height = maze.length;
    width = maze[0].length;
    heroLoc = null;
    cost = 0;
    flags = new ArrayList<Loc>();
    children = new ArrayList<Maze>();
    
    analyzeMaze(); //adds flags, the hero location too the Maze object.
  }
  
  public Maze clone()
  {
    Maze mazeClone;
    Loc cloneHeroLoc;
    int[][] arrayClone = new int[this.maze.length][];
    for ( int i= 0; i < this.maze.length; i++)
    {
      arrayClone[i] = this.maze[i].clone();
    }
    
    mazeClone = new Maze(arrayClone);
    cloneHeroLoc = this.heroLoc.clone();
    mazeClone.setHeroLoc(cloneHeroLoc);
    return mazeClone;
  }

  public Loc getFlag(int position)
  {
    return flags.get(position);
  }

  public int flagsLeft()
  {
    return flags.size();
  }

  public void deleteFlag(int arrayLoc)
  {
    flags.remove(arrayLoc);
  }

//finds the euclidean distance from the flag to the hero
  public double heuristicValue(Loc goal)
  {
    return heroLoc.euclideanDistance(goal);
  }

  public int getCost() {return cost;}
  
   //takes the hero location and goes through all of the flags to find the location of the closest flag!
    public int getClosestFlagLoc()
    {
      //Loc result = null;
      int result = 0;
      double closestDist = 99999;
      double currDist;
      for (int i = 0; i< flags.size(); i++)
      {
        currDist = heroLoc.euclideanDistance(flags.get(i));
        if (currDist < closestDist)
        {
          closestDist = currDist;
          result = i;
//result = flags.get(i);  //assign location to closest flag as return value
        }
      }
      return result;
    }
  
  public void addChild(Maze child)
  {
    children.add(child);
  }
  
  public void setHeroLoc(Loc location) { this.heroLoc = location; }
  public Loc getHeroLoc() {return heroLoc; }
  
  
  public ArrayList<Maze> generateChildren()
  {
    ArrayList<Maze> children = new ArrayList<Maze>();
    boolean valid = false; //tells us if the move is valid, so we can add it to children or not
    //for each possible move direction attempt to move the hero that direction
    
    
    for ( int i = 0; i < 4; i++) //for the 4 directions we can move
    {
      valid = false; //resetting valid as false
      Maze child = this.clone();
      valid = child.moveHero(i);
      System.out.println("Valid? "+valid+", child: "+child.toString());
      
      if (valid == true)
      {
        children.add(child);
      }
    }
    return children;
  }
  
  //Moves the hero in the desired direciton if the move is possible and returns results (true/false) for that direction
  /*
   * public ArrayList<Node> generateChildren()
   {
   ArrayList<Node> children = new ArrayList<Node>();
   boolean valid; //tells us if the move is valid, so we can add it to children or not
   //for each possible move direction attempt to move the hero that direction
   
   Node parent = new Node(this);
   Node child = null;
   
   for ( int i = 0; i < 4; i++)
   {
   Maze clone = this.clone();
   valid = clone.moveHero(i);
   
   if (valid = true)
   {
   child = new Node(clone);
   children.add(child);
   }
   }
   return children;
   }*/
  
  public int bestMove()
  {
    return 0;
  }
  
  //Calculates the cost from the hero location to the location of a flag in the maze
  public double getCost(Loc flag)
  {
    double cost = 0;
    return cost;
  }
  
  public boolean moveHero(int direction)
  {
    boolean valid = false;
    //get the direction, see if its valid (not a wall), then move the hero and modify the 
    //System.out.println("Moving direction: "+direction);
    if (direction == 0) { //up direction
      if ( (maze[heroLoc.getY()-1][heroLoc.getX()] ) != Tile.WALL.getValue() )
      {
        //System.out.println("Moving up");
        maze[heroLoc.getY()][heroLoc.getX()] = maze[heroLoc.getY()][heroLoc.getX()] +1;
        heroLoc.setY(heroLoc.getY()-1);
        valid = true;
      }
    }
    else if (direction == 1) { //right direction
      if ( (maze[heroLoc.getY()][heroLoc.getX()+1] )!= Tile.WALL.getValue() )
      {
        //System.out.println("Moving right");
        maze[heroLoc.getY()][heroLoc.getX()] = maze[heroLoc.getY()][heroLoc.getX()] +1;
        heroLoc.setX(heroLoc.getX()+1);
        valid = true;
      }
    }
    else if (direction == 2) { //down direction
      if ( (maze[heroLoc.getY()+1][heroLoc.getX()] ) != Tile.WALL.getValue() )
      {
        //System.out.println("Moving down");
        maze[heroLoc.getY()][heroLoc.getX()] = maze[heroLoc.getY()][heroLoc.getX()] +1;
        heroLoc.setY(heroLoc.getY()+1);
        valid = true;
      }
    }
    else if (direction == 3) { //left direction
      if ( (maze[heroLoc.getY()][heroLoc.getX()-1] ) != Tile.WALL.getValue() )
      {
        //System.out.println("Moving left");
        maze[heroLoc.getY()][heroLoc.getX()] = maze[heroLoc.getY()][heroLoc.getX()] +1;
        heroLoc.setX(heroLoc.getX()-1);
        valid = true;
      }
    }

    if (valid == true)  //only increase the current cost to reach this state when a move is valid
    {
      cost++;
    }
    return valid;
  }
  
  
  //Makes sure that the flags aren't immediatly surrounded by falls, and if so
  private void analyzeMaze()
  {
    Loc newItem;
    
    for (int y = 0; y < height; y++)
    {
      for(int x = 0; x< width; x++)
      {
        if (maze[y][x] == Tile.FLAG.value)
        {
          newItem = new Loc(x, y);
          flags.add(newItem);
        }
        else if (maze[y][x] == Tile.HERO.value)
        {
          heroLoc = new Loc(x, y);
          maze[y][x] = Tile.OPEN.value; //modify the tile back to 0, and then just print out the hero in print method instead
        }
      }
    }
  }
  
  
  public String toString()
  {
    String result = "\n";
    char wall = '#';
    char hero = 'h';
    char flag = '!';
    char print;
    for (int y = 0; y< height; y++)
    {
      for(int x = 0; x< width; x++)
      {
        
        if (maze[y][x] == -1)
        {
          result+= wall;
        }
        else if (maze[y][x] == -2)
        {
          result+=flag;
        }
        /*else if (y == heroLoc.getY() && x == heroLoc.getX() )
         {
         print = hero;
         } */
        else
        {
          result+= ( (char) (maze[y][x]+'0') );
        }
      }
      result+="\n"; //start a new line
    }  
    return result;
  }
  
  
  public void print()
  {
    char wall = '#';
    char hero = 'h';
    char flag = '!';
    char print;
    for (int y = 0; y< height; y++)
    {
      for(int x = 0; x< width; x++)
      {
        
        if (maze[y][x] == -1)
        {
          print = wall;
        }
        else if (maze[y][x] == -2)
        {
          print = flag;
        }
        /*else if (y == heroLoc.getY() && x == heroLoc.getX() )
         {
         print = hero;
         } */
        else
        {
          print = (char) (maze[y][x]+'0');
        }
        
        System.out.print(print);
        
      }
      System.out.println();
    }
    
    //Print out the locations of important items on the map
    if (heroLoc != null)
    {
      System.out.println("Hero at: "+heroLoc.toString());
    }
    if ( flags != null)
    {
      System.out.println("Flags at: "+flags.toString());
    }
    System.out.println("Current cost: "+cost);
  }
}

/*
class FlagDistanceComparator implements Comparator<Loc>
{
  //Compares two maps for distance to the flag
  @Override
  public int compare(Loc one, Loc two)
  {
    //double distOne = one.euclideanDistance()
    
    if (one > two)
    {
      return -1;
    }
    else if (two > one)
    {
      return 1;
    }
    return 0; //when even
  }
}
*/
class Node 
{
  private Maze data;
  private ArrayList<Node> children;
  
  public Node( Maze data)
  {
    this.data = data;
    ArrayList<Node> children = new ArrayList<Node>();
  }
  
  public void addChild(Node child)
  {
    if (children != null)
    {
      children.add(child);
    }
  }
  
  public Maze getData() 
  {
    return data;
  }
}