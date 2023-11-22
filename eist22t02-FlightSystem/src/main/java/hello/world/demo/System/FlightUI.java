package hello.world.demo.System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class FlightUI extends JFrame{
    private JFrame frame;
    private JPanel flightManager;
    private JPanel service;
    private JPanel destination;
    private JPanel myFlights;
/*
    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];



    public FlightUI() {
        this.frame = new JFrame();
        this.myFlights = new MyFlightsUI();
        this.destination = new DestinationUI();
        this.flightManager = new FlightManagerUI();
        this.service = new OnFlightUI();

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {  // handler
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            }});
        device.setFullScreenWindow(frame);
        MenuBar bar = new MenuBar(flightManager, destination, myFlights, service);
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        frame.add(bar, BorderLayout.NORTH);
        bar.setVisible(true);
        destination.setVisible(false);
        myFlights.setVisible(true);
        frame.add(myFlights, BorderLayout.CENTER);




    }

    public static void main(String[] args) {
        new FlightUI();
    }*/
}
