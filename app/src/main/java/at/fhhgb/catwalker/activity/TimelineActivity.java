package at.fhhgb.catwalker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.controller.TimelineController;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;
import at.fhhgb.catwalker.fragment.FragmentAllPosts;
import at.fhhgb.catwalker.fragment.FragmentMyPosts;

/**
 *
 */
public class TimelineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PropertyChangeListener {
    LocalData data;
    private FragmentPagerAdapter adapter;
    private ViewPager viewPager;
    private FragmentAllPosts fragmentAllPosts;
    private FragmentMyPosts fragmentMyPosts;
    private TextView drawerUsername, drawerCat, drawerUniversity;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applyFontForToolbarTitle(this);

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimelineActivity.this, NewPostActivity.class);
                startActivity(i);
            }
        });

        // Navigation Drawer
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.str_nav_drawer_open, R.string.str_nav_drawer_close);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerUsername = (TextView) drawerView.findViewById(R.id.drawer_username);
                drawerUniversity = (TextView) drawerView.findViewById(R.id.drawer_university);
                drawerCat = (TextView) drawerView.findViewById(R.id.drawer_cat);
                drawerUsername.setText(data.getUser());
                String university = data.getUniversityId();
                drawerUniversity.setText(university);
                drawerCat.setText(data.getUniversityList().get(university));
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
        TODO: Set Username in Navigation Drawer
*/
        View v = getLayoutInflater().inflate(R.layout.nav_header_timeline, (ViewGroup) findViewById(R.id.header_root));
        drawerUsername = (TextView) v.findViewById(R.id.drawer_username);

        // Create the adapter that will return a fragment for each section
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new FragmentAllPosts(),
                    new FragmentMyPosts(),
            };
            private final String[] mFragmentNames = new String[]{
                    "All Entries",
                    "My Entries",
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        data = ServiceLocator.getDataModel().getLocalData();
        data.addPropertyChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.saveState();
    }

    public static void applyFontForToolbarTitle(Activity context){
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        for(int i = 0; i < toolbar.getChildCount(); i++){
            View view = toolbar.getChildAt(i);
            if(view instanceof TextView){
                TextView tv = (TextView) view;
                Typeface titleFont = Typeface.
                        createFromAsset(context.getAssets(), "fonts/Champagne_Limousines-Thick.ttf");
                if(tv.getText().equals(toolbar.getTitle())){
                    tv.setTypeface(titleFont);
                    tv.setTextSize(25);
                    break;
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Inflates the main menu
     *
     * @param menu
     * @return always returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_new_entry: {
                Intent i = new Intent(this, NewPostActivity.class);
                startActivity(i);
            }
            break;

            case R.id.nav_map: {
                Intent i = new Intent(this, SightingsActivity.class);
                startActivity(i);
            }
            break;

            case R.id.nav_settings: {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
            }
            break;

            case R.id.nav_help: {
                Toast.makeText(TimelineActivity.this, "TODO: - Help - Activity", Toast.LENGTH_SHORT).show();
            }
            break;

            default: {
                Toast.makeText(TimelineActivity.this, "Unknown Drawer ID selected", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName()=="user" && event.getOldValue().equals(data.getUserId())){
            drawerUsername.setText((String)event.getNewValue());
        }else if(event.getPropertyName()=="university.change") {
            drawerCat.setText((String)event.getNewValue());
            drawerUniversity.setText((String)event.getNewValue());
        }
    }
}
