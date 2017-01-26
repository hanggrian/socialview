package io.github.hendraanggrian.socialviewdemo.model;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class User {

    private String username;
    private String displayname;
    private String url;

    public User(String username, String displayname, String url) {
        this.username = username;
        this.displayname = displayname;
        this.url = url;
    }
}