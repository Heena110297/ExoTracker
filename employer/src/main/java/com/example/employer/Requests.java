package com.example.employer;

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

    public Requests(String pd,String serv,String dt,String time,String address)
    {
        this.pd=pd;
        this.serv=serv;
        this.dt=dt;
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
