package Util;

public class Vector
{
    public int x, y;

    public Vector(int x, int y)
    {
        this.x = x;
        this.y = y;
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
}