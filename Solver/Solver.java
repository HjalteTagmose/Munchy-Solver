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
    private Visualizer visualizer = new Visualizer();

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
        visualizer.start();
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

        while (!solved && iterations < maxIterations) 
        {
            // System.out.println("");
            System.out.println("Iteration: " + iterations);
            tryEverything(curStep);
            iterations++;
            
            if (solved) 
                break;
            
            Step step = pq.poll();
            resetToStep(step);
            curStep = step;
        }

        if (solved)
        {
            System.out.println("");
            int steps = countSteps(curStep);
            printSteps(curStep);
            System.out.println("");
            System.out.println("--- RESULT ---");
            System.out.println("SOLVED IN " + steps + " STEPS");
            System.out.println("SOLVE TOOK " + iterations + " ITERATIONS");
    
            visualizer.showSolve(getSteps(curStep));
        }

        visualize();
    }

    public void visualize()
    {
        visualizer.visualize();
    }

    private void tryEverything(Step step)
    {
        int depth = countSteps(step) + 1;

        // PRINT START STEP
        resetToStep(step);
        // System.out.println("Init: ");     
        // Grid.instance().print();
        
        for (Animal a : Animal.animals) 
        {
            for (Vector dir : dirs) 
            {                
                if (a.finished)
                    continue;

                // System.out.println(""); 
                // System.out.println(a + " tries: " + dir); 
                
                if (a.tryMove(dir))
                {
                    // System.out.println(a + " moves: " + dir);
                    //Grid.instance().print();

                    if (isSolved())
                    {
                        curStep = new Step(curStep, a, dir, 0, depth);
                        visualizer.addStep(curStep);
                        solved = true;
                        return;
                    }

                    // Evaluate
                    int steps = countSteps(step) + 1;
                    int val = evaluate() + steps;

                    // Create step
                    Step newStep = new Step(step, a, dir, val, depth);
                    visualizer.addStep(newStep);
                    pq.add(newStep);
                    curStep = newStep;
                    
                    //TEST
                    //visualize();

                    // Reset
                    resetToStep(step);
                }
                else
                {
                    // System.out.println(a.toString() + " couldn't move: " + dir.toString());
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
        //System.out.println(a + ": dist is " + distToFinish(a));
        return distToFinish(a);
    }
    
    private int distToFinish(Animal a)
    {
        int shortest = 100000; //avoid overflow
        boolean noGoal = true;

        for (Goal g : Goal.goals) 
        {        
            if (!g.typeOK(a)) 
                continue;

            noGoal = false;

            // Prioritize animals close to required length
            int dist = getPathLength(a, g);
            if (dist < shortest)
                shortest = dist;
        }
        
        if (noGoal)
        {
            var foods = getFoods(a instanceof Carnivore);
            return getPathLength(new ArrayList<>() {{ add( a.pos ); addAll( foods ); }}) - 
                    foods.size();
        }

        return shortest;
    }

    private int getPathLength(Animal a, Goal g)
    {
        // Find how much food is needed
        int foodNeeded = g.length - a.body.size();
        boolean isCarni = a instanceof Carnivore; 
        
        // Return direct if no food needed
        if (foodNeeded <= 0)
            return Vector.dist(a, g);
        
        // Keep list of points in path
        List<Vector> points = new ArrayList<>();
        points.add(a.pos);
        
        // Get food positions
        List<Vector> foodPos = getFoods(isCarni);
        int foodOnBoard = foodPos.size() - (isCarni? a.body.size() : 0);
        
        // Return max if not enough food available
        if (foodNeeded > foodOnBoard)
        {
            int alive = 0;
            
            for (Animal animal : Animal.animals) 
                alive += animal.finished? 0 : 1;
            
            int val = 10000 + (alive > 1 ? 0 : 10000);
            System.out.println(alive);
            System.out.println(val);
            return val;
        }

        // Sort by distance to Animal and Goal
        Collections.sort(foodPos, new Comparator<Vector>(){
            public int compare(Vector posA, Vector posB) 
            {
                return 
                ( Vector.dist(posB, a.pos) + Vector.dist(posB, g.pos) ) -
                ( Vector.dist(posA, a.pos) + Vector.dist(posA, g.pos) ) ;
            }
        });

        // Add first {foodNeeded} elements & finally goal
        points.addAll(
            foodPos.stream()
                   .limit(foodNeeded)
                   .collect(Collectors.toList())
        );
        points.add(g.pos);

        return getPathLength(points);
    }

    private List<Vector> getFoods(boolean isCarni)
    {
        List<Vector> foods = new ArrayList<>();

        // Add appropriate food
        for (Food f : isCarni? Food.meats : Food.plants)
        {
            if (!f.eaten) 
                foods.add(f.pos);       
        }

        // Add animals as food if carnivore
        if (isCarni)
        {
            for (Animal ani : Animal.animals)
            {
                if (!ani.finished)
                    foods.addAll(ani.body);
            }
        }

        return foods;
    }

    private int getPathLength(List<Vector> points)
    {
        // Find length of path
        int dist = 0;
        for (int i = 0; i < points.size()-1; i++) 
            dist += Vector.dist(points.get(i), points.get(i+1));

        return dist;
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
    private List<Step> getSteps(Step step)
    {
        List<Step> steps = new ArrayList<>();
        getSteps(step, steps);
        return steps;
    }

    private void getSteps(Step step, List<Step> steps)
    {
        steps.add(step);

        if (step == null)
            return;

        getSteps(step.prev, steps);
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
        Grid.instance().reset();
    }
    //#endregion
}