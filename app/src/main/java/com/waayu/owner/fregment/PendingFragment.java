package com.waayu.owner.fregment;

import static com.waayu.owner.utils.SessionManager.curruncy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.activity.ChooseDeliveryActivity;
import com.waayu.owner.activity.OrderActivity;
import com.waayu.owner.model.GetCount;
import com.waayu.owner.model.OrderHistoryItem;
import com.waayu.owner.model.Pending;
import com.waayu.owner.model.RestResponse;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.waayu.owner.utils.TimeAgo2;
import com.waayu.owner.utils.Utiles;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;


public class PendingFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.recycle_pending)
    RecyclerView recyclePending;


    @BindView(R.id.txt_titel)
    TextView txtTitle;
    @BindView(R.id.txt_neworder)
    TextView txtNeworder;
    @BindView(R.id.txt_ongoing)
    TextView txtOngoing;
    @BindView(R.id.txt_complet)
    TextView txtComplet;
    @BindView(R.id.txtNodata)
    TextView txtNodata;

    public PendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store user;
    List<OrderHistoryItem> pendinglistMain = new ArrayList<>();
    PendingAdepter myOrderAdepter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pendding, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclePending.setLayoutManager(recyclerLayoutManager);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
//        txtTitle.setText("Pending Order");
        getData(sessionManager.getPendingData("status_fragment"));


        return view;
    }


    @OnClick({R.id.txt_neworder, R.id.txt_ongoing, R.id.txt_complet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_neworder:
                getData("Pending");
                break;
            case R.id.txt_ongoing:
                getData("Cancel");
                break;
            case R.id.txt_complet:
                getData("Complete");
                break;
            default:
                break;
        }
    }

    private void getPendingOrder(String status) {
        custPrograssbar.prograssCreate(getActivity());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getPending((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                Pending pending = gson.fromJson(result.toString(), Pending.class);
                if (pending.getResult().equalsIgnoreCase("true")) {

                    if (!pending.getOrderHistory().isEmpty()) {
                        txtNodata.setVisibility(View.GONE);
                        recyclePending.setVisibility(View.VISIBLE);
                        pendinglistMain = pending.getOrderHistory();
                        myOrderAdepter = new PendingAdepter(pendinglistMain);
                        recyclePending.setAdapter(myOrderAdepter);
                    } else {
                        txtNodata.setVisibility(View.VISIBLE);
                        txtNodata.setText("" + pending.getResponseMsg());
                        recyclePending.setVisibility(View.GONE);
                    }
                } else {
                    txtNodata.setVisibility(View.VISIBLE);
                    txtNodata.setText("" + pending.getResponseMsg());
                    recyclePending.setVisibility(View.GONE);
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result, RestResponse.class);
                Toast.makeText(getActivity(), response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (response.getResult().equalsIgnoreCase("true")) {

                    getPendingOrder("Pending");
                } else {
                    getPendingOrder("Pending");
                    //finish();
                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                GetCount pending = gson.fromJson(result.toString(), GetCount.class);
                if (pending.getResult().equalsIgnoreCase("true")) {
                    Utiles.getDiningValue = pending.getDining();
                    Utiles.getDeliveryvalue = pending.getDelivery();
                    MainHomeFragment.setTextCountData();
                }
            }

        } catch (Exception e) {
            e.toString();
        }
    }


    public class PendingAdepter extends RecyclerView.Adapter<PendingAdepter.ViewHolder> {
        private List<OrderHistoryItem> pendinglist;

        public PendingAdepter(List<OrderHistoryItem> pendinglist) {
            this.pendinglist = pendinglist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pending_item1, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            Log.e("position", "" + position);
            OrderHistoryItem order = pendinglist.get(position);
            holder.txtOderid.setText("OrderID:  #" + order.getId());
            holder.txtStuts.setText(" " + order.getStatus() + " ");
            holder.txtDateandstatus.setText(order.getOrderDate());

            holder.txt_name.setText(order.getCustomer_name());
            holder.txt_mobile.setText(order.getCustomer_mobile());
            holder.txt_pmode.setText(order.getP_method_name());

            holder.txt_orderplace.setText(order.getOrder_count());
            holder.txtDateandstatus.setText(timesss(order.getOrderDate()));

            holder.txtAddress.setText("" + order.getCustAdd());
            holder.txtPricetotla.setText("Total : " + sessionManager.getStringData(curruncy) + order.getTotal());
            holder.lvlClick.setOnClickListener(v -> {
                SessionManager.plod = order.getOrder_count();
                SessionManager.dateod = order.getOrderDate();
                startActivity(new Intent(getActivity(), OrderActivity.class).putExtra("oid", order.getId())
                        .putExtra("d_mode", order.getSelf_pickup()));
            });
            if(order.getOrderFlowId()!=null){
                if (order.getOrderFlowId().equalsIgnoreCase("0")) {
                    holder.reject.setVisibility(View.VISIBLE);
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.status_txt.setVisibility(View.GONE);
                    holder.getTxtPricetotla1.setVisibility(View.GONE);
                } else {
                    holder.reject.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                    holder.status_txt.setVisibility(View.VISIBLE);
                    holder.status_txt.setText(order.getCurrent_status());
                    if (order.getCurrent_status().equalsIgnoreCase("Cancelled")) {
                        holder.status_txt.setTextColor(getContext().getResources().getColor(R.color.colorRed));
                    } else {
                        holder.status_txt.setTextColor(getContext().getResources().getColor(R.color.colorDarkGreen));
                    }
                    holder.getTxtPricetotla1.setVisibility(View.VISIBLE);
                    holder.getTxtPricetotla1.setText("Total : " + sessionManager.getStringData(curruncy) + order.getTotal());
                    holder.txtPricetotla.setVisibility(View.GONE);
                }

            }

            if (order.getSelf_pickup().equalsIgnoreCase("0")) {
                holder.txt_dmode.setText("Delivery");
            } else if (order.getSelf_pickup().equalsIgnoreCase("1")) {
                holder.txt_dmode.setText("Self Pickup");
            }

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SessionManager.oid = order.getId();
                    SessionManager.caddress = order.getCustAdd();
                    Intent intent = new Intent(getActivity(), ChooseDeliveryActivity.class);
                    intent.putExtra("d_mode", order.getSelf_pickup());
                    startActivity(intent);
                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure for reject order")
                            .setContentText("")
                            .setCancelText("No,cancel")
                            .setConfirmText("Yes,reject it!")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    getCountData();
                                    getMackDecision("2", order.getId());
                                    sDialog.cancel();

                                }
                            })
                            .show();
                }
            });

            TimeAgo2 timeAgo2 = new TimeAgo2();
            holder.date_ago.setText(timeAgo2.covertTimeToText(order.getOrderDate()));


        }

        @Override
        public int getItemCount() {
            return pendinglist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_pricetotla1)
            TextView getTxtPricetotla1;
            @BindView(R.id.txt_oderid)
            TextView txtOderid;
            @BindView(R.id.txt_pricetotla)
            TextView txtPricetotla;
            @BindView(R.id.txt_dateandstatus)
            TextView txtDateandstatus;
            @BindView(R.id.txt_address)
            TextView txtAddress;
            @BindView(R.id.txt_stuts)
            TextView txtStuts;

            @BindView(R.id.txt_pmode)
            TextView txt_pmode;

            @BindView(R.id.txt_name)
            TextView txt_name;
            @BindView(R.id.txt_mobile)
            TextView txt_mobile;

            @BindView(R.id.accept)
            TextView accept;
            @BindView(R.id.reject)
            TextView reject;

            @BindView(R.id.txt_orderplace)
            TextView txt_orderplace;


            @BindView(R.id.status_txt)
            TextView status_txt;

            @BindView(R.id.date_ago)
            TextView date_ago;

            @BindView(R.id.lvl_click)
            RelativeLayout lvlClick;

            @BindView(R.id.txt_dmode)
            TextView txt_dmode;


            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public void getCountData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getCount((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMackDecision(String status, String oid) {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", oid);
            jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getMackDecision((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getData(sessionManager.getPendingData("status_fragment"));

       /* getPendingOrder(sessionManager.getPendingData("status"));

        txtTitle.setText("New Order");
        txtNeworder.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
        txtComplet.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
        txtOngoing.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

        txtNeworder.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        txtComplet.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
        txtOngoing.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));*/

    }

    public String timesss(String orderDate) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = inputFormat.parse(orderDate);
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return orderDate;
    }

    public void getData(String status) {
        if (status.equalsIgnoreCase("Pending")) {
            getPendingOrder(status);
            txtTitle.setText("New Order");
            txtNeworder.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
            txtComplet.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
            txtOngoing.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

            txtNeworder.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            txtComplet.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
            txtOngoing.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

        } else if (status.equalsIgnoreCase("Cancel")) {
            getPendingOrder(status);
            txtTitle.setText("Reject Order");
            txtNeworder.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
            txtComplet.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
            txtOngoing.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));

            txtNeworder.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
            txtComplet.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
            txtOngoing.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));

        } else if (status.equalsIgnoreCase("Complete")) {
            getPendingOrder(status);
            txtTitle.setText("Past Order");

            txtNeworder.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
            txtComplet.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
            txtOngoing.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

            txtNeworder.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
            txtComplet.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            txtOngoing.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

        }
    }
}
