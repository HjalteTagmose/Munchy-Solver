package Objects;
import Animals.*;
import Util.Vector;
import Grid.*;

public class Food extends Object implements Edible
{
    private boolean isMeat = false;

    public Food(Vector pos, boolean isMeat)
    {
        this(pos.x, pos.y, isMeat);
    }
    public Food(int x, int y, boolean isMeat)
    {
        super(x, y);
        this.isMeat = isMeat;
    }

    public void eatenAt(Vector pos)
    {
        Grid.instance().clear(pos);
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
