import java.util.ArrayList;
import Util.Vector;
import Objects.*;
import Animals.*;
import Solver.*;
import Grid.*;

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
            add( new Vector(4,0) );
            add( new Vector(4,1) );
            add( new Vector(3,1) );
            //add( new Vector(4,1) );
        }});        
        // new Herbivore( new ArrayList<Vector>() {{
        //     add( new Vector(0,4) );
        //     add( new Vector(1,4) );
        //     //add( new Vector(1,3) );
        // }});

        // new Food(3,3,false);
        // new Food(1,3,false);
        new Goal(2,1,"h4");
    }
}