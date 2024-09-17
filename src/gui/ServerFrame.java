package gui;

import core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class ServerFrame extends JFrame  {
    private Server server;
   private boolean isOn = false;

    private JLabel label = new JLabel();

    private  JPanel upPanel = new JPanel();
    private  JPanel downPanel = new JPanel();
    private  JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    public ServerFrame(){
        //basics
        setTitle("Server");
        setSize(new Dimension(350,200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,1));


        add(upPanel);
        upPanel.add(startButton);
        upPanel.add(stopButton);
        startButton.addActionListener(new StartServer());
        stopButton.addActionListener(new StopServer());


        add(downPanel);
        label.setText("The server is off.");
        downPanel.add(label);


        setVisible(true);



    }


    class StartServer implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isOn) {
                server = new Server();
                isOn = true;
                label.setText("The server is on!");
                server.start();

            }
            else{
                JOptionPane.showMessageDialog(null, "Error! The server is already on!");

            }
        }
    }



    class StopServer implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            if(isOn) {

                isOn = false;
                label.setText("The server is off.");
                server.shutdown();
            }
            else{
                JOptionPane.showMessageDialog(null, "Error! The server is already off!");
            }

        }
    }


    public static void main(String[] args) {
        ServerFrame frame = new ServerFrame();
    }


}

