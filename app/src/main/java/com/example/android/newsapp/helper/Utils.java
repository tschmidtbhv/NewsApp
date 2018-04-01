package com.example.android.newsapp.helper;

import android.util.Log;

import com.example.android.newsapp.data.Article;
import com.example.android.newsapp.data.Tag;

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
import java.util.List;

/**
 * NewsApp
 * Created by Thomas Schmidt on 01.04.2018.
 */
public final class Utils {

    /**
     * Make URL Object from
     * given url String
     * @param url URLString
     * @return URL Object
     */
    public static URL makeURL(String url){

        URL urlObj = null;
        if(url != null && url.length() > 0) {
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
     * @param url Website URL Obj
     * @return InputStream
     * @throws IOException
     */
    public static String makeHTTPRequest(URL url) throws IOException {

        String jsonString = "";

        if(url == null) return jsonString;

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection =  (HttpURLConnection )url.openConnection();
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setReadTimeout(1500);
            httpURLConnection.setRequestMethod(Config.REQUESTMODE);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == Config.RESPONSEOK) {
                inputStream = httpURLConnection.getInputStream();
                jsonString = makeJSONFromInputStream(inputStream);
            }
        } finally {

            if(httpURLConnection != null) httpURLConnection.disconnect();
            if(inputStream != null) inputStream.close();
        }

        return jsonString;
    }

    /**
     * Get JSON from the InputStream
     * @param inputStream
     * @return JSONString
     * @throws IOException
     */
    private static String makeJSONFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if(inputStream != null) {

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Config.CHARSET);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();
                while (line != null) {
                    output.append(line);
                    line = bufferedReader.readLine();
                }
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }

        return output.toString();
    }

    /**
     * Extract Articles from JsonString
     * @param jsonString JSON Response
     * @return Article List
     */
    public static List<Article> createAriclesFromJson(String jsonString){

        List<Article> articleList = new ArrayList<>();

        if(jsonString != null){

            try {
                JSONObject jsonRoot = new JSONObject(jsonString);
                JSONObject response = jsonRoot.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");

                for(int i = 0; i < results.length(); i++){

                    JSONObject result = results.getJSONObject(i);
                    String sectionName = result.getString("sectionName");
                    String webPublicationDate = result.getString("webPublicationDate");
                    String webTitle = result.getString("webTitle");
                    String webUrl = result.getString("webUrl");

                    //Article can have multiple contributor
                    List<Tag> tagList = new ArrayList<>();

                    JSONArray tags = result.optJSONArray("tags");
                    if(tags != null){
                        for(int y = 0; y < tags.length(); y++){
                            JSONObject tag = tags.getJSONObject(y);
                            String firstName = tag.optString("firstName");
                            String lastName = tag.optString("lastName");

                            if(firstName != null && lastName != null){
                                tagList.add(new Tag(firstName, lastName));
                            }
                        }
                    }
                    articleList.add(new Article(sectionName, webPublicationDate, webTitle,webUrl,tagList));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return articleList;
    }
}
