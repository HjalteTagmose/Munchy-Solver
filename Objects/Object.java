package Objects;
import Util.Vector;
import Grid.*;

public  abstract class Object
{
    public Vector pos;
    
    public Object(int x, int y)
    {
        this(new Vector(x, y));
    }
    public Object(Vector pos) 
    {
        this.pos = pos;
        Grid.instance().place(this);
    }

    @Override
    public abstract String toString();
}