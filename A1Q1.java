import java.util.*;
import java.io.*;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class aone
{
  
  public static void main(String[] args) 
  {
    int maxSteps = 0;
    int numPeople = 0;
    ArrayList<Integer> people = new ArrayList<Integer>();
    
    ArrayList<Integer> testPeople = new ArrayList<Integer>();
    testPeople.add(5);
    testPeople.add(12);
    testPeople.add(3);
    testPeople.add(8);
    
    int testSteps = 28;
    beginSearches(testPeople, testSteps);
    
    
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
  }
  
  public static void beginSearches(ArrayList<Integer> people, int maxSteps)
  {
    boolean complete;
    int searches = 0;
    int failed = 0;
    
    complete = HeuristicSearch(people, maxSteps);
    if (complete == false)
    {
      failed++; 
    }
  }
  
  
  public static boolean HeuristicSearch(ArrayList<Integer> people, int maxSteps)
  {
    System.out.println("Starting Heuristic Search...");
    Stack solution = new Stack();
    
    return true;
  }  
}

//*******!*#&(*!&#$)#($)!(*#!)#(*)!#(*
//TURN ME INTO A STACK OR QUEUE

class State extends Data
{
  public ArrayList<Integer> left;
  public ArrayList<Integer> right;
  public boolean light; //always move the light when moving people. false = on left, true = light on right side.
  public boolean complete;
  
  public State()
  {
    left = new ArrayList<Integer>();
    right = new ArrayList<Integer>();
    light = false;
    complete = false;
  }
  
  public State(ArrayList<Integer> startingPeople)
  {
    left = startingPeople;
    right = new ArrayList<Integer>();
    light = false;
    complete = false;
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
  
  
  
  public boolean completeCheck()
  {
    if (left.size() == 0 && right.size() >= 1 && light == true)
    {
      complete = true;
    }
    return complete;
  }
}