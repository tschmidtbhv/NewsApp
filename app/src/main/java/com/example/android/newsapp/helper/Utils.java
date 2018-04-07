package com.example.android.newsapp.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.preference.PreferenceManager;

import com.example.android.newsapp.data.Article;
import com.example.android.newsapp.data.Section;
import com.example.android.newsapp.data.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public final class Utils {


    /**
     * Check the Connection to the internet
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        return isConnected;
    }

    /**
     * Make URL Object from
     * given url String
     *
     * @param url URLString
     * @return URL Object
     */
    public static URL makeURL(String url) {

        URL urlObj = null;
        if (url != null && url.length() > 0) {
            try {
                urlObj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return urlObj;
    }

    /**
     * Fire an HttpRequest to given URL
     *
     * @param url Website URL Obj
     * @return InputStream
     * @throws IOException
     */
    public static String makeHTTPRequest(URL url) throws IOException {

        String jsonString = "";

        if (url == null) return jsonString;

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(Config.CONNECTTIMEOUT);
            httpURLConnection.setReadTimeout(Config.READTIMEOUT);
            httpURLConnection.setRequestMethod(Config.REQUESTMODE);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                jsonString = makeJSONFromInputStream(inputStream);
            }
        } finally {

            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return jsonString;
    }

    /**
     * Get JSON from the InputStream
     *
     * @param inputStream
     * @return JSONString
     * @throws IOException
     */
    private static String makeJSONFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Config.CHARSET);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();
                while (line != null) {
                    output.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return output.toString();
    }

    /**
     * Create Section from JSON
     * @param jsonString Section as jsonString
     * @return List<Section> List of Sectionitems
     * @throws JSONException
     */
    public static List<Section> createSectionsFromJson(String jsonString) throws JSONException {
        List<Section> sectionList = new ArrayList<>();

        if (jsonString != null) {
            JSONArray results = getResults(jsonString);

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String id = result.getString("id");
                String webTitle = result.getString("webTitle");

                Section section = new Section(id, webTitle);
                sectionList.add(section);
            }
        }

        return sectionList;
    }

    /**
     * Extract Articles from JsonString
     *
     * @param jsonString JSON Response
     * @return Article List
     */
    public static List<Article> createAriclesFromJson(String jsonString) throws JSONException {

        List<Article> articleList = new ArrayList<>();

        if (jsonString != null) {


            JSONArray results = getResults(jsonString);

            for (int i = 0; i < results.length(); i++) {

                JSONObject result = results.getJSONObject(i);
                String sectionName = result.getString("sectionName");
                String webPublicationDate = result.getString("webPublicationDate");
                String webTitle = result.getString("webTitle");
                String webUrl = result.getString("webUrl");

                //Article can have multiple contributor
                List<Tag> tagList = new ArrayList<>();

                JSONArray tags = result.optJSONArray("tags");
                if (tags != null) {
                    for (int y = 0; y < tags.length(); y++) {
                        JSONObject tag = tags.getJSONObject(y);
                        String firstName = tag.optString("firstName");
                        String lastName = tag.optString("lastName");

                        if (firstName != null && lastName != null) {
                            tagList.add(new Tag(firstName, lastName));
                        }
                    }
                }
                articleList.add(new Article(sectionName, webPublicationDate, webTitle, webUrl, tagList));
            }

        }

        return articleList;
    }

    /**
     * Get Results Array from JSON
     *
     * @param jsonString Serverresponse as JSONString
     * @return JSONArray with Results
     * @throws JSONException
     */
    private static JSONArray getResults(String jsonString) throws JSONException {
        JSONObject jsonRoot = new JSONObject(jsonString);
        JSONObject response = jsonRoot.getJSONObject("response");
        JSONArray results = response.getJSONArray("results");
        return results;
    }

    /**
     * Check is posible to open Intent
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * Check was Section loaded before
     * @param context Act Context
     * @return boolean isLoaded or not
     */
    public static boolean hasSectionsLoaded(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(Config.SECTIONLOADED, false);
    }

    /**
     * Save SectionData to prefs
     * @param data Section
     */
    public static void saveToPreferences(Context context, List<?> data) {

        String jsonString = new Gson().toJson(data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor =  sharedPreferences.edit();

        editor.putString(Config.SECTIONJSON,jsonString);
        editor.putBoolean(Config.SECTIONLOADED, true);
        editor.apply();
    }

    public static List<Section> getSavedSections(Context context){


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(Config.SECTIONJSON,"");
        Gson gson = new Gson();

        TypeToken<List<Section>> token = new TypeToken<List<Section>>(){};
        List<Section> sectionsList = gson.fromJson(json,token.getType());
        return sectionsList;
    }
}
