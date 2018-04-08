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

    String LIMITKEY = "LIMITKEY";
    String LISTKEY = "LISTKEY";
    String INITIALLIMIT = "10";
    String INITIALSECTION = "money";
}
