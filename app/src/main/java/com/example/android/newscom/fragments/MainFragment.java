package com.example.android.newscom.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newscom.News;
import com.example.android.newscom.NewsAdapter;
import com.example.android.newscom.NewsLoader;
import com.example.android.newscom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Constant value for the news loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;
    /**
     * URL for news data from the Guardian API
     */
    private static final String GUARDIANAPI_REQUEST_URL =
            "http://content.guardianapis.com/search";

    /**
     * Adapter for the news items
     */
    private NewsAdapter adapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyView;

    /**
     * View for the loading indicator
     */
    private ProgressBar loadingIndicator;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Find a reference to the {@link RecyclerView} in the layout
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);

        // Set the layoutManager on the {@link RecyclerView}
        recyclerView.setLayoutManager(layoutManager);

        // Find a reference to the {@link ProgressBar} in the layout
        loadingIndicator = rootView.findViewById(R.id.loading_indicator);

        // Find a reference to the {@link TextView} in the layout
        emptyView = rootView.findViewById(R.id.empty_view);

        // Create a new adapter that takes an empty list of news as input
        adapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        // Set the adapter on the {@link recyclerView}
        recyclerView.setAdapter(adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        // If there is a network connection, fetch data
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyView.setText(R.string.no_internet_connection);
        }

        return rootView;
    }

    @NonNull
    @Override
    // onCreateLoader insanities and returns a new Loader for the given ID
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // GetString retrieves a String value from rhe preferences. The second parameter is the default value for this preference
        String limitItems = sharedPreferences.getString(getString(R.string.settings_number_of_news_key), getString(R.string.settings_number_of_news_default));

        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_number_of_news_default));
        // Parse breaks apart the URI string that' s passed into its parameter
        Uri baseUri = Uri.parse(GUARDIANAPI_REQUEST_URL);

        // BuildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the 'format=geojson'
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", limitItems);
        uriBuilder.appendQueryParameter("q", "economy");
        uriBuilder.appendQueryParameter("api-key", "test");

        // Return the complete uri
        return new NewsLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsItems) {

        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous news data
        adapter.clearAll();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (newsItems != null && !newsItems.isEmpty()) {
            adapter.addAll(newsItems);
        } else {
            // Set empty state text to display "No news items found."
            emptyView.setText(R.string.no_data_available);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clearAll();
    }
}