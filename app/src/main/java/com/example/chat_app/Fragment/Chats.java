package com.example.chat_app.Fragment;

import android.accounts.AbstractAccountAuthenticator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chat_app.Adapter.UserAdapter;
import com.example.chat_app.Model.Chat;
import com.example.chat_app.R;
import com.example.chat_app.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chats extends Fragment {
    List<String> users;
    RecyclerView recyclerView;
    ArrayList<User> user_list;
    TextView no_one;
    private static ArrayList<String> chat_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        user_list=new ArrayList<>();
        chat_user=new ArrayList<>();
        no_one=view.findViewById(R.id.no_one);
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               chat_user.clear();
               for(DataSnapshot dataSnapshot:snapshot.getChildren())
               {

                   Chat chat=dataSnapshot.getValue(Chat.class);
                   if(chat.getSender().equals(fuser.getUid()))
                   {
                       chat_user.add(chat.getReceiver());
                   }
                   else if (chat.getReceiver().equals(fuser.getUid()))
                   {chat_user.add(chat.getSender());}
               }
               readUsers();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void readUsers() {
        final DatabaseReference database=FirebaseDatabase.getInstance().getReference("Users");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user_list.clear();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        User user=dataSnapshot.getValue(User.class);
                        for(String id:chat_user)
                            if(user.getUser_ID().equals(id))
                                user_list.add(user);
                    }
                    ArrayList<User> newList = new ArrayList<>();
                    for (User element : user_list) {

                        if (!newList.contains(element)) {

                            newList.add(element);
                        }
                    }

                    UserAdapter userAdapter=new UserAdapter(getContext(),newList);
                    if(userAdapter.getItemCount()==0)
                        no_one.setVisibility(View.VISIBLE);
                    else
                        no_one.setVisibility(View.GONE);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }


}