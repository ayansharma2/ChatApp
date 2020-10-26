package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_in extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView name,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        password=findViewById(R.id.password);
    }

    public void register(View view) {
        final String image="default";
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful())
             {
                 Toast.makeText(getApplicationContext(),"Created Successfully",Toast.LENGTH_LONG).show();
                 final User user=new User(name.getText().toString(),email.getText().toString(),password.getText().toString(),image,firebaseAuth.getUid(),"offline",name.getText().toString().toLowerCase());
                 databaseReference.child(firebaseAuth.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful())
                         {
                             Toast.makeText(getApplicationContext(),"Please Login",Toast.LENGTH_LONG).show();
                             Intent intent=new Intent(getApplicationContext(),Log_in.class);
                             startActivity(intent);
                         }
                         else
                         {
                             Toast.makeText(getApplicationContext(),"Error Registering",Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
            }
        });
    }
}