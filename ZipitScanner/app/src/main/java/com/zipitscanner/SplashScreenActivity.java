package com.zipitscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Splash Screen for the App
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * onCreate that initializes the app db and sets the UI
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        TextView scanner = (TextView) findViewById(R.id.scanner);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/BeforeTheRain.ttf");
        scanner.setTypeface(custom_font1);

        RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.myanim);
        ranim.setFillAfter(true);
        scanner.setAnimation(ranim);

        ImageView splashImg = (ImageView) findViewById(R.id.splash_img);
        splashImg.setImageResource(R.drawable.food_bag);

        TextView title = (TextView) findViewById(R.id.zipittext);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/FasterOne.ttf");
        title.setTypeface(custom_font);
        new gatherSplashInfo().execute();
    }

    private class gatherSplashInfo extends AsyncTask<Void, Void, Void> {

        /**
         * Asynk Task to display the background
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
         * Moves onto the next activity
         *
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            Intent loginActivity = new Intent(SplashScreenActivity.this, ScanActivity.class);
            startActivity(loginActivity);

        }
    }
}
