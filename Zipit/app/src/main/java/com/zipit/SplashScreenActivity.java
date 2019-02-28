package com.zipit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Splash Screen for the app. The first screen the user sees when the App is opened.
 */
public class SplashScreenActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String email;
    private String password;

    /**
     * Creates and sets the UI and database initializations for the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);


        ImageView splashImg = (ImageView) findViewById(R.id.splash_img);
        splashImg.setImageResource(R.drawable.food_bag);

        TextView title = (TextView) findViewById(R.id.zipittext);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/FasterOne.ttf");
        title.setTypeface(custom_font);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences loginPreferences = this.getSharedPreferences("Login", 0);
        email = loginPreferences.getString("email", null);
        password = loginPreferences.getString("password", null);


        new gatherSplashInfo().execute();


    }

    private class gatherSplashInfo extends AsyncTask<Void, Void, Void> {

        /**
         * Asynk Task to display and compute the user's credentials along with background
         *
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Re-authorizes the user into the account so the user doesnt have to keep logging in again and again
         *
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
//            Intent loginActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);
//            startActivity(loginActivity);
            if (email == null || password == null) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, password);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SplashScreenActivity.this, MainLandActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
