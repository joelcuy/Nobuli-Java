package com.example.nobulijava.model;

import java.io.Serializable;

public class NewsObj implements Serializable {
    private String title;
    private String content;
    private String datePosted;
    private String newsID;

    public NewsObj(String title, String content, String datePosted) {
        this.title = title;
        this.content = content;
        this.datePosted = datePosted;
    }

    public NewsObj() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    @Override
    public String toString() {
        return "NewsObj{" +
                "title='" + title + '\'' +
                ", datePosted='" + datePosted + '\'' +
                ", content='" + content + '\'' +
                ", newsID='" + newsID + '\'' +
                '}';
    }
}
