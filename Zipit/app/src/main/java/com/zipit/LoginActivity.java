package com.zipit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * An  full-screen Splash Screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailinput;
    private EditText passwordInput;
    private Button loginbutton;
    private Button signupbutton;
    private int authenticated;
    private Button alreadyregistered;
    private Button forgotpasswordbutton;
    private Button signup;
    private FirebaseUser myuser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor editor;

    /**
     * Creates and sets the UI and database initializations for the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        TextView title = (TextView) findViewById(R.id.Zip_it);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/FasterOne.ttf");
        title.setTypeface(custom_font);

        authenticated = 0;
        emailinput = (EditText) findViewById(R.id.emailinput);
        assert emailinput != null;
        emailinput.clearComposingText();
        final int viewwidth = emailinput.getWidth();

        passwordInput = (EditText) findViewById(R.id.loginpassword);

        loginbutton = (Button) findViewById(R.id.loginbutton);

        signupbutton = (Button) findViewById(R.id.signupbutton);
        assert signupbutton != null;
        signupbutton.animate()
                .translationX(viewwidth)
                .alpha(0.0f)
                .setDuration(1);

        signupbutton.setVisibility(View.GONE);

        alreadyregistered = (Button) findViewById(R.id.alreadyregistered);
        assert alreadyregistered != null;
        alreadyregistered.animate()
                .translationX(viewwidth)
                .alpha(0.0f)
                .setDuration(1);

        alreadyregistered.setVisibility(View.GONE);

        forgotpasswordbutton = (Button) findViewById(R.id.forgotpasswordbutton);

        signup = (Button) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbutton.animate()
                        .translationX(-(loginbutton.getWidth()))
                        .alpha(0.0f)
                        .setDuration(400);
                forgotpasswordbutton.animate()
                        .translationX(-(forgotpasswordbutton.getWidth()))
                        .alpha(0.0f)
                        .setDuration(400);
                signup.animate()
                        .translationX(-(signup.getWidth()))
                        .alpha(0.0f)
                        .setDuration(400);
                signupbutton.animate()
                        .translationX(-(viewwidth))
                        .alpha(1.0f)
                        .setDuration(400);
                alreadyregistered.animate()
                        .translationX(-(viewwidth))
                        .alpha(1.0f)
                        .setDuration(400);
                signupbutton.setVisibility(View.VISIBLE);
                alreadyregistered.setVisibility(View.VISIBLE);
            }
        });


        alreadyregistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbutton.animate()
                        .translationX((viewwidth))
                        .alpha(1.0f)
                        .setDuration(400);
                forgotpasswordbutton.animate()
                        .translationX((viewwidth))
                        .alpha(1.0f)
                        .setDuration(400);
                signup.animate()
                        .translationX((viewwidth))
                        .alpha(1.0f)
                        .setDuration(400);

                signupbutton.animate()
                        .translationX((viewwidth))
                        .alpha(0.0f)
                        .setDuration(400);
                alreadyregistered.animate()
                        .translationX((viewwidth))
                        .alpha(0.0f)
                        .setDuration(400);
                signupbutton.setVisibility(View.GONE);
                alreadyregistered.setVisibility(View.GONE);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login(emailinput.getText().toString(), passwordInput.getText().toString());

            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount(emailinput.getText().toString(), passwordInput.getText().toString());


            }
        });
        forgotpasswordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword(emailinput.getText().toString());
            }
        });
    }

    /**
     * Sends verification email to the
     *
     * @param email
     * @param password
     */
    private void validateAccount(String email, String password) {
        if (!validate())
            return;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Verification Email Sent!!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    /**
     * initiates forgot Password if user forgets password
     *
     * @param email
     */
    private void forgotPassword(String email) {
        if (!validateForgotPassword()) {
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Password Reset Email Sent",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    /**
     * Returns true if form is valid
     *
     * @return
     */
    private boolean validateForgotPassword() {
        boolean valid = true;
        String email = emailinput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailinput.setError("Required");
            valid = false;
        } else {
            emailinput.setError(null);
        }
        return valid;
    }


    /**
     * Creates a new user with these below credentials
     *
     * @param email
     * @param password
     */
    private void createAccount(final String email, final String password) {

        if (!validate()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign Up Successful",
                                    Toast.LENGTH_LONG).show();

                            loginPreferences = getSharedPreferences("Login", 0);
                            editor = loginPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();
                            validateAccount(email, password);
                        }
                    }

                });
    }

    /**
     * User Login Authentication
     */

    private void login(final String email, final String password) {
        if (!validate()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            loginPreferences = getSharedPreferences("Login", 0);
                            editor = loginPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainLandActivity.class);
                            intent.putExtra("emailId", email);
                            startActivity(intent);
                        }
                    }
                });
    }


    /**
     * Validating the form input
     *
     * @return
     */
    private boolean validate() {
        boolean valid = true;

        String email = emailinput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailinput.setError("Required");
            valid = false;
        } else {
            emailinput.setError(null);
        }

        String password = passwordInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Required");
            valid = false;
        } else {
            passwordInput.setError(null);
        }
        return valid;
    }


    /*
    * Instead of returning to Splash Screen, hide the app
    * */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /*
    * Make sure the database calls halt when the app is stopped for resource consumption
    * */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
