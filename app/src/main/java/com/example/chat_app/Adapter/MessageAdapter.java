package com.example.chat_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_app.Model.Chat;
import com.example.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context context;
    ArrayList<Chat> chats;
    String image_URL;

    public MessageAdapter(Context context,ArrayList<Chat> chats,String image_URL)
    {
        this.context=context;
        this.chats=chats;
        this.image_URL=image_URL;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.show_message.setText(chats.get(position).getMessage());
        if(image_URL!=null)
        {if(!image_URL.equals("default"))
            Glide.with(context).load(image_URL).into(holder.profile_image);}
        else
            Toast.makeText(context," null",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid()))
            return 1;
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image;
        TextView show_message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            show_message=itemView.findViewById(R.id.show_message);
        }
    }
}
