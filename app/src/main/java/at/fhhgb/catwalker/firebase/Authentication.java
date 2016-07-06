package at.fhhgb.catwalker.firebase;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Handles the anonymous firebase authentication.
 */
public class Authentication {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authListener;
    private boolean isLoggedIn = false;

    /**
     * Initializes the Authentication state listener.
     */
    public Authentication() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    isLoggedIn = true;
                    Log.d("Firebase_Auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Firebase_Auth", "onAuthStateChanged:signed_out");
                    isLoggedIn = false;
                }
            }
        };
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    /**
     * Tries to sign in the anonymous user.
     * @param view the activity in which a toast will be created if the authentication fails.
     */
    public void signIn(final Activity view){
        auth.signInAnonymously()
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase_Auth", "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Firebase_Auth", "signInAnonymously", task.getException());
                            Toast.makeText(view, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        startListener();
    }

    public void startListener() {
        auth.addAuthStateListener(authListener);
    }

    public void removeListener() {
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
