package com.example.chanyoung.nattrip;

/**
 * Created by CHANYOUNG on 2017-12-01.
 */

public class Tour {
    private String tourname;
    private String tourDetail;
    private String guideID;
    //사진
    private String tourplace;
    private String startyear;
    private String startmonth;
    private String endyear;
    private String endmonth;

    Tour(){}
    Tour(String guideID, String name,  String  detail, String  place, String sy, String  sm, String ey, String em){
        this.guideID=guideID;
        this.tourname=name;
        this.tourplace=place;
        this.tourDetail=detail;
        this.startyear=sy;
        this.startmonth=sm;
        this.endyear=ey;
        this.endmonth=em;
    }
    public String getGuideID(){return guideID;}
    public void setGuideID(String guideID){this.guideID=guideID;}

    public String getTourName(){return tourname;}
    public void setTourName(String name){this.tourname=name;}

    public String getDetail(){return tourDetail;}
    public void setDetail(String detail){this.tourDetail=detail;}

    public String getplace(){return tourplace;}
    public void setPlace(String place){this.tourplace=place;}

    public  String getStartyear(){return startyear;}
    public void setStartyear(String sy){this.startyear=sy;}

    public  String getStartmonth(){return startmonth;}
    public void setStartmonth(String sy){this.startmonth=sy;}

    public  String getEndyear(){return endyear;}
    public void setEndyear(String sy){this.endyear=sy;}
    public  String getEndmonth(){return endmonth;}
    public void setEndmonth(String sy){this.endmonth=sy;}


}
