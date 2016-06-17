package at.fhhgb.catwalker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.new_toolbar_bottom);

        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager mgr = getFragmentManager();
                FragmentTransaction ft = mgr.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.new_tab_info: {
                        ft.replace(R.id.new_fragment, new FragmentInfo());
                    } break;

                    case R.id.new_tab_location: {
                        ft.replace(R.id.new_fragment, new FragmentLocation());
                    } break;

                    case R.id.new_tab_picture: {
                        ft.replace(R.id.new_fragment, new FragmentPicture());
                    } break;
                }

                ft.commit();
                return true;
            }
        });

        // Inflate a menu to be displayed in the toolbar
        toolbarBottom.inflateMenu(R.menu.new_menu_bottom);

        Toolbar send = (Toolbar) findViewById(R.id.new_toolbar_send);
        send.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.new_tab_send) {
                    Toast.makeText(NewEntryActivity.this, "SEND", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        send.inflateMenu(R.menu.new_menu_send);
    }
}