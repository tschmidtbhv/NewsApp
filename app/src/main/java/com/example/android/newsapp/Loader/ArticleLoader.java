package com.example.android.newsapp.Loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.newsapp.data.Article;
import com.example.android.newsapp.helper.Config;
import com.example.android.newsapp.helper.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {


    public ArticleLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {

        List<Article> articleList = new ArrayList<>();

        URL url = Utils.makeURL(Config.GUARDIANURL);
        String jsonString = null;

        try {
            jsonString = Utils.makeHTTPRequest(url);
            articleList = Utils.createAriclesFromJson(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return articleList;
    }
}
