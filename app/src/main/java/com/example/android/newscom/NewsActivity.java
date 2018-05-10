package com.example.android.newscom;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = NewsActivity.class.getName();

    /**
     * URL for news data from the Guardian API
     */
    private static final String GUARDIANAPI_REQUEST_URL =
            "http://content.guardianapis.com/search?q=debates&api-key=test";

    /**
     * Constant value for the news loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    private Context context;
    /**
     * RecyclerView to view the news
     */
    private RecyclerView recyclerView;

    /**
     * Adapter for the news items
     */
    private RecyclerView.Adapter adapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyView;

    /**
     * View for the loading indicator
     */
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find a reference to the {@link TextView} in the layout
        emptyView = (TextView) findViewById(R.id.empty_view);

        // Find a reference to the {@link ProgressBar} in the layout
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        // Find a reference to the {@link RecyclerView} in the layout
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Use a {@link LinearLayoutManager} for the {@link RecyclerView}
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create a new adapter that takes an empty list of news items as input
        adapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link RecyclerView}
        // so the list can be populated in the user interface
        recyclerView.setAdapter(adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            Log.i(LOG_TAG, "Test:Earthquake initLoader() called");

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyView.setText(R.string.no_internet_connection);
        }


    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, GUARDIANAPI_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsItems) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        if (newsItems.isEmpty()) {
            // Set empty state text to display "No news items found."
            emptyView.setText(R.string.no_data_available);
        } else {
            adapter = new NewsAdapter(this, newsItems);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {


    }
}
