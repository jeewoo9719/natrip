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
    private int startyear;
    private int startmonth;
    private int endyear;
    private int endmonth;

    Tour(){}
    Tour(String guideID, String name,  String  detail, String  place, int sy, int  sm, int ey, int em){
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
}
