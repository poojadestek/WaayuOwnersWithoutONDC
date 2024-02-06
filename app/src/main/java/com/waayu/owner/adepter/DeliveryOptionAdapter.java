package com.waayu.owner.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waayu.owner.R;
import com.waayu.owner.activity.ChooseDeliveryActivity;
import com.waayu.owner.model.DeliveryOption;
import com.waayu.owner.utils.SessionManager;

import java.util.List;

public class DeliveryOptionAdapter extends RecyclerView.Adapter<DeliveryOptionAdapter.ViewHolder> {
    private List<DeliveryOption> dataItemList;
    Context mContext;
    private int lastSelectedPosition = -1;
    SessionManager sessionManager;

    public DeliveryOptionAdapter(List<DeliveryOption> dataItemList, Context mContext) {
        this.dataItemList = dataItemList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deliveryoption, parent, false);
        DeliveryOptionAdapter.ViewHolder viewHolder = new DeliveryOptionAdapter.ViewHolder(view);
        sessionManager = new SessionManager(mContext);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.rd1.setText(dataItemList.get(position).getDpName());
        holder.rd1.setChecked(lastSelectedPosition == position);
        if(!dataItemList.get(position).getEstimated_price().isEmpty())
        {

            if (dataItemList.get(position).getDunzo_taskid().equalsIgnoreCase("")){
                holder.rd1.setEnabled(true);
                holder.rd1.setClickable(true);
                holder.msg.setVisibility(View.GONE);
                holder.price.setText(sessionManager.getStringData(SessionManager.curruncy) + dataItemList.get(position).getEstimated_price());

            }else {
                holder.rd1.setEnabled(false);
                holder.rd1.setClickable(false);
                holder.msg.setVisibility(View.VISIBLE);
                holder.msg.setText(dataItemList.get(position).getDunzo_err_msg()+"");
                holder.price.setText(sessionManager.getStringData(SessionManager.curruncy) + dataItemList.get(position).getEstimated_price());

            }


        }else {
            if (dataItemList.get(position).getDpName().equalsIgnoreCase("Own Delivery Boy")){
                holder.rd1.setEnabled(true);
                holder.rd1.setClickable(true);
                holder.msg.setVisibility(View.GONE);
               // holder.price.setText(sessionManager.getStringData(SessionManager.curruncy) + dataItemList.get(position).getEstimated_price());

            }else {
                holder.rd1.setEnabled(false);
                holder.rd1.setClickable(false);
                holder.msg.setVisibility(View.VISIBLE);
                if (dataItemList.get(position).getDunzo_additional_err_msg().equalsIgnoreCase("")){
                    holder.msg.setText(dataItemList.get(position).getDunzo_err_msg()+"");
                }else {
                    holder.msg.setText(dataItemList.get(position).getDunzo_additional_err_msg()+"");
                }
            }

        }
        //Toast.makeText(mContext, ""+ChooseDeliveryActivity.doid, Toast.LENGTH_SHORT).show();
//       holder.rd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//           @Override
//           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//               if (isChecked){
//                   Toast.makeText(mContext, "Allready checked", Toast.LENGTH_SHORT).show();
//               }else {
//
//
//               }
//           }
//       });
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView price,msg;
        RadioButton rd1;
        LinearLayout lyt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyt=itemView.findViewById(R.id.lyt_op);
            rd1=itemView.findViewById(R.id.rd1);
            price=itemView.findViewById(R.id.price);
            msg=itemView.findViewById(R.id.msg);


            rd1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    ChooseDeliveryActivity.doid=dataItemList.get(getAdapterPosition()).getDpId();

                    notifyDataSetChanged();

                }
            });

        }
    }
}
