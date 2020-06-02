package com.zash.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText userName , email, password;
    Button btn_register;
    FirebaseAuth mFirebaseAuth ;
    DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName= findViewById(R.id.username);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        btn_register= findViewById(R.id.btn_register);
        mFirebaseAuth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username= userName.getText().toString();
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();

                }
                else if(txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this,"password must be 6 char long",Toast.LENGTH_SHORT).show();

                }
                else{
                    Register(txt_username,txt_email,txt_password);
                }

            }
        });
    }

    private void Register(final String username, String email, String password){
        Log.d("chat","inside Register");

        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("chat","oncomplete task ");
                if(task.isSuccessful()){
                    Log.d("chat","inside Register if");

                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    String userid=firebaseUser.getUid();
                    mDatabaseReference= FirebaseDatabase.getInstance().getReference("User").child(userid);
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageURL","default");
                    mDatabaseReference.setValue(hashMap).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });


                }

                else{
                    Toast.makeText(RegisterActivity.this,"You can't register withis this email or password",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
