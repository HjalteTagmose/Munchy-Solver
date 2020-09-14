import Objects.*;
import java.util.ArrayList;
import Animals.*;
import Grid.Grid;
import Solver.*;
import Util.Vector;

public class Main
{
    public static void main(String[] args) 
    {
        new Main();
    }

    public Main()
    {
        createLevel();
        new Solver(1000).solve();
    }

    private void createLevel()
    {
        Grid.instance().create(5, 5);

        new Herbivore( new ArrayList<Vector>() {{
            add( new Vector(3,0) );
            add( new Vector(3,1) );
            //add( new Vector(4,1) );
        }});        
        new Herbivore( new ArrayList<Vector>() {{
            add( new Vector(0,4) );
            add( new Vector(1,4) );
            //add( new Vector(1,3) );
        }});

        new Food(3,3,false);
        new Food(1,3,false);
        new Wall(2,2);
        new Goal(0,2,"h3");
    }
}