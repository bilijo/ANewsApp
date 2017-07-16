package com.example.android.anewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
//import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.android.anewsapp.R.id.listView_news;
import static com.example.android.anewsapp.Utils.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    // Variable to use in the saving state method
    static final String SEARCH_NAME = "";

    public String stringToSearch = "";

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    // url to perform search book by author name
    //public String guardian_api_url = "http://content.guardianapis.com/search?section=science&q=science&api-key=51352e97-5002-4c5d-a466-10788a261e6a";
    public String guardian_api_url = "http://content.guardianapis.com/search?q=" ;
   String keyUser = "&api-key=51352e97-5002-4c5d-a466-10788a261e6a";

    // variable which will concatenate the url abobe with the author name given by the user
    String guardian_news_api_url2;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    // Make adapter and listview instances as global variable
    private NewsDataAdapter mAdapter;
    private ListView newsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;

        // hide keyboard on the UI device then didn't needed
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Create a reference to the {@link listView_news} in the layout
        newsListView = (ListView) findViewById(listView_news);
        // Create a new adapter instance that takes an empty list of news as input
        mAdapter = new NewsDataAdapter(this, new ArrayList<NewsData>());

                //Check connection
                if (checkNetworkConnection()) {

     guardian_news_api_url2 = "http://content.guardianapis.com/search?section=science&api-key=51352e97-5002-4c5d-a466-10788a261e6a";


Toast.makeText(MainActivity.this, guardian_news_api_url2, LENGTH_SHORT).show();

                        /******************************** begin loader *********************************/
                        // Get a reference to the LoaderManager, in order to interact with loaders.
                        LoaderManager loaderManager = getLoaderManager();

                        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                        // because this activity implements the LoaderCallbacks interface).
                        loaderManager.restartLoader(NEWS_LOADER_ID, null, MainActivity.this).forceLoad();



                } else {
                    //newsListView.setVisibility(View.INVISIBLE);
                    mEmptyStateTextView = (TextView) findViewById(R.id.text_emptyView);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }


                // Set an item click listener on the ListView, which sends an intent to a web browser
                // to open a website with more information about the selected news.
                newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        // Find the current earthquake that was clicked on
                        NewsData currentNewsData = mAdapter.getItem(position);

                        // Convert the String URL into a URI object (to pass into the Intent constructor)
                        Uri newsUri = Uri.parse(currentNewsData.getmWebUrl());

                        // Create a new intent to view the earthquake URI
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                        // Send the intent to launch a new activity
                        startActivity(websiteIntent);
                    }
                });





        // Set the adapter on the {@link ListView}
        // so the list can be diplayed in the user interface
        newsListView.setAdapter(mAdapter);
    }

    private boolean checkNetworkConnection() {

        Log.d("Main","checkNetworkConnection=");
        // Query the active network and determine if it has Internet connectivity.
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    @Override
    public Loader<List<NewsData>> onCreateLoader(int i, Bundle bundle) {

        Log.d(LOG_TAG ,"initLoader"+ bundle);

        // Create a new loader for the given URL
        return new NewsLoader(this, guardian_news_api_url2);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> newsDatasArrayList) {
        // Clear the adapter of previous News data
        mAdapter.clear();
        mEmptyStateTextView = (TextView) findViewById(R.id.text_emptyView);
        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsDatasArrayList != null && !newsDatasArrayList.isEmpty()) {
            mEmptyStateTextView.setVisibility(View.GONE);
            mAdapter.addAll(newsDatasArrayList);
        } else {
            // Create a reference to the emptyView
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            newsListView.setEmptyView(mEmptyStateTextView);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.d(LOG_TAG ,"onLoaderReset" + this);
        mAdapter.clear();
    }

}
