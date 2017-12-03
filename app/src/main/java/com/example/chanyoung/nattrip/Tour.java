package com.example.chanyoung.nattrip;

/**
 * Created by CHANYOUNG on 2017-12-01.
 */

public class Tour {
    private String tourname;
    private String tourDetail;
    private String guideID;

    private String tourplace;
    private String startyear;
    private String startmonth;
    private String endyear;
    private String endmonth;

    //사진
    private String image1FilePath;
    private String image2FilePath;
    private String image3FilePath;

    //예약한 사람 ID
    private  String reserveUserID;


    Tour(){}
    Tour(String guideID, String name,  String  detail, String  place, String sy, String  sm, String ey, String em, String image1FilePath, String image2FilePath, String image3FilePath){
        this.guideID=guideID;
        this.tourname=name;
        this.tourplace=place;
        this.tourDetail=detail;
        this.startyear=sy;
        this.startmonth=sm;
        this.endyear=ey;
        this.endmonth=em;
        this.image1FilePath=image1FilePath;
        this.image2FilePath=image2FilePath;
        this.image3FilePath=image3FilePath;
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

    public  String getImage1FilePath(){return image1FilePath;}
    public void setImage1FilePath(String image1FilePath){this.image1FilePath=image1FilePath;}

    public  String getImage2FilePath(){return image2FilePath;}
    public void setImage2FilePath(String image2FilePath){this.image2FilePath=image2FilePath;}

    public  String getImage3FilePath(){return image3FilePath;}
    public void setImage3FilePath(String image3FilePath){this.image1FilePath=image3FilePath;}

    public  String getReserveUserID(){return reserveUserID;}
    public void setReserveUserID(String reserveUserID){this.reserveUserID=reserveUserID;}

}
