package Solver;
import Util.Vector;
import Grid.Grid;

public class Step implements Comparable<Step>
{
    public Step prev;
    public int index;
    public int val;
    public Vector dir;
    public int step;

    public Step(Step prev, int index, Vector dir, int val)
    {
        this.prev = prev;
        this.index = index;
        this.dir = dir;
        this.val = val;
    }

    public void execute()
    {
        Grid.instance().animals.get(index).tryMove(dir);
    }

    @Override
    public int compareTo(Step that) 
    {
        return this.val - that.val;
    }

    public void print()
    {
        System.out.println(
            Grid.instance().animals.get(index).toString()
             + " moves: " + dir.toString()
        );
    }
}
