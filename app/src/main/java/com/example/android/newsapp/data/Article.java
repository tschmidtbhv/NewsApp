package com.example.android.newsapp.data;

import java.util.List;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public class Article {

    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;

    private List<Tag> tags;

    public Article(String sectionName, String webPublicationDate, String webTitle, String webUrl, List<Tag> tags) {
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.tags = tags;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
