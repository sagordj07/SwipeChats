package com.messengers.swipechat.swipechats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText userEmail,userPassword;
    private TextView alreadyHaveAccount;
    private DatabaseReference rootRef;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        rootRef=FirebaseDatabase.getInstance().getReference();

        InitializeFields();
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();

            }
        });
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreatNewAccount();
            }

            private void CreatNewAccount() {

                String email=userEmail.getText().toString();
                String pass=userPassword.getText().toString();




                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(RegisterActivity.this,"please enter email",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(RegisterActivity.this,"please enter password",Toast.LENGTH_SHORT).show();
                }
               else
                {

                    loadingBar.setTitle("Creating your new account");
                    loadingBar.setMessage("Please wait");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                String currentUserId= mAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(currentUserId).setValue("");

                                SendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this,"Account create Successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                             String messge=task.getException().toString();
                                Toast.makeText(RegisterActivity.this,"Error : "+messge,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

    }

    private void InitializeFields() {

        CreateAccountButton=(Button)findViewById(R.id.register_button);
        userEmail=(EditText) findViewById(R.id.register_email_id);
        userPassword=(EditText) findViewById(R.id.register_pass_id);
        alreadyHaveAccount=(TextView) findViewById(R.id.already_have_account_link_id);

        loadingBar = new ProgressDialog(this);

    }

    private void sendUserToLoginActivity() {

        Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);

    }

    private void SendUserToMainActivity() {

        Intent mainIntet=new Intent(RegisterActivity.this,MainActivity.class);
        mainIntet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntet);

        finish();
    }
}


