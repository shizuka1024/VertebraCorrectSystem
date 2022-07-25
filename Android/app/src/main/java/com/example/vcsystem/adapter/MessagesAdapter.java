package com.example.vcsystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vcsystem.R;
import com.example.vcsystem.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesHolder> {
    String currentUserId;

    List<ChatMessage> messages;
    SimpleDateFormat format = new SimpleDateFormat("hh:mm a, dd-MM");

    public MessagesAdapter(String currentUserId){
        this.currentUserId = currentUserId;
        messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        MessagesHolder messagesHolder = new MessagesHolder(view);

        return messagesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesHolder holder, int position) {
        holder.setValues(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setData(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    class MessagesHolder extends RecyclerView.ViewHolder {
        private TextView txvUser_Other;
        private TextView txvMsg_Other;
        private TextView txvTime_Other;

        private TextView txvMsg_User;
        private TextView txvTime_User;
        RelativeLayout userLayout, otherUserLayout;

        ImageView imgMsg_user, imgMsg_other;


        public MessagesHolder(@NonNull View v) {
            super(v);
            txvUser_Other = v.findViewById(R.id.txv_user_other);
            txvMsg_Other = v.findViewById(R.id.txv_msg_other);
            txvTime_Other = v.findViewById(R.id.txv_time_other);

            txvMsg_User = v.findViewById(R.id.txv_msg_user);
            txvTime_User = v.findViewById(R.id.txv_time_user);

            userLayout = v.findViewById(R.id.userLayout);
            otherUserLayout = v.findViewById(R.id.otherUserLayout);

            imgMsg_user = v.findViewById(R.id.imgmsg_user);
            imgMsg_other = v.findViewById(R.id.imgmsg_otheruser);

        }

        public void setValues(ChatMessage chatMessage) {
            if (chatMessage != null) {
                String chatMsg = chatMessage.getMessage();
                if (chatMsg == null) {
                    otherUserLayout.setVisibility(View.GONE);
                    userLayout.setVisibility(View.GONE);
                }else{
                    if (!chatMessage.getUid().equals(currentUserId)) {
                        otherUserLayout.setVisibility(View.VISIBLE);
                        userLayout.setVisibility(View.GONE);
                        txvMsg_Other.setText(chatMsg);
//                        txvUser_Other.setText(chatMessage.getName());
                        if(chatMessage.getDateSent() != null) {
                            txvTime_Other.setText(format.format(chatMessage.getDateSent()));
                        }else{
                            txvTime_Other.setText(format.format(new Date()));
                        }
                    }else{
                        userLayout.setVisibility(View.VISIBLE);
                        otherUserLayout.setVisibility(View.GONE);
                        txvMsg_User.setText(chatMsg);
                        if(chatMessage.getDateSent() != null) {
                            txvTime_User.setText(format.format(chatMessage.getDateSent()));
                        }else{
                            txvTime_User.setText(format.format(new Date()));
                        }
                    }
                }
            }
        }
    }
}
