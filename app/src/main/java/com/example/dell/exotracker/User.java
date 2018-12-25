package com.example.dell.exotracker;

/**
 * Created by Dell on 16-03-2018.
 */

public class User {

    public String emailId;
    public String name;
    public String address;

    public User(){}

    public User(String emailId,String name,String address)
    {
        this.emailId=emailId;
        this.name=name;
        this.address=address;
    }
}
