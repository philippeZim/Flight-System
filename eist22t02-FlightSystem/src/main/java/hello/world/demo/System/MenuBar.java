package hello.world.demo.System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class MenuBar extends JPanel implements ActionListener {
    private JPanel manager;
    private JPanel servicePanel;
    private JPanel destinationPanel;
    private JPanel myFlightsPanel;


    public MenuBar(JPanel manager, JPanel destinationPanel, JPanel servicePanel, JPanel myFlightsPanel) {
        this.myFlightsPanel = myFlightsPanel;
        this.destinationPanel = destinationPanel;
        this.manager = manager;
        this.servicePanel = servicePanel;


        JButton flightManager = new JButton("Look for flights");
        JButton destination = new JButton("Destination information");
        JButton service = new JButton("Flight Services");
        JButton myFlights = new JButton("Favorite flights");
        setBackground(Color.BLUE);
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        this.setLayout(layout);

        add(flightManager);
        add(destination);
        add(service);
        add(myFlights);

        service.addActionListener(this);
        myFlights.addActionListener(this);
        destination.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        switch (((JButton) e.getSource()).getText().charAt(1)) {
            case 'o':
                manager.setVisible(true);
                servicePanel.setVisible(false);
                myFlightsPanel.setVisible(false);
                destinationPanel.setVisible(false);

                break;
            case 'e':
                manager.setVisible(false);
                servicePanel.setVisible(false);
                myFlightsPanel.setVisible(false);
                destinationPanel.setVisible(true);
                break;
            case 'l':
                manager.setVisible(false);
                servicePanel.setVisible(true);
                myFlightsPanel.setVisible(false);
                destinationPanel.setVisible(false);
                break;
            case 'a':
                manager.setVisible(false);
                servicePanel.setVisible(false);
                myFlightsPanel.setVisible(true);
                destinationPanel.setVisible(false);
                break;
        }


    }
}
