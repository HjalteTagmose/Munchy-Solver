package Grid;
import Objects.*;
import Animals.*;
import java.util.*;
import Objects.Object;
import Util.Vector;

public class Grid implements Resetable
{
    //#region Singleton 
    private static Grid instance;
    public static Grid instance()
    {
        if (instance == null)
            instance = new Grid();

        return instance;
    }
    private Grid()
    {
        instance = this;
    }
    //#endregion
    
    public Object[][] grid;

    private int sizeX, sizeY;
    private List<Resetable> resetables;
    private List<Goal> goals;

    public void create(int x, int y)
    {
        sizeX = x;
        sizeY = y;
        goals = new ArrayList<>();
        resetables = new ArrayList<>();
        grid = new Object[x][y];
    }
    
    public void place(Object obj)
    {
        set(obj.pos, obj);

        if (obj instanceof Resetable)
            resetables.add((Resetable)obj);
        else if (obj instanceof Goal)
            goals.add((Goal)obj);
    }
    public void place(Animal a)
    {
        for(var pos : a.body)
            set(pos, a);
    }

    public Object get(Vector pos)
    {
        return grid[pos.x][pos.y];
    }
    public void set(Vector pos, Object obj)
    {
        grid[pos.x][pos.y] = obj;
    }
    public void clear(Vector pos) 
    {
        set(pos, null);        
    }

    public boolean isWithinGrid(Vector pos) 
    {
        return pos.x >= 0 && pos.y >= 0 &&
               pos.x < sizeX && pos.y < sizeY;        
    }

    @Override
    public void reset()
    {
        for (int x = 0; x < sizeX; x++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                Object obj = grid[x][y];
                grid[x][y] = obj instanceof Resetable? null : obj;
            }
        }

        for (Resetable r : resetables)
            r.reset();
    }

    public Goal getClosestGoal(Animal a)
    {
        int minDist = sizeX * sizeY;
        Goal closest = null;

        for (Goal g : goals) 
        {
            int dist = Vector.dist(g, a);

            if (g.typeOK(a) && dist < minDist)
            {
                minDist = dist;
                closest = g;
            }            
        }

        return closest;
    }

    public void print()
    {
        for (int y = 0; y < sizeY; y++) 
        {
            for (int x = 0; x < sizeX; x++) 
            {
                Object obj = get(new Vector(x,y));
                System.out.print(
                    (obj == null?".":obj.toString()) + 
                    ((obj instanceof Goal || 
                      obj instanceof Animal)?"":" ")
                );
            }
            System.out.println("");
        }
    }
}
