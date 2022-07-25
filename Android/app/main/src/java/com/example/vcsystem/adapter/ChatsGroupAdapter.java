package com.example.vcsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vcsystem.ChatActivity;
import com.example.vcsystem.R;
import com.example.vcsystem.model.chatsGroupModel;

import java.util.List;

public class ChatsGroupAdapter extends RecyclerView.Adapter<ChatsGroupAdapter.ViewHolder>{

    View.OnClickListener myClickListener;
    List<chatsGroupModel> list;
    Context context;

    public ChatsGroupAdapter(List<chatsGroupModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatsgroup_list_item,parent,false);


        ViewHolder chatsGroupHolder = new ViewHolder(view);

        return chatsGroupHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        chatsGroupModel chatsGroupModel = list.get(position);
        holder.setValues(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("name", chatsGroupModel.getUsername());
                intent.putExtra("uid", chatsGroupModel.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setData(List<chatsGroupModel> chatsGroupModelList) {
        this.list = chatsGroupModelList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.nameofuser);

        }

        public void setValues(chatsGroupModel chatsGroupModel) {
            if (chatsGroupModel != null) {
                String name = chatsGroupModel.getUsername();

                userName.setText(name);
            }
        }
    }

}
