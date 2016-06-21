package at.fhhgb.catwalker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import at.fhhgb.catwalker.controller.RegisterController;

public class RegisterActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RegisterController controller = new RegisterController(this);
        if(controller.restorePreferences()){
            navigateToTimeline();
        }

        setContentView(R.layout.activity_register);

        controller.initUniversityList();

        Button register  = (Button) findViewById(R.id.registerUser);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controller.storePreferences()){
                    navigateToTimeline();
                }
            }
        });

    }


    public void navigateToTimeline() {
        intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }
}
