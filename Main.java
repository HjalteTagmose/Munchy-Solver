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
        Solver solver = new Solver(1000);

        try {  solver.solve();  } 
        catch (Exception e) 
        {
            System.out.println( "STEP: " + (Step.STEPS-1) );
            e.printStackTrace();
            solver.visualize();  
        }
    }

    private void createLevel()
    {        
        // STUCK TEST LEVEL
        // Grid.instance().create(4, 3);
        // new Herbivore( new ArrayList<Vector>() {{
        //     add( new Vector(0,0) );
        //     add( new Vector(1,0) );
        // }});
        // new Carnivore( new ArrayList<Vector>() {{
        //     add( new Vector(2,0) );
        // }});
        // new Wall(1, 1);
        // new Wall(0, 1);

        // LONG CARNIVORE LEVEL
        Grid.instance().create(4, 4);
        new Carnivore( new ArrayList<Vector>() {{
            add( new Vector(2,0) );
            add( new Vector(1,0) );
            add( new Vector(1,1) );
        }});
        new Carnivore( new ArrayList<Vector>() {{
            add( new Vector(2,2) );
            add( new Vector(2,3) );
            add( new Vector(3,3) );
        }});
        new Herbivore( new ArrayList<Vector>() {{
            add( new Vector(1,2) );
            add( new Vector(1,3) );
        }});
        new Herbivore( new ArrayList<Vector>() {{
            add( new Vector(2,1) );
            add( new Vector(3,1) );
        }});
        new Food(0,0,false);
        new Food(3,0,false);
        new Food(3,2,false);
        new Food(0,1,true);
        new Goal(0,3,"c14");

        // CARNIVORE TEST LEVEL
        // Grid.instance().create(3, 4);

        // new Carnivore( new ArrayList<Vector>() {{
        //     add( new Vector(1,2) );
        //     add( new Vector(1,1) );
        // }});        
        // new Herbivore( new ArrayList<Vector>() {{
        //     add( new Vector(1,3) );
        //     add( new Vector(2,3) );
        // }});

        // new Food(0,1,false);
        // new Food(1,0,false);
        // new Goal(2,0,"h3");
        // new Goal(0,3,"c3");

        // SMALL CARNIVORE TEST LEVEL
        // Grid.instance().create(3, 4);

        // new Carnivore( new ArrayList<Vector>() {{
        //     add( new Vector(1,2) );
        //     add( new Vector(1,1) );
        // }});        
        // new Herbivore( new ArrayList<Vector>() {{
        //     add( new Vector(1,3) );
        // }});

        // new Food(0,1,false);
        // new Food(1,0,false);
        // new Goal(0,3,"c5");

        // HERBIVORE TEST LEVEL
        // Grid.instance().create(5, 5);

        // new Herbivore( new ArrayList<Vector>() {{
        //     add( new Vector(3,0) );
        //     add( new Vector(3,1) );
        //     add( new Vector(4,1) );
        // }});        
        // new Herbivore( new ArrayList<Vector>() {{
        //     add( new Vector(0,4) );
        //     add( new Vector(1,4) );
        //     add( new Vector(1,3) );
        // }});

        // new Food(3,3,false);
        // new Food(1,3,false);
        // new Wall(2,2);
        // new Goal(0,2,"h3");

        Grid.instance().finish();
    }
}