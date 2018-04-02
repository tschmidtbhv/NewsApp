package com.example.android.newsapp.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public String getFormatedPublicationDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("EE dd MMM yyyy", Locale.ENGLISH);

        try {
            Date newsDate = dateFormatter.parse(getWebPublicationDate());
            return dateFormatter2.format(newsDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getWebPublicationDate();
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
