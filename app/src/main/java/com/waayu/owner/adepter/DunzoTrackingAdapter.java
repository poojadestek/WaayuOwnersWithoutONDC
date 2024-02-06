package com.waayu.owner.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waayu.owner.R;
import com.waayu.owner.model.DunzoResponse;
import com.waayu.owner.model.QrModel;

import java.util.ArrayList;

public class DunzoTrackingAdapter extends RecyclerView.Adapter<DunzoTrackingAdapter.ViewHolder> {

    Context context;
    ArrayList<DunzoResponse> dunzoResponses;

    public DunzoTrackingAdapter(Context context, ArrayList<DunzoResponse> dunzoResponses) {
        this.context = context;
        this.dunzoResponses = dunzoResponses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dunzo, parent, false);
        DunzoTrackingAdapter.ViewHolder viewHolder = new DunzoTrackingAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txt_oderid.setText(dunzoResponses.get(position).getOrderId());
        holder.txt_status.setText(dunzoResponses.get(position).getResponse());
        holder.txt_taskid.setText(dunzoResponses.get(position).getTaskid());
        holder.txt_time.setText(dunzoResponses.get(position).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return dunzoResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_oderid,txt_status,txt_taskid,txt_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_oderid=itemView.findViewById(R.id.txt_oderid);
            txt_status=itemView.findViewById(R.id.txt_status);
            txt_taskid=itemView.findViewById(R.id.txt_taskid);
            txt_time=itemView.findViewById(R.id.txt_time);

        }
    }
}
