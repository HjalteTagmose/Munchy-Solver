package Solver;
import java.util.*;
import Util.Vector;
import Animals.*;
import Grid.*;
import Objects.Goal;
import Objects.Object;

public class Solver
{
    private List<Animal> initAnimals;
    private Object[][] initGrid;
    private PriorityQueue<Step> pq;

    private Step curStep = null;
    private int maxIterations;   
    private boolean solved = false;

    private Vector[] dirs = new Vector[]{
        new Vector(0, 1),
        new Vector(0, -1),
        new Vector(1, 0),
        new Vector(-1, 0),
    };

    public Solver()
    {
        this(10000);
    }
    public Solver(int maxIterations)
    {
        pq = new PriorityQueue<>();
        this.maxIterations = maxIterations;
    }

    public void solve()
    {
        // Presort animals by best position
        Collections.sort(
            Grid.instance().animals, 
            new Comparator<Animal>()
            {
                public int compare(Animal a, Animal b) 
                {
                    return evaluate(a) - evaluate(b); 
                }
            }
        );

        // Get initial state
        int iterations = 0;
        initGrid = Grid.instance().grid;
        initAnimals = Grid.instance().copyAnimals();
        reset();

        System.out.println("Init:");
        Grid.instance().print();

        while (!solved) 
        {   
            if (iterations > maxIterations)
                return;
            
            System.out.println("");
            System.out.println("Iteration: " + iterations);
            tryEverything(curStep);
            
            if (solved) 
                break;
            
            Step step = pq.poll();
            resetToStep(step);
            curStep = step;

            iterations++;
        }

        System.out.println("");
        int steps = countSteps(curStep);
        printSteps(curStep);
        System.out.println("");
        System.out.println("--- RESULT ---");
        System.out.println("SOLVED IN " + steps + " STEPS");
        System.out.println("SOLVE TOOK " + iterations + " ITERATIONS");
    }

    private void tryEverything(Step step)
    {
        // PRINT START STEP
        System.out.println("Init: ");     
        resetToStep(step);
        Grid.instance().print();

        for (int i = 0; i < initAnimals.size(); i++) 
        {
            for (Vector dir : dirs) 
            {
                Animal a = Grid.instance().animals.get(i);
                
                if (a.finished)
                    continue;

                System.out.println(""); 
                
                if (a.tryMove(dir))
                {
                    System.out.println(a.toString() + " moves: " + dir.toString());  
                    Grid.instance().print();

                    if (isSolved())
                    {
                        curStep = new Step(curStep, i, dir, 0);
                        solved = true;
                        return;
                    }

                    // Evaluate
                    int steps = countSteps(step) + 1;
                    int val = evaluate() + steps;

                    // Create step
                    Step newStep = new Step(step, i, dir, val);
                    pq.add(newStep);
                    curStep = newStep;
                    
                    // Reset
                    resetToStep(step);
                }
                else
                {
                    System.out.println(a.toString() + " couldn't move: " + dir.toString());
                }
            }
        }
    }

    private int evaluate()
    {
        int eval = 0;

        for (Animal a : Grid.instance().animals) 
        {
            eval += evaluate(a);
        }

        return eval;
    }
    private int evaluate(Animal a)
    {
        if(a.finished) return -1;

        Goal g = Grid.instance().getClosestGoal(a);
        int foodNeeded = g.length - a.body.size();
        int dist = Vector.dist(a, g);

        System.out.println(a + " is " + dist + " away from goal");

        return dist;
    }
    
    private boolean isSolved()
    {
        for(Animal animal : Grid.instance().animals)
        {
            if (!animal.finished)
            {
                return false;
            }
        }
        return true;
    }

    private int countSteps(Step step)
    {
        return countSteps(step, 0);
    }
    private int countSteps(Step step, int count)
    {
        if (step == null)
            return count;
        else 
            count = countSteps(step.prev, ++count);

        return count;
    }

    private void printSteps(Step step)
    {
        System.out.println("--- STEPS ---");
        printSteps(step, 1);
    }
    private void printSteps(Step step, int count)
    {
        if (step != null)
            printSteps(step.prev, ++count);

        if (step != null) 
            step.print();
        else
            System.out.println("Start");

        resetToStep(step);
        Grid.instance().print();
        System.out.println("");
    }

    private void doStep(Step step)
    {
        if (step == null)
            return;

        doStep(step.prev);
        step.execute();
    }

    private void resetToStep(Step step)
    {
        if (curStep != null && curStep == step)
            return;

        reset();
        curStep = step;
        doStep(step);
    }
    private void reset()
    {
        Grid.instance().resetTo(initGrid, initAnimals);
    }
}