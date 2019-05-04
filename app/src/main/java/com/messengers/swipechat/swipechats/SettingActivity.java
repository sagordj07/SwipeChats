package com.messengers.swipechat.swipechats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private Button updateAccountSettingbutton;
    private EditText userName,userStatus;
    private CircleImageView userProfileImage;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        Rootref=FirebaseDatabase.getInstance().getReference();


        InitializeFields();
        userName.setVisibility(View.INVISIBLE);

        updateAccountSettingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSetting();
            }
        });

        RetrieveUserInfo();
    }


    private void RetrieveUserInfo() {



        Rootref.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image")))
                {
                    String RetrieveUsername= dataSnapshot.child("name").getValue().toString();
                    String RetrievStatus= dataSnapshot.child("status").getValue().toString();
                    String RetrieveImage= dataSnapshot.child("image").getValue().toString();

                    userName.setText(RetrieveUsername);
                    userStatus.setText(RetrievStatus);


                }
                else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                {

                    String RetrieveUsername= dataSnapshot.child("name").getValue().toString();
                    String RetrievStatus= dataSnapshot.child("status").getValue().toString();

                    userName.setText(RetrieveUsername);
                    userStatus.setText(RetrievStatus);


                }
                else {
                    userName.setVisibility(View.VISIBLE);

                    Toast.makeText(SettingActivity.this,"please set & update your profile",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void InitializeFields() {

        updateAccountSettingbutton= (Button) findViewById(R.id.update_profile);
        userName=(EditText) findViewById(R.id.set_username_id);
        userStatus=(EditText) findViewById(R.id.set_profile_status);
        userProfileImage =(CircleImageView)findViewById(R.id.profile_image_id);



    }

    private void updateSetting() {

        String SetUsername= userName.getText().toString();
        String setUserStatus= userStatus.getText().toString();
        if(TextUtils.isEmpty(SetUsername) || (TextUtils.isEmpty(setUserStatus)))
        {
            Toast.makeText(SettingActivity.this,"please enter user Name or Status",Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,String> profileMap=new HashMap<>();

            profileMap.put("uId",currentUserId);
            profileMap.put("name",SetUsername);
            profileMap.put("status",setUserStatus);
            Rootref.child("Users").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        SendUserToMainActivity();
                        Toast.makeText(SettingActivity.this,"profile Update is successfuly",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        String masssege = task.getException().toString();
                        Toast.makeText(SettingActivity.this,"Error" + masssege,Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }


    }
    private void SendUserToMainActivity() {

        Intent mainIntet=new Intent(SettingActivity.this,MainActivity.class);
        mainIntet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntet);

        finish();
    }
}
