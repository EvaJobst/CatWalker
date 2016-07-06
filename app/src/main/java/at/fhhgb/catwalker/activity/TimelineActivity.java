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
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.TypefaceSpan;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;
import at.fhhgb.catwalker.fragment.FragmentAllPosts;
import at.fhhgb.catwalker.fragment.FragmentMyPosts;

public class TimelineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PropertyChangeListener {
    LocalData data;
    private FragmentPagerAdapter adapter;
    private ViewPager viewPager;
    private TextView drawerUsername, drawerCat, drawerUniversity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Custom Font for Title
        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new TypefaceSpan(this, "Champagne_Limousines-Thick.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);

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
            // Sets up Navigation Header and its TextView-contents
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
            public void onDrawerOpened(View drawerView) {}

            @Override
            public void onDrawerClosed(View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    /**
     * Closes Navigation Drawer on Back-Button pressed
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
     * Inflates the main menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle navigation view item clicks
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

            default: {
                Toast.makeText(TimelineActivity.this, "Unknown Drawer ID selected", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Catwalker","Restart Timeline");
        DataModel model = ServiceLocator.getDataModel();
        LocalData data = model.getLocalData();
        data.restorePreferences(this);
        data.resetTimeline(false);
        data.resetTimeline(true);
        model.addAllListeners(data.getUniversityId());
    }

    @Override
    protected void onStop() {
        Log.d("Catwalker","Stop Timeline");
        DataModel model = ServiceLocator.getDataModel();
        LocalData data = model.getLocalData();
        model.removeAllListeners(data.getUserId(), data.getUniversityId());
        super.onStop();
    }

    /**
     * Handles changes in the username, university name and cat name
     * @param event
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName()=="user" && event.getOldValue().equals(data.getUserId())){
            if(drawerUsername!=null)
                drawerUsername.setText((String)event.getNewValue());
        }else if(event.getPropertyName()=="university.change") {
            if(drawerCat!=null)
                drawerCat.setText((String)event.getNewValue());
            if(drawerUniversity!=null)
                drawerUniversity.setText((String)event.getNewValue());
        }
    }
}
