package at.fhhgb.catwalker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.TypefaceSpan;
import at.fhhgb.catwalker.controller.RegisterController;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.firebase.DataModel;
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

        // Contains logic for registration
        final RegisterController controller = new RegisterController(this);
        controller.signIn();

        // If user data is available, automatically opens TimelineActivity
        if(controller.restorePreferences()){
            navigateToTimeline();
        }

        // If user data is not available --> opens layout and properly starts Activity
        setContentView(R.layout.activity_register);

        // Contains University List and helps with Autocomplete
        controller.initUniversityList();

        // Saves data to database
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

    @Override
    protected void onStop() {
        Log.d("Catwalker","Stop Register");
        DataModel model = ServiceLocator.getDataModel();
        LocalData data = model.getLocalData();
        model.removeAllListeners(data.getUserId(), data.getUniversityId());
        super.onStop();
    }

     /**
     * Opens TimelineActivity
     */
    public void navigateToTimeline() {
        intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }
}
