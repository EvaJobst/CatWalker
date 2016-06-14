package at.fhhgb.catwalker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "TODO: Open -New Entry- Activity", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.str_nav_drawer_open, R.string.str_nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

        //TimelineArray timeline = new TimelineArray(this);
        //timeline.add(new TimelineEntry("New Image", "Campuskater gesichtet!", R.drawable.pusheen));
        //timeline.add(new TimelineEntry("New Image", "Campuskater derzeit im Studentenwohnheim", R.drawable.pusheen));
        //timeline.add(new TimelineEntry("New Image", "Derzeit schläft der Kater im Labor", R.drawable.pusheen));
        //this.setListAdapter(timeline);


    //@Override
    //protected void onListItemClick(ListView l, View v, int pos, long id) {
    //    ListAdapter data = l.getAdapter();
    //    TimelineEntry element = (TimelineEntry) data.getItem(pos);
    //    Toast.makeText(this, "selected element " + element, Toast.LENGTH_SHORT).show();
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_settings: {
                Toast.makeText(MainActivity.this, "TODO: Move to Navigation Drawer", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            } //break;
            default : {Toast.makeText(MainActivity.this, "Unknown ActionBar ID selected", Toast.LENGTH_SHORT).show();}
        }

        return super.onOptionsItemSelected(item);

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_new_entry : {
                Toast.makeText(MainActivity.this, "TODO: - New Entry - Activity", Toast.LENGTH_SHORT).show();
            } break;

            case R.id.nav_gallery : {
                Toast.makeText(MainActivity.this, "TODO: - Gallery - Activity", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "TODO: - Help - Activity", Toast.LENGTH_SHORT).show();
            } break;

            default : {
                Toast.makeText(MainActivity.this, "Unknown Drawer ID selected", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
