
package com.amcoder.dto;
public class User 
{
    public String username;
    public String password;
    public int userscore;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserscore() {
        return userscore;
    }

    public void setUserscore(int userscore) {
        this.userscore = userscore;
    }

    public User(String username, String password, int userscore) {
        this.username = username;
        this.password = password;
        this.userscore = userscore;
    }
    
}
