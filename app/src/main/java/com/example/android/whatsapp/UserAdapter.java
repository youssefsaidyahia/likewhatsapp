package com.example.android.whatsapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserListRecyclerViewHolder> {
    ArrayList<usedata> UserList;

    public UserAdapter(ArrayList<usedata> U) {
        this.UserList = U;
    }

    @NonNull
    @Override
    public UserListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View l = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userlist, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.setLayoutParams(lp);
        UserListRecyclerViewHolder ulr = new UserListRecyclerViewHolder(l);
        return ulr;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListRecyclerViewHolder userListRecyclerViewHolder, int i) {
        userListRecyclerViewHolder.na.setText(UserList.get(i).getName());
        userListRecyclerViewHolder.ui.setText(UserList.get(i).getPhone());

         userListRecyclerViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 UserList.get(userListRecyclerViewHolder.getAdapterPosition()).setSelected(isChecked);
             }
         });

    }

    private void createChat(int position) {
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        HashMap hashMap = new HashMap();
        hashMap.put("id", key);
        hashMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);
        hashMap.put("users/" + UserList.get(position).getUid(), true);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        databaseReference1.updateChildren(hashMap);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).child(key).setValue(true);
        databaseReference.child(UserList.get(position).getUid()).child(key).setValue(true);
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class UserListRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView na;
        public TextView ui;
        public LinearLayout mlayo;
        CheckBox checkBox;
        public UserListRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            na = (TextView) itemView.findViewById(R.id.name);
            ui = (TextView) itemView.findViewById(R.id.phone);
            mlayo = (LinearLayout) itemView.findViewById(R.id.user_list);
            checkBox=itemView.findViewById(R.id.add);
        }
    }

}
