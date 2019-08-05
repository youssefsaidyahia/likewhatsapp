package com.example.android.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  private EditText mphone  , mcode;
  private Button msend;
  String verificationcode;
  PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mphone = (EditText) findViewById(R.id.phone);
        mcode = (EditText) findViewById(R.id.code);
        msend = (Button) findViewById(R.id.send);
        userlogedin();
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verificationcode!=null){
                    getverification();
                }
                phoneverify();
            }
        });
        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signin(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationcode=s;
                msend.setText("verify");
            }
        };
    }

    private void signin(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //userlogedin();
                    final FirebaseUser  fbu=FirebaseAuth.getInstance().getCurrentUser();
                    if(fbu!=null){
                        final DatabaseReference d= FirebaseDatabase.getInstance().getReference().child("user").child(fbu.getUid());
                        d.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){
                                   final  Map<String,Object> map1=new HashMap<String, Object>();
                                   map1.put("phone",fbu.getPhoneNumber());
                                   map1.put("name",fbu.getPhoneNumber());
                                   d.updateChildren(map1);


                                }
                                userlogedin();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                     }

                });
        }
    private void userlogedin() {
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
                finish();
                return;
            }

        }

         public  void  getverification(){
          PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationcode,mcode.getText().toString());
          signin(credential);
         }

    private void phoneverify() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mphone.getText().toString(),60, java.util.concurrent.TimeUnit.SECONDS,this,mcallback);
    }
}
