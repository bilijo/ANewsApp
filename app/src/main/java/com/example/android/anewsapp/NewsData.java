package com.example.android.anewsapp;

/**
 * Created by dam on 05.07.2017.
 */

public class NewsData {
    // Variables of the NewsData class which will be displayed
    private String mNewsTitle;
    private String mSectionName;
    private String mWebUrl;

    // Constructor of the NewsData class
    public NewsData(String vTitle,String vSectionName,String vWebUrl) {
        mNewsTitle = vTitle;
        mSectionName = vSectionName;
        mWebUrl = vWebUrl;
    }

    // getters
    public String getmNewsTitle() {
        return mNewsTitle;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }
}
