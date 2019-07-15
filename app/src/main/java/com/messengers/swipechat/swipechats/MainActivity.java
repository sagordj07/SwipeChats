package com.messengers.swipechat.swipechats;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.messengers.swipechat.swipechats.R.menu.optionmenu;

public class MainActivity extends AppCompatActivity {

   private Toolbar mtoolbar;
   private  ViewPager myviewPager;
   private TabLayout mytablaypout;
   private  TabAccessAdapter mytabAccessAdapter;

   private DatabaseReference RootRef;
   private FirebaseUser currentUser;
   private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        RootRef=FirebaseDatabase.getInstance().getReference();

        mtoolbar=(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("SwipeChat");


        myviewPager=(ViewPager)findViewById(R.id.main_tbs_pager);
        mytabAccessAdapter= new TabAccessAdapter(getSupportFragmentManager());
        myviewPager.setAdapter(mytabAccessAdapter);

        mytablaypout=(TabLayout) findViewById(R.id.mainTab);
        mytablaypout.setupWithViewPager(myviewPager);



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser==null)
        {
            sendUserToLoginActivity();
        }
        else
        {
            VerifyUserExist();
        }
    }

    private void VerifyUserExist() {

        String currentUserId=mAuth.getCurrentUser().getUid();

        RootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.child("name").exists()))
                {
                    Toast.makeText(MainActivity.this,"Welcome to SwipeChats",Toast.LENGTH_SHORT).show();
                }
                else {

                    sendUserToSettingActivity();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });
    }

    private void sendUserToLoginActivity() {

        Intent LoginIntent=new Intent(MainActivity.this,LoginActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(LoginIntent);
        finish();
    }


    private void sendUserToFindFriendsActivity() {

        Intent findfriendIntent=new Intent(MainActivity.this,FindFriendsActivity.class);
        startActivity(findfriendIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.optionmenu,menu );

        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.loggout_id)
         {

               mAuth.signOut();
               sendUserToLoginActivity();
         }

        if(item.getItemId()==R.id.setting_id)
        {
            sendUserToSettingActivity();


        }
        if(item.getItemId()==R.id.about_id)
        {
            sendToAboutActivity();


        }

        if(item.getItemId()==R.id.Group_option_id)
{
             RequestCreatNewGroup();


        }

        if(item.getItemId()==R.id.find_friend_id)
        {
            sendUserToFindFriendsActivity();

        }


         return  true;
    }

    private void sendToAboutActivity() {

        Intent aboutIntent = new Intent(MainActivity.this,AboutActivity.class);

        aboutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(aboutIntent);

    }

    private void RequestCreatNewGroup() {

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);

        builder.setTitle("Set Group Name");
        final EditText GroupNameField = new EditText(MainActivity.this);
        GroupNameField.setHint("Swipechats");
        builder.setView(GroupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String GroupName= GroupNameField.getText().toString();

                if(TextUtils.isEmpty(GroupName))
                {
                    Toast.makeText(MainActivity.this,"Please Put a Group name",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(GroupName);
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
              dialog.cancel();

            }
        });
        builder.show();

    }

    private void CreateNewGroup(final String groupName) {

        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,groupName+ "is Created",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void sendUserToSettingActivity() {

        Intent SettingIntent=new Intent(MainActivity.this,SettingActivity.class);
        SettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(SettingIntent);
        finish();
    }
}
