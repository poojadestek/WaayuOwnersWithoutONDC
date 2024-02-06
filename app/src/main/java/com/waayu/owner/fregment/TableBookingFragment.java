package com.waayu.owner.fregment;

import static com.waayu.owner.utils.SessionManager.curruncy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.model.BookaTableModel;
import com.waayu.owner.model.Dinein.DineInPaymentHistoryItem;
import com.waayu.owner.model.Dinein.DineInResponse;
import com.waayu.owner.model.GetCount;
import com.waayu.owner.model.Store;
import com.waayu.owner.model.TableBookingResponse;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;

public class TableBookingFragment extends Fragment implements GetResult.MyListener {
    RecyclerView recyclePending;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store user;
    List<BookaTableModel.OrderHistory> pendinglistMain = new ArrayList<>();
    PendingAdepter myOrderAdepter;

    CustomerDataAdapter customerDataAdapter;
    TextView txt_pending,txt_payment,txt_history;
    TextView txt_cancelled;
    TextView txt_all;

    String statusss = "Pending";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_table_booking, container, false);
        recyclePending = v.findViewById(R.id.recycle_pending);
        txt_pending = v.findViewById(R.id.txt_pending);
        txt_payment = v.findViewById(R.id.txt_payment);
        txt_cancelled = v.findViewById(R.id.txt_cancelled);
        txt_history=v.findViewById(R.id.txt_history);
        txt_all = v.findViewById(R.id.txt_all);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclePending.setLayoutManager(recyclerLayoutManager);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        getPendingOrder("Pending");
        txt_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPendingOrder("Pending");
                statusss = "Pending";
                txt_pending.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
                txt_cancelled.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_all.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

                txt_pending.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
                txt_cancelled.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_all.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

                txt_payment.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_payment.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

                txt_history.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_history.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

            }
        });

        txt_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPendingOrder("History");
                statusss = "History";
                txt_pending.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_cancelled.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_all.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

                txt_pending.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_cancelled.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_all.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

                txt_payment.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_payment.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

                txt_history.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
                txt_history.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            }
        });


        txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fetchData();
                txt_pending.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_payment.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
                txt_cancelled.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_all.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_pending.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_payment.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
                txt_cancelled.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_all.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

                txt_history.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_history.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
            }
        });
        txt_cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPendingOrder("Cancelled");
                statusss = "Cancelled";
                txt_pending.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_payment.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_payment.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_cancelled.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));
                txt_all.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));

                txt_pending.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_cancelled.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
                txt_all.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

                txt_history.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_history.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
            }
        });

        txt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPendingOrder("Accepted");
                statusss = "Accepted";
                txt_pending.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_cancelled.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_all.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t1));

                txt_pending.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_cancelled.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_all.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));

                txt_payment.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_payment.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

                txt_history.setBackground(getActivity().getResources().getDrawable(R.drawable.border_t2));
                txt_history.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));

            }
        });

        return v;
    }

    private void getPendingOrder(String status) {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getTablebooking((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchData() {
       // Log.i("DivyaScroll3:-", offset);
        // custPrograssbar.prograssCreate(Customer_list_activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            // jsonObject.put("offset", offset);
            JsonParser jsonParser = new JsonParser();

            Log.i("DivyaJson", jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getDineinList((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "4");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("DivyaError", e.toString());
        }
    }



    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                BookaTableModel pending = gson.fromJson(result.toString(), BookaTableModel.class);
                if (pending.getResult().equalsIgnoreCase("true")) {

                    if (!pending.getOrderHistory().isEmpty()) {
                        recyclePending.setVisibility(View.VISIBLE);
                        pendinglistMain = pending.getOrderHistory();
                        myOrderAdepter = new PendingAdepter(pendinglistMain);
                        recyclePending.setAdapter(myOrderAdepter);
                    } else {
                        recyclePending.setVisibility(View.GONE);
                    }
                } else {
                    recyclePending.setVisibility(View.GONE);
                }
            }
            else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                TableBookingResponse response = gson.fromJson(result, TableBookingResponse.class);
                Toast.makeText(getActivity(), response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (response.getResult().equalsIgnoreCase("true")) {
                    getPendingOrder(statusss);
                } else {
                    getPendingOrder(statusss);
                }
            }
            else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                GetCount pending = gson.fromJson(result.toString(), GetCount.class);
                if (pending.getResult().equalsIgnoreCase("true")) {
                    Utiles.getDiningValue = pending.getDining();
                    Utiles.getDeliveryvalue = pending.getDelivery();
                    MainHomeFragment.setTextCountData();
                }
            }
            else if (callNo.equalsIgnoreCase("4")) {
                Log.i("DivyaScroll4:-", "-->");
                Gson gson = new Gson();
                DineInResponse customerList = gson.fromJson(result.toString(), DineInResponse.class);

                if (customerList.getResult().equalsIgnoreCase("true")) {
                    Log.i("DivyaScroll5:-", "-->");

                    if (!customerList.getDineInPaymentHistory().isEmpty()) {
                        //   Log.i("DivyaScroll6:-", "-->" + customerList.getUserData().size());

                        //txtNodata.setVisibility(View.GONE);
                        recyclePending.setVisibility(View.VISIBLE);
                       // pendinglistMain = pending.getOrderHistory();
//                        myOrderAdepter = new PendingAdepter(pendinglistMain);
//                        recyclePending.setAdapter(myOrderAdepter);
                        List<DineInPaymentHistoryItem> customerData = customerList.getDineInPaymentHistory();
                        String cust_name = "", cust_mobile = "", cust_email = "", cust_order = "";

                       customerDataAdapter = new CustomerDataAdapter(customerList.getDineInPaymentHistory(), getContext());
                        recyclePending.setAdapter(customerDataAdapter);
                        // }


                    }else {
                        customerDataAdapter = new CustomerDataAdapter(customerList.getDineInPaymentHistory(), getContext());
                        recyclePending.setAdapter(customerDataAdapter);
                    }
                }
            }

        } catch (Exception e) {
            e.toString();
        }

    }


    public class PendingAdepter extends RecyclerView.Adapter<PendingAdepter.ViewHolder> {
        private List<BookaTableModel.OrderHistory> pendinglist;


        public PendingAdepter(List<BookaTableModel.OrderHistory> pendinglist) {
            this.pendinglist = pendinglist;
        }



        @Override
        public PendingAdepter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tablebookinglayout, parent, false);
            PendingAdepter.ViewHolder viewHolder = new PendingAdepter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PendingAdepter.ViewHolder holder,
                                     int position) {
            Log.e("position", "" + position);
            BookaTableModel.OrderHistory order = pendinglist.get(position);
            holder.txtOderid.setText("Booking ID:  #" + order.getId());
            holder.txtDateandstatus.setText(order.getTable_booking_date());

            holder.txt_name.setText(order.getName());
           // holder.txt_address.setText(order.get());
            holder.txt_mobile.setText(order.getCustomer_mobile());
            holder.txt_pmode.setText(order.getTable_booking_time());

            holder.txtPricetotla.setText("Guests : " + order.getNumber_of_guests());
            if (order.getStatus().equals("Cancelled")) {
                holder.txt_rejection.setVisibility(View.VISIBLE);

                if (order.getCancel_dueto().equalsIgnoreCase("Others")) {
                    holder.txt_address.setText("" + " " + order.getCancel_reason());
                } else {
                    holder.txt_address.setText("" + order.getCancel_dueto());
                }

                holder.linAddress.setVisibility(View.VISIBLE);
                //holder.txt_address.setText(order.getCancel_dueto());
                if (order.getCancel_by().equalsIgnoreCase("0")){

                    holder.txt_rejection.setText("Cancelled By Customer");

                }else {
                    holder.txt_rejection.setText("Cancelled By Restaurant");
                }

                holder.txt_rejection.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            } else if (order.getStatus().equals("Completed")) {
                holder.linAddress.setVisibility(View.GONE);
                holder.txt_rejection.setVisibility(View.VISIBLE);
                holder.txt_rejection.setText("Accepted By Restaurant");
                holder.txt_rejection.setTextColor(getActivity().getResources().getColor(R.color.colorGreen));
            } else {

                holder.linAddress.setVisibility(View.GONE);
                holder.txt_rejection.setVisibility(View.GONE);



//                if (order.getOrder_flow_id().equalsIgnoreCase("0")) {
//                    holder.reject.setVisibility(View.VISIBLE);
//                    holder.accept.setVisibility(View.VISIBLE);
//                } else {
//                    holder.reject.setVisibility(View.GONE);
//                    holder.accept.setVisibility(View.GONE);
//                    holder.linvisReject.setVisibility(View.VISIBLE);
//                }

            }
            if (order.getOrder_flow_id().equalsIgnoreCase("0")) {
                holder.reject.setVisibility(View.VISIBLE);
                holder.accept.setVisibility(View.VISIBLE);
                holder.linvisReject.setVisibility(View.GONE);
            }else if (order.getOrder_flow_id().equalsIgnoreCase("1")) {
                holder.reject.setVisibility(View.GONE);
                holder.accept.setVisibility(View.GONE);
                holder.linvisReject.setVisibility(View.VISIBLE);

            } else {
                holder.reject.setVisibility(View.GONE);
                holder.accept.setVisibility(View.GONE);
            }


            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getMackDecision("Accepted", order.getId(),"","");
                }
            });

            holder.Visted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getMackDecision("Visited", order.getId(),"","");
                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCancellationBottomMenu(order.getId(),"Cancelled");
//                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Are you sure for reject order")
//                            .setContentText("")
//                            .setCancelText("No,cancel")
//                            .setConfirmText("Yes,reject it!")
//                            .showCancelButton(true)
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//
//                                    getMackDecision("Cancelled", order.getId(),"","");
//                                    getData();
//                                    sDialog.cancel();
//
//                                }
//                            })
//                            .show();
                }
            });

            holder.notvisted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  getMackDecision("NotVisited", order.getId(),"","");
                  //  showCancellationBottomMenu(order.getId(),"NotVisited");
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure for reject order")
                            .setContentText("")
                            .setCancelText("No,cancel")
                            .setConfirmText("Yes,reject it!")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    getMackDecision("NotVisited", order.getId(),"","");
                                    //getData();
                                    sDialog.cancel();

                                }
                            })
                            .show();
                }
            });

            try {
                TimeAgo2 timeAgo2 = new TimeAgo2();
                holder.date_ago.setText(timeAgo2.covertTimeToText(order.getCreated_at()));
            }catch (Exception e){

            }



        }

        @Override
        public int getItemCount() {
            return pendinglist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.date_ago)
            TextView date_ago;
            @BindView(R.id.txt_oderid)
            TextView txtOderid;
            @BindView(R.id.txt_pricetotla)
            TextView txtPricetotla;
            @BindView(R.id.txt_dateandstatus)
            TextView txtDateandstatus;
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
            @BindView(R.id.txt_rejection)
            TextView txt_rejection;

            @BindView(R.id.txt_address)
            TextView txt_address;

            @BindView(R.id.linvisReject)
            LinearLayout linvisReject;

            @BindView(R.id.linAddress)
            LinearLayout linAddress;

            @BindView(R.id.Visted)
            TextView Visted;
            @BindView(R.id.notvisted)
            TextView notvisted;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.ViewHolder> {
        private List<DineInPaymentHistoryItem> dataItemList;
        Context mContext;

        public CustomerDataAdapter(List<DineInPaymentHistoryItem> dataItemList, Context context) {
            this.dataItemList = dataItemList;
            mContext = context;
        }

        @Override
        public CustomerDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                    int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tablebookinglayout_payment, parent, false);
            CustomerDataAdapter.ViewHolder viewHolder = new CustomerDataAdapter.ViewHolder(view);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(CustomerDataAdapter.ViewHolder holder,
                                     int position) {

            DineInPaymentHistoryItem item = dataItemList.get(position);
            holder.t_amount.setText(sessionManager.getStringData(curruncy) +" " + item.getAmount());

            holder.p_time.setText("" + item.getDateTime());
            holder.txt_ptype.setText("" + item.getPaymentType());

            holder.t_id.setText("#"+item.getTransactionId());





            holder.txtOderid.setText("Booking ID:  #" + item.getTableId());
            holder.txtDateandstatus.setText(item.getDate());

            holder.txt_name.setText(item.getName());
            // holder.txt_address.setText(order.get());
            holder.txt_mobile.setText(item.getTableBookingMobile());
            holder.txt_pmode.setText(item.getTimeSlot());

            holder.txtPricetotla.setText("Guests : " + item.getNumberOfGuests());

            holder.txt_know_more.setText(item.getOrderStatus());

       /* holder.txtCustName.setTypeface(null, Typeface.BOLD);
        holder.txtMobNo.setTypeface(null, Typeface.NORMAL);
        holder.txtEmail.setTypeface(null, Typeface.NORMAL);
        holder.txtOrder.setTypeface(null,Typeface.NORMAL);*/

            // holder.txtOrder.setText(""+item.get());

            try {
              //  TimeAgo2 timeAgo2 = new TimeAgo2();
                holder.date_ago.setText(item.getCreatedAt());
            }catch (Exception e){

            }

        }

        @Override
        public int getItemCount() {
            return dataItemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.date_ago)
            TextView date_ago;
            @BindView(R.id.txt_oderid)
            TextView txtOderid;
            @BindView(R.id.txt_pricetotla)
            TextView txtPricetotla;
            @BindView(R.id.txt_dateandstatus)
            TextView txtDateandstatus;
            @BindView(R.id.txt_pmode)
            TextView txt_pmode;
            @BindView(R.id.txt_name)
            TextView txt_name;
            @BindView(R.id.txt_mobile)
            TextView txt_mobile;
            @BindView(R.id.txt_ptype)
            TextView txt_ptype;
            @BindView(R.id.t_id)
            TextView t_id;
            @BindView(R.id.t_amount)
            TextView t_amount;

            @BindView(R.id.p_time)
            TextView p_time;

            @BindView(R.id.txt_know_more)
            TextView txt_know_more;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public void getData() {

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

    private void getMackDecision(String status, String oid,String canceldueto , String cancelreason) {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("table_id", oid);
            jsonObject.put("status", status);
            jsonObject.put("cancel_dueto", canceldueto);
            jsonObject.put("cancel_reason", cancelreason);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getTableMackDecision((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    String getTheReson = "";

    private void showCancellationBottomMenu(String id,String stat) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());

        String otherResoan = "";
        View sheetView = getLayoutInflater().inflate(R.layout.dine_in_cancellation_reason, null);
        RadioButton rd1 = sheetView.findViewById(R.id.rd1);
        RadioButton rd2 = sheetView.findViewById(R.id.rd2);
        RadioButton rd3 = sheetView.findViewById(R.id.rd3);
        RadioButton rd4 = sheetView.findViewById(R.id.rd4);
        TextInputEditText etText = sheetView.findViewById(R.id.etText);
        View dummy_view = sheetView.findViewById(R.id.dummy_view);
        TextView btn_add_instriction = sheetView.findViewById(R.id.btn_add_instriction);
        ImageView close = sheetView.findViewById(R.id.close);
        getTheReson = "Table not avilable";

        etText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sheetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        sheetView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = sheetView.getRootView().getHeight();
                        int keypadHeight = screenHeight - r.bottom;
                        if (keypadHeight > screenHeight * 0.15) {
                            dummy_view.setVisibility(View.VISIBLE);
                        } else {
                            dummy_view.setVisibility(View.GONE);
                        }
                    }
                });

                return false;
            }
        });

        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheReson = "Table not avilable";
            }
        });
        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheReson = "Close for a private event";
            }
        });

//        rd3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getTheReson = "I am not able to visit.";
//            }
//        });

        rd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheReson = "Others";
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        btn_add_instriction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Do you want to cancel the booking ?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                mBottomSheetDialog.cancel();
                                getMackDecision(stat, id,getTheReson,etText.getText().toString());
                                //getData();
                                //cancelDinein(getTheReson, otherResoan, item.getTable_id());
                            }
                        })
                        .show();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

}