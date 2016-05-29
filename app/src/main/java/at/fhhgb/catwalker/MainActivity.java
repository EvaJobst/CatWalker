package at.fhhgb.catwalker;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Open -New Entry- Activity", Toast.LENGTH_SHORT).show();
            }
        });

        //TimelineArray timeline = new TimelineArray(this);
        //timeline.add(new TimelineEntry("New Image", "Campuskater gesichtet!", R.drawable.pusheen));
        //timeline.add(new TimelineEntry("New Image", "Campuskater derzeit im Studentenwohnheim", R.drawable.pusheen));
        //timeline.add(new TimelineEntry("New Image", "Derzeit schläft der Kater im Labor", R.drawable.pusheen));
        //this.setListAdapter(timeline);
    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
