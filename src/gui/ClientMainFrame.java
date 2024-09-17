package gui;

import javax.swing.*;

public class ClientMainFrame extends JFrame {

    ClientMainFrame(){
        //basics
        setTitle("Chat Room");
        setSize(600,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
    }

}
