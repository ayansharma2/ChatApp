package com.example.chat_app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chat_app.R;
import com.example.chat_app.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class UserProfile extends Fragment {
    TextView user_name,email;
    CircleImageView profile_image;

    StorageReference storageReference;
    Uri filePath;
    Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 22;
    StorageTask uploadTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_profile, container, false);
        user_name=view.findViewById(R.id.user_name);
        profile_image=view.findViewById(R.id.profile_image);
        email=view.findViewById(R.id.email);

        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    User user=dataSnapshot.getValue(User.class);
                    if(user.getUser_ID().equals(firebaseUser.getUid()))
                    {
                        user_name.setText(user.getName());
                        email.setText(user.getEmail());
                        if(user.getImage_URL().equals("default"))
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        else
                            Glide.with(getContext()).load(user.getImage_URL()).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        return view;
    }

    private void SelectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filePath=data.getData();
            ImageDecoder.Source source=ImageDecoder.createSource(getContext().getContentResolver(),filePath);
            try {
               bitmap =ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            upload_Image();
        }
    }
    private void upload_Image()
    {
        if(filePath!=null)
        {
            final ProgressDialog progressDialog=new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading....");
            progressDialog.show();
            final StorageReference reference=storageReference.child("images/"+ UUID.randomUUID().toString());
            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                             Uri url=uri;
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            profile_image.setImageBitmap(bitmap);
                            HashMap<String ,Object> hashMap=new HashMap<>();
                            hashMap.put("image_URL",url.toString());
                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
                            reference1.updateChildren(hashMap);

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Failed :(",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress
                            = (100.0
                            * snapshot.getBytesTransferred()
                            / snapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
                }
            });
        }
    }
}