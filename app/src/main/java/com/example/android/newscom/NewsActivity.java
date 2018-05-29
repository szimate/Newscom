package com.example.android.newscom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class NewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ViewPager to view transitions between screens
     */
    private ViewPager viewPager;
    /**
     * DrawerLayout to view the news sections in the menu
     */
    private DrawerLayout drawerLayout;

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

        // Find a reference to the {@link ViewPager} in the layout
        viewPager = findViewById(R.id.viewpager);

        // Find a reference to the {@link TabLayout} in the layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        // Set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Find a reference to the {@link NavigationView} in the layout
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set category fragment pager adapter
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, getSupportFragmentManager());
        // Set the pager adapter onto the view pager
        viewPager.setAdapter(categoryAdapter);
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
        // Handle navigation view item clicks here
        int id = item.getItemId();
        // Switch Fragments in a ViewPager on clicking items in Navigation Drawer
        if (id == R.id.nav_home) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_world) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_science) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_sport) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_environment) {
            viewPager.setCurrentItem(4);
        } else if (id == R.id.nav_business) {
            viewPager.setCurrentItem(5);
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
