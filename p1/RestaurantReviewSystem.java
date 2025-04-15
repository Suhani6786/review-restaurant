package p1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RestaurantReviewSystem {
    private JFrame frame;
    private JPanel mainPanel;
    private JComboBox<String> sortDropdown;
    private JPanel cardsPanel;
    private Reviewer currentUser = new Reviewer("you");

    private String[] restaurantNames = {
        "McDonald's 🍔", "Pizza Hut 🍕", "Starbucks ☕", "Taco Bell 🌮", "Subway 🥪"
    };

    private HashMap<String, String[]> topItems = new HashMap<>();
    private HashMap<String, String[]> itemPrices = new HashMap<>();
    private HashMap<String, ArrayList<Review>> restaurantReviews = new HashMap<>();
    private HashMap<String, String> logoPaths = new HashMap<>(); // ✅ logo paths

    public RestaurantReviewSystem() {
        frame = new JFrame("🔥 Restaurant Review System");
        frame.setSize(700, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout());

        setupTopItems();
        setupLogos(); // ✅ logo setup
        generateFakeReviews();
        setupMainPanel();
        refreshCards();

        frame.setVisible(true);
    }

    private void setupTopItems() {
        topItems.put("McDonald's 🍔", new String[]{"🍟 Fries", "🍔 Big Mac", "🍗 Nuggets", "🍦 McFlurry", "🥤 Coke"});
        topItems.put("Pizza Hut 🍕", new String[]{"🍕 Pepperoni Pizza", "🧀 Cheese Sticks", "🥗 Veggie Pizza", "🥤 Pepsi", "🍪 Cookie"});
        topItems.put("Starbucks ☕", new String[]{"☕ Latte", "🍪 Cookie", "🥐 Croissant", "🍰 Cake Pop", "🧋 Cold Brew"});
        topItems.put("Taco Bell 🌮", new String[]{"🌮 Taco", "🌯 Burrito", "🍟 Nacho Fries", "🧀 Quesadilla", "🥤 Baja Blast"});
        topItems.put("Subway 🥪", new String[]{"🥪 Italian BMT", "🥗 Veggie Delite", "🍪 Cookie", "🧀 Tuna Sub", "🥤 Lemonade"});

        itemPrices.put("McDonald's 🍔", new String[]{"$2.49", "$5.99", "$4.29", "$3.49", "$1.99"});
        itemPrices.put("Pizza Hut 🍕", new String[]{"$8.99", "$4.99", "$9.49", "$1.79", "$5.49"});
        itemPrices.put("Starbucks ☕", new String[]{"$4.25", "$2.25", "$3.95", "$2.95", "$3.75"});
        itemPrices.put("Taco Bell 🌮", new String[]{"$1.79", "$4.99", "$2.49", "$3.79", "$1.99"});
        itemPrices.put("Subway 🥪", new String[]{"$6.49", "$5.49", "$1.29", "$6.99", "$1.99"});
    }

    private void setupLogos() {
        logoPaths.put("McDonald's 🍔", "/images/mcdonalds.png");
        logoPaths.put("Pizza Hut 🍕", "/images/pizzahut.png");
        logoPaths.put("Starbucks ☕", "/images/starbucks.png");
        logoPaths.put("Taco Bell 🌮", "/images/tacobell.png");
        logoPaths.put("Subway 🥪", "/images/subway.png");
    }

    private void generateFakeReviews() {
        String[] users = {"cheeeseloverr", "foodieQueen", "tacogawd", "icedLatte", "spicyBoi"};
        String[][] comments = {
            {"Fries are 🔥", "Big Mac is a classic", "Love those nuggets!", "McFlurry = 😋", "Coke always cold"},
            {"So cheesy!", "Great crust!", "Fresh salad!", "Pepsi hits", "Warm cookie 🥺"},
            {"Perfect latte!", "Best cookie!", "Buttery croissant!", "Cute cake pop", "Cold brew energizer"},
            {"Crunchy tacos 😍", "Beef burrito slaps", "Love the fries", "Cheesy Q rocks!", "Blast is life"},
            {"Footlong = filling", "Fresh veggies", "Soft cookie!", "Love their tuna", "Cold drink = win"}
        };

        Random rand = new Random();
        for (int i = 0; i < restaurantNames.length; i++) {
            ArrayList<Review> list = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                int rating = rand.nextInt(5) + 1;
                list.add(new Review(restaurantNames[i], rating, comments[i][j], users[j], false));
            }
            restaurantReviews.put(restaurantNames[i], list);
        }
    }

    private void setupMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortPanel.setBackground(Color.BLACK);
        sortPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        sortDropdown = new JComboBox<>(new String[]{"Sort by", "Name (A-Z)", "Rating (High–Low)"});
        sortDropdown.addActionListener(e -> refreshCards());
        sortPanel.add(new JLabel("Sort:")).setForeground(Color.WHITE);
        sortPanel.add(sortDropdown);

        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);

        mainPanel.add(sortPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);
    }

    private void refreshCards() {
        cardsPanel.removeAll();
        ArrayList<String> sorted = new ArrayList<>(Arrays.asList(restaurantNames));

        if ("Name (A-Z)".equals(sortDropdown.getSelectedItem())) {
            sorted.sort(Comparator.naturalOrder());
        } else if ("Rating (High–Low)".equals(sortDropdown.getSelectedItem())) {
            sorted.sort((a, b) -> Double.compare(getAverageRating(b), getAverageRating(a)));
        }

        for (String restaurant : sorted) {
            JPanel card = new JPanel(new BorderLayout());
            card.setMaximumSize(new Dimension(650, 150));
            card.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            card.setBackground(Color.BLACK);

            // ✅ Add logo on the left
            String path = logoPaths.get(restaurant);
            if (path != null) {
                ImageIcon icon = new ImageIcon(getClass().getResource(path));
                Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel logo = new JLabel(new ImageIcon(scaled));
                logo.setBorder(new EmptyBorder(10, 10, 10, 10));
                card.add(logo, BorderLayout.WEST);
            }

            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setBackground(Color.BLACK);

            JLabel nameLabel = new JLabel(restaurant);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
            nameLabel.setForeground(Color.WHITE);

            double avg = getAverageRating(restaurant);
            JLabel ratingLabel = getColoredStars((int) Math.round(avg));
            JLabel summary = new JLabel("🔥 Top rated by foodies!");
            summary.setForeground(Color.LIGHT_GRAY);

            info.add(nameLabel);
            info.add(ratingLabel);
            info.add(summary);

            JButton editBtn = new JButton("✏️ Edit");
            editBtn.addActionListener(e -> openEditPopup(restaurant));

            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(Color.BLACK);
            right.add(editBtn, BorderLayout.NORTH);

            card.add(info, BorderLayout.CENTER);
            card.add(right, BorderLayout.EAST);

            cardsPanel.add(card);
            cardsPanel.add(Box.createVerticalStrut(10));
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private double getAverageRating(String restaurant) {
        return restaurantReviews.get(restaurant).stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    private JLabel getColoredStars(int rating) {
        JLabel label = new JLabel("★".repeat(rating) + "☆".repeat(5 - rating));
        label.setFont(new Font("Arial", Font.BOLD, 16));
        switch (rating) {
            case 1 -> label.setForeground(Color.GREEN);
            case 2 -> label.setForeground(Color.ORANGE);
            case 3 -> label.setForeground(Color.RED);
            case 4 -> label.setForeground(Color.YELLOW);
            case 5 -> label.setForeground(Color.CYAN);
            default -> label.setForeground(Color.GRAY);
        }
        return label;
    }

    private void openEditPopup(String restaurant) {
        // your original popup code goes here (unchanged)
    }

    private Color getUsernameColor(String user) {
        return switch (user) {
            case "cheeeseloverr" -> Color.CYAN;
            case "foodieQueen" -> Color.PINK;
            case "tacogawd" -> new Color(255, 102, 0);
            case "icedLatte" -> new Color(153, 102, 255);
            case "spicyBoi" -> Color.RED;
            default -> Color.LIGHT_GRAY;
        };
    }

    private String getMood(int stars) {
        return switch (stars) {
            case 1 -> "💚 Terrible";
            case 2 -> "🧡 Meh";
            case 3 -> "❤️ Okay";
            case 4 -> "💛 Great!";
            case 5 -> "💙 Amazing!";
            default -> "😐";
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RestaurantReviewSystem::new);
    }
}
