package Objects;

import java.util.*;
import Animals.*;
import Util.Vector;
import Grid.*;

public class Food extends Object implements Edible, Resetable
{
    public static List<Food> meats  = new ArrayList<>();
    public static List<Food> plants = new ArrayList<>();

    public boolean eaten = false;
    private boolean isMeat = false;
    private boolean isTemp = false;

    public Food(Vector pos, boolean isMeat)
    {
        this(pos.x, pos.y, isMeat);
    }
    public Food(int x, int y, boolean isMeat)
    {
        super(x, y);
        this.isMeat = isMeat;
        
        if (isMeat)  meats.add(this);
        else        plants.add(this);
    }

    @Override
    public void reset()
    {
        if (isTemp)
        {
            if (isMeat) meats.remove(this);
            else       plants.remove(this);
            return;
        }

        Grid.instance().set(pos, this);
        eaten = false;
    }

    @Override
    public void markTemp()
    {
        isTemp = true;
    }

    public void eatenAt(Vector pos)
    {
        Grid.instance().clear(pos);
        eaten = true;
    }

    public boolean canEat(Animal a) 
    {
        return isMeat == (a instanceof Carnivore);            
    }

    @Override
    public String toString()
    {
        return isMeat?"M":"P";
    }
}
