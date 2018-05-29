package com.example.android.newscom.fragments;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import com.example.android.newscom.News;
import com.example.android.newscom.NewsLoader;
import com.example.android.newscom.R;

import java.util.List;

/**
 * A simple {@link WorldFragment} is a subclass of {@link MainFragment}.
 */
public class WorldFragment extends MainFragment {

    private static final String GUARDIANAPI_REQUEST_URL =
            "http://content.guardianapis.com/search";

    public WorldFragment() {
        // Required empty public constructor
    }


    @NonNull
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // getString retrieves a String value from rhe preferences. The second parameter is the default value for this preference
        String limitItems = sharedPreferences.getString(getString(R.string.settings_number_of_news_key), getString(R.string.settings_number_of_news_default));

        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_number_of_news_default));
        // parse breaks apart the URI string that' s passed into its parameter
        Uri baseUri = Uri.parse(GUARDIANAPI_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the 'format=geojson'
        uriBuilder.appendQueryParameter("section", "world");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", limitItems);
        uriBuilder.appendQueryParameter("q", "economy");
        uriBuilder.appendQueryParameter("api-key", "test");

        // Return the complete uri
        return new NewsLoader(getActivity(), uriBuilder.toString());

    }

}
