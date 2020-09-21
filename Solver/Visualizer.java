package Solver;

import java.util.*;

import Grid.Grid;

public class Visualizer 
{
    private List<List<String[]>> visual;

    public Visualizer()
    {
        visual = new ArrayList<>();
        visual.add(new ArrayList<>());
    }

    public void addCurToDepth(int d)
    {
        if (d >= visual.size())
            visual.add(new ArrayList<>());

        String[] cur = Grid.instance().toString().split("\\*");
        visual.get(d).add(cur);
    }

    public void print()
    {
        System.out.println(toString());
    }

    @Override
    public String toString() 
    {
        return build().toString();      
    }

    private StringBuilder build()
    {
        StringBuilder builder = new StringBuilder();
        int d = 0;

        for (var list : visual) 
        {
            builder.append("DEPTH: " + d);
            builder.append("\n"); 
            String depth = pack(list);
            builder.append(depth);
            builder.append("\n"); 
            d++;
        }

        return builder;
    }

    private String pack(List<String[]> list)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Grid.instance().sizeY; i++) 
        {
            for (String[] lines : list) 
            {
                builder.append(lines[i]);
                builder.append("      ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
