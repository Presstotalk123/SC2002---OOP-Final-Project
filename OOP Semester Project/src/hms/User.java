package hms;

public abstract class User {
    protected String userId;
    protected String password;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public abstract void login();
}

