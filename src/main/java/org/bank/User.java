package org.bank;

import java.sql.Timestamp;

public class User {
    private static int count = 0;
    private int id;
    private String fullName;

    private Timestamp createAt;
    private String username;
    private String password;

    public User(String username, String password,String fullName) {
        this.id = ++count;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.createAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean existAccount(String username, String password){
        boolean result = false;
        if(this.username.contains(username)&&this.password.contains(password)){
            result = true;
        }
        return result;
    }
    @Override
    public String toString() {
        return "Information Account\n" +
                "Full Name: " + fullName + '\n' +
                "Username: " + username + '\n' +
                "Password " + password;
    }
}
