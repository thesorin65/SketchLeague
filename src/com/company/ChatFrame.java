package com.company;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ChatFrame extends JFrame
{
    private ChatPanel cp;
    private Panel p;

    public ChatFrame(String userName, ObjectOutputStream os) throws Exception
    {
        super("Chat Client");

        cp = new ChatPanel(userName, os);
        p = new Panel(os);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);

        p.setBounds(480,0,1120,900);
        setLayout(null);


        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent winEvt){
                try
                {
                    cp.exit();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        add(p);
        add(cp);

        setResizable(false);
        setVisible(true);
    }

    public ChatPanel getChat()
    {
        return cp;
    }

    public Panel getDrawings()
    {
        return p;
    }
}