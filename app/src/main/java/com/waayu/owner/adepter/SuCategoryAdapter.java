package com.waayu.owner.adepter;

import static com.waayu.owner.retrofit.APIClient.baseUrl;
import static com.waayu.owner.utils.SessionManager.curruncy;
import static com.waayu.owner.utils.Utiles.updatestatus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.activity.ProductActivity;
import com.waayu.owner.model.MenuitemDataItem;
import com.waayu.owner.model.ProductData;
import com.waayu.owner.model.RestResponse;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SuCategoryAdapter extends RecyclerView.Adapter<SuCategoryAdapter.ViewHolder> implements GetResult.MyListener {

    List<MenuitemDataItem> arrayListdata;
    Context activity;
    int a = 0;
    public SuCategoryAdapter(Context activities, List<MenuitemDataItem> arrayList) {
        this.activity = activities;
        this.arrayListdata = arrayList;
    }

    @Override
    public SuCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        SuCategoryAdapter.ViewHolder viewHolder = new SuCategoryAdapter.ViewHolder(view);

        return viewHolder;
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public void onBindViewHolder(SuCategoryAdapter.ViewHolder holder,
                                 int position) {
        MenuitemDataItem order = arrayListdata.get(position);
        SessionManager sessionManager = new SessionManager(activity);
        holder.txt_title.setText("" + order.getTitle());
        holder.txt_price.setText(sessionManager.getStringData(curruncy) + "" + order.getPrice());
        holder.txt_strikeprice.setText(sessionManager.getStringData(curruncy) + order.getS_price());
        holder.txt_strikeprice.setPaintFlags(holder.txt_strikeprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txt_percentageoffprice.setText(sessionManager.getStringData(curruncy) + order.getDisc_perc() + "%OFF");

        if (order.getStatus().equals("1")) {
            holder.switch_.setChecked(true);
        } else {
            holder.switch_.setChecked(false);
        }

        if (order.getIsVeg() == 0) {
            holder.txt_ptype.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_nonveg));
        } else if (order.getIsVeg() == 1) {
            holder.txt_ptype.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_veg));
        } else if (order.getIsVeg() == 2) {
            holder.txt_ptype.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_egg));
        }
        Glide.with(activity).load(APIClient.baseUrl + "/" + order.getItemImg()).placeholder(R.drawable.slider).into(holder.img_icon);

        holder.lvl_clicl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ProductActivity.class).putExtra("pid", order.getId()));
            }
        });

        holder.switch_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    a = 1;
                    productstatus( order.getId(), String.valueOf(a));
                }else{
                    int a = 0;
                    productstatus( order.getId(), String.valueOf(a));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayListdata.size();
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {

            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);

                if (response.getResult().equalsIgnoreCase("true")) {
                    Toast.makeText(activity, response.getResponseMsg(), Toast.LENGTH_LONG).show();
                    updatestatus = true;
//                    finish();
                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txt_title;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_strikeprice)
        TextView txt_strikeprice;
        @BindView(R.id.txt_percentageoffprice)
        TextView txt_percentageoffprice;
        @BindView(R.id.txt_ptype)
        ImageView txt_ptype;

        @BindView(R.id.img_icon)
        ImageView img_icon;

        @BindView(R.id.switch_)
        Switch switch_;

        @BindView(R.id.lvl_clicl)
        RelativeLayout lvl_clicl;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void productstatus(String id, String status) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().ProductStatus((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}