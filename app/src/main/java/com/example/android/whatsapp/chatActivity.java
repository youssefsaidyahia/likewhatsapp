package com.example.android.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chatActivity extends AppCompatActivity {
    private ArrayList<messageData>messagelist;
    private RecyclerView messageview , mMediaview;
    private  RecyclerView.Adapter messageAdapter,mMedaiaAdapter;
    private RecyclerView.LayoutManager messagelinearlayout , mMediaLinearlayout;
    chatData mchatdata;
    DatabaseReference mchat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mchatdata=(chatData)getIntent().getSerializableExtra("chatobject");

        mchat= FirebaseDatabase.getInstance().getReference().child("chat").child(mchatdata.getChatid()).child("messages");

        Button  msend=findViewById(R.id.send);
        Button  mMedia=findViewById(R.id.media);

        mMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        intalize();
        intalizeMedia();
        getchaeMessage();
    }
    int pick_image=1;
    ArrayList<String>mediauri=new ArrayList<>();
    private void opengallery() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture(s)"),pick_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            if(requestCode==pick_image){
                if(data.getClipData()==null){
                    mediauri.add(data.getData().toString());
                }
                else{
                    for (int i=0;i<data.getClipData().getItemCount();i++){
                        mediauri.add(data.getClipData().getItemAt(i).getUri().toString());
                    }

                }
                mMedaiaAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getchaeMessage() {
       mchat.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               if (dataSnapshot.exists()) {
                   ArrayList<String>mediaUrlList=new ArrayList<>();
                   String message = "", creator = "";
                   if (dataSnapshot.child("text").getValue() != null) {
                       message = dataSnapshot.child("text").getValue().toString();
                   }
                   if (dataSnapshot.child("creator").getValue() != null) {
                       creator = dataSnapshot.child("creator").getValue().toString();
                   }
                   if (dataSnapshot.child("media").getChildrenCount() > 0) {
                       for(DataSnapshot dataSnapshot1 : dataSnapshot.child("media").getChildren())
                           mediaUrlList.add(dataSnapshot1.getValue().toString());
                   }
                   messageData md = new messageData(dataSnapshot.getKey(), creator, message,mediaUrlList);
                   messagelist.add(md);
                   messagelinearlayout.scrollToPosition(messagelist.size()-1);
                   messageAdapter.notifyDataSetChanged();
               }
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    int totalMediaUploaded = 0;
    ArrayList<String> mediaIdList = new ArrayList<>();
    EditText mMessage;
    private void sendMessage(){
        mMessage = findViewById(R.id.message);

        String messageId = mchat.push().getKey();
        final DatabaseReference newMessageDb = mchat.child(messageId);

        final Map newMessageMap = new HashMap<>();

        newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

        if(!mMessage.getText().toString().isEmpty())
            newMessageMap.put("text", mMessage.getText().toString());


        if(!mediauri.isEmpty()){
            for (String mediaUri : mediauri){
                String mediaId = newMessageDb.child("media").push().getKey();
                mediaIdList.add(mediaId);
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("chat").child(mchatdata.getChatid()).child(messageId).child(mediaId);

                UploadTask uploadTask = filePath.putFile(Uri.parse(mediaUri));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newMessageMap.put("/media/" + mediaIdList.get(totalMediaUploaded) + "/", uri.toString());

                                totalMediaUploaded++;
                                if(totalMediaUploaded == mediauri.size())
                                    UpdateDatabasewithNewMessage(newMessageDb, newMessageMap);

                            }
                        });
                    }
                });
            }
        }else{
            if(!mMessage.getText().toString().isEmpty())
                UpdateDatabasewithNewMessage(newMessageDb, newMessageMap);
        }


    }


    private void UpdateDatabasewithNewMessage(DatabaseReference databaseReference ,Map map){
        databaseReference.updateChildren(map);
        mMessage.setText(null);
        mediauri.clear();
        mediaIdList.clear();
        mMedaiaAdapter.notifyDataSetChanged();
        String message;
        if(map.get("text") != null)
            message=map.get("text").toString();
        else
            message="Have amedia or both";

        for(usedata usedata : mchatdata.getUsedata()){
            if(!usedata.getUid().equals(FirebaseAuth.getInstance().getUid())){
                new sendNotification( message , "new message",usedata.getNotification_key());
            }
        }

       }
    public  void intalize(){
        messagelist=new ArrayList<>();
        messageview=(RecyclerView)findViewById(R.id.message_list);
        messageview.setNestedScrollingEnabled(false);
        messageview.setHasFixedSize(false);

        messagelinearlayout=new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false);
        messageview.setLayoutManager(messagelinearlayout);
        messageAdapter=new messageAdapter(messagelist);
        messageview.setAdapter(messageAdapter);
    }
    public  void intalizeMedia(){
        mediauri=new ArrayList<>();
        mMediaview=findViewById(R.id.mediarecy);
        mMediaview.setNestedScrollingEnabled(false);
        mMediaview.setHasFixedSize(false);

        mMediaLinearlayout=new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL,false);
        mMediaview.setLayoutManager(mMediaLinearlayout);
        mMedaiaAdapter=new MediaAdapter(getApplicationContext(),mediauri);
        mMediaview.setAdapter(mMedaiaAdapter);
    }
}
