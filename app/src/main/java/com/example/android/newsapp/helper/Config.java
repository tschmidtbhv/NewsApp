package com.example.android.newsapp.helper;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public final class Config {

    public final static int LOADERID = 1;
    public final static String REQUESTMODE = "GET";
    public final static int RESPONSEOK = 200;
    public final static String CHARSET = "UTF-8";

    //Guardian Url ordered by newest include contributor
    public final static String GUARDIANURL = "http://content.guardianapis.com/lifeandstyle?order-by=newest&show-tags=contributor&page-size=20&api-key=test";
}
