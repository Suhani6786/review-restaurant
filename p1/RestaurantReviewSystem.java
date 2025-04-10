package p1;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class RestaurantReviewSystem {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel panelContainer;
    private Reviewer currentUser;
    private ArrayList<Reviewer> users = new ArrayList<>();
    private String[] restaurantNames = {"McDonald's", "Pizza Hut", "Starbucks", "Taco Bell", "Subway"};
    private HashMap<String, String> logoPaths = new HashMap<>();

    public RestaurantReviewSystem() {
        frame = new JFrame("Restaurant Review System");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Load logo paths (make sure these files are in /images folder)
        logoPaths.put("McDonald's", "images/mcdonalds.png");
        logoPaths.put("Pizza Hut", "images/pizzahut.png");
        logoPaths.put("Starbucks", "images/starbucks.png");
        logoPaths.put("Taco Bell", "images/tacobell.png");
        logoPaths.put("Subway", "images/subway.png");

        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);

        panelContainer.add(loginPanel(), "Login");
        panelContainer.add(mainPanel(), "Main");

        frame.add(panelContainer);
        frame.setVisible(true);
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Continue");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Fields cannot be empty!");
                return;
            }

            boolean userExists = false;
            for (Reviewer user : users) {
                if (user.username.equals(username)) {
                    userExists = true;
                    if (user.login(username, password)) {
                        currentUser = user;
                        cardLayout.show(panelContainer, "Main");
                        return;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incorrect password!");
                        return;
                    }
                }
            }

            if (!userExists) {
                Reviewer newUser = new Reviewer(username, password);
                users.add(newUser);
                currentUser = newUser;
                JOptionPane.showMessageDialog(frame, "New user registered!");
                cardLayout.show(panelContainer, "Main");
            }
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);

        return panel;
    }

    private JPanel mainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JTextField("Search"), BorderLayout.NORTH);

        JComboBox<String> sortDropdown = new JComboBox<>(new String[]{"Sort by"});
        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortDropdown);
        headerPanel.add(sortPanel, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Review Cards Section
        JPanel reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));

        for (String name : restaurantNames) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setBackground(Color.WHITE);
            card.setMaximumSize(new Dimension(350, 100));

            // Load logo image
            JLabel logoLabel;
            if (logoPaths.containsKey(name)) {
                ImageIcon icon = new ImageIcon(logoPaths.get(name));
                Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                logoLabel = new JLabel(new ImageIcon(scaled));
            } else {
                logoLabel = new JLabel("[Logo]");
            }
            logoLabel.setPreferredSize(new Dimension(80, 80));
            logoLabel.setHorizontalAlignment(JLabel.CENTER);

            // Info panel
            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.add(new JLabel(name));
            info.add(new JLabel("★★★★☆"));
            info.add(new JLabel("Sample review."));

            // Button panel
            JPanel buttons = new JPanel();
            buttons.add(new JButton("Edit"));
            buttons.add(new JButton("Delete"));

            card.add(logoLabel, BorderLayout.WEST);
            card.add(info, BorderLayout.CENTER);
            card.add(buttons, BorderLayout.EAST);

            reviewsPanel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addReview = new JButton("+ Add Review");
        mainPanel.add(addReview, BorderLayout.SOUTH);

        return mainPanel;
    }

    public static void main(String[] args) {
        new RestaurantReviewSystem();
    }
}
