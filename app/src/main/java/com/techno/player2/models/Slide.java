package com.techno.player2.models;

public class Slide  {
    private String Image;
    private String Title;
    private String Poster;
    private String SiteLink;
    private int drawable;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public Slide(String  image, String title,String poster,String site) {
        Image = image;
        Title = title;
        Poster=poster;
        SiteLink=site;
    }
    public Slide(int drawable){
        this.drawable=drawable;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPoster() {
        return Poster;
    }

    public String getSiteLink() {
        return SiteLink;
    }
}
