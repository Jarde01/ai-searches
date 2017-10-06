import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ArrayDeque;

public class A1Q1
{
  
  public static void main(String[] args) 
  {
    int maxTime = 0;
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
    testPeople.add(12);
    testPeople.add(11);
    testPeople.add(3);
    testPeople.add(8);
    
    
    int testTime = 28;
    
    //HeuristicSearch heurSoln = new HeuristicSearch(testPeople, testTime);
    BreadthFirstSearch bfsSoln = new BreadthFirstSearch(testPeople, testTime);
    
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
  
  public Move clone()
  {
    Move moveClone = new Move(this.personOne, this.personTwo, this.moveDirection);
    return moveClone;
  }
  
  public int getFirstPerson()
  {
    return personOne;
  }
  
  public int getSecondPerson()
  {
    return personTwo;
  }
  
  public MoveType getDirection()
  {
    return moveDirection;
  }
  
  public String toString()
  {
    String direction;
    
    //finding out if moving to the right or left
    if (moveDirection == Move.MoveType.RIGHT)
    {
      direction = "Right";
    }
    else
    {
      direction = "Left";
    }
    
    //finding out if the move is for one or two people
    if (personTwo >= 0)
    {
      return "Move people: "+personOne+" and "+personTwo+" "+direction;
    }
    else 
    {
      return "Move people: "+personOne+" "+direction;
    }
  }
}

abstract class Data 
{
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
  private int elapsedTime;
  
  public State()
  {
    left = new ArrayList<Integer>();
    right = new ArrayList<Integer>();
    light = false;
    complete = false;
    lastMove = null;
    int elapsedTime = 0;
  }
  
  public State(ArrayList<Integer> startingPeople)
  {
    left = startingPeople;
    right = new ArrayList<Integer>();
    light = false;
    complete = false;
    lastMove = null;
    elapsedTime = 0;
  }
  
  public boolean compareState(State previous)
  {
    boolean same = false;
    if (previous.left.size() == left.size() && light == previous.light && complete == previous.complete && elapsedTime == previous.elapsedTime)
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
  
  public ArrayList<Integer> getSide(Move.MoveType side)
  {
    if (side == Move.MoveType.RIGHT)
    {
      return getRight();
    }
    else 
    {
      return getLeft();
    }
  }
  
  public boolean getLight()
  {
    return light;
  }
  
  public int getTime()
  {
    return elapsedTime;
  }
  
  //Proper clone method for arraylists and this class as a whole
  public State clone()
  {
    State copyState = new State();
    for (int i = 0; i< this.left.size(); i++)
    {
      copyState.left.add(this.left.get(i));
    }
    
    for (int j = 0; j< this.right.size(); j++)
    {
      copyState.right.add(this.right.get(j));
    }
    
    copyState.light = this.light;
    copyState.complete = this.complete;
    copyState.lastMove = this.lastMove;
    copyState.elapsedTime = this.elapsedTime;
    return copyState;
  }
  
  public ArrayList<Move> generateMoves(Move.MoveType direction)
  {
    ArrayList<Move> moveList = new ArrayList<Move>(); 
    
    if (direction == Move.MoveType.RIGHT) //we want to move people to the right
    {
      for (int i = 0; i< left.size()-1; i++)
      {
        for ( int j = i+1; j < left.size(); j++)
        {
          Move newMove = new Move(i, j, direction);
          moveList.add(newMove);
        }    
      }
    }
    
    else //moving people to the left side (always one person)
    {
      int rightSize = right.size();
      for (int i = 0; i< right.size(); i++)
      {
        Move newMove = new Move(i, direction);
        moveList.add(newMove);
      }
    }
    
    return moveList;
  }
  
  public State movePeople(Move move)
  {
    lastMove = move;
    ArrayList<Integer> side;
    ArrayList<Integer> otherside;
    int firstPerson = move.getFirstPerson();
    int secondPerson = move.getSecondPerson();
    Move.MoveType direction = move.getDirection();
    
    if (direction == Move.MoveType.RIGHT)
    {
      side = this.left;
      otherside = this.right;
    }
    else
    {
      side = this.right;
      otherside = this.left;
    }
    
    otherside.add(side.get(firstPerson)); //this possible contains -1, when we are moving from right to left
    if (secondPerson >= 0)
    {
      otherside.add(side.get(secondPerson));
      
      //find out which person is slower and add that time to the current states time
      if ( side.get(firstPerson) > side.get(secondPerson))
      {
        this.elapsedTime+= side.get(firstPerson);
      }
      else 
      {
        this.elapsedTime+= side.get(secondPerson);
      }
      
      side.remove(secondPerson); //always remove the second person first (so order isn't bad)
    }
    
    else //just add the single persons time to elapsedTime
    {
      this.elapsedTime+=side.get(firstPerson);
    }
    
    //able to remove first person if second person is now removed or didn't exist
    //because the move generator always makes the first person earlier in the list
    side.remove(firstPerson);
    
    //Flipping the light to the opposite side
    if (this.light == false)
    {
      this.light = true;
    }
    else
    {
      this.light = false;
    }
    
    return this;
  }
  
  public boolean completeCheck()
  {
    if (left.size() == 0 && right.size() >= 1 && light == true)
    {
      complete = true;
    }
    return complete;
  }
  
  public String toString()
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
    
    return "\nState:\n  ->Left side: "+left.toString()+"\n  ->Right side: "+right.toString()+"\n  ->Light location: "+lightLoc+"\n  ->Elapsed time: "+elapsedTime+"\n  ->Is complete? "+complete;
  }
}

class Node
{
  private Node prev;
  private Data data;
  private boolean visited;
  
  public Node(Node p, State d)
  {
    prev = p;
    data = d;
    visited = false;
  }
  
  public Node(State d)
  {
    data = d;
    prev = null;
    visited = false;
  }
  
  public Node getPrev()
  {
    return prev;
  }
  
  public boolean visited()
  {
    return visited;
  }
  public void visit()
  {
    this.visited = true;
  }
  
  public Data getData()
  {
    return data;
  }
}

class Solution
{
  private ArrayList<State> stateList;  //list of states visited
  private ArrayList<Move> moveList;    //list of moves taken to get to each state
  private boolean solved;              //using seperate solved flag from the last state b/c this takes into account how long it took
  
  public Solution()
  {
    stateList = new ArrayList<State>();
    moveList = new ArrayList<Move>();
    solved = false;
  }
  
  public void addState(State s)
  {
    stateList.add(s);
  }
  
  public void addMove(Move m)
  {
    moveList.add(m);
  }
  
  public String StatesToString()
  {
    String result = "";
    for (int i = 0; i< stateList.size(); i++)
    {
      result +=stateList.get(i).toString();
    }
    return result;
  }
  
  public String MovesToString()
  {
    String result = "";
    for (int i = 0; i< moveList.size(); i++)
    {
      result += "  "+i+". " +moveList.get(i).toString()+"\n";
    }
    return result;
  }
  
  public boolean isComplete()
  {
    return solved;
  }
  
  public void toggleSolved()
  {
    solved = true;
  }
  
  public void printResults()
  {
    String results = "";
    
    if (isComplete() == true)
    {
      results += "Search was successful!\nHere are your results:\n";
    }
    else 
    {
            results += "Search was unsuccessful in the alotted time... \nHere is the best solution:\n";
    }

    System.out.println(results + this.StatesToString());
    System.out.println("\nHere is how we got here: (NOTE: Numbers are referencing positions)\n"+ this.MovesToString()); 
  }
}

class HeuristicSearch
{
  private Solution solution;
  
  public HeuristicSearch(ArrayList<Integer> people, int maxTime)
  {
    solution = new Solution();
    beginSearch(people, maxTime); 
    solution.printResults();
  }
  
//heuristic search always chooses to move the fastest person with anyone else to the 
  //right, and the fastest person on the right back to the left
  private void beginSearch(ArrayList<Integer> people, int maxTime)
  {
    int currentTime = 0;
    State initialState = new State(people);   //create the initial starting state
    ArrayList<Move> potentialMoves;           //all of the moves possible from any state given
    ArrayList<Move> potentialMovesRight;
    Move bestMove;                            //the move we will use to generate the next state
    boolean complete = false;                 //flag telling while loop when solution is found
    State currentState = initialState;        //keeping track of the current state in the while loop
    State newState;
    
    System.out.println("Starting Heuristic Search...");
    solution.addState(initialState);
    
    complete = initialState.completeCheck();   //check if the solution is already completed (impossible b/c people are always put onto left side at start)
        
    //setting up while loop
    while (currentTime <= maxTime && complete != true)
    {
      //Create possible moves to the right
      potentialMoves = currentState.generateMoves(Move.MoveType.RIGHT);
      //System.out.println("Possible moves RIGHT: "+potentialMovesRight.toString());
      
      bestMove = heuristicFunction(currentState, potentialMoves);
      
      if (bestMove != null)
      {
        solution.addMove(bestMove);  //adding the best move to the move solution list
        
        newState = currentState.clone();
        newState.movePeople(bestMove);
        currentTime = newState.getTime();
        //do the move on the state, create a new state 
        
        currentState = newState;
        solution.addState(currentState);     //must clone the state otherwise the final state is only one
        complete = currentState.completeCheck();
        //currentState.printState();
      }
      
      //Moving people back onto the right if no solution yet!
      if ( complete != true)
      {
        potentialMoves = currentState.generateMoves(Move.MoveType.LEFT);
        //System.out.println("Possible moves LEFT: "+potentialMovesLeft.toString());
        
        bestMove = heuristicFunction(currentState, potentialMoves);
        
        if (bestMove != null)
        {
          solution.addMove(bestMove);  //adding the best move to the move solution list
          
          newState = currentState.clone();
          newState.movePeople(bestMove);
          currentTime = newState.getTime();
          //do the move on the state, create a new state 
          
          //update currentState for next while loop
          //add this new state to the state list
          //check if the new state is complete
          currentState = newState;
          solution.addState(currentState);     //must clone the state otherwise the final state is only one
          complete = currentState.completeCheck();
          //currentState.printState();
        }
      }
    }
    
    if (currentState.getTime() < maxTime)
    {
      solution.toggleSolved();  //mark the solution as solved
    }
  }
  
  private Move heuristicFunction(State state, ArrayList<Move> move)
  {
    int currTime;  //want to minimize this value when moving right
    int bestTime = 999;
    Move bestMove = null;
    ArrayList<Integer> currentSide;
    Move currentMove;
    Move.MoveType direction;
    
    for (int i = 0; i < move.size(); i++)
    {
      currTime = 0; //restart at 0 for each iteration
      
      currentMove = move.get(i);         //get the next possible move from the array list
      direction = currentMove.getDirection();  //find out direction moving people from
      
      if (direction == Move.MoveType.RIGHT) //we want to move people to the right, so grab the left people
      {
        currentSide = state.getLeft();
      }
      else
      {
        currentSide = state.getRight();
      }
      
      
      if (currentSide.size() > 0) //must be true otherwise there is nobody to move
      {
        currTime += currentSide.get(currentMove.getFirstPerson());
        
        int secondPersonPosition = currentMove.getSecondPerson(); //check to see if we have one or two people to move
        
        if (secondPersonPosition >= 0)  //will be a valid position to choose when true
        {
          currTime += currentSide.get(currentMove.getSecondPerson());
        }
        
        //checking if this is the most optimal time encountered so far
        if (currTime < bestTime)  //remember the best move so we can return it and act on it (create the new state)
        {
          bestMove = currentMove.clone();
          bestTime = currTime;
        }
      }
    }
    //System.out.println(bestMove.toString());
    return bestMove;
  }
}

class BreadthFirstSearch
{
  private Solution solution;
  
  public BreadthFirstSearch(ArrayList<Integer> people, int maxTime)
  {
    solution = new Solution();
    beginSearch(people, maxTime); 
    solution.printResults();
  }
  
  private void beginSearch(ArrayList<Integer> people, int maxTime)
  {
    int currentTime = 0;
    State initialState = new State(people);   //create the initial starting state
    ArrayList<Move> potentialMoves = new ArrayList<Move>();
    ArrayDeque<Node> queue = new ArrayDeque<Node>();
    Move bestMove;                            //the move we will use to generate the next state
    boolean complete = false;                 //flag telling while loop when solution is found
    State currentState = initialState;        //keeping track of the current state in the while loop
    State newState;
    Node currNode;
    
    System.out.println("Starting Breadth First Search...");
    solution.addState(initialState);
    
    complete = initialState.completeCheck();
    
    //create a new node with initial state and add to the queue
    currNode = new Node(initialState);
    currNode.visit();
    queue.add(currNode);
    
    
    //setting up while loop
    while (!queue.isEmpty() && currentTime <= maxTime && complete != true)
    {
      //Create possible moves to the right
      potentialMoves = currentState.generateMoves(Move.MoveType.RIGHT);
      
      queue = generateStates(currentState, potentialMoves); //Make the priority queue
      
      //fill the queue with all of the move nodes
      
      
      //bestMove = heuristicFunction(currentState, potentialMovesRight);
      bestMove = null;
      if (bestMove != null)
      {
        solution.addMove(bestMove);  //adding the best move to the move solution list
        
        newState = currentState.clone();
        newState.movePeople(bestMove);
        currentTime = newState.getTime();
        //do the move on the state, create a new state 
        
        currentState = newState;
        solution.addState(currentState);     //must clone the state otherwise the final state is only one
        complete = currentState.completeCheck();
        //currentState.printState();
      }
      
      //Moving people back onto the right if no solution yet!
      if ( complete != true)
      {
        potentialMoves = currentState.generateMoves(Move.MoveType.LEFT);
        //System.out.println("Possible moves LEFT: "+potentialMovesLeft.toString());
        
        //bestMove = heuristicFunction(currentState, potentialMovesLeft);
        
        if (bestMove != null)
        {
          solution.addMove(bestMove);  //adding the best move to the move solution list
          
          newState = currentState.clone();
          newState.movePeople(bestMove);
          currentTime = newState.getTime();
          //do the move on the state, create a new state 
          
          //update currentState for next while loop
          //add this new state to the state list
          //check if the new state is complete
          currentState = newState;
          solution.addState(currentState);     //must clone the state otherwise the final state is only one
          complete = currentState.completeCheck();
          //currentState.printState();
        }
      }
    }
    
    if (currentState.getTime() < maxTime)
    {
      solution.toggleSolved();  //mark the solution as solved
    }
  }
  
  private ArrayDeque<Node> generateStates(State state, ArrayList<Move> moveList)
  {
    ArrayDeque<Node> queue = new ArrayDeque<Node>();
    return queue;
  }
}