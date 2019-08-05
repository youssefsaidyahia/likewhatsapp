package com.example.android.whatsapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private ArrayList<chatData> ch;
    private  RecyclerView.Adapter ra1;
    private RecyclerView.LayoutManager rl1;
    private RecyclerView Rj1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Fresco.initialize(this);

        setContentView(R.layout.activity_main2);
        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
        Button b=findViewById(R.id.signout);
         Button f_U_l=findViewById(R.id.userList);
         b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneSignal.setSubscription(false);
                FirebaseAuth.getInstance().signOut();
                Intent n=new Intent(getApplicationContext(),MainActivity.class);
                n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(n);
                finish();
                return;
            }
        });
        f_U_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fUserlist=new Intent(getApplicationContext(),findUserList.class);
                startActivity(fUserlist);
            }
        });

        userpremissions();
        intalize();
        getuserchatdetails();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void userpremissions() {
        requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},1);
    }

    private void getuserchatdetails(){
        DatabaseReference muserchatdb= FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
        muserchatdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        chatData cd=new chatData(ds.getKey());
                        ch.add(cd);
                        getChatData(cd.getChatid());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void getChatData(String chatid) {
        DatabaseReference muserchatdb= FirebaseDatabase.getInstance().getReference().child("chat").child(chatid).child("info");
        muserchatdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String chatid="";

                    if(dataSnapshot.child("id").getValue() !=null){
                        chatid=dataSnapshot.child("id").getValue().toString();

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.child("users").getChildren()){
                            for(chatData chatData : ch){
                                if(chatData.getChatid().equals(chatid)){
                                    usedata userData =new usedata(dataSnapshot.getKey());
                                    chatData.addUsedata(userData);
                                    getUserData(userData);

                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserData(final usedata userData) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("user").child(userData.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usedata userData1=new usedata(dataSnapshot.getKey());
                if(dataSnapshot.child("notificationKey").getValue() != null){
                    userData1.setNotification_key(dataSnapshot.child("notificationKey").getValue().toString());

                    for(chatData chatData : ch){
                        for(usedata userData2 : chatData.getUsedata()){
                            if(userData2.getUid().equals(userData2.getUid())){
                                userData2.setNotification_key(userData1.getNotification_key());
                            }
                        }
                    }
                    ra1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void intalize(){
        ch=new ArrayList<>();
        Rj1=(RecyclerView)findViewById(R.id.chatList);
        Rj1.setNestedScrollingEnabled(false);
        Rj1.setHasFixedSize(false);

        rl1=new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false);
        Rj1.setLayoutManager(rl1);
        ra1=new chatAdapter(ch);
        Rj1.setAdapter(ra1);
    }
}
