package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chat_app.Fragment.Chats;
import com.example.chat_app.Fragment.UserProfile;
import com.example.chat_app.Fragment.Users;
import com.google.android.material.tabs.TabLayout;
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

import static com.example.chat_app.R.menu.menu;

public class MainActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.user_name);
        toolbar=findViewById(R.id.toolbar);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                username.setText(user.name);
                if(user.getImage_URL().equals("default"))
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                else
                    Glide.with(getApplicationContext()).load(user.getImage_URL()).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new Users(),"Users");
        viewPagerAdapter.AddFragment(new Chats(),"Chats");
        viewPagerAdapter.AddFragment(new UserProfile(),"Profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.inflateMenu(menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.log_out:
                        status("offline");
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences sharedPreferences=getSharedPreferences("Noicee",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("isUser","No");
                        editor.commit();
                        Intent intent=new Intent(getApplicationContext(),Log_in.class);
                        startActivity(intent);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        ArrayList<Fragment> fragments;
        ArrayList<String> title;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.title=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return title.size();
        }
        public void AddFragment(Fragment fragment, String title)
        {
            fragments.add(fragment);
            this.title.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
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
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}