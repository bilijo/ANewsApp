package com.example.android.anewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dam on 05.07.2017.
 */

public class NewsDataAdapter extends ArrayAdapter<NewsData> {

    //Allows access to application-specific resources and classes, as well as up-calls
    private Context context;

    // Constructor
      /* @param context The current context. Used to inflate the layout file.
      * @param newsDatasArrayList A List of NewsData objects to display in a list
     */
    public NewsDataAdapter(Context context, ArrayList<NewsData> newsDatasArrayList) {
        super(context, 0, newsDatasArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            // use the layout of the data items to be displayed
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_news_data, parent, false);
        }
        // Get the {@link NewsData} object located at this position in the list
        NewsData currentNewsData = getItem(position);

        // Add news title with getter getmBookTitle()
        TextView titleTexView = (TextView) listItemView.findViewById(R.id.text_title_news);
        titleTexView.setText(currentNewsData != null ? currentNewsData.getmNewsTitle() : null);

        // Add news author with getter getmAuthor()
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.text_author_news);
        authorTextView.setText(currentNewsData.getmSectionName());

        // Add news publisher with getter getmPublisher()
        TextView publisherTextView = (TextView) listItemView.findViewById(R.id.text_publisher_news);
        publisherTextView.setText(currentNewsData.getmWebUrl());
        // return the populated listView to show in the UI
        return listItemView;
    }
}
