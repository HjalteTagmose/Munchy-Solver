package Objects;
import Animals.*;

public class Goal extends Object
{
    String type = "";
    int length = 0;

    public Goal(int x, int y, String req)
    {
        super(x, y);
        type = (req.charAt(0)+"").toLowerCase();
        length = Integer.parseInt(req.substring(1));
    }

    public boolean canEnter(Animal a)
    {
        boolean typeOK =
            type.equals("c") && a instanceof Carnivore ||
            type.equals("h") && a instanceof Herbivore ||
            !type.equals("c") && !type.equals("h");
        boolean lengthOK =
            a.body.size() == length ||
            length <= 0;
        
        return typeOK && lengthOK;
    }

    @Override
    public String toString()
    {
        return type + length;
    }
}
