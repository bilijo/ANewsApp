package com.example.android.anewsapp;

/**
 * Created by dam on 05.07.2017.
 */

public class NewsData {
    // Variables of the BooksData class which will be displayed
    private String mBookTitle;
    private String mAuthor;
    private String mPublisher;

    // Constructor of the BooksData class
    public NewsData(String vTitle,String vAuthor,String vPublisher) {
        mBookTitle = vTitle;
        mAuthor = vAuthor;
        mPublisher = vPublisher;
    }

    // getters
    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPublisher() {
        return mPublisher;
    }
}
