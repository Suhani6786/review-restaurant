package p1;

public class Reviewer {
    public String username;
    private String password;

    public Reviewer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String u, String p) {
        return username.equals(u) && password.equals(p);
    }

    public String toString() {
        return username;
    }
}
