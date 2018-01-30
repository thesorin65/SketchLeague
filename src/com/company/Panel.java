package com.company;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.awt.event.KeyListener;

public class Panel extends JPanel implements MouseMotionListener, MouseListener
{
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;


    private ButtonGroup colors = new ButtonGroup();
    private JRadioButton black          = new JRadioButton("black      ");
    private JRadioButton gray           = new JRadioButton("gray       ");
    private JRadioButton lightGray      = new JRadioButton("lightGray  ");
    private JRadioButton white          = new JRadioButton("white      ");
    private JRadioButton red            = new JRadioButton("red        ");
    private JRadioButton darkRed        = new JRadioButton("darkRed    ");
    private JRadioButton orange         = new JRadioButton("orange     ");
    private JRadioButton lightOrange    = new JRadioButton("lightOrange");
    private JRadioButton yellow         = new JRadioButton("yellow     ");
    private JRadioButton tan            = new JRadioButton("tan        ");
    private JRadioButton green          = new JRadioButton("green      ");
    private JRadioButton brown          = new JRadioButton("brown      ");
    private JRadioButton lightGreen     = new JRadioButton("lightGreen ");
    private JRadioButton lightBlue      = new JRadioButton("lightBlue  ");
    private JRadioButton blue           = new JRadioButton("blue       ");
    private JRadioButton darkBlue       = new JRadioButton("darkBlue   ");
    private JRadioButton purple         = new JRadioButton("purple     ");
    private JRadioButton lightPurple    = new JRadioButton("lightPurple");
    private JRadioButton blueGray       = new JRadioButton("blueGray   ");
    private JRadioButton pink           = new JRadioButton("pink       ");


    private BufferedImage buffer;

    private CommandToServer data = new CommandToServer("", 999, "", null);
    private String userName = "";
    private ArrayList<String> users = new ArrayList<String>();
    private ObjectOutputStream os;
    private Painting draw = new Painting();
    private Point point1 = new Point(0, 0);
    private Point point2 = new Point(0, 0);

    public Panel(ObjectOutputStream os) throws Exception
    {
        super();
        setSize(1000, 900);
        this.os = os;

        addMouseMotionListener(this);
        addMouseListener(this);

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        repaint();
    }

    public void updateCanvas(Painting image)
    {
        draw = image;
        repaint();
    }

    public void paint(Graphics bg)
    {
        Graphics g = buffer.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.black);
        for (ArrayList<Point> line : draw.getImage())
        {
            for (int x = 1; x < line.size(); x++)
            {
                g.drawLine(line.get(x - 1).x, line.get(x - 1).y, line.get(x).x, line.get(x).y);
            }
        }

        //g.drawLine((int)point1.getX(), (int)point1.getY(), (int)point2.getX(), (int)point2.getY());
        //System.out.println(x+" "+y);


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
        x1 = e.getX();
        y1 = e.getY();
        point1.setLocation(x1, y1);

        draw.addPoint(point1);
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
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            x1 = x2 = e.getX();
            y1 = y2 = e.getY();

            point1.setLocation(x1, y1);

            draw.addPoint(point1);
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
