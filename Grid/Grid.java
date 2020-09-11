package Grid;
import Objects.*;
import Animals.*;
import Util.*;
import java.util.*;
import Objects.Object;
import Util.Vector;

public class Grid 
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
    
    public List<Animal> animals;
    public Object[][] grid;
    private int sizeX, sizeY;

    public void create(int x, int y)
    {
        sizeX = x;
        sizeY = y;
        animals = new ArrayList<>();
        grid = new Object[x][y];
    }
    
    public void place(Object obj)
    {
        set(obj.pos, obj);
    }
    public void place(Animal a)
    {
        for(var pos : a.body)
            set(pos, a);

        animals.add(a);
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

    public void resetTo(Object[][] newGrid, List<Animal> newAnimals)
    {
        grid = newGrid;
        for (int x = 0; x < sizeX; x++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                Object obj = newGrid[x][y];
                grid[x][y] = obj instanceof Animal? null : obj;
            }
        }

        animals.clear();
        for (Animal a : newAnimals)
        {
            Animal copy = Animal.copy(a);
            place(copy);
        }
    }
    public List<Animal> copyAnimals()
    {
        List<Animal> copy = new ArrayList<>();
        for (Animal a : animals)
        {
            copy.add(Animal.copy(a));
        }
        return copy;
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
