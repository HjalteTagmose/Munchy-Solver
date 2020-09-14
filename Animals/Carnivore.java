package Animals;
import java.util.*;
import Util.Vector;
import Objects.Object;

public class Carnivore extends Animal
{
    public Carnivore(List<Vector> body)
    {
        super(body);
    }

    @Override
    public boolean canMove(Object obj) 
    {
        if (obj instanceof Animal) return true;
        return super.canMove(obj);
    }
}
