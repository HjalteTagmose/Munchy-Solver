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

    private boolean setupFinished = false;
    private List<Object> temp;

    public void create(int x, int y)
    {
        sizeX = x;
        sizeY = y;
        temp = new ArrayList<>();
        resetables = new ArrayList<>();
        grid = new Object[x][y];
    }

    public void finish()
    {
        setupFinished = true;
    }
    
    public void place(Object obj)
    {
        set(obj.pos, obj);

        if (setupFinished)
            temp.add(obj);
        else if (obj instanceof Resetable)
            resetables.add((Resetable)obj);
    }
    public void place(Animal a)
    {
        for(var pos : a.body)
            set(pos, a);
    }
    public void clear(Object obj) 
    {
        set(obj.pos, null);        
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
        for (Object obj : temp) 
            clear(obj);

        temp.clear();

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
    @Override
    public void markTemp()
    {
        //grid cant be temp
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
