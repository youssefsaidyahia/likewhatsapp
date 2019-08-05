package com.example.android.whatsapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MesaageRecyclerViewHolder> {
   private  ArrayList<messageData> Mesaage;
    public messageAdapter(ArrayList<messageData> U){
        this.Mesaage=U;
    }

    @NonNull
    @Override
    public MesaageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View l= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mesaage_layout,null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        l.setLayoutParams(lp);
        MesaageRecyclerViewHolder ulr=new MesaageRecyclerViewHolder(l);
        return ulr;
    }

    @Override
    public void onBindViewHolder(@NonNull final MesaageRecyclerViewHolder MesaageRecyclerViewHolder,final int i) {
         MesaageRecyclerViewHolder.mmessage.setText(Mesaage.get(i).getMessage());
         MesaageRecyclerViewHolder.msender.setText(Mesaage.get(i).getSenderid());
         if(Mesaage.get(MesaageRecyclerViewHolder.getAdapterPosition()).getMediaUrlList().isEmpty())
             MesaageRecyclerViewHolder.viewmedia.setVisibility(View.GONE);
             MesaageRecyclerViewHolder.viewmedia.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 new ImageViewer.Builder(v.getContext(), Mesaage.get(MesaageRecyclerViewHolder.getAdapterPosition()).getMediaUrlList())
                         .setStartPosition(0)
                         .show();
             }
         });

   /*     MesaageRecyclerViewHolder.mlayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    }

        });
    */
    }

    @Override
    public int getItemCount() {
        return Mesaage.size();
    }

    public class MesaageRecyclerViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout mlayo;
        public TextView mmessage ,msender;
        Button viewmedia;
        public MesaageRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mlayo=itemView.findViewById(R.id.chat_list);
            mmessage=itemView.findViewById(R.id.messageeeeeeee);
            msender=itemView.findViewById(R.id.sender);
            viewmedia=itemView.findViewById(R.id.viewMedia);
        }
    }

}
