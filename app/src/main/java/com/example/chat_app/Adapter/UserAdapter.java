package com.example.chat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_app.Messaging_Activity;
import com.example.chat_app.R;
import com.example.chat_app.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {
  Context context;
  List<User> users;


    public UserAdapter(Context context, List<User> userList) {
        this.context=context;
        users=userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
      return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      final User user=users.get(position);
      holder.username.setText(user.getName());
      if(user.getImage_URL().equals("default"))
          holder.profile_image.setImageResource(R.drawable.ic_launcher_background);
      else
          Glide.with(context).load(user.getImage_URL()).into(holder.profile_image);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.i("User",user.getName());
              Intent intent=new Intent(context, Messaging_Activity.class);
              intent.putExtra("userid",user.getUser_ID());
              context.startActivity(intent);
          }
      });
      if(user.getUser_status().equals("online"))
      {
          holder.online.setVisibility(View.VISIBLE);
          holder.offline.setVisibility(View.GONE);
      }
      else
      {
          holder.offline.setVisibility(View.INVISIBLE);
          holder.online.setVisibility(View.GONE);
      }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
      public TextView username;
      public ImageView profile_image;
      CircleImageView online,offline;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_pic);
            online=itemView.findViewById(R.id.online);
            offline=itemView.findViewById(R.id.offline);
       }
   }
}
