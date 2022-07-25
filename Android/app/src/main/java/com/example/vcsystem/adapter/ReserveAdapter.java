package com.example.vcsystem.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vcsystem.R;
import com.example.vcsystem.model.ReserveModel;

import java.util.List;

import static com.example.vcsystem.ui.ReserveFragment.onYesRequested;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ViewHolder> {

    List<ReserveModel> list;
    Context context;

    public ReserveAdapter(List<ReserveModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ReserveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserve_item,parent,false);


        ReserveAdapter.ViewHolder ReserveHolder = new ReserveAdapter.ViewHolder(view);

        return ReserveHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveAdapter.ViewHolder holder, int position) {
        ReserveModel ReserveModel = list.get(position);
        holder.setValues(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setTitle("預約看診");
                String[] types = {"掛號", "取消"};
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch(which){
                            case 0:
                                onYesRequested(ReserveModel);
                                break;
                            case 1:
                                break;
                        }
                    }
                });
                b.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setData(List<ReserveModel> ReserveModelList) {
        this.list = ReserveModelList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateSent_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateSent_txt = itemView.findViewById(R.id.dateSent);
        }
        public void setValues(ReserveModel ReserveModel) {
            if (ReserveModel != null) {
                String dateSent = ReserveModel.getDateSent3();

                dateSent_txt.setText(dateSent);
            }
        }
    }
}
