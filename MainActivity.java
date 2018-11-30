package com.funkymaster.yalmazhasanbutt.firebaseandapiscrud;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    protected static FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private Button email_login_btn;
    private Button email_sign_up_btn;
    static FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniVariables();
        buttonWorkings();
    }

    private  void iniVariables(){
        mAuth = FirebaseAuth.getInstance();
        emailField = (EditText)findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        email_login_btn = (Button) findViewById(R.id.login_btn);
        email_sign_up_btn = (Button) findViewById(R.id.email_signup_btn);
    }

    private void email_login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseUser = mAuth.getCurrentUser();
                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed. "+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void buttonWorkings(){
        email_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailField.getText().toString().equalsIgnoreCase("") || passwordField.getText().toString().equalsIgnoreCase(""))
                    Toast.makeText(MainActivity.this, "fill in both fields!", Toast.LENGTH_SHORT).show();
                else
                    email_login(emailField.getText().toString(), passwordField.getText().toString());
            }
        });

        email_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.dialog_email_signup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText email = (EditText) promptsView.findViewById(R.id.email_dialog_field);
                final EditText new_passcode = (EditText) promptsView.findViewById(R.id.new_passcode_dialogfield);
                final EditText again_passcode = (EditText) promptsView.findViewById(R.id.again_passcode_dialogField);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)

                        .setPositiveButton("register",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (email.getText().toString().equalsIgnoreCase("") ||
                                                new_passcode.getText().toString().equalsIgnoreCase("") ||
                                                again_passcode.getText().toString().equalsIgnoreCase("")) {
                                            Toast.makeText(MainActivity.this, "All Fields are required!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if(new_passcode.getText().toString().equals(again_passcode.getText().toString())){
                                                System.out.println("Mars!");
                                                email_sign_up(email.getText().toString(), new_passcode.getText().toString());
                                            }else{
                                                Toast.makeText(MainActivity.this,"Passwords do not match!",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentFirebaseUser){
        if(currentFirebaseUser!=null) {
            //already signed in
            Intent crud_intent = new Intent(MainActivity.this, CRUDActivity.class);
            crud_intent.putExtra("Firebase_User", currentFirebaseUser);
            Toast.makeText(MainActivity.this, "sign in success!", Toast.LENGTH_LONG).show();
            firebaseUser = currentFirebaseUser;
            startActivity(crud_intent);
        }
    }

    private void email_sign_up(String email, String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                         //   Log.d("creation_success", "createUserWithEmail:success");
                            Toast.makeText(MainActivity.this, "signup success!.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed. "+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

}
