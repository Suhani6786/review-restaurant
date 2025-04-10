package p1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantReviewSystem {
    private JFrame frame;
    private JPanel panelContainer;
    private HashMap<String, String> logoPaths = new HashMap<>();
    private ArrayList<Review> reviews = new ArrayList<>();

    private String[] restaurantNames = {
        "McDonald's 🍔", "Pizza Hut 🍕", "Starbucks ☕", "Taco Bell 🌮", "Subway 🥪"
    };

    private HashMap<String, String[]> topItems = new HashMap<>();
    private JPanel reviewsPanel;
    private JComboBox<String> sortDropdown;

    public RestaurantReviewSystem() {
        frame = new JFrame("Restaurant Review System");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        logoPaths.put("McDonald's 🍔", "images/mcdonalds.png");
        logoPaths.put("Pizza Hut 🍕", "images/pizzahut.png");
        logoPaths.put("Starbucks ☕", "images/starbucks.png");
        logoPaths.put("Taco Bell 🌮", "images/tacobell.png");
        logoPaths.put("Subway 🥪", "images/subway.png");

        topItems.put("McDonald's 🍔", new String[]{"🍔 Big Mac", "🍟 Fries", "🥤 Coke", "🍦 Sundae", "🍗 Nuggets"});
        topItems.put("Pizza Hut 🍕", new String[]{"🍕 Pepperoni Pizza", "🧀 Cheese Sticks", "🥗 Salad", "🍝 Pasta", "🥤 Pepsi"});
        topItems.put("Starbucks ☕", new String[]{"☕ Latte", "🥐 Croissant", "🧋 Frappuccino", "🍪 Cookie", "🥯 Bagel"});
        topItems.put("Taco Bell 🌮", new String[]{"🌮 Tacos", "🌯 Burrito", "🥤 Baja Blast", "🧀 Quesadilla", "🍟 Nacho Fries"});
        topItems.put("Subway 🥪", new String[]{"🥪 Italian BMT", "🥗 Veggie Delight", "🍞 Wheat Bread", "🧀 Cheese Melt", "🥤 Lemonade"});

        panelContainer = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JTextField searchBar = new JTextField("Search");

        sortDropdown = new JComboBox<>(new String[]{"Sort by", "Name (A-Z)", "Rating (High–Low)"});
        sortDropdown.addActionListener(e -> refreshReviews());

        topPanel.add(searchBar);
        topPanel.add(sortDropdown);

        panelContainer.add(topPanel, BorderLayout.NORTH);

        reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        panelContainer.add(scrollPane, BorderLayout.CENTER);

        JButton addBtn = new JButton("+ Add Review");
        addBtn.addActionListener(e -> openReviewForm(null));
        panelContainer.add(addBtn, BorderLayout.SOUTH);

        generateFakeData();
        refreshReviews();

        frame.add(panelContainer);
        frame.setVisible(true);
    }

    private void generateFakeData() {
        String[][] sampleComments = {
            {"Great service!", "Love the Big Mac!", "Fast and hot!", "Would go again!", "Classic spot!"},
            {"Perfect crust 🍕", "So cheesy!", "Family fave", "Loved the service!", "10/10"},
            {"Great vibes ☕", "Cozy and quiet", "Fav morning spot", "Best espresso!", "Always fresh"},
            {"Taco heaven 🌮", "Late night savior", "Cheap & tasty", "Quick bite!", "Spicy and good!"},
            {"Healthy & fast 🥗", "Fresh bread!", "Customizable 😋", "Always tasty", "Best for lunch!"}
        };

        for (int i = 0; i < restaurantNames.length; i++) {
            for (int j = 1; j <= 5; j++) {
                int rating = 5 - (j % 3); // Varying rating
                reviews.add(new Review(restaurantNames[i], rating, "User" + j + ": " + sampleComments[i][j - 1]));
            }
        }
    }

    private void refreshReviews() {
        reviewsPanel.removeAll();

        ArrayList<Review> sorted = new ArrayList<>(reviews);
        String sortOption = (String) sortDropdown.getSelectedItem();

        if ("Name (A-Z)".equals(sortOption)) {
            sorted.sort(Comparator.comparing(Review::getRestaurant));
        } else if ("Rating (High–Low)".equals(sortOption)) {
            sorted.sort((a, b) -> b.getRating() - a.getRating());
        }

        for (Review review : sorted) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setBackground(Color.WHITE);
            card.setMaximumSize(new Dimension(450, 110));

            JLabel logoLabel;
            ImageIcon icon = new ImageIcon(logoPaths.get(review.getRestaurant()));
            Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaled));
            logoLabel.setPreferredSize(new Dimension(80, 80));
            logoLabel.setHorizontalAlignment(JLabel.CENTER);

            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(review.getRestaurant());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel stars = new JLabel(getStars(review.getRating()));
            stars.setForeground(getStarColor(review.getRating()));

            JLabel comment = new JLabel(review.getComment());

            info.add(nameLabel);
            info.add(stars);
            info.add(comment);

            JPanel buttons = new JPanel();
            JButton edit = new JButton("Edit");
            JButton delete = new JButton("Delete");

            edit.addActionListener(e -> openReviewForm(review));
            delete.addActionListener(e -> {
                reviews.remove(review);
                refreshReviews();
            });

            buttons.add(edit);
            buttons.add(delete);

            card.add(logoLabel, BorderLayout.WEST);
            card.add(info, BorderLayout.CENTER);
            card.add(buttons, BorderLayout.EAST);

            reviewsPanel.add(card);
        }

        reviewsPanel.revalidate();
        reviewsPanel.repaint();
    }

    private void openReviewForm(Review review) {
        JFrame popup = new JFrame("Edit Review");
        popup.setSize(400, 500);
        popup.setLocationRelativeTo(frame);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField commentField = new JTextField(review != null ? review.getComment() : "");
        JComboBox<Integer> starDropdown = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JLabel emojiLabel = new JLabel("😐 Mood");

        starDropdown.addActionListener(e -> {
            int rating = (int) starDropdown.getSelectedItem();
            emojiLabel.setText(getEmojiMood(rating));
        });

        panel.add(new JLabel("Your Comment:"));
        panel.add(commentField);
        panel.add(new JLabel("Rating:"));
        panel.add(starDropdown);
        panel.add(emojiLabel);

        panel.add(new JLabel("🔥 Rate Top 5 Items:"));
        String restaurant = review.getRestaurant();
        String[] items = topItems.get(restaurant);

        HashMap<String, Integer> itemRatings = new HashMap<>();
        for (String item : items) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel itemLabel = new JLabel(item);
            JComboBox<Integer> itemRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            itemRating.addActionListener(e -> {
                itemRatings.put(item, (int) itemRating.getSelectedItem());
            });
            itemPanel.add(itemLabel);
            itemPanel.add(itemRating);
            panel.add(itemPanel);
        }

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            review.setComment(commentField.getText());
            review.setRating((int) starDropdown.getSelectedItem());
            refreshReviews();
            popup.dispose();
        });

        panel.add(save);
        popup.add(panel);
        popup.setVisible(true);
    }

    private String getStars(int rating) {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    private Color getStarColor(int rating) {
        switch (rating) {
            case 1: return Color.RED;
            case 2: return Color.ORANGE;
            case 3: return Color.YELLOW.darker();
            case 4: return new Color(255, 215, 0); // golden
            case 5: return new Color(255, 165, 0); // dark orange
            default: return Color.GRAY;
        }
    }

    private String getEmojiMood(int rating) {
        return switch (rating) {
            case 1 -> "😬 Awful!";
            case 2 -> "😕 Meh";
            case 3 -> "😌 Okay";
            case 4 -> "😋 Yummy!";
            case 5 -> "🤩 Amazing!";
            default -> "😐";
        };
    }

    public static void main(String[] args) {
        new RestaurantReviewSystem();
    }
}
