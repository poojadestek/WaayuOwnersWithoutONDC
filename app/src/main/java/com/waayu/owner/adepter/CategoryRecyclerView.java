package com.waayu.owner.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.waayu.owner.R;
import com.waayu.owner.model.ProductDataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryRecyclerView extends RecyclerView.Adapter<CategoryRecyclerView.ViewHolder> {

    List<ProductDataItem> arrayListdata;
    Context activity;
    private boolean isFlag;

    public CategoryRecyclerView(FragmentActivity activity, List<ProductDataItem> productData) {
        this.activity = activity;
        this.arrayListdata = productData;
    }

    @Override
    public CategoryRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_group, parent, false);
        CategoryRecyclerView.ViewHolder viewHolder = new CategoryRecyclerView.ViewHolder(view);
        return viewHolder;
    }

    public void filterList(List<ProductDataItem> filterlist) {
        arrayListdata = filterlist;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public void onBindViewHolder(CategoryRecyclerView.ViewHolder holder,
                                 int position) {
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");
            }
        };

        thread.start();

        isFlag = false;
        holder.txtTitel.setText("" + arrayListdata.get(position).getTitle());
        holder.listTotalTitle.setText("Total Count : "+String.valueOf(arrayListdata.get(position).getMenuitemData().size()));
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(activity);
        holder.childview.setLayoutManager(recyclerLayoutManager);
        SuCategoryAdapter myOrderAdepter = new SuCategoryAdapter(activity,arrayListdata.get(position).getMenuitemData());
        holder.childview.setAdapter(myOrderAdepter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(holder.childview.getVisibility()==View.VISIBLE){
                        isFlag = true;
                        holder.childview.setVisibility(View.GONE);

                    }else{
                        isFlag = false;
                        holder.childview.setVisibility(View.VISIBLE);
                    }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.listTitle)
        TextView txtTitel;
        @BindView(R.id.listTotalTitle)
        TextView listTotalTitle;

        @BindView(R.id.itemView)
        LinearLayout itemView;

        @BindView(R.id.childview)
        RecyclerView childview;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}