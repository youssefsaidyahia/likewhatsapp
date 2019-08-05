package com.example.android.whatsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class findUserList extends AppCompatActivity {
  private   ArrayList<usedata>ul , cl;
   private  RecyclerView.Adapter ra;
   private RecyclerView.LayoutManager rl;
   private RecyclerView Rj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_list);
        ul=new ArrayList<>();
        cl=new ArrayList<>();
        final Button create_chat=findViewById(R.id.Create);
        create_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
            }
        });
        intalize();

       getcontentlist();
    }
    private void createChat() {
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user");

        HashMap hashMap = new HashMap();
        hashMap.put("id", key);
        hashMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);

        Boolean valid=false;
        for(usedata usedata :ul) {
            if (usedata.getSelected()){
                valid=true;
                hashMap.put("users/" + usedata.getUid(), true);
                databaseReference.child(usedata.getUid()).child(key).setValue(true);
            }
        }
        if(valid){
            databaseReference1.updateChildren(hashMap);
            databaseReference.child(FirebaseAuth.getInstance().getUid()).child(key).setValue(true);
            }


    }
    public void getcontentlist(){
        String C_iso=getiso();
        Cursor c=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(c.moveToNext()){
            String name=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone=phone.replace(" ","");
            phone=phone.replace("-","");
            phone=phone.replace("(","");
            phone=phone.replace(")","");
            if(!String.valueOf(phone.charAt(0)).equals("+"))
            {
                phone=C_iso+phone;
            }
            usedata ud=new usedata("",name,phone);
            cl.add(ud);
            getuserdetails(ud);
        }
        c.close();
    }

    private void getuserdetails(usedata ud) {
        DatabaseReference dbf= FirebaseDatabase.getInstance().getReference().child("user");
        Query q=dbf.orderByChild("phone").equalTo(ud.getPhone());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()){
                  String phone="",name="";
                  for(DataSnapshot ds : dataSnapshot.getChildren()){
                      if(ds.child("phone").getValue()!=null){
                          phone=ds.child("phone").getValue().toString();
                      }
                      if(ds.child("name").getValue()!=null){
                          name=ds.child("name").getValue().toString();
                      }
                      usedata ud=new usedata(ds.getKey(),name,phone);
                      if(name.equals(phone)){
                          for(usedata u :cl){
                            if(u.getPhone().equals(ud.getPhone())){
                                ud.setName(u.getName());
                            }
                          }
                      }
                      ul.add(ud);
                      ra.notifyDataSetChanged();
                  }
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void intalize(){
        Rj=(RecyclerView)findViewById(R.id.usrlis);
        Rj.setNestedScrollingEnabled(false);
        Rj.setHasFixedSize(false);

        rl=new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false);
        Rj.setLayoutManager(rl);
        ra=new UserAdapter(ul);
        Rj.setAdapter(ra);
    }
    public String getiso(){
        String iso=null;
        TelephonyManager tm=(TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(tm.getNetworkCountryIso()!=null){
            if(!tm.getNetworkCountryIso().toString().equals("")){
                iso=tm.getNetworkCountryIso().toString();
            }
        }
        return countrytophoneiso.getPhone(iso);
    }
}
