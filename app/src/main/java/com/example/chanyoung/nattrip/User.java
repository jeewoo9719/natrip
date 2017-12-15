package com.example.chanyoung.nattrip;

/**
 * Created by CHANYOUNG on 2017-11-17.
 */

public class User {
    private String userName;
    private String userID;
    private String userPW;
    private String language;
    public int guide = 0;
    private String email;
    private  String userPlace;

    public User(){
    }

    public User(String userId, String userPW,String userName, String email){
        this.userID =  userId;
        this.userPW = userPW;
        this.userName = userName;
        this.email = email;
    }

    public String getUserID(){ return userID;}
    public void setUserID(String userId){ this.userID = userId;}

    public String getUserName(){ return userName;}
    public void setUserName(String userName){ this.userName = userName;}

    public String getUserPW(){ return userPW;}
    public void setUserPW(String userName){ this.userPW = userPW;}

    public String getUserLanguage(){ return language;}
    public void setUserLanguage(String language){ this.language = language;}

    public String getUserEmail(){ return email;}
    public void setUserEmail(String language){ this.email = email;}

    public String  getUserPlace(){return  userPlace;}
    public void setUserPlace(String place){this.userPlace=place;}


}
