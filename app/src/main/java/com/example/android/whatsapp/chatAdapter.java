package com.example.android.whatsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.chatListRecyclerViewHolder> {
    ArrayList<chatData> ChatList;
    public chatAdapter(ArrayList<chatData> U){
        this.ChatList=U;
    }

    @NonNull
    @Override
    public chatListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View l= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chaitem,null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        l.setLayoutParams(lp);
        chatListRecyclerViewHolder ulr=new chatListRecyclerViewHolder(l);
        return ulr;
    }

    @Override
    public void onBindViewHolder(@NonNull final chatListRecyclerViewHolder chatListRecyclerViewHolder, final int i) {
        chatListRecyclerViewHolder.mt.setText(ChatList.get(i).getChatid());


     chatListRecyclerViewHolder.mlayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(), chatActivity.class);
                intent.putExtra("chatobject", ChatList.get(chatListRecyclerViewHolder.getAdapterPosition()));
                v.getContext().startActivity(intent);
   }

        });

    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }

    public class chatListRecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView mt;
         public LinearLayout mlayo;
        public chatListRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mt=itemView.findViewById(R.id.mtitle1);
            mlayo=itemView.findViewById(R.id.chat_list);
        }
    }

}
