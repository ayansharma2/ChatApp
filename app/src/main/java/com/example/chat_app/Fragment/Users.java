package com.example.chat_app.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chat_app.Adapter.UserAdapter;
import com.example.chat_app.R;
import com.example.chat_app.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Users extends Fragment {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    EditText search_users;
    ArrayList<User> users;
    TextView no_users;
    LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recycler_vew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        users=new ArrayList<>();
        linearLayout=view.findViewById(R.id.linear_layout);
        no_users=view.findViewById(R.id.no_users);
        search_users=view.findViewById(R.id.search_users);
        readUsers();
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void searchUsers(String s) {
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    User user=dataSnapshot.getValue(User.class);
                    if(!user.getUser_ID().equals(FirebaseAuth.getInstance().getUid()))
                        users.add(user);
                }
                UserAdapter userAdapter=new UserAdapter(getContext(),users);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        final List<User> userList=new ArrayList<>();
            final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList.clear();
                    if(search_users.getText().toString().isEmpty())
                    {

                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            User user=dataSnapshot.getValue(User.class);
                            if(!user.getUser_ID().equals(FirebaseAuth.getInstance().getUid()))
                            userList.add(user);
                        }

                        userAdapter=new UserAdapter(getContext(),userList);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}