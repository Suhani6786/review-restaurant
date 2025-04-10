package p1;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RestaurantReviewSystem {
    private JFrame frame;
    private Reviewer currentUser = new Reviewer("userxxx233");
    private HashMap<String, List<Review>> reviewsMap = new HashMap<>();
    private HashMap<String, String> logoPaths = new HashMap<>();
    private HashMap<String, List<String>> topItems = new HashMap<>();
    private HashMap<String, List<String>> topPrices = new HashMap<>();

    private final String[] restaurants = {
        "McDonald's 🍔", "Pizza Hut 🍕", "Starbucks ☕", "Taco Bell 🌮", "Subway 🥪"
    };

    public RestaurantReviewSystem() {
        initData();
        showMainUI();
    }

    private void initData() {
        logoPaths.put("McDonald's 🍔", "images/mcdonalds.png");
        logoPaths.put("Pizza Hut 🍕", "images/pizzahut.png");
        logoPaths.put("Starbucks ☕", "images/starbucks.png");
        logoPaths.put("Taco Bell 🌮", "images/tacobell.png");
        logoPaths.put("Subway 🥪", "images/subway.png");

        topItems.put("McDonald's 🍔", Arrays.asList("🍟 Fries", "🍔 Big Mac", "🥤 Coke", "🍦 McFlurry", "🍗 Nuggets"));
        topPrices.put("McDonald's 🍔", Arrays.asList("$2.49", "$5.99", "$1.99", "$2.79", "$3.49"));

        topItems.put("Pizza Hut 🍕", Arrays.asList("🍕 Pepperoni Pizza", "🧀 Cheese Sticks", "🥗 Veggie Pizza", "🥤 Pepsi", "🍪 Cookie"));
        topPrices.put("Pizza Hut 🍕", Arrays.asList("$5.89", "$4.99", "$9.49", "$1.79", "$5.49"));

        topItems.put("Starbucks ☕", Arrays.asList("☕ Latte", "🍪 Cookie", "🥐 Croissant", "🍰 Cake Pop", "🧋 Cold Brew"));
        topPrices.put("Starbucks ☕", Arrays.asList("$4.25", "$2.25", "$3.95", "$2.95", "$3.75"));

        topItems.put("Taco Bell 🌮", Arrays.asList("🌮 Taco", "🌯 Burrito", "🍟 Nacho Fries", "🧀 Quesadilla", "🥤 Baja Blast"));
        topPrices.put("Taco Bell 🌮", Arrays.asList("$1.79", "$4.99", "$2.49", "$3.79", "$1.99"));

        topItems.put("Subway 🥪", Arrays.asList("🥪 Italian BMT", "🥗 Veggie Delite", "🍪 Cookie", "🧀 Tuna Sub", "🥤 Lemonade"));
        topPrices.put("Subway 🥪", Arrays.asList("$6.49", "$5.49", "$1.29", "$6.99", "$1.99"));

        generateFakeReviews();
    }

    private void generateFakeReviews() {
        String[] fakeUsers = {"@cheeeseloverrxx123", "@icedLattexx678", "@saucysnackerxx234", "@veggiequeenxx987", "@tacogawdxx765"};
        String[][] sampleComments = {
            {"Fries 🔥", "Big Mac hit", "Classic Coke", "Flurry is life", "Crunchy nugs!"},
            {"So cheesy!", "Tasty crust", "Fresh veggies", "Pepsi power", "Cookie is life"},
            {"Love lattes", "Sweet cookie!", "Yummy croissant", "Cutest cake pop", "Cold brew supremacy"},
            {"Taco = 🔥", "Big fan of burritos", "Nacho hit", "So cheesy", "Baja is life"},
            {"Footlong wins", "Veggie life 🥗", "Cookies always hit 😋", "Tuna is 🔥", "Fresh lemonade"}
        };

        for (int i = 0; i < restaurants.length; i++) {
            List<Review> reviewList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                int rating = new Random().nextInt(5) + 1;
                reviewList.add(new Review(restaurants[i], rating, sampleComments[i][j], fakeUsers[j], false));
            }
            reviewsMap.put(restaurants[i], reviewList);
        }
    }

    private void showMainUI() {
        frame = new JFrame("🍽️ Restaurant Review System");
        frame.setSize(550, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.BLACK);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.BLACK);
        topBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField searchBar = new JTextField(" Search...");
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Sort by", "Name A-Z", "Rating High-Low", "Rating Low-High"});

        topBar.add(searchBar, BorderLayout.WEST);
        topBar.add(sortBox, BorderLayout.EAST);

        JPanel restaurantPanel = new JPanel();
        restaurantPanel.setLayout(new BoxLayout(restaurantPanel, BoxLayout.Y_AXIS));
        restaurantPanel.setBackground(Color.BLACK);

        for (String name : restaurants) {
            restaurantPanel.add(createCard(name));
        }

        JScrollPane scrollPane = new JScrollPane(restaurantPanel);
        scrollPane.setBorder(null);

        frame.add(topBar, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createCard(String restaurantName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(500, 90));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(new Color(40, 40, 40));
        try {
            JLabel logo = new JLabel(new ImageIcon(logoPaths.get(restaurantName)));
            left.add(logo);
        } catch (Exception e) {
            left.add(new JLabel("❓"));
        }

        JLabel title = new JLabel(restaurantName);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel stars = getStars(getAvgRating(restaurantName));
        JLabel sample = new JLabel("\"" + reviewsMap.get(restaurantName).get(0).getComment() + "\"");
        sample.setForeground(Color.LIGHT_GRAY);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(40, 40, 40));
        center.add(title);
        center.add(stars);
        center.add(sample);

        JPanel right = new JPanel();
        right.setBackground(new Color(40, 40, 40));
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        edit.addActionListener(e -> showEditPopup(restaurantName));
        right.add(edit);
        right.add(delete);

        card.add(left, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private int getAvgRating(String restaurant) {
        List<Review> reviews = reviewsMap.get(restaurant);
        return (int) reviews.stream().mapToInt(Review::getRating).average().orElse(0);
    }

    private JLabel getStars(int rating) {
        String stars = "★★★★★".substring(0, rating) + "☆☆☆☆☆".substring(0, 5 - rating);
        JLabel label = new JLabel(stars);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(rating >= 4 ? Color.YELLOW : Color.LIGHT_GRAY);
        return label;
    }

    private void showEditPopup(String restaurant) {
        JDialog popup = new JDialog(frame, "Edit Review – " + restaurant, true);
        popup.setSize(500, 600);
        popup.setLocationRelativeTo(frame);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel yourRatingLabel = new JLabel("Your Rating:");
        yourRatingLabel.setForeground(Color.WHITE);
        panel.add(yourRatingLabel);

        JComboBox<Integer> ratingBox = new JComboBox<>(new Integer[]{1,2,3,4,5});
        panel.add(ratingBox);

        panel.add(Box.createVerticalStrut(10));
        JLabel commentLabel = new JLabel("Comment:");
        commentLabel.setForeground(Color.WHITE);
        panel.add(commentLabel);

        JTextArea commentArea = new JTextArea(3, 20);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        panel.add(commentArea);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("🔥 Rate Top Items:")).setForeground(Color.WHITE);

        List<String> items = topItems.get(restaurant);
        List<String> prices = topPrices.get(restaurant);
        for (int i = 0; i < items.size(); i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setBackground(Color.BLACK);
            JLabel itemLabel = new JLabel(items.get(i) + " – " + prices.get(i));
            itemLabel.setForeground(Color.WHITE);
            JSlider slider = new JSlider(1, 5, 3);
            slider.setMajorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            row.add(itemLabel);
            row.add(slider);
            panel.add(row);
        }

        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(0, 153, 76));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            reviewsMap.get(restaurant).removeIf(r -> r.getUsername().equals(currentUser.getUsername()));
            String comment = commentArea.getText().trim();
            int stars = (int) ratingBox.getSelectedItem();
            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(popup, "Comment can't be empty!");
                return;
            }
            reviewsMap.get(restaurant).add(new Review(restaurant, stars, comment, currentUser.getUsername(), true));
            JOptionPane.showMessageDialog(popup, "Thank you for your rating! 🙏");
            popup.dispose();
            frame.dispose();
            new RestaurantReviewSystem();
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(saveBtn);

        panel.add(Box.createVerticalStrut(20));
        JLabel others = new JLabel("💬 Others' Reviews:");
        others.setForeground(Color.WHITE);
        panel.add(others);

        for (Review r : reviewsMap.get(restaurant)) {
            if (!r.getUsername().equals(currentUser.getUsername())) {
                JPanel box = new JPanel();
                box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
                box.setBackground(Color.DARK_GRAY);
                JLabel user = new JLabel(r.getUsername());
                user.setForeground(Color.CYAN);
                JLabel star = getStars(r.getRating());
                JLabel text = new JLabel("\"" + r.getComment() + "\"");
                text.setForeground(Color.LIGHT_GRAY);
                box.setBorder(new EmptyBorder(5, 10, 5, 10));
                box.add(user);
                box.add(star);
                box.add(text);
                panel.add(Box.createVerticalStrut(5));
                panel.add(box);
            }
        }

        popup.add(new JScrollPane(panel));
        popup.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RestaurantReviewSystem::new);
    }
}
