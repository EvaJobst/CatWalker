package at.fhhgb.catwalker.activity;

import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.controller.TimelineController;
import at.fhhgb.catwalker.fragment.FragmentAllPosts;
import at.fhhgb.catwalker.fragment.FragmentMyPosts;

public class TimelineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TimelineController controller;
    private FragmentPagerAdapter adapter;
    private ViewPager viewPager;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.str_nav_drawer_open, R.string.str_nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimelineActivity.this, NewPostActivity.class);
                startActivity(i);
            }
        });

        // Create the adapter that will return a fragment for each section
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new FragmentAllPosts(),
                    new FragmentMyPosts(),
            };
            private final String[] mFragmentNames = new String[] {
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
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i("Toggle", String.valueOf(id));

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_new_entry : {
                Intent i = new Intent(this, NewPostActivity.class);
                startActivity(i);
            } break;

            case R.id.nav_gallery : {
                Toast.makeText(TimelineActivity.this, "TODO: - Gallery - Activity", Toast.LENGTH_SHORT).show();
            } break;

            case R.id.nav_map : {
                Intent i = new Intent(this, SightingsActivity.class);
                startActivity(i);
            } break;

            case R.id.nav_settings : {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
            } break;

            case R.id.nav_help : {
                Toast.makeText(TimelineActivity.this, "TODO: - Help - Activity", Toast.LENGTH_SHORT).show();
            } break;

            default : {
                Toast.makeText(TimelineActivity.this, "Unknown Drawer ID selected", Toast.LENGTH_SHORT).show();
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
