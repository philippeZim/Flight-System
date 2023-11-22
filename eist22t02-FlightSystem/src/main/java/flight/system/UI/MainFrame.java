package flight.system.UI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import flight.system.Data.API;
import flight.system.FlightManager.FlightInfo;
import flight.system.Destination.POI;
import flight.system.Destination.Scraper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.JButton;
import javax.swing.JFrame;

@SpringBootApplication
public class MainFrame extends JFrame {

    private JButton searchFlightsButton;
    private JButton favoritesButton;
    private JButton onThePlaneButton;
    private JPanel searchFlightsPanel;
    private JTextField originTextField;
    private JTextField destinationTextField;
    private JButton searchButton;
    private JTextField fromDateTextField;
    private JTextField toDateTextField;
    private JPanel mainPanel;
    private JPanel panel1;
    private JScrollPane scrollPane;
    private JPanel flightsList;
    private JPanel favoritesPanel;
    private JPanel flightInfoDestPanel;
    private JPanel imageInfoPanel;
    private JTextArea textArea1;
    private JLabel imageLabel;
    private JToolBar toolBarBar;
    private JPanel favoritesPane;
    private JPanel poiPanel;
    private JTextArea weatherTextArea;
    private JPanel OnThePlanePanel;
    private JTextArea textArea2;
    private JButton requestServiceButton;
    private JLabel savetyImageLabel;
    private JButton submitFeedbackButton;

    private ArrayList<FlightInfo> favorites;

    private API api;

    CardLayout cl;

    public MainFrame() {
        cl = (CardLayout) panel1.getLayout();
        toolBar(cl);
        favorites = new ArrayList<>();
        search();
        api = new API();
        setContentPane(mainPanel);
        setTitle("Flight System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public void search() {
        searchButton.addActionListener(e -> {
            ArrayList<FlightInfo> flights = api.getFlights(originTextField.getText(), destinationTextField.getText());
            ArrayList<JButton[]> buttons = new ArrayList<>();
            flights.forEach(f -> buttons.add(new JButton[]{new JButton("Details"), new JButton("Add to Favorites")}));
            buttons.forEach(b -> {
                b[0].addActionListener(e1 -> {
                    cl.show(panel1, "Card4");
                    panel1.revalidate();
                    panel1.repaint();
                    FlightInfo save = flights.get(buttons.indexOf(b));
                    showFlightDetails(save);
                });
                b[1].addActionListener(e1 -> favorites.add(flights.get(buttons.indexOf(b))));
            });
            flightsList.removeAll();
            flightsList.setLayout(new GridLayout(flights.size(), 9));


            for (int i = 0; i < flights.size(); i++) {
                flightsList.add(new JLabel(flights.get(i).getFlightNumber()));
                flightsList.add(new JLabel(flights.get(i).getAirline()));
                flightsList.add(new JLabel(flights.get(i).getOrigin()));
                flightsList.add(new JLabel(flights.get(i).getDestination()));
                flightsList.add(new JLabel(flights.get(i).getStartTime()));
                flightsList.add(new JLabel(flights.get(i).getEndTime()));
                flightsList.add(new JLabel(fromDateTextField.getText()));
                flightsList.add(buttons.get(i)[0]);
                flightsList.add(buttons.get(i)[1]);
            }
            flightsList.revalidate();
            flightsList.repaint();
            flights.forEach(System.out::println);
        });
    }

    public void showFavorites() {
        favoritesPane.removeAll();
        favoritesPane.setLayout(new GridLayout(favorites.size(), 8));
        ArrayList<JButton[]> buttons = new ArrayList<>();
        favorites.forEach(f -> buttons.add(new JButton[]{new JButton("Details"), new JButton("Remove from Favorites")}));
        buttons.forEach(b -> {
            b[0].addActionListener(e1 -> showFlightDetails(favorites.get(buttons.indexOf(b))));
            b[1].addActionListener(e1 -> {
                favorites.remove(buttons.indexOf(b));
                showFavorites();
            });
        });
        for (int i = 0; i < favorites.size(); i++) {
            favoritesPane.add(new JLabel(favorites.get(i).getFlightNumber()));
            favoritesPane.add(new JLabel(favorites.get(i).getAirline()));
            favoritesPane.add(new JLabel(favorites.get(i).getOrigin()));
            favoritesPane.add(new JLabel(favorites.get(i).getDestination()));
            favoritesPane.add(new JLabel(favorites.get(i).getStartTime()));
            favoritesPane.add(new JLabel(favorites.get(i).getEndTime()));
            favoritesPane.add(buttons.get(i)[0]);
            favoritesPane.add(buttons.get(i)[1]);
        }
        favoritesPane.revalidate();
        favoritesPane.repaint();
    }

    public void showFlightDetails(FlightInfo flightInfo) {
        cl.show(panel1, "Card4");
        panel1.revalidate();
        panel1.repaint();
        weatherArea(flightInfo.getDestination());
        textArea1.setText(
                "Flight Number: " + flightInfo.getFlightNumber() + "\n" +
                        "Airline: " + flightInfo.getAirline() + "\n" +
                        "Origin: " + flightInfo.getOrigin() + "\n" +
                        "Destination: " + flightInfo.getDestination() + "\n" +
                        "Start Time: " + flightInfo.getStartTime() + "\n" +
                        "End Time: " + flightInfo.getEndTime() + "\n" +
                        "Gate: " + flightInfo.getGate() + "\n" +
                        "Terminal: " + flightInfo.getTerminal() + "\n" +
                        "Aircraft: " + flightInfo.getAirplaneType());
        textArea1.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        String path = api.getMap(flightInfo.getOrigin(), flightInfo.getDestination());
        URL url;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageLabel.setIcon(new ImageIcon(myPicture));
        flightInfoDestPanel.revalidate();
        flightInfoDestPanel.repaint();
        imageInfoPanel.revalidate();
        imageInfoPanel.repaint();
        setPoiPanel(flightInfo.getDestination());
        poiPanel.revalidate();
        poiPanel.repaint();


    }

    BufferedImage resizeImage(BufferedImage originalImage) {
        Rectangle r = this.getBounds();
        int h = r.height;
        int w = r.width;
        BufferedImage resizedImage = new BufferedImage(w / 2 - 100, (h / 4) * 3, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, w / 2 - 100, (h / 4) * 3, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private void setupOnThePlanePanel() {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("src/main/java/flight/system/Data/fullSavety.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        myPicture = resizeImage(myPicture);
        savetyImageLabel.setIcon(new ImageIcon(myPicture));
        submitFeedbackButton.addActionListener(e -> textArea2.setText("Thank You For Your Feedback!"));
    }

    public void toolBar(CardLayout cl) {
        searchFlightsButton.addActionListener(e -> cl.show(panel1, "Card1"));
        favoritesButton.addActionListener(e -> {
            showFavorites();
            cl.show(panel1, "Card2");
        });
        onThePlaneButton.addActionListener(e -> {
            setupOnThePlanePanel();
            cl.show(panel1, "Card3");
        });
    }

    public void weatherArea(String destination) {
        weatherTextArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        weatherTextArea.setText(destination.substring(0, 1).toUpperCase() + destination.substring(1) + " weather next week:\n");
        api.weatherNextWeek(destination).forEach(strings -> {
            weatherTextArea.setText(weatherTextArea.getText() + strings[0] + " " + strings[1] + " " + strings[2] + "\n");
        });
        weatherTextArea.revalidate();
        weatherTextArea.repaint();
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setPoiPanel(String destination) {
        Scraper s = new Scraper();
        POI[][] pois = s.getPOI(destination);
        int maxLength = Math.max(pois[0].length, Math.max(pois[1].length, pois[2].length));
        poiPanel.setLayout(new GridLayout(3, maxLength));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < maxLength; j++) {
                JPanel elem = new JPanel();
                Random r = new Random();
                if (j < pois[i].length) {
                    elem.setLayout(new BorderLayout());
                    JButton image = new JButton();
                    String path = pois[i][j].getImageLink();
                    URL url;
                    try {
                        url = new URL(path);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    BufferedImage myPicture = null;
                    try {
                        myPicture = ImageIO.read(url);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (myPicture.getHeight() > 250) {
                        myPicture = myPicture.getSubimage(0, myPicture.getHeight() / 2 - 125, myPicture.getWidth(), 250);
                    }
                    System.out.println(myPicture.getHeight() + ", " + myPicture.getWidth());
                    image.setIcon(new ImageIcon(myPicture));
                    String nameT = pois[i][j].getName();
                    JLabel name = new JLabel(nameT);

                    name.setMaximumSize(new Dimension(200, 60));
                    name.setFont(new Font(Font.DIALOG, Font.PLAIN, 28));
                    int finalI = i;
                    int finalJ = j;
                    image.addActionListener(e -> {
                        try {
                            openWebpage(new URI(pois[finalI][finalJ].getTripAdvisorLink()));
                        } catch (URISyntaxException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    elem.add(image, BorderLayout.CENTER);
                    elem.add(name, BorderLayout.NORTH);
                }
                poiPanel.add(elem);

            }
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        toolBarBar = new JToolBar();
        mainPanel.add(toolBarBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        searchFlightsButton = new JButton();
        searchFlightsButton.setText("Search Flights");
        toolBarBar.add(searchFlightsButton);
        favoritesButton = new JButton();
        favoritesButton.setText("Favorites");
        toolBarBar.add(favoritesButton);
        onThePlaneButton = new JButton();
        onThePlaneButton.setText("On The Plane");
        toolBarBar.add(onThePlaneButton);
        panel1 = new JPanel();
        panel1.setLayout(new CardLayout(0, 0));
        mainPanel.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchFlightsPanel = new JPanel();
        searchFlightsPanel.setLayout(new GridLayoutManager(3, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(searchFlightsPanel, "Card1");
        originTextField = new JTextField();
        originTextField.setText("");
        searchFlightsPanel.add(originTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        destinationTextField = new JTextField();
        destinationTextField.setText("");
        searchFlightsPanel.add(destinationTextField, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setText("search");
        searchFlightsPanel.add(searchButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fromDateTextField = new JTextField();
        fromDateTextField.setText("");
        searchFlightsPanel.add(fromDateTextField, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        toDateTextField = new JTextField();
        toDateTextField.setText("");
        searchFlightsPanel.add(toDateTextField, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        scrollPane = new JScrollPane();
        searchFlightsPanel.add(scrollPane, new GridConstraints(2, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        flightsList = new JPanel();
        flightsList.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        flightsList.setEnabled(false);
        scrollPane.setViewportView(flightsList);
        final JLabel label1 = new JLabel();
        label1.setText("Origin");
        searchFlightsPanel.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Destination");
        searchFlightsPanel.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("From (Date)");
        searchFlightsPanel.add(label3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("To (Date)");
        searchFlightsPanel.add(label4, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        searchFlightsPanel.add(spacer1, new GridConstraints(0, 6, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        searchFlightsPanel.add(spacer2, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(favoritesPanel, "Card2");
        final JScrollPane scrollPane1 = new JScrollPane();
        favoritesPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        favoritesPane = new JPanel();
        favoritesPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane1.setViewportView(favoritesPane);
        OnThePlanePanel = new JPanel();
        OnThePlanePanel.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(OnThePlanePanel, "Card3");
        final JLabel label5 = new JLabel();
        label5.setText("Savety Instructions");
        OnThePlanePanel.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textArea2 = new JTextArea();
        OnThePlanePanel.add(textArea2, new GridConstraints(1, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Please Give Us Some Feedback");
        OnThePlanePanel.add(label6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        savetyImageLabel = new JLabel();
        savetyImageLabel.setText("");
        OnThePlanePanel.add(savetyImageLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        requestServiceButton = new JButton();
        requestServiceButton.setText("Request Service");
        OnThePlanePanel.add(requestServiceButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        submitFeedbackButton = new JButton();
        submitFeedbackButton.setText("Submit Feedback");
        OnThePlanePanel.add(submitFeedbackButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flightInfoDestPanel = new JPanel();
        flightInfoDestPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(flightInfoDestPanel, "Card4");
        imageInfoPanel = new JPanel();
        imageInfoPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        flightInfoDestPanel.add(imageInfoPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        imageLabel = new JLabel();
        imageLabel.setText("");
        imageInfoPanel.add(imageLabel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textArea1 = new JTextArea();
        imageInfoPanel.add(textArea1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        weatherTextArea = new JTextArea();
        imageInfoPanel.add(weatherTextArea, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        flightInfoDestPanel.add(scrollPane2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        poiPanel = new JPanel();
        poiPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane2.setViewportView(poiPanel);
        final JLabel label7 = new JLabel();
        label7.setText("  ");
        mainPanel.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText(" ");
        mainPanel.add(label8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public static void main(String[] args) {
        MainFrame m = new MainFrame();
    }

}
