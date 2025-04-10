package p1;

public class Reviewer {
    private final String username;
    private final String password;

    public Reviewer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
}
