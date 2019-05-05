package com.messengers.swipechat.swipechats;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupMassageActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ImageButton usersendtextbutton;
    private ScrollView mScrolView;
    private TextView massagetextdisplay;
    private EditText usermassgageinput;

    private String currentgroupName,currentUserId,currentUserName,currentDate,currentTime;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,GroupNameRef,groupMassageKeyref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_massage);

        currentgroupName=getIntent().getExtras().get("Group name").toString();
        Toast.makeText(GroupMassageActivity.this,currentgroupName,Toast.LENGTH_LONG).show();

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentgroupName);


        initialized();
        GetUserDetails();

        usersendtextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMassage();

                usermassgageinput.setText("");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

                if(dataSnapshot.exists())
                {
                    DisplayMassage(dataSnapshot);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if(dataSnapshot.exists())
                {
                    DisplayMassage(dataSnapshot);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void saveMassage() {
        String message=usermassgageinput.getText().toString();
        String massageKey=GroupNameRef.push().getKey();


        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(this,"Please write you massage",Toast.LENGTH_LONG).show();
        }
        else
        {
            Calendar calforDate= Calendar.getInstance();
            SimpleDateFormat currentDateFoemat= new SimpleDateFormat("MMM dd,yyyy");
            currentDate=currentDateFoemat.format(calforDate.getTime());


            Calendar calForTime= Calendar.getInstance();
            SimpleDateFormat currentTimeformat= new SimpleDateFormat("hh:mm a");
            currentTime=currentTimeformat.format(calForTime.getTime());

            HashMap<String,Object> groupMassageKey=new HashMap<>();
            GroupNameRef.updateChildren(groupMassageKey);
            groupMassageKeyref=GroupNameRef.child(massageKey);


            HashMap<String,Object> massagInfo=new HashMap<>();

            massagInfo.put("name",currentUserName);
            massagInfo.put("massage",message);
            massagInfo.put("Date",currentDate);
            massagInfo.put("Time",currentTime);

            groupMassageKeyref.updateChildren(massagInfo);

        }
    }

    private void DisplayMassage(DataSnapshot dataSnapshot)
    {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while (iterator.hasNext())
        {
            String massgdate=(String)((DataSnapshot)iterator.next()).getValue();
            String massageTime=(String)((DataSnapshot)iterator.next()).getValue();
            String maassage=(String)((DataSnapshot)iterator.next()).getValue();
            String massagename=(String)((DataSnapshot)iterator.next()).getValue();

            massagetextdisplay.append(massagename+"\n");
            massagetextdisplay.append(maassage+"\n");
            massagetextdisplay.append(massageTime+"\n");
            massagetextdisplay.append(massgdate+"\n\n\n");



        }


    }

    private void GetUserDetails() {
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    currentUserName=dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void initialized() {
        mtoolbar=(Toolbar)findViewById(R.id.group_masege_app_bar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(currentgroupName);

        usersendtextbutton=(ImageButton)findViewById(R.id.usersendMassageButton);
        usermassgageinput=(EditText)findViewById(R.id.usersendmassgeinput);

        mScrolView=(ScrollView)findViewById(R.id.scrollView);

        massagetextdisplay=(TextView)findViewById(R.id.group_massge_display);




    }
}

