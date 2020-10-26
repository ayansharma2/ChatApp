package com.example.chat_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Log_in extends AppCompatActivity {
    EditText email,password;
    Button button;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void log_in(View view) {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    SharedPreferences sharedPreferences=getSharedPreferences("Noicee",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("isUser","Yes");
                    editor.commit();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"Error Logging In",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void newIntent(View view) {
        Intent intent=new Intent(getApplicationContext(),Sign_in.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        startActivity(intent);
    }
}