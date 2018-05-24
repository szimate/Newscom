package com.example.android.newscom;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LoaderCallbacks<List<News>> {

    /**
     * URL for news data from the Guardian API
     */
    private static final String GUARDIANAPI_REQUEST_URL =
            "http://content.guardianapis.com/search";

    /**
     * Constant value for the news loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * DrawerLayout to view the news sections in the menu
     */
    private DrawerLayout drawerLayout;
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
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link Toolbar} in the layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find a reference to the {@link DrawerLayout} in the layout
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Find a reference to the {@link TextView} in the layout
        emptyView = findViewById(R.id.empty_view);

        // Find a reference to the {@link ProgressBar} in the layout
        loadingIndicator = findViewById(R.id.loading_indicator);

        // Find a reference to the {@link RecyclerView} in the layout
        recyclerView = findViewById(R.id.recycler_view);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            View loadingIndicator = findViewById(R.id.loading_indicator);

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    // onCreateLoader insanities and returns a new Loader for the given ID
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from rhe preferences. The second parameter is the default value for this preference
        String limitItems = sharedPreferences.getString(getString(R.string.settings_number_of_news_key), getString(R.string.settings_number_of_news_default));

        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_number_of_news_default));
        // parse breaks apart the URI string that' s passed into its parameter
        Uri baseUri = Uri.parse(GUARDIANAPI_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", limitItems);
        uriBuilder.appendQueryParameter("q", "economy");
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsItems) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No news items found."


        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (newsItems != null && !newsItems.isEmpty()) {
            adapter = new NewsAdapter(this, newsItems);
            recyclerView.setAdapter(adapter);
        } else {
            emptyView.setText(R.string.no_data_available);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
    }

    @Override
    //This method initialize the contents of the Activity's option menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.string.title_home:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
            default:
                Toast.makeText(getApplicationContext(), "else", Toast.LENGTH_SHORT).show();

                return true;
        }
    }
}
