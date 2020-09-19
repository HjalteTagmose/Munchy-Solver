package Util;
import java.lang.Math;
import Objects.Object;

public class Vector
{
    public int x, y;

    public Vector(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override 
    public boolean equals(java.lang.Object obj) 
    {
        Vector that = (Vector)obj;
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public String toString()
    {
        return "("+x+", "+y+")";
    }

    public static Vector add(Vector a, Vector b)
    {
        Vector c = new Vector(a.x+b.x,a.y+b.y);    
        return c;        
    }
    public static Vector inv(Vector vec)
    { 
        return new Vector(-vec.x, -vec.y);
    }

    public static int dist(Object a, Object b)
    {
        return dist(a.pos, b.pos);
    }
    public static int dist(Vector a, Vector b)
    {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}