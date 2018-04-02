package com.example.shengx.geostories;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by SHENG.X on 2018-03-22.
 */

public class GeostoryCardAdapter extends RecyclerView.Adapter<GeostoryCardAdapter.GeostoryHolder> {
    List<Geostory> mGeostories;
    ImageButton profileImage;
    TextView username, datePosted, geostory;
    ImageView geostoryImage;
    Button like, comment,dismiss;

    public GeostoryCardAdapter(List<Geostory> mGeostories) {
        this.mGeostories = mGeostories;
    }

    @Override
    public GeostoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.storycard,parent,false);
        GeostoryHolder vh=new GeostoryHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(GeostoryHolder holder, int position) {
        profileImage.setImageResource(R.drawable.ic_menu_camera);
        username.setText(mGeostories.get(position).getUsername());
        datePosted.setText(mGeostories.get(position).getDatePosted());
        geostory.setText(mGeostories.get(position).getGeostory());
        geostoryImage.setImageResource(R.drawable.ic_menu_camera);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Liked",Toast.LENGTH_LONG).show();
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"comment",Toast.LENGTH_LONG).show();
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"dissmiss",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGeostories.size();
    }

    public class GeostoryHolder extends RecyclerView.ViewHolder{

        public GeostoryHolder(View itemView) {
            super(itemView);
            profileImage=(ImageButton)itemView.findViewById(R.id.profileImage_cd);
            username=(TextView)itemView.findViewById(R.id.username_cd);
            datePosted=(TextView)itemView.findViewById(R.id.dateposted_cd);
            geostory=(TextView)itemView.findViewById(R.id.geostory_cd);
            geostoryImage=(ImageView)itemView.findViewById(R.id.geostoryimage_cd);
            like=(Button)itemView.findViewById(R.id.like_cd);
            comment=(Button)itemView.findViewById(R.id.comment_cd);
            dismiss=(Button)itemView.findViewById(R.id.dismiss_cd);
        }
    }
}