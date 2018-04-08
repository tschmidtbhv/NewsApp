package com.example.android.newsapp.helper;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public interface Config {

    int SECTIONLOADERID = 1;
    int ARTICLELOADERID = 2;

    String REQUESTMODE = "GET";
    int CONNECTTIMEOUT = 1000;
    int READTIMEOUT = 1500;
    String CHARSET = "UTF-8";

    int NOCONNECTION = 0;
    int NOCONTENT = 1;

    //Guardian Url ordered by newest include contributor
    String GUARDIANURL = "http://content.guardianapis.com/";

    String SECTIONLOADED = "SECTIONLOADED";
    String SECTIONJSON = "SECTIONJSON";

    //Setting Key`S
    String LIMITKEY = "LIMITKEY";
    String LISTKEY = "LISTKEY";
    String INITIALLIMIT = "10";
    String INITIALSECTION = "money";

    //QueryParams
    String SHOWTAGS = "show-tags";
    String CONTRIBUTOR = "contributor";

    String ORDERBY = "order-by";
    String NEWEST = "newest";

    String PAGESIZE = "page-size";

    String APIKEY = "api-key";
    String TEST = "test";

    //JSON TAGS
    String ID = "id";
    String TAGS = "tags";
    String RESPONSE = "response";
    String RESULTS = "results";
    String WEBTITLE = "webTitle";
    String WEBURL = "webUrl";
    String SECTIONNAME = "sectionName";
    String WEBPUBDATE = "webPublicationDate";
    String FIRSTNAME = "firstName";
    String LASTNAME = "lastName";
}
