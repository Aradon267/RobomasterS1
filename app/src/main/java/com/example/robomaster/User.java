package com.example.robomaster;

/**
 * The class will hold the information of the user.
 * username - will hold the name of the user.
 * password - will hold the password of the user.
 */

public class User {

    public String username;
    public String password;

    /**
     * An empty constructor to construct an empty user
     */
    public User(){}

    /**
     * A regular constructor of the user
     * @param name the username of the user
     * @param pass the password of the user
     */
    public User(String name, String pass){
        this.username = name;
        this.password = pass;
    }

    /**
     * The function will return the password of the user
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * The function will set the password of the user
     * @param password the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The function will return the username of the user
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * The function will set the username of the user
     * @param username the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The function will return the information of the user as a string
     * @return the string representing the user
     */
    public String toString(){
        return "Username: " + this.username + " Password: " + this.password;
    }
}
