package p1;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;

public class RestaurantReviewSystem {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel panelContainer;
    private Reviewer currentUser = new Reviewer("guest", "123"); // one user only
    private String[] restaurantNames = {"McDonald's", "Pizza Hut", "Starbucks", "Taco Bell", "Subway"};
    private HashMap<String, String> logoPaths = new HashMap<>();
    private JPanel reviewsPanel;

    public RestaurantReviewSystem() {
        frame = new JFrame("Restaurant Review System");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Logo paths (must be in 'images/' folder)
        logoPaths.put("McDonald's", "images/mcdonalds.png");
        logoPaths.put("Pizza Hut", "images/pizzahut.png");
        logoPaths.put("Starbucks", "images/starbucks.png");
        logoPaths.put("Taco Bell", "images/tacobell.png");
        logoPaths.put("Subway", "images/subway.png");

        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);

        panelContainer.add(mainPanel(), "Main");

        frame.add(panelContainer);
        frame.setVisible(true);
    }

    private JPanel mainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JTextField searchField = new JTextField("Search");
        mainPanel.add(searchField, BorderLayout.NORTH);

        String[] sortOptions = {
            "Sort by", "Rating: High to Low", "Rating: Low to High", "Name: A to Z", "Name: Z to A"
        };
        JComboBox<String> sortDropdown = new JComboBox<>(sortOptions);

        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortDropdown);
        mainPanel.add(sortPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Review display panel
        reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
        refreshReviews();

        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add review button
        JButton addReview = new JButton("+ Add Review");
        mainPanel.add(addReview, BorderLayout.SOUTH);

        // Listeners
        addReview.addActionListener(e -> openReviewPopup());
        sortDropdown.addActionListener(e -> {
            String selected = (String) sortDropdown.getSelectedItem();
            JOptionPane.showMessageDialog(frame, "Sort selected: " + selected);
            // TODO: Add real sorting logic later
        });

        return mainPanel;
    }

    private void refreshReviews() {
        reviewsPanel.removeAll();

        for (String name : restaurantNames) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setBackground(Color.WHITE);
            card.setMaximumSize(new Dimension(350, 100));

            // Logo
            JLabel logoLabel;
            if (logoPaths.containsKey(name)) {
                ImageIcon icon = new ImageIcon(new File(logoPaths.get(name)).getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                logoLabel = new JLabel(new ImageIcon(scaled));
            } else {
                logoLabel = new JLabel("[Logo]");
            }
            logoLabel.setPreferredSize(new Dimension(80, 80));
            logoLabel.setHorizontalAlignment(JLabel.CENTER);

            // Info
            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel starsLabel = new JLabel("★★★★☆");
            starsLabel.setForeground(Color.YELLOW);

            JLabel commentLabel = new JLabel(getReviewComment(name));

            info.add(nameLabel);
            info.add(starsLabel);
            info.add(commentLabel);

            // Buttons
            JPanel buttons = new JPanel();
            buttons.add(new JButton("Edit"));   // future
            buttons.add(new JButton("Delete")); // future

            card.add(logoLabel, BorderLayout.WEST);
            card.add(info, BorderLayout.CENTER);
            card.add(buttons, BorderLayout.EAST);

            reviewsPanel.add(card);
        }

        reviewsPanel.revalidate();
        reviewsPanel.repaint();
    }

    private String getReviewComment(String restaurant) {
        for (Review review : currentUser.getReviews()) {
            if (review.getRestaurant().equalsIgnoreCase(restaurant)) {
                return review.getComment();
            }
        }
        return "Sample review.";
    }

    private void openReviewPopup() {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));

        JComboBox<String> restaurantSelect = new JComboBox<>(restaurantNames);
        JComboBox<String> starSelect = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        JTextField commentField = new JTextField();

        form.add(new JLabel("Restaurant:"));
        form.add(restaurantSelect);
        form.add(new JLabel("Rating:"));
        form.add(starSelect);
        form.add(new JLabel("Comment:"));
        form.add(commentField);

        int result = JOptionPane.showConfirmDialog(frame, form, "Add Review", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String restaurant = (String) restaurantSelect.getSelectedItem();
            int stars = Integer.parseInt((String) starSelect.getSelectedItem());
            String comment = commentField.getText().trim();

            currentUser.writeReview(restaurant, stars, comment);
            refreshReviews();
        }
    }

    public static void main(String[] args) {
        new RestaurantReviewSystem();
    }
}


