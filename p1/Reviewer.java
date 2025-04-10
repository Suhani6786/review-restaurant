package p1;

import java.util.ArrayList;

public class Reviewer {
    public String username;
    private String password;
    private ArrayList<Review> reviews = new ArrayList<>();

    public Reviewer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void writeReview(String restaurant, int rating, String comment) {
        reviews.add(new Review(restaurant, rating, comment));
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
