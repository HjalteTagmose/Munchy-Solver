package Objects;
import Animals.*;

public class Goal extends Object
{
    public String type = "";
    public int length = 0;

    public Goal(int x, int y, String req)
    {
        super(x, y);
        type = (req.charAt(0)+"").toLowerCase();
        length = Integer.parseInt(req.substring(1));
    }

    public boolean canEnter(Animal a)
    {
        return typeOK(a) && lengthOK(a);
    }

    public boolean typeOK(Animal a)
    {
        return
            type.equals("c") && a instanceof Carnivore ||
            type.equals("h") && a instanceof Herbivore ||
            !type.equals("c") && !type.equals("h");
    }

    public boolean lengthOK(Animal a)
    {
        return
            a.body.size() == length ||
            length <= 0;
    }

    @Override
    public String toString()
    {
        return type + length;
    }
}
