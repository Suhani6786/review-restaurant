package p1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RestaurantReviewSystem {
    private JFrame frame;
    private Reviewer currentUser;
    private HashMap<String, List<Review>> reviewsMap = new HashMap<>();
    private HashMap<String, String> logoPaths = new HashMap<>();
    private HashMap<String, List<String>> topMenuItems = new HashMap<>();
    private HashMap<String, List<Double>> topItemPrices = new HashMap<>();
    private String[] restaurants = {"McDonald's 🍔", "Pizza Hut 🍕", "Starbucks ☕", "Taco Bell 🌮", "Subway 🥪"};

    public RestaurantReviewSystem() {
        initializeData();
        loginScreen();
    }

    private void initializeData() {
        for (String res : restaurants) {
            reviewsMap.put(res, new ArrayList<>());
        }

        logoPaths.put("McDonald's 🍔", "images/mcdonalds.png");
        logoPaths.put("Pizza Hut 🍕", "images/pizzahut.png");
        logoPaths.put("Starbucks ☕", "images/starbucks.png");
        logoPaths.put("Taco Bell 🌮", "images/tacobell.png");
        logoPaths.put("Subway 🥪", "images/subway.png");

        topMenuItems.put("McDonald's 🍔", Arrays.asList("🍟 Fries", "🍔 Big Mac", "🥤 Soda", "🍦 Sundae", "🍗 Nuggets"));
        topMenuItems.put("Pizza Hut 🍕", Arrays.asList("🍕 Pepperoni Pizza", "🧀 Cheese Sticks", "🥗 Veggie Pizza", "🥤 Pepsi", "🍪 Cookie"));
        topMenuItems.put("Starbucks ☕", Arrays.asList("☕ Latte", "🍰 Cake Pop", "🥐 Croissant", "🍫 Mocha", "🥤 Frappuccino"));
        topMenuItems.put("Taco Bell 🌮", Arrays.asList("🌮 Taco", "🌯 Burrito", "🥤 Baja Blast", "🧀 Nachos", "🍩 Churro"));
        topMenuItems.put("Subway 🥪", Arrays.asList("🥪 Turkey Sub", "🥓 BLT", "🍪 Cookie", "🥗 Salad", "🥤 Drink"));

        topItemPrices.put("McDonald's 🍔", Arrays.asList(2.99, 4.49, 1.99, 2.29, 3.99));
        topItemPrices.put("Pizza Hut 🍕", Arrays.asList(8.99, 4.99, 9.49, 1.79, 5.49));
        topItemPrices.put("Starbucks ☕", Arrays.asList(4.79, 2.49, 3.59, 4.99, 5.79));
        topItemPrices.put("Taco Bell 🌮", Arrays.asList(2.49, 3.99, 1.89, 2.99, 1.99));
        topItemPrices.put("Subway 🥪", Arrays.asList(5.49, 6.49, 1.99, 4.79, 1.59));
    }

    private void loginScreen() {
        JFrame loginFrame = new JFrame("Login 🍽️");
        loginFrame.setSize(300, 200);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.getContentPane().setBackground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.BLACK);

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        JButton loginBtn = new JButton("Login 🔐");
        loginBtn.addActionListener(e -> {
            String user = username.getText().trim();
            String pass = new String(password.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter both username and password!");
                return;
            }
            currentUser = new Reviewer(user, pass);
            loginFrame.dispose();
            showMainUI();
        });

        panel.add(new JLabel("👤 Username:", SwingConstants.CENTER));
        panel.add(username);
        panel.add(new JLabel("🔑 Password:", SwingConstants.CENTER));
        panel.add(password);
        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.add(loginBtn, BorderLayout.SOUTH);
        loginFrame.setVisible(true);
    }

    private void showMainUI() {
        frame = new JFrame("Restaurant Review System ✨");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout());

        JPanel restaurantPanel = new JPanel();
        restaurantPanel.setLayout(new BoxLayout(restaurantPanel, BoxLayout.Y_AXIS));
        restaurantPanel.setBackground(Color.BLACK);

        for (String restaurant : restaurants) {
            JPanel card = createRestaurantCard(restaurant);
            restaurantPanel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(restaurantPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createRestaurantCard(String restaurantName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.DARK_GRAY);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(550, 150));

        JLabel logo = new JLabel();
        ImageIcon icon = new ImageIcon(logoPaths.get(restaurantName));
        Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(scaled));
        card.add(logo, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.DARK_GRAY);

        JLabel nameLabel = new JLabel(restaurantName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        info.add(nameLabel);

        double avgRating = reviewsMap.get(restaurantName).stream()
                .mapToInt(Review::getRating)
                .average().orElse(4.0);
        info.add(createStarPanel(avgRating));

        JButton editButton = new JButton("✏️ Edit");
        editButton.addActionListener(e -> openEditPopup(restaurantName));

        card.add(info, BorderLayout.CENTER);
        card.add(editButton, BorderLayout.EAST);
        return card;
    }

    private JPanel createStarPanel(double rating) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.DARK_GRAY);

        int roundedRating = (int) Math.round(rating);
        Color starColor = switch (roundedRating) {
            case 5 -> Color.BLUE;
            case 3 -> Color.RED;
            case 1 -> Color.GREEN;
            default -> Color.YELLOW;
        };

        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= roundedRating ? "★" : "☆");
            star.setForeground(i <= roundedRating ? starColor : Color.GRAY);
            panel.add(star);
        }

        return panel;
    }

    private void openEditPopup(String restaurantName) {
        JFrame popup = new JFrame("Edit Review 📝");
        popup.setSize(450, 600);
        popup.setLocationRelativeTo(frame);
        popup.getContentPane().setBackground(Color.BLACK);
        popup.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);

        mainPanel.add(new JLabel("🔥 Rate Top Items:"));
        List<String> items = topMenuItems.get(restaurantName);
        List<Double> prices = topItemPrices.get(restaurantName);
        HashMap<String, Integer> itemRatings = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            double price = prices.get(i);
            JPanel itemRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemRow.setBackground(Color.BLACK);

            JLabel itemLabel = new JLabel(item + " – $" + price);
            itemLabel.setForeground(Color.WHITE);
            itemRow.add(itemLabel);

            JComboBox<Integer> ratingBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            itemRow.add(ratingBox);

            ratingBox.addActionListener(e -> itemRatings.put(item, (Integer) ratingBox.getSelectedItem()));
            mainPanel.add(itemRow);
        }

        mainPanel.add(Box.createVerticalStrut(10));
        JTextField commentField = new JTextField("Your review...");
        mainPanel.add(commentField);

        JComboBox<Integer> starRatingBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JLabel moodLabel = new JLabel("🙂");
        moodLabel.setForeground(Color.ORANGE);
        starRatingBox.addActionListener(e -> {
            int r = (int) starRatingBox.getSelectedItem();
            moodLabel.setText(switch (r) {
                case 5 -> "🤩";
                case 4 -> "😊";
                case 3 -> "😐";
                case 2 -> "🫤";
                default -> "😬";
            });
        });

        JPanel moodRow = new JPanel();
        moodRow.setBackground(Color.BLACK);
        moodRow.add(new JLabel("Your Rating:"));
        moodRow.add(starRatingBox);
        moodRow.add(moodLabel);
        mainPanel.add(moodRow);

        JButton save = new JButton("💾 Save");
        save.addActionListener(e -> {
            int rating = (int) starRatingBox.getSelectedItem();
            String comment = commentField.getText();
            reviewsMap.get(restaurantName).add(new Review(currentUser.getUsername(), restaurantName, rating, comment));
            JOptionPane.showMessageDialog(popup, "Thanks for your rating, " + currentUser.getUsername() + "! 🙏");
            popup.dispose();
            frame.dispose();
            showMainUI();
        });

        popup.add(mainPanel, BorderLayout.CENTER);
        popup.add(save, BorderLayout.SOUTH);
        popup.setVisible(true);
    }

    public static void main(String[] args) {
        new RestaurantReviewSystem();
    }
}
