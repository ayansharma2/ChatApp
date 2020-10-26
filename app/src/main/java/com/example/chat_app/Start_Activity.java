package com.example.chat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Start_Activity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_);
        sharedPreferences =getSharedPreferences("Noicee",MODE_PRIVATE);
        String isUser=sharedPreferences.getString("isUser","No");
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(isUser.equals("Yes"))
        {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent=new Intent(getApplicationContext(),Log_in.class);
            startActivity(intent);
            finish();
        }
    }
}