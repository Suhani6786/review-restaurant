package p1;

import java.util.*;

public class Reviewer {
    String username;
    String password;
    private List<Review> reviews = new ArrayList<>();

    public Reviewer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void writeReview(String restaurant, int rating, String comment) {
        reviews.add(new Review(restaurant, rating, comment));
    }

    public List<Review> getReviews() {
        return reviews;
    }
}

