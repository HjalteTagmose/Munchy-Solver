package Solver;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import Grid.Grid;

public class Visualizer extends JFrame 
{
    public Visualizer()
    {
        setTitle("Visualizer");
        setLocation(new Point(300,200));
        setLayout(new GridLayout());    
        setSize(1000,1000);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        drawPane = new DrawPane();
        drawPane.setPreferredSize(new Dimension(1000, 1000));
        drawPane.setFont( new Font(Font.MONOSPACED, Font.PLAIN, 12) );
        drawPane.setVisible(true);
        
        pane = new JScrollPane(drawPane);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane, new GridLayout());
        
        pack();
        setSize(1000,1000);
    }

    private static final int spacing = 100;
    private static int depth = 0;
    private static StepVisual root;
    private static DrawPane drawPane;
    private static JScrollPane pane;

    private Map<Step, StepVisual> stepToVisual = new HashMap<>();
    
    public void start()
    {
        root = new StepVisual();
        stepToVisual.put(null, root);
        depth = 1;
    }

    public void addStep(Step step)
    {
        if (step.depth > depth)
            depth = step.depth;

        StepVisual visual = new StepVisual(step);
        StepVisual parent = stepToVisual.get(step.prev);

        parent.addChild(visual);
        stepToVisual.put(step, visual);
    }
    
    public void showSolve(List<Step> steps)
    {
        for (Step step : steps) 
        {
            stepToVisual.get(step).solveStep = true;
        }
    }

    public void visualize()
    {
        setVisible(true);
        repaint();
        pane.revalidate();
    }

    private static class StepVisual
    {
        private int x, y;
        private int treeWidth;
        private List<StepVisual> children;
        private String[] gridLines;
        private boolean solveStep = false;
        private String info = "0"; 

        public StepVisual()
        {
            this(0,0);
        }

        public StepVisual(Step step)
        {
            this();
            info = "[" + step.step + "] " + step.animal +" "+ step.val;
        }

        private StepVisual(int x, int y)
        {
            this.x = x;
            this.y = y;
            
            children = new ArrayList<>();
            gridLines = Grid.instance().toString().split("\\*");
        }

        public void addChild(StepVisual step)
        {
            children.add(step);
        }

        public void draw(Graphics g)
        {
            for (StepVisual child : children)
            {
                g.setColor(child.solveStep ? Color.lightGray : Color.black);
                g.drawLine(x, y, child.x(), child.y());
                g.setColor(Color.black);
                child.draw(g);
            }
            drawGrid(g, x, y);
        }

        public void align()
        {  
            int c = children.size();
            int curX = -(treeWidth / 2) + x;

            for (int i = 0; i < c; i++)
            {
                StepVisual child = children.get(i);

                child.setX( curX + child.treeWidth/2 );
                child.setY( y + spacing );
                child.align();

                curX += child.treeWidth;
            }
        }

        public int getTreeWidth()
        {
            treeWidth = 0;
            for (StepVisual step : children) 
                treeWidth += step.getTreeWidth();

            if (children.size() == 0)
                treeWidth = spacing;
            
            return treeWidth;
        }

        private void drawGrid(Graphics g, int x, int y) 
        {
            int h = g.getFontMetrics().getHeight();
            int w = (int)g.getFontMetrics().getStringBounds(gridLines[0], g).getWidth();
            
            g.setColor(Color.black);
            g.drawString(info, x, y-h);
            
            g.setColor(solveStep ? Color.lightGray : Color.white);
            g.fillRect(x, y, w, h * gridLines.length);
            
            g.setColor(Color.black);
            for (String line : gridLines)
                g.drawString(line, x, y += h);
        }

        public int x() { return x; }
        public int y() { return y; }
        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }
    }
    
    private static class DrawPane extends JPanel implements Scrollable
    {
        public DrawPane()
        {
            setAutoscrolls(true); 
        }

        public void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            int w = root.getTreeWidth();
            root.setX(w / 2);
            root.setY(10);
            root.align();
            root.draw(g);

            setPreferredSize(
                new Dimension(
                    w + spacing, 
                    (depth+1) * spacing + 20
                ));
        }

        @Override public boolean getScrollableTracksViewportWidth() { return false; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 0; }
        @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 0; }
    }
}