package p1;

public class Review {
    private String restaurant;
    private int rating;
    private String comment;

    public Review(String restaurant, int rating, String comment) {
        this.restaurant = restaurant;
        this.rating = rating;
        this.comment = comment;
    }

    public String getReviewDetails() {
        return restaurant + " | " + rating + " stars | " + comment;
    }
}
