package com.funkymaster.yalmazhasanbutt.firebaseandapiscrud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CRUDActivity extends AppCompatActivity {

    TextView user_view;
    Button signout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        iniVariables();

        buttonWorkings();
    }

    private void iniVariables(){
        user_view =  findViewById(R.id.user_txt);
        user_view.setText("Welcome : "+MainActivity.firebaseUser.getEmail().toString());
        signout_btn =  findViewById(R.id.signout_btn);

    }

    private void buttonWorkings(){
        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mAuth.getInstance().signOut();
                startActivity(new Intent(CRUDActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}
