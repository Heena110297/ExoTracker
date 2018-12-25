package com.example.dell.exotracker;

/**
 * Created by Dell on 20-03-2018.
 */

public class Requests {
    public String pd;
    public String dt;
    public String serv;
    public String time;
    public String address;


    public Requests(){}

    public Requests(String probdes,String category,String date,String time,String address)
    {
        this.pd=probdes;
        this.serv=category;
        this.dt=date;
        this.time=time;
        this.address = address;
    }

    public String getProbdes() {
        return pd;
    }

    public String category() {
        return serv;
    }

    public String date() {
        return dt;
    }
    public String time() {
        return time;
    }
    public String address() {return address;}
}

