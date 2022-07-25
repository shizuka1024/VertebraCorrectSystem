package com.example.vcsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vcsystem.ArticleReader;
import com.example.vcsystem.ChatActivity;
import com.example.vcsystem.R;
import com.example.vcsystem.model.BulletinBoardModel;

import java.util.List;

public class BulletinBoardAdapter extends RecyclerView.Adapter<BulletinBoardAdapter.ViewHolder> {

    List<BulletinBoardModel> list;
    Context context;

    public BulletinBoardAdapter(List<BulletinBoardModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BulletinBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bulletin_board_item,parent,false);


        BulletinBoardAdapter.ViewHolder BulletinBoardHolder = new BulletinBoardAdapter.ViewHolder(view);

        return BulletinBoardHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinBoardAdapter.ViewHolder holder, int position) {
        BulletinBoardModel BulletinBoardModel = list.get(position);
        holder.setValues(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ArticleReader.class);
                intent.putExtra("title", BulletinBoardModel.getTitle());
                intent.putExtra("content", BulletinBoardModel.getContent());
                intent.putExtra("publishName", BulletinBoardModel.getPublishName());
                intent.putExtra("photo", BulletinBoardModel.getPhoto());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setData(List<BulletinBoardModel> BulletinBoardModelList) {
        this.list = BulletinBoardModelList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView PublishName, Title, Content;
        ImageView Photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            PublishName = itemView.findViewById(R.id.notice_id);
            Title = itemView.findViewById(R.id.notice_title);
            Content = itemView.findViewById(R.id.notice_description);
            Photo = itemView.findViewById(R.id.notice_image);
        }

        public void setValues(BulletinBoardModel BulletinBoardModel) {
            if (BulletinBoardModel != null) {
                String name = BulletinBoardModel.getPublishName();
                String title = BulletinBoardModel.getTitle();
                String content = BulletinBoardModel.getContent();
                String photo = BulletinBoardModel.getPhoto();

                PublishName.setText(name);
                Title.setText(title);
                Content.setText(content);
                if(photo == null){

                }else{
                    Glide.with(context)
                            .load(photo)
                            .into(Photo);
                }
            }
        }
    }
}
