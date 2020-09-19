package Solver;

import java.util.*;
import java.util.stream.Collectors;

import Util.Vector;
import Animals.*;
import Grid.*;
import Objects.*;

public class Solver
{
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
            Animal.animals, 
            new Comparator<Animal>()
            {
                public int compare(Animal a, Animal b) 
                {
                    return evaluate(a) - evaluate(b); 
                }
            }
        );

        int iterations = 0;

        System.out.println("Init:");
        Grid.instance().print();

        while (!solved) 
        {
            if (iterations > maxIterations)
                return;
            
            System.out.println("");
            System.out.println("Iteration: " + iterations);
            tryEverything(curStep);
            iterations++;
            
            if (solved) 
                break;
            
            Step step = pq.poll();
            resetToStep(step);
            curStep = step;
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

        for (Animal a : Animal.animals) 
        {
            for (Vector dir : dirs) 
            {                
                if (a.finished)
                    continue;

                System.out.println(""); 
                
                if (a.tryMove(dir))
                {
                    System.out.println(a.toString() + " moves: " + dir.toString());
                    Grid.instance().print();

                    if (isSolved())
                    {
                        curStep = new Step(curStep, a, dir, 0);
                        solved = true;
                        return;
                    }

                    // Evaluate
                    int steps = countSteps(step) + 1;
                    int val = evaluate() + steps;

                    // Create step
                    Step newStep = new Step(step, a, dir, val);
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

    //#region Evaluation
    private int evaluate()
    {
        int eval = 0;

        for (Animal a : Animal.animals) 
        {
            eval += evaluate(a);
        }

        return eval;
    }
    private int evaluate(Animal a)
    {
        if(a.finished) return -1;
        System.out.println(a + ": dist is " + distToFinish(a));
        return distToFinish(a);
    }
    
    private int distToFinish(Animal a)
    {
        int shortest = Integer.MAX_VALUE;

        for (Goal g : Goal.goals) 
        {        
            List<Vector> points = new ArrayList<>();
            points.add(a.pos);
            points.addAll( getClosestFoods(a, g) );
            points.add(g.pos);

            int dist = 0;
            for (int i = 0; i < points.size()-1; i++) 
                dist += Vector.dist(points.get(i), points.get(i+1));

            if (dist < shortest)
                shortest = dist;
        }

        return shortest;
    }

    private List<Vector> getClosestFoods(Animal a, Goal g)
    {
        int foodNeeded = g.length - a.body.size();
        boolean isCarni = a instanceof Carnivore; 
        
        List<Vector> pos = new ArrayList<>();

        // Return empty if no food needed
        if (foodNeeded <= 0) 
            return pos;
        
        // Add appropriate food
        for (Food f : isCarni? Food.meats : Food.plants)
        {
            if (!f.eaten) 
                pos.add(f.pos);               
        }

        // Add animals as food if carnivore
        if (isCarni)
        {
            for (Animal ani : Animal.animals)
            {
                pos.addAll(ani.body);
            }
        }

        // Sort by distance to Animal and Goal
        Collections.sort(pos, new Comparator<Vector>(){
            public int compare(Vector posA, Vector posB) 
            {
                return 
                ( Vector.dist(posB, a.pos) + Vector.dist(posB, g.pos) ) -
                ( Vector.dist(posA, a.pos) + Vector.dist(posA, g.pos) ) ;
            }
        });

        // Return first {foodNeeded} elements
        return pos.stream().limit(foodNeeded).collect(Collectors.toList());
    }

    private boolean isSolved()
    {
        for(Animal animal : Animal.animals)
        {
            if (!animal.finished)
            {
                return false;
            }
        }
        return true;
    }
    //#endregion

    //#region Step
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
        Grid.instance().reset();
    }
    //#endregion
}