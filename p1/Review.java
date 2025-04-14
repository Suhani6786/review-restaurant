package p1;

public class Review {
    private String restaurant;
    private int rating;
    private String comment;
    private String username;
    private boolean editable;

    public Review(String restaurant, int rating, String comment, String username, boolean editable) {
        this.restaurant = restaurant;
        this.rating = rating;
        this.comment = comment;
        this.username = username;
        this.editable = editable;
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

    public String getUsername() {
        return username;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
