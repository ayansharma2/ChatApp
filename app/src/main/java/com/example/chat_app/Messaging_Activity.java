package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chat_app.Adapter.MessageAdapter;
import com.example.chat_app.Model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messaging_Activity extends AppCompatActivity {
   RecyclerView recyclerView;
   ArrayList<Chat> chats;
   Toolbar toolbar;
   String uid;
   EditText message;
   ImageButton send_button;
   CircleImageView profile_pic;
   TextView user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_);

    }

    @Override
    protected void onResume() {
        chats=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_view);
        message=findViewById(R.id.message);
        send_button=findViewById(R.id.send_button);
        toolbar=findViewById(R.id.toolbar);
        profile_pic=findViewById(R.id.profile_image);
        user_name=findViewById(R.id.user_name);
        super.onResume();
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent=getIntent();
        uid=intent.getStringExtra("userid");
        Log.i("userid",uid);


        DatabaseReference userDetail=FirebaseDatabase.getInstance().getReference("Users");
        userDetail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    User user=dataSnapshot.getValue(User.class);
                    if(user.getUser_ID().equals(uid)){
                        Log.i("Username",user.getName());
                        user_name.setText(user.getName());
                        if(user.getImage_URL().equals("default"))
                            profile_pic.setImageResource(R.mipmap.ic_launcher);
                        else
                            Glide.with(getApplicationContext()).load(user.getImage_URL()).into(profile_pic);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error fetching Details",Toast.LENGTH_SHORT).show();
            }
        });


        status("online");


        DatabaseReference profile_pic=FirebaseDatabase.getInstance().getReference("Users");
        profile_pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image_URL = null;
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    User user=dataSnapshot.getValue(User.class);
                    if(user.getUser_ID().equals(uid))
                        image_URL=user.getImage_URL();
                }
                readMessage(image_URL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(final String image_URL) {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        chats.clear();
        final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if((chat.getReceiver().equals(uid) && chat.getSender().equals(FirebaseAuth.getInstance().getUid())) || (chat.getSender().equals(FirebaseAuth.getInstance().getUid()) && chat.getReceiver().equals(uid)))
                    chats.add(chat);
                }

                MessageAdapter messageAdapter=new MessageAdapter(getApplicationContext(),chats,image_URL);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void send_message(View view) {
        if (!message.getText().toString().equals("")) {
            Chat chat = new Chat(message.getText().toString(), uid, FirebaseAuth.getInstance().getCurrentUser().getUid());
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
            databaseReference.push().setValue(chat);
            message.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Message can't be Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void status(String status)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("user_status",status);
        reference.updateChildren(hashMap);
    }


    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}