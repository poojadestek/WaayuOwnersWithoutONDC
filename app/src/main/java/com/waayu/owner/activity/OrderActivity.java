package com.waayu.owner.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.waayu.owner.R;
import com.waayu.owner.model.OrderDetail;
import com.waayu.owner.model.OrderItemsItem;
import com.waayu.owner.model.RestResponse;
import com.waayu.owner.model.RiderDataItem;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.waayu.owner.retrofit.APIClient.baseUrl;
import static com.waayu.owner.utils.SessionManager.curruncy;


public class OrderActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_rtitle)
    TextView txtRtitle;
    @BindView(R.id.txt_ctitle)
    TextView txtCtitle;
    @BindView(R.id.txt_rlocation)
    TextView txtRlocation;
    @BindView(R.id.txt_customer)
    TextView txtCustomer;
    @BindView(R.id.txt_caddress)
    TextView txtCaddress;
    @BindView(R.id.lvl_completdate)
    LinearLayout lvlCompletdate;
    @BindView(R.id.lvl_rider)
    LinearLayout lvlRider;
    @BindView(R.id.txt_deliveryboy)
    TextView txtDeliveryboy;
    @BindView(R.id.txt_completdate)
    TextView txtCompletdate;
    @BindView(R.id.lvl_itmelist)
    LinearLayout lvlItmelist;
    @BindView(R.id.txt_itemtotal)
    TextView txtItemtotal;
    @BindView(R.id.txt_dcharge)
    TextView txtDcharge;
    @BindView(R.id.lvl_discount)
    LinearLayout lvlDiscount;
    @BindView(R.id.txt_discount)
    TextView txtDiscount;
    @BindView(R.id.txt_pmethod)
    TextView txtPmethod;
    @BindView(R.id.txt_topay)
    TextView txtTopay;
    @BindView(R.id.txt_orderid)
    TextView txtOrderid;

    @BindView(R.id.lvl_deliverytips)
    LinearLayout lvlDeliverytips;
    @BindView(R.id.txt_dtips)
    TextView txtDtips;
    @BindView(R.id.lvl_storecharge)
    LinearLayout lvlStorecharge;
    @BindView(R.id.txt_storecharge)
    TextView txtStorecharge;
    @BindView(R.id.lvl_texandcharge)
    LinearLayout lvlTexandcharge;
    @BindView(R.id.txt_taxcharge)
    TextView txtTaxcharge;
    @BindView(R.id.lvl_wallet)
    LinearLayout lvlWallet;

    @BindView(R.id.txt_wallet)
    TextView txtWallet;

    @BindView(R.id.img_rider)
    ImageView imgRider;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_otp)
    TextView txtOtp;


    @BindView(R.id.txt_oderid)
    TextView txt_oderid;
    @BindView(R.id.txt_pricetotla)
    TextView txt_pricetotla;
    @BindView(R.id.dates)
    TextView dates;
    @BindView(R.id.item)
    TextView item;
    @BindView(R.id.txt_pmode)
    TextView txt_pmode;
    @BindView(R.id.txt_orderplace)
    TextView txt_orderplace;
    @BindView(R.id.txt_names)
    TextView txt_names;
    @BindView(R.id.txt_mobile)
    TextView txt_mobile;
    @BindView(R.id.txt_rdist_time)
    TextView txt_distance_time;


    @BindView(R.id.lvl_makedecision)
    LinearLayout lvlMakedecision;
    @BindView(R.id.txt_makedecision)
    TextView txtMakedecision;


    @BindView(R.id.txt_taskid)
    TextView txt_taskid;

    @BindView(R.id.update_status)
    LinearLayout update_status;

    @BindView(R.id.update_status_cancel)
    LinearLayout update_status_cancel;

    @BindView(R.id.txt_dmode)
    TextView txt_dmode;

    @BindView(R.id.track)
    TextView track;

    @BindView(R.id.txt_additional_notes)
    TextView txtAdditionalNotes;
    @BindView(R.id.lvl_notes)
    LinearLayout lvlNotes;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    String oid, rid, dmode;
    Store store;
    String Rstatus = "";
    String tid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        oid = getIntent().getStringExtra("oid");
        dmode = getIntent().getStringExtra("d_mode");
        store = sessionManager.getUserDetails("");

        getOrderItem();
        txt_orderplace.setText(SessionManager.plod);
        dates.setText("" + SessionManager.dateod);
        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.oid = oid;
                SessionManager.caddress = orderDetail.getOrderInfo().getCustAddress();
                Intent intent = new Intent(OrderActivity.this, ChooseDeliveryActivity.class);
                intent.putExtra("d_mode", dmode);
                startActivity(intent);
                finish();
                //finish();
            }
        });

        txt_taskid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(""+txt_taskid.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("text label",""+txt_taskid.getText().toString());
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(OrderActivity.this, "Copied Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMackDecision("2");
            }
        });
        findViewById(R.id.track).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderDetail.getOrderInfo().getDp_type().equalsIgnoreCase("3")){
                    SessionManager.oid = oid;
                    Intent intent = new Intent(OrderActivity.this, DunzoTrackingActivity.class);
                    intent.putExtra("d_mode", dmode);
                    startActivity(intent);
                }else {
                    SessionManager.oid = oid;
                    Intent intent = new Intent(OrderActivity.this, OrderTrackingActivity.class);
                    intent.putExtra("d_mode", dmode);
                    startActivity(intent);
                }


            }
        });

        findViewById(R.id.update_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSingleChoiceDialog();

            }
        });

        findViewById(R.id.update_status_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText inputEditTextField = new EditText(OrderActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("Dunzo Cancel Order")
                        .setMessage("Enter Reason for cancel order")
                        .setView(inputEditTextField)
                        .setPositiveButton("Cancel Order", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String editTextInput = inputEditTextField.getText().toString();
                                Log.d("onclick","editext value is: "+ editTextInput);
                                getstatuscancel(editTextInput+"");
                            }
                        })
                        .setNegativeButton("Back", null)
                        .create();
                dialog.show();


            }
        });
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

//    private static final String[] RINGTONE = new String[]{
//            "OnRoute", "Complete"
//    };

    private String single_choice_selected;
    String[] RINGTONE;

    private void showSingleChoiceDialog() {
        if (Rstatus.equalsIgnoreCase("Processing")) {
            if (dmode.equalsIgnoreCase("1")) {
                RINGTONE = new String[]{
                        "Ready for pickup", "Complete"
                };
            } else {
                RINGTONE = new String[]{
                        "Ready for pickup", "OnRoute", "Complete"
                };
            }

        } else if (Rstatus.equalsIgnoreCase("Ready For Pickup")) {
            if (dmode.equalsIgnoreCase("1")) {
                RINGTONE = new String[]{
                        "Complete"
                };
            } else {
                RINGTONE = new String[]{
                        "OnRoute", "Complete"
                };
            }

        }
//        else if (Rstatus.equalsIgnoreCase("Pending")) {
//            RINGTONE = new String[]{
//                    "Accept as a delivery boy"
//            };
//        }
        else {
            RINGTONE = new String[]{
                    "Complete"
            };
        }
        single_choice_selected = RINGTONE[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Order Status");
        builder.setSingleChoiceItems(RINGTONE, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                single_choice_selected = RINGTONE[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (single_choice_selected.equalsIgnoreCase("OnRoute")) {
                    if (Rstatus.equalsIgnoreCase("Processing") || Rstatus.equalsIgnoreCase("Ready For Pickup")) {
                        getstatusupdate("pickup");
                    } else {
                        Toast.makeText(OrderActivity.this, "This Status Already updated", Toast.LENGTH_SHORT).show();
                    }
                } else if (single_choice_selected.equalsIgnoreCase("Ready for pickup")) {
                    getstatusupdate("readyforpickup");
                } else if (single_choice_selected.equalsIgnoreCase("Complete")) {
                    getstatusupdate("complete");
                } else if (single_choice_selected.equalsIgnoreCase("Accept as a delivery boy")) {
                    getstatusupdate("accept");
                }
                //Snackbar.make(parent_view, "selected : " + single_choice_selected, Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }



    private void getOrderItem() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", oid);
            jsonObject.put("sid", store.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getOrderDetail((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getstatusupdate(String status) {
        custPrograssbar.prograssCreate(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", Integer.parseInt(rid));
            jsonObject.put("oid", oid);
            jsonObject.put("status", status);


            JsonParser jsonParser = new JsonParser();
            Log.i("getstatusjosn", String.valueOf((JsonObject) jsonParser.parse(jsonObject.toString())));
            Call<JsonObject> call = APIClient.getInterface().getorderstatus((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getstatuscancel(String cancellation_reason) {
        custPrograssbar.prograssCreate(this);

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("task_id", tid);
            jsonObject.put("cancellation_reason", cancellation_reason);


            JsonParser jsonParser = new JsonParser();
            Log.i("getstatusjosn", String.valueOf((JsonObject) jsonParser.parse(jsonObject.toString())));
            Call<JsonObject> call = APIClient.getInterface().e_dunzo_order_cancle((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "4");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    OrderDetail orderDetail;

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                orderDetail = gson.fromJson(result.toString(), OrderDetail.class);
                if (orderDetail.getResult().equalsIgnoreCase("true")) {
                    txtRtitle.setText("" + orderDetail.getOrderInfo().getRestName());
                    txtRlocation.setText("" + orderDetail.getOrderInfo().getRestAddress());
                    txtCustomer.setText("" + orderDetail.getOrderInfo().getAddressType());
                    txtCtitle.setText("" + orderDetail.getOrderInfo().getCustomer_name());
                    txtCaddress.setText("" + orderDetail.getOrderInfo().getCustAddress());
                    if (!orderDetail.getOrderInfo().getaNote().isEmpty()) {
                        lvlNotes.setVisibility(View.VISIBLE);
                        txtAdditionalNotes.setText(orderDetail.getOrderInfo().getaNote());
                    } else {
                        lvlNotes.setVisibility(View.GONE);
                    }

                    txt_oderid.setText("ORDERID #" + orderDetail.getOrderInfo().getOrderId());
                    SessionManager.nme = orderDetail.getOrderInfo().getCustomer_name();
                    SessionManager.mobl = orderDetail.getOrderInfo().getCustomer_mobile();

                    txtItemtotal.setText(sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getSubtotal());
                    txtDcharge.setText("+" + sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getDeliveryCharge());

                    if (dmode.equalsIgnoreCase("1")) {
                        txt_dmode.setText("Self Pickup");
                    } else {
                        txt_dmode.setText("Delivery");
                    }

                    if (orderDetail.getOrderInfo().getCouAmt() != null && orderDetail.getOrderInfo().getCouAmt().equalsIgnoreCase("0")) {
                        lvlDiscount.setVisibility(View.GONE);
                    } else {
                        txtDiscount.setText("-" + sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getCouAmt());
                    }

                    if (orderDetail.getOrderInfo().getTax() != null && orderDetail.getOrderInfo().getTax().equalsIgnoreCase("0")) {
                        lvlTexandcharge.setVisibility(View.GONE);
                    } else {
                        txtTaxcharge.setText("+" + sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getTax());
                    }

                    if (orderDetail.getOrderInfo().getRestCharge() != null && orderDetail.getOrderInfo().getRestCharge().equalsIgnoreCase("0")) {
                        lvlStorecharge.setVisibility(View.GONE);
                    } else {
                        txtStorecharge.setText("+" + sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getRestCharge());
                    }
                    if (orderDetail.getOrderInfo().getRiderTip() != null && orderDetail.getOrderInfo().getRiderTip().equalsIgnoreCase("0")) {
                        lvlDeliverytips.setVisibility(View.GONE);
                    } else {
                        txtDtips.setText(sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getRiderTip());
                    }

                    if (orderDetail.getOrderInfo().getWallAmt() != null && orderDetail.getOrderInfo().getWallAmt().equalsIgnoreCase("0")) {
                        lvlWallet.setVisibility(View.GONE);
                    } else {
                        txtWallet.setText("-" + sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getWallAmt());
                    }


                    txtTopay.setText(sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getOrderTotal());

                    txt_pricetotla.setText("Total :- " + sessionManager.getStringData(curruncy) + orderDetail.getOrderInfo().getOrderTotal());
                    txt_pmode.setText("" + orderDetail.getOrderInfo().getPMethodName());

                    item.setText("" + orderDetail.getOrderInfo().getOrderItems().size());
                    txt_names.setText("" + orderDetail.getOrderInfo().getCustomer_name());
                    txt_mobile.setText("" + orderDetail.getOrderInfo().getCustomer_mobile());

                    txtPmethod.setText("" + orderDetail.getOrderInfo().getPMethodName());
                    txtOrderid.setText("ORDERID #" + orderDetail.getOrderInfo().getOrderId());
                    txt_distance_time.setText(orderDetail.getOrderInfo().getRestDistance());
                    Rstatus = orderDetail.getOrderInfo().getOStatus();
                    tid =orderDetail.getOrderInfo().getDunzo_task_id();

                    rid = orderDetail.getOrderInfo().getRid();


                    if (orderDetail.getOrderInfo().getDp_type().equalsIgnoreCase("3")){
                        findViewById(R.id.lyt_task).setVisibility(View.VISIBLE);
                        txt_taskid.setText(""+orderDetail.getOrderInfo().getDunzo_task_id());
                        track.setText("Dunzo Tracking");
                    }else {
                        findViewById(R.id.lyt_task).setVisibility(View.GONE);
                        track.setText("Tracking Order");
                    }


                    if (orderDetail.getOrderInfo().getOStatus().equalsIgnoreCase("Completed")) {
                        update_status.setVisibility(View.GONE);
                        update_status_cancel.setVisibility(View.GONE);
                        lvlCompletdate.setVisibility(View.VISIBLE);
                        txtDeliveryboy.setText("" + orderDetail.getOrderInfo().getRiderName());
                        txtCompletdate.setText("" + orderDetail.getOrderInfo().getOrderCompleteDate());
                    } else if (orderDetail.getOrderInfo().getOStatus().equalsIgnoreCase("Processing") || orderDetail.getOrderInfo().getOStatus().equalsIgnoreCase("On Route") || orderDetail.getOrderInfo().getOStatus().equalsIgnoreCase("Ready for pickup")) {


                        if (orderDetail.getOrderInfo().getDp_type().equalsIgnoreCase("3")){

                            if (orderDetail.getOrderInfo().getOStatus().equalsIgnoreCase("Processing")  || orderDetail.getOrderInfo().getOStatus().equalsIgnoreCase("Ready for pickup")){
                                update_status_cancel.setVisibility(View.VISIBLE);
                                update_status.setVisibility(View.GONE);
                                lvlRider.setVisibility(View.VISIBLE);
                                txtName.setText("" + orderDetail.getOrderInfo().getRiderName());
                                txtOtp.setText("OTP - " + orderDetail.getOrderInfo().getOtp());
                                Glide.with(this).load(orderDetail.getOrderInfo().getRiderImg()).placeholder(R.drawable.slider).into(imgRider);
                            }else {
                                update_status_cancel.setVisibility(View.GONE);
                                update_status.setVisibility(View.GONE);
                                lvlRider.setVisibility(View.VISIBLE);
                                txtName.setText("" + orderDetail.getOrderInfo().getRiderName());
                                txtOtp.setText("OTP - " + orderDetail.getOrderInfo().getOtp());
                                Glide.with(this).load(orderDetail.getOrderInfo().getRiderImg()).placeholder(R.drawable.slider).into(imgRider);
                            }


                        }else {
                            update_status.setVisibility(View.VISIBLE);
                            lvlRider.setVisibility(View.VISIBLE);
                            txtName.setText("" + orderDetail.getOrderInfo().getRiderName());
                            txtOtp.setText("OTP - " + orderDetail.getOrderInfo().getOtp());
                            Glide.with(this).load(baseUrl + orderDetail.getOrderInfo().getRiderImg()).placeholder(R.drawable.slider).into(imgRider);
                        }
                    }

                    if (orderDetail.getOrderInfo().getOrderFlowId().equalsIgnoreCase("0")) {
                        lvlMakedecision.setVisibility(View.VISIBLE);

                    } else if (orderDetail.getOrderInfo().getOrderFlowId().equalsIgnoreCase("1")) {
                        if (orderDetail.getOrderInfo().getDp_type().equalsIgnoreCase("3")){
                            update_status.setVisibility(View.GONE);
                            update_status_cancel.setVisibility(View.VISIBLE);
                        }else {
                            update_status.setVisibility(View.GONE);
                            update_status_cancel.setVisibility(View.GONE);

                        }


                    }


                    setNotiList(lvlItmelist, orderDetail.getOrderInfo().getOrderItems());
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result, RestResponse.class);
                Toast.makeText(this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (response.getResult().equalsIgnoreCase("true")) {
                    lvlMakedecision.setVisibility(View.GONE);
                    finish();
                } else {
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(new Gson().toJson(result));
//                //RestResponse response = gson.fromJson(result, RestResponse.class);
                Toast.makeText(this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                if (jsonObject.getString("Result").equalsIgnoreCase("true")) {

                    // lvlMakedecision.setVisibility(View.GONE);
                    finish();
                } else {

                }
            }

            else if (callNo.equalsIgnoreCase("4")) {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(new Gson().toJson(result));
                //RestResponse response = gson.fromJson(result, RestResponse.class);
                Toast.makeText(this, jsonObject.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                if (jsonObject.getString("Result").equalsIgnoreCase("true")) {

                    update_status_cancel.setVisibility(View.GONE);
                    getOrderItem();
                    // lvlMakedecision.setVisibility(View.GONE);
                   // finish();
                } else {

                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    private void setNotiList(LinearLayout lnrView, List<OrderItemsItem> list) {
        lnrView.removeAllViews();


        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(OrderActivity.this);

            View view = inflater.inflate(R.layout.item_orderitem, null);

            TextView txtTitel = view.findViewById(R.id.txt_title);
            TextView txtPextra = view.findViewById(R.id.txt_pextra);
            TextView txtPrice = view.findViewById(R.id.txt_price);

            switch (list.get(i).getIsVeg()) {
                case "0":
                    txtTitel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nonveg, 0, 0, 0);
                    break;
                case "1":
                    txtTitel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veg, 0, 0, 0);
                    break;
                case "2":
                    txtTitel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veg, 0, 0, 0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + list.get(i).getIsVeg());
            }
            txtTitel.setText("" + list.get(i).getItemName());
            txtPextra.setText("" + list.get(i).getItemAddon());
            txtPrice.setText("" + sessionManager.getStringData(curruncy) + list.get(i).getItemTotal());
            lnrView.addView(view);

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.img_back, R.id.txt_makedecision, R.id.img_call})
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.txt_makedecision:
                bottonOrderMakeDecision();
                break;


            case R.id.img_back:
                finish();
                break;
            case R.id.img_call:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + orderDetail.getOrderInfo().getRiderMobile()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getMackDecision(String status) {
        custPrograssbar.prograssCreate(this);
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

    public void bottonOrderMakeDecision() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.order_makedecision, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView txtAccept = sheetView.findViewById(R.id.txt_accept);
        TextView txtReject = sheetView.findViewById(R.id.txt_reject);
        txtAccept.setOnClickListener(view -> {
            mBottomSheetDialog.cancel();
            SessionManager.oid = oid;
            SessionManager.caddress = orderDetail.getOrderInfo().getCustAddress();
            Intent intent = new Intent(OrderActivity.this, ChooseDeliveryActivity.class);
            startActivity(intent);
            //getMackDecision("1");
        });
        txtReject.setOnClickListener(view -> {
            mBottomSheetDialog.cancel();
            getMackDecision("2");
        });
        mBottomSheetDialog.show();
    }

    List<RiderDataItem> arrayList = new ArrayList<>();


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getOrderItem();
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}