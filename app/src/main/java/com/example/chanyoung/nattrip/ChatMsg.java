package com.example.chanyoung.nattrip;

/**
 * Created by CHANYOUNG on 2017-11-26.
 */

public class ChatMsg {
    private String userName;
    private String message;
    private String time;

    public ChatMsg(){
    }
    public ChatMsg(String userName, String message, String time){
        this.userName = userName;
        this.message = message;
        this.time = time;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }
}
