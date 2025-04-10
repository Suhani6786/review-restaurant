package p1;

public class Review {
    private final String username;
    private final String restaurant;
    private final int rating;
    private final String comment;

    public Review(String username, String restaurant, int rating, String comment) {
        this.username = username;
        this.restaurant = restaurant;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }
}
