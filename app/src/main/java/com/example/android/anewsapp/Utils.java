package com.example.android.anewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dam on 03.07.2017.
 */

public class Utils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();
    // Constant used to check items to be displayed : title, author and webUrlField
    static String TITLE_KEY = "webTitle";
    static String SECTION_KEY = "sectionId";
    static String WEBURL_KEY = "webUrl";

    /**
     * Query the Books API dataset and return an {@link NewsData} object to represent a single news data.
     */
    public static List<NewsData> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link NewsData} object
        List<NewsData> news = extractFeatureFromJson(jsonResponse);

        // Return the {@link NewsData}

        Log.d(LOG_TAG, "*** news= ******" + news);
        return news;
    }

    /*
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news api JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.d(LOG_TAG, "*****jsonResponse*******" + jsonResponse);
        return jsonResponse;
    }

    /*
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /*
     * Return an {@link NewsData} object by parsing out information
     * about the first news from the input newsApiJSON string.
     */
    private static ArrayList<NewsData> extractFeatureFromJson(String newsApiJSON) {
        ArrayList<NewsData> listOfNews = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsApiJSON)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(newsApiJSON);
            // If there are "results" in the response create an array
            if (baseJsonResponse.has("response")) {

                JSONObject hasResponse = new JSONObject(newsApiJSON);

                if (hasResponse.has("results")) {

                    JSONArray itemsArray = baseJsonResponse.getJSONArray("results");


                    // Retrieve all items what are needed to be shown
                    for (int i = 0; i < itemsArray.length(); i++) {

                        // Extract out the first feature (which is a news info)
                        JSONObject properties = itemsArray.getJSONObject(i);
                        // JSONObject properties = itemsRow.getJSONObject("results");


                        String title = " ";
                        // Extract out the title, author, and webUrlField  values
                        if (properties.has(TITLE_KEY)) {
                            if (!properties.getString(TITLE_KEY).isEmpty()) {
                                title = properties.getString(TITLE_KEY);
                                Log.d(LOG_TAG, "******title=" + title);
                            }
                        } else title = "N/A";

                        String sectionToSearch = " ";
                        if (properties.has(SECTION_KEY)) {
                            if (!properties.getString(SECTION_KEY).isEmpty()) {
                                sectionToSearch = properties.getString(SECTION_KEY);

                            }
                        } else sectionToSearch = "N/A";

                        String webUrlField = " ";
                        if (properties.has(WEBURL_KEY)) {
                            if (!properties.getString(WEBURL_KEY).isEmpty()) {
                                webUrlField = properties.getString(WEBURL_KEY);
                            }
                        } else webUrlField = "N/A";

                        // Create a new {@link NewsData} object
                        NewsData newsObject = new NewsData(title, sectionToSearch, webUrlField);
                        listOfNews.add(newsObject);
                        Log.d(LOG_TAG, "listOfNews=" + listOfNews);
                    }
                }

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        // return null;

        return listOfNews;
    }
}
