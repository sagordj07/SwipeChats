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

public class LoginActivity extends AppCompatActivity {

    private Button loginButton,phoneLoginbutton;
    private EditText userEmail,userPassword;
    private TextView NeedNewAccountlink,forgetPasswordLink;

    private FirebaseAuth mAuth;


    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();


        InitializeFields();

        NeedNewAccountlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendUserToRegisterActivity();

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

        phoneLoginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phonLoginIntent=new Intent(LoginActivity.this,PhoneLogActivity.class);
                startActivity(phonLoginIntent);
            }
        });


    }

    private void AllowUserToLogin() {

        String email=userEmail.getText().toString();
        String pass=userPassword.getText().toString();



        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this,"please enter email",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(LoginActivity.this,"please enter password",Toast.LENGTH_SHORT).show();
        }
        else {


            loadingBar.setTitle("Singed in your account");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        SendUserToMainActivity();
                    Toast.makeText(LoginActivity.this,"Logged is successfull",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                    else {

                        String message=task.getException().toString();
                        Toast.makeText(LoginActivity.this,"Error login"+message,Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }

                }
            });
        }

    }

    private void InitializeFields() {

        loginButton=(Button)findViewById(R.id.login_Button_id);
        phoneLoginbutton=(Button)findViewById(R.id.phone_login_button_id);
        userEmail=(EditText) findViewById(R.id.login_email);
        userPassword=(EditText) findViewById(R.id.pass_id);
        NeedNewAccountlink=(TextView) findViewById(R.id.new_account_id_link);
        forgetPasswordLink=(TextView) findViewById(R.id.forget_id_link);
        loadingBar=new ProgressDialog(this);


    }




    private void SendUserToMainActivity() {

        Intent mainIntet=new Intent(LoginActivity.this,MainActivity.class);
        mainIntet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntet);

        finish();
    }
    private void sendUserToRegisterActivity() {

        Intent registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);

    }
}
