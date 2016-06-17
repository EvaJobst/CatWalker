package at.fhhgb.catwalker;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class NewEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.new_bottom_menu);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_tab_info: {
                        Toast.makeText(NewEntryActivity.this, "Info", Toast.LENGTH_SHORT).show();
                    } break;

                    case R.id.new_tab_location: {
                        Toast.makeText(NewEntryActivity.this, "Location", Toast.LENGTH_SHORT).show();
                    } break;

                    case R.id.new_tab_picture: {
                        Toast.makeText(NewEntryActivity.this, "Picture", Toast.LENGTH_SHORT).show();
                    } break;
                }
                return true;
            }
        });

        // Inflate a menu to be displayed in the toolbar
        toolbarBottom.inflateMenu(R.menu.menu_main);
    }
}