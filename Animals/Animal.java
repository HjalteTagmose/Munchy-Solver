package Animals;

import Util.*;
import Grid.*;
import Objects.*;
import Animals.*;
import java.util.*;

import Util.Vector;
import Objects.Object;

public abstract class Animal extends Object implements Edible
{
    public static Animal copy(Animal a)
    {
        List<Vector> newBody = new ArrayList(a.body.size());
        for (Vector part : a.body) 
            newBody.add(part);

        a = a instanceof Carnivore?
            new Carnivore(newBody, false) : 
            new Herbivore(newBody, false) ;

        return a;
    }

    public boolean finished = false;
    public List<Vector> body;
    public Vector head() { return body.get(0); }
    public Vector tail() { return body.get(body.size()-1); }

    public Animal(List<Vector> body)
    {
        this(body, true);
    }
    public Animal(List<Vector> body, boolean place)
    {
        super(body.get(0));
        this.body = body;
        
        if (place)
            Grid.instance().place(this);
    }

    public boolean canMove(Vector pos) 
    {
        if (!Grid.instance().isWithinGrid(pos))
            return false;
        
        Object col = Grid.instance().get(pos);
        return canMove(col);
    }
    protected boolean canMove(Object col) 
    {
        if (col == null) return true;
        if (col instanceof Food) return ((Food)col).canEat(this);
        if (col instanceof Goal) return ((Goal)col).canEnter(this);
        return false;
    }
    public boolean tryMove(Vector dir)
    {
        Vector newPos = Vector.add(head(), dir);
        if (canMove(newPos))
        {
            Object col = Grid.instance().get(newPos);
            if( col instanceof Edible ) 
                grow(newPos);
            else if( col instanceof Goal ) 
                finish();
            else
                move(newPos);

            return true;
        }
        return false;
    }

    public void eatenAt(Vector pos)
    {
        int index = getIndexFromPos(pos);
        int count = body.size()-1;

        for (int i = count; i > index; i--) 
        {
            new Food(body.get(i), true);
            body.remove(i);
        }
        body.remove(index);
    }
    public int getIndexFromPos(Vector pos)
    {
        for (int i = 0; i < body.size(); i++) 
        {
            if (body.get(i).equals(pos))
                return i;            
        }
        return -1;
    }

    public void move(Vector pos)
    {        
        Grid.instance().clear(tail());
        Grid.instance().set(pos, this);

        for (int i = body.size()-1; i > 0; i--)
            body.set(i, body.get(i - 1));

        body.set(0, pos);
        this.pos = pos;    
    }
    public void grow(Vector pos)
    {
        Grid.instance().set(pos, this);
        body.add(0, pos);
        this.pos = pos;    
    }
    public void finish()
    {
        for (Vector part : body) 
        {
            Grid.instance().clear(part);
        }
        finished = true;
    }
    
    @Override
    public String toString()
    {
        return (this instanceof Carnivore ? "C" : "H") + 
            Grid.instance().animals.indexOf(this);
    }
}