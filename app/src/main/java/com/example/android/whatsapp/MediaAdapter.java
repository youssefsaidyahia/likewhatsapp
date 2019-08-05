package com.example.android.whatsapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.mediaviewholder> {
    Context context;
    ArrayList<String>medialisturi;
    public MediaAdapter(Context c ,ArrayList<String>s){
        this.context=c;
        this.medialisturi=s;
    }

    @NonNull
    @Override
    public mediaviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.medialayout,null,false);
        mediaviewholder mediaviewholder=new mediaviewholder(view);
        return mediaviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull mediaviewholder mediaviewholder, int i) {
        Glide.with(context).load(Uri.parse(medialisturi.get(i))).into(mediaviewholder.imageView);
    }

    @Override
    public int getItemCount() {
        return medialisturi.size();
    }

    public class mediaviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public mediaviewholder(@NonNull View itemView) {
            super(itemView);
             imageView=itemView.findViewById(R.id.mediaId);
        }
    }
}
