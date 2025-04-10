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

    public String getRestaurant() {
        return restaurant;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
