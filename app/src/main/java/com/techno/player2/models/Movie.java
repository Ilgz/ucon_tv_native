package com.techno.player2.models;



public class Movie  {



    private String title;
    private  String  thumbnail;
    private   String siteLink;
    private String details;


    public Movie(String title,  String thumbnail,String siteLink,String details) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.siteLink=siteLink;
        this.details=details;
    }

public Movie(String title,  String thumbnail,String siteLink) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.siteLink=siteLink;
    }public Movie(String title,  String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
    }











    public String getTitle() {
        return title;
    }



    public String getThumbnail() {
        return thumbnail;
    }


    public String getSiteLink() {
        return siteLink;
    }

    public String getDetails() {
        return details;
    }
}
