import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class A1Q1
{
  
  public static void main(String[] args) 
  {
    int maxSteps = 0;
    int numPeople = 0;
    ArrayList<Integer> people = new ArrayList<Integer>();
    
    /*
     if (args.length > 1) //greater than one because we need atleast 2 arguments to create the problem
     {
     System.out.println(args[0]);
     maxSteps = Integer.parseInt(args[0]);
     
     for ( int i = 1; i< args.length; i++)
     {
     System.out.println("New child: "+args[i]);
     people.add(Integer.parseInt(args[i]));
     numPeople++;
     }
     
     //Do all the searches here with
     //heuristicSearch(people, maxSteps);
     
     }
     else
     {
     System.out.println("No command line arguments, ending program...");
     }
     */
    
    ArrayList<Integer> testPeople = new ArrayList<Integer>();
    testPeople.add(5);
    testPeople.add(12);
    testPeople.add(3);
    testPeople.add(8);
    
    
    int testSteps = 10;
    
    HeuristicSearch heurSoln = new HeuristicSearch(testPeople, testSteps);
    
  }
  
  public static void testCompare()
  {
    ArrayList<Integer> testPeople = new ArrayList<Integer>();
    testPeople.add(5);
    testPeople.add(12);
    testPeople.add(3);
    testPeople.add(8);
    
    ArrayList<Integer> testPeople2 = new ArrayList<Integer>();
    testPeople2.add(5);
    testPeople2.add(6);
    testPeople2.add(1);
    
    ArrayList<Integer> testPeople3 = new ArrayList<Integer>();
    testPeople3.add(5);
    testPeople3.add(12);
    testPeople3.add(3);
    testPeople3.add(8);
    
    int testSteps = 28;
    //beginSearches(testPeople, testSteps);
    
    State testState = new State(testPeople);
    State testState2 = new State(testPeople2);
    State testState3 = new State(testPeople3);
    
    System.out.println("test 1: " +testState.compareState(testState2));   //false
    System.out.println("test 2: " +testState2.compareState(testState2));  //true
    System.out.println("test 3: " +testState.compareState(testState3));   //true
    System.out.println("test 4: " +testState3.compareState(testState));   //true
    
  }
  
  public static void beginSearches(ArrayList<Integer> people, int maxSteps)
  {
    boolean complete;
    int searches = 0;
    int failed = 0;
    
    //complete = HeuristicSearch(people, maxSteps);
    /*if (complete == false)
     {
     failed++; 
     }*/
  }
}


class Move
{
  public enum MoveType {LEFT, RIGHT}
    
  private int personOne;
  private int personTwo;
  private MoveType moveDirection;
  
  public Move(int pOne, MoveType direction)
  {
    personOne = pOne;
    personTwo = -1;
    moveDirection = direction;
  }
  
  public Move(int pOne, int pTwo, MoveType direction)
  {
    this.personOne = pOne;
    this.personTwo = pTwo;
    this.moveDirection = direction;
      
  }
}

abstract class Data 
{
  abstract void printState();
  abstract ArrayList<Integer> getRight();
  abstract ArrayList<Integer> getLeft();
}

class State extends Data
{ 
  private ArrayList<Integer> left;
  private ArrayList<Integer> right;
  private boolean light; //always move the light when moving people. false = on left, true = light on right side.
  private boolean complete;
  private Move lastMove;
  
  public State()
  {
    left = new ArrayList<Integer>();
    right = new ArrayList<Integer>();
    light = false;
    complete = false;
    lastMove = null;
  }
  
  public State(ArrayList<Integer> startingPeople)
  {
    left = startingPeople;
    right = new ArrayList<Integer>();
    light = false;
    complete = false;
    lastMove = null;
  }
  
  public State newState(State old, Move move)
  {
    State newState = new State();
    newState = old.clone();
    
    return newState;
  }
  
  public boolean compareState(State previous)
  {
    boolean same = false;
    if (previous.left.size() == left.size() && light == previous.light && complete == previous.complete)
    {
      same = true;
    }
    return same;
  }
  
  public ArrayList<Integer> getLeft()
  {
    return left;
  }
  
  public ArrayList<Integer> getRight()
  {
    return right;
  }
  
  public boolean getLight()
  {
    return light;
  }
  
  public State clone()
  {
    State copyState = new State();
    copyState.left = this.left;
    copyState.right = this.right;
    copyState.light = this.light;
    copyState.complete = this.complete;
    copyState.lastMove = this.lastMove;
    return copyState;
  }
  
  public ArrayList<Move> generateMoves()
  {
   ArrayList<Move> moveList = new ArrayList<Move>(); 
   return moveList;
  }
  
  public State movePeople(Move move, Move.MoveType direction)
  {
    
    
    System.out.println("movePeople method");
    return null;
  }
  
  public boolean completeCheck()
  {
    if (left.size() == 0 && right.size() >= 1 && light == true)
    {
      complete = true;
    }
    return complete;
  }
  
  public void printState()
  {
    String lightLoc;
    if (light == false)
    {
      lightLoc = "Left side";
    }
    else
    {
      lightLoc = "Right side";
    }
    
    System.out.println("State:\n  ->Left side: "+left.toString());
    System.out.println("  ->Right side: "+right.toString());
    System.out.println("  ->Light location: "+lightLoc);
    System.out.println("  ->Is complete? "+complete);
  }
}

class Node
{
  private Node prev;
  private Data data;
  
  public Node(Node p, State d)
  {
    prev = p;
    data = d;
  }
  
  public Node(State d)
  {
    data = d;
    prev = null;
  }
  
  public Node getPrev()
  {
    return prev;
  }
  
  public void printState()
  {
    data.printState();
  }
  
  public Data getData()
  {
    return data;
  }
}


class HeuristicSearch
{
  
  private ArrayList<State> stateList;  
  private ArrayList<Move> moveList;
  private boolean solved;
  
  public HeuristicSearch(ArrayList<Integer> people, int maxSteps)
  {
    stateList = new ArrayList<State>();
    moveList = new ArrayList<Move>();
    solved = false;
    beginSearch(people, maxSteps);
    
  }
//heuristic search always chooses to move the fastest person with anyone else to the 
  //right, and the fastest person on the right back to the left
  private static void beginSearch(ArrayList<Integer> people, int maxSteps)
  {
    int stepNumber = 0;
    State initialState = new State(people);   //create the initial starting state
    Deque promisingStates = new LinkedList();             //fill this up with potential next states Nodes to visit
    
    System.out.println("Starting Heuristic Search...");
    
    
    
    initialState.printState();
    State blankState = new State();
    blankState.printState();
    
    State testCopy = initialState.clone();
    testCopy.printState();
    
    
    //ArrayList<Move> potentialMoves = generateMoves(Move.MoveType.RIGHT);
    
    //setting up while loop
    boolean complete = false;
    State currentState = initialState;
    int round = 1;
    while (stepNumber <= maxSteps && complete != true)
    {
      ArrayList<Integer> right = currentState.getRight();
      ArrayList<Integer> left = currentState.getLeft();
      
      System.out.println("Round: "+round+": Step Number: "+stepNumber+" maxSteps: " +maxSteps);
      System.out.println(right.toString());
      System.out.println(left.toString());
      
//create nodes for all of the possible moves
      
      
      //do best move for right
      
      //check if state is complete?
      
      //create new state and node and add to solution
      //complete = checkState(newState);
      /*if (complete == false)
      {
        //get best move left
        //move person back left
      }*/
      round++;
      stepNumber++;
    }
  }
}