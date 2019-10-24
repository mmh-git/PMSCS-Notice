package com.juniv.pmscsnotice;

public class Notice {
    private String URL;
    private String Title;
    private int Discussion;
    private int File;
    public  Notice(String URL,String Title,String Discussion)
    {
        this.URL=URL;
        this.Title=Title;
        this.Discussion=  Integer.parseInt(Discussion);
        this.File=this.Discussion+1;
    }
    public  Notice(String URL)
    {
        this.URL=URL;

    }
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getDiscussion() {
        return Discussion;
    }

    public void setDiscussion(int discussion) {
        Discussion = discussion;
    }

    public int getFile() {
        return File ;
    }


}
