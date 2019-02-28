package com.zipit;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class LoginTests {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Test
    public void addition_isCorrect() throws Exception {
        mAuth = FirebaseAuth.getInstance();
        String email = "ajaka@illinois.edu";
        String password = "ksjdhfgk";
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            assertEquals(true, true);
                        } else {
                            assertEquals(true, false);
                        }
                    }
                });
    }
}