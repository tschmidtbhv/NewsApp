package com.example.android.newsapp.Loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.newsapp.helper.Config;
import com.example.android.newsapp.helper.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public class DataLoader extends AsyncTaskLoader<List<?>> {

    private boolean mLoadSection;
    private Context context;

    public DataLoader(@NonNull Context context, boolean loadSection) {
        super(context);
        mLoadSection = loadSection;
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<?> loadInBackground() {

        List<?> dataList = new ArrayList<>();

        URL url;
        if (mLoadSection) {
            url = Utils.makeURL(Utils.getBuildedSectionURL());
        } else {
            String sectionId = Utils.getValueForSavedKey(context, Config.LISTKEY);
            String limit = Utils.getValueForSavedKey(context, Config.LIMITKEY);

            url = Utils.makeURL(Utils.getBuildedURLForSettings(sectionId, limit));
        }

        String jsonString;

        try {
            jsonString = Utils.makeHTTPRequest(url);
            if (mLoadSection) {
                dataList = Utils.createSectionsFromJson(jsonString);
            } else {
                dataList = Utils.createAriclesFromJson(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
