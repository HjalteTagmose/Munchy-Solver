package Solver;
import Util.Vector;
import Animals.Animal;

public class Step implements Comparable<Step>
{
    public Step prev;
    public Animal animal;
    public int val;
    public Vector dir;
    public int step;

    public Step(Step prev, Animal animal, Vector dir, int val)
    {
        this.prev = prev;
        this.animal = animal;
        this.dir = dir;
        this.val = val;
    }

    public void execute()
    {
        animal.tryMove(dir);
    }

    @Override
    public int compareTo(Step that) 
    {
        return this.val - that.val;
    }

    public void print()
    {
        System.out.println(animal.toString() + " moves: " + dir.toString());
    }
}
