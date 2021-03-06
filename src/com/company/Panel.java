package com.company;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class Panel extends JPanel implements MouseMotionListener, MouseListener
{
    /**
     * used to store the x and y values of the points used to draw.
     */
    private int x1 = 0;
    private int y1 = 0;

    private BufferedImage buffer;

    /**
     * used to send data to the server.
     * this class uses the task and drawing.
     */
    private CommandToServer data = new CommandToServer("", 999, "", null);
    private ObjectOutputStream os;

    /**
     * the image that is being drawn on the screen
     */
    private Painting draw = new Painting();

    /**
     * used to store the point where the mouse is drawing
     */
    private Point point1 = new Point(0, 0);

    /**
     * stores the current color selected
     */
    private Color color = Color.black;

    /**
     * stores whether or not the player is the one currently drawing
     */
    private boolean isDrawing = false;

    private String name;


    public Panel(ObjectOutputStream os) throws Exception
    {
        super();
        setSize(1120, 900);
        this.os = os;

        addMouseMotionListener(this);
        addMouseListener(this);

        data.setTask(2);
        data.setDraw(null);
        os.writeObject(data);
        os.reset();

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        isDrawing = true;

        repaint();
    }

    public void updateCanvas(Painting image, Color c)
    {
        draw = image;
        if(c != null)
            color = c;
        repaint();
    }

    public void updateCurrentDrawer(boolean isDrawing)
    {
        this.isDrawing = isDrawing;
    }

    public void recieveName(String name)
    {
        this.name = name;
        data.setName(name);
    }

    public void clear()
    {
        draw.clear();
        repaint();
    }

    public void paint(Graphics bg)
    {
        Graphics g2 = buffer.getGraphics();
        Graphics2D g = (Graphics2D) g2.create();
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g.setColor(Color.WHITE);
        g.fillRect(130, 0, getWidth(), getHeight());

        g.setColor(Color.black);
        for (int a = 0;a<draw.getImage().size();a++)
        {
            ArrayList<Point> line = draw.getImage().get(a);
            g.setColor(draw.getColors().get(a));

            for (int x = 1; x < line.size(); x++)
            {
                if(line.get(x - 1).x>130 && line.get(x).x>130)
                    g.drawLine(line.get(x - 1).x, line.get(x - 1).y, line.get(x).x, line.get(x).y);

            }
        }

        bg.drawImage(buffer, 0, 0, null);
    }

    public void addNotify()
    {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if(!isDrawing)
            return;
        x1 = e.getX();
        y1 = e.getY();
        point1.setLocation(x1, y1);

        draw.addPoint(point1, color);
        data.setDraw(draw);
        data.setTask(-2);
        try
        {
            os.writeObject(data);
            os.reset();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(!isDrawing)
            return;
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            x1 = e.getX();
            y1 = e.getY();

            point1.setLocation(x1, y1);

            draw.addPoint(point1, color);
            data.setDraw(draw);
            data.setTask(-2);

            try
            {
                os.writeObject(data);
                os.reset();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(!isDrawing)
            return;
        // clear
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            draw.clear();
            data.setDraw(draw);
            data.setTask(-4);

            try
            {
                os.writeObject(data);
                os.reset();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            repaint();
        }
        else if (e.getButton() == MouseEvent.BUTTON1)
        {
            draw.finishLine();
            data.setDraw(draw);
            data.setTask(-3);

            try
            {
                os.writeObject(data);
                os.reset();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }
}
