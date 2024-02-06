package com.waayu.owner.fregment;

import static com.waayu.owner.utils.SessionManager.curruncy;

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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.model.BookaTableModel;
import com.waayu.owner.model.CustomerData;
import com.waayu.owner.model.CustomerList;
import com.waayu.owner.model.Dinein.DineInPaymentHistoryItem;
import com.waayu.owner.model.Dinein.DineInResponse;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;


public class DineInListFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.recycle_customer)
    RecyclerView recycle_customer;
    @BindView(R.id.txt_cust)
    TextView txt_cust;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    LinearLayoutManager layoutManager;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store user;
    @BindView(R.id.txtNodata)
    TextView txtNodata;

    @BindView(R.id.txt_titel)
    TextView txt_titel;
    CustomerDataAdapter customerDataAdapter;
    private int currentPage = 0;
    boolean reload = false;
    public static ArrayList<CustomerData> resultArrayList;
    @BindView(R.id.iv_notification)
    ImageView ivNotification;


    public DineInListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ButterKnife.bind(this, view);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails("");


        view.findViewById(R.id.lin_total_customer).setVisibility(View.GONE);
        txt_titel.setText("Todays Dine-In Booking");
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_customer.setLayoutManager(layoutManager);

        fetchData(String.valueOf(currentPage));

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NotificationFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack(null).commit();
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new HomeFragment();
                if (fragment != null) {
                    Utiles.backPress = false;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack(null).commit();
                }
            }
        });


//        recycle_customer.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                currentItems = layoutManager.getChildCount();
//                totalItems = layoutManager.getItemCount();
//                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
//                Log.i("DivyaScroll:-", +currentItems + "--" + totalItems + "--" + scrollOutItems);
//
//                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
//                    isScrolling = false;
//                    Log.i("DivyaScroll1:-", String.valueOf(currentPage));
//                    currentPage = currentPage + 10;
//                    Log.i("DivyaScroll2:-", String.valueOf(currentPage));
//                    reload = true;
//                    fetchData(String.valueOf(currentPage));
//
//                }
//            }
//        });
        return view;
    }

    private void getNotification() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getNoti((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchData(String offset) {
        Log.i("DivyaScroll3:-", offset);
        // custPrograssbar.prograssCreate(Customer_list_activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
           // jsonObject.put("offset", offset);
            JsonParser jsonParser = new JsonParser();

            Log.i("DivyaJson", jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().gettodayDineinList((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("DivyaError", e.toString());
        }
    }

    PendingAdepter myOrderAdepter;

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            // custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Log.i("DivyaScroll4:-", "-->");
                Gson gson = new Gson();
                BookaTableModel pending = gson.fromJson(result.toString(), BookaTableModel.class);
                if (pending.getResult().equalsIgnoreCase("true")) {

                    if (!pending.getOrderHistory().isEmpty()) {
                        recycle_customer.setVisibility(View.VISIBLE);
                        //pendinglistMain = pending.getOrderHistory();
                        myOrderAdepter = new PendingAdepter(pending.getOrderHistory());
                        recycle_customer.setAdapter(myOrderAdepter);
                    } else {
                        recycle_customer.setVisibility(View.GONE);
                    }
                } else {
                    recycle_customer.setVisibility(View.GONE);
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                TableBookingResponse response = gson.fromJson(result, TableBookingResponse.class);
                Toast.makeText(getActivity(), response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (response.getResult().equalsIgnoreCase("true")) {
                   fetchData("0");
                } else {
                    fetchData("0");
                }
            }

        } catch (Exception e) {
            e.toString();
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
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dinein_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {

            DineInPaymentHistoryItem item = dataItemList.get(position);
            holder.txtCustName.setText(sessionManager.getStringData(curruncy) +" " + item.getAmount());
            holder.txtMobNo.setText("Name - " + item.getName());
            holder.txtEmail.setText("Mobile No - " + item.getTableBookingMobile());
            holder.txtOrder.setText("" + item.getDateTime());
            holder.txt_status.setText("" + item.getOrderStatus());

            holder.txt_oid.setText("OrderId - #"+item.getId());


            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.poppins_regular);
            Typeface typeface1 = ResourcesCompat.getFont(mContext, R.font.poppins_bold);
            holder.txtCustName.setTypeface(typeface1);
            holder.txtMobNo.setTypeface(typeface);
            holder.txtEmail.setTypeface(typeface);
            holder.txtOrder.setTypeface(typeface);

       /* holder.txtCustName.setTypeface(null, Typeface.BOLD);
        holder.txtMobNo.setTypeface(null, Typeface.NORMAL);
        holder.txtEmail.setTypeface(null, Typeface.NORMAL);
        holder.txtOrder.setTypeface(null,Typeface.NORMAL);*/

            // holder.txtOrder.setText(""+item.get());


        }

        @Override
        public int getItemCount() {
            return dataItemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txt_cust_name)
            TextView txtCustName;
            @BindView(R.id.txt_mob_no)
            TextView txtMobNo;
            @BindView(R.id.txt_order)
            TextView txtOrder;
            @BindView(R.id.txt_email_id)
            TextView txtEmail;

            @BindView(R.id.txt_status)
            TextView txt_status;

            @BindView(R.id.txt_oid)
            TextView txt_oid;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
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
                holder.linAddress.setVisibility(View.VISIBLE);

                if (order.getCancel_dueto().equalsIgnoreCase("Others")) {
                    holder.txt_address.setText("" +  " " + order.getCancel_reason());
                } else {
                    holder.txt_address.setText("" + order.getCancel_dueto());
                }
                if (order.getCancel_by().equalsIgnoreCase("0")){

                    holder.txt_rejection.setText("Cancelled By Customer");

                }else {
                    holder.txt_rejection.setText("Cancelled By Restaurant");
                }
                holder.txt_rejection.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
            } else if (order.getStatus().equals("Completed")) {
                holder.txt_rejection.setVisibility(View.VISIBLE);
                holder.txt_rejection.setText("Accepted By Restaurant");
                holder.txt_rejection.setTextColor(getActivity().getResources().getColor(R.color.colorGreen));
            } else {

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
            @BindView(R.id.linAddress)
            LinearLayout linAddress;
            @BindView(R.id.linvisReject)
            LinearLayout linvisReject;

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

    String getTheReson = "";
    private void showCancellationBottomMenu(String id,String stat) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());

        String otherResoan = "";
        View sheetView = getLayoutInflater().inflate(R.layout.dine_in_cancellation_reason, null);
        RadioButton rd1 = sheetView.findViewById(R.id.rd1);
        RadioButton rd2 = sheetView.findViewById(R.id.rd2);
        RadioButton rd3 = sheetView.findViewById(R.id.rd3);
        RadioButton rd4 = sheetView.findViewById(R.id.rd4);
        View dummy_view = sheetView.findViewById(R.id.dummy_view);
        TextInputEditText etText = sheetView.findViewById(R.id.etText);
        TextView btn_add_instriction = sheetView.findViewById(R.id.btn_add_instriction);
        ImageView close = sheetView.findViewById(R.id.close);
        getTheReson = "Item not available";
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
                System.out.println("abc :- "+etText.getText().toString());
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

