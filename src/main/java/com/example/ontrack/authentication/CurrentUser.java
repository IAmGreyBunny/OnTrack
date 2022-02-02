package com.example.ontrack.authentication;

public class CurrentUser {
    private User user;
    private final static CurrentUser INSTANCE = new CurrentUser();

    private CurrentUser(){}//Prevents creation of any more instances

    public static CurrentUser getInstance()
    {
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
