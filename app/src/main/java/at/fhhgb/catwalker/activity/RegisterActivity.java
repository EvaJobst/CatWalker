package at.fhhgb.catwalker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.TypefaceSpan;
import at.fhhgb.catwalker.controller.RegisterController;
import at.fhhgb.catwalker.firebase.ServiceLocator;

public class RegisterActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Custom Font for Title
        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new TypefaceSpan(this, "Champagne_Limousines-Thick.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);

        final RegisterController controller = new RegisterController(this);
        controller.signIn();
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
