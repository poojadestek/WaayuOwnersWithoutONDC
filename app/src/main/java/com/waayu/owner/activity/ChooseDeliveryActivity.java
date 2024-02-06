package com.waayu.owner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waayu.owner.R;
import com.waayu.owner.adepter.DeliveryOptionAdapter;
import com.waayu.owner.fregment.MainHomeFragment;
import com.waayu.owner.model.DeliveryOptionModel;
import com.waayu.owner.model.GetCount;
import com.waayu.owner.model.RestResponse;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.utils.Utiles;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

public class ChooseDeliveryActivity extends AppCompatActivity implements GetResult.MyListener {
    Store user;
    SessionManager sessionManager;
    TextView txt_orderid, times, waitingtxt, waitord, txt_dmode;
    RecyclerView recyclerView;
    LinearLayout linDelivery;
    ImageView plus, minus;
    public static String doid = "", dmode = "";
    CustPrograssbar custPrograssbar;

    int timevar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_delivery);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        dmode = getIntent().getStringExtra("d_mode");
        txt_orderid = findViewById(R.id.txt_orderid);
        times = findViewById(R.id.times);
        txt_dmode = findViewById(R.id.txt_dmode);

        waitingtxt = findViewById(R.id.waitingtxt);
        waitord = findViewById(R.id.wait_ord);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        linDelivery = findViewById(R.id.linDelivery);

        if (dmode.equalsIgnoreCase("0")) {
            txt_dmode.setVisibility(View.GONE);
            linDelivery.setVisibility(View.VISIBLE);
        } else if (dmode.equalsIgnoreCase("1")) {
            txt_dmode.setVisibility(View.VISIBLE);
            txt_dmode.setText("Delivery Option: Self Pickup");
            linDelivery.setVisibility(View.GONE);
            doid="0";
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timevar = timevar + 5;
                times.setText("" + timevar);
                waitingtxt.setText("Std. Waiting: " + timevar + " Mins");
                waitord.setText("Waiting for order: " + timevar + " Mins");

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timevar > 15) {
                    timevar = timevar - 5;
                    times.setText("" + timevar);
                    waitingtxt.setText("Std. Waiting: " + timevar + " Mins");
                    waitord.setText("Waiting for order: " + timevar + " Mins");

                }

            }
        });

        txt_orderid.setText("ORDER ID#" + SessionManager.oid);
        recyclerView = findViewById(R.id.recycler);
        getlistdelivery();
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doid.equalsIgnoreCase("")) {
                    Toast.makeText(ChooseDeliveryActivity.this, "Please choose delivery option", Toast.LENGTH_SHORT).show();
                } else {
                    getCountData();
                    getMackDecision("1", SessionManager.oid, String.valueOf(timevar));
                }
            }
        });

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

    private void getlistdelivery() {
        //custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            jsonObject.put("oid", SessionManager.oid);
            // jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getDeliverylist((JsonObject) jsonParser.parse(jsonObject.toString()));
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
            //custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                DeliveryOptionModel deliveryOption = gson.fromJson(result.toString(), DeliveryOptionModel.class);
                waitingtxt.setText("Std. Waiting: " + deliveryOption.getmWaitingTime() + " Mins");
                waitord.setText("Waiting for order: " + deliveryOption.getmWaitingTime() + " Mins");
                timevar = Integer.parseInt(deliveryOption.getmWaitingTime());
                times.setText("" + String.valueOf(timevar));
                recyclerView.setAdapter(new DeliveryOptionAdapter(deliveryOption.getOrderData(), ChooseDeliveryActivity.this));

            } else if (callNo.equalsIgnoreCase("2")) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result, RestResponse.class);
                Toast.makeText(getApplicationContext(), response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (response.getResult().equalsIgnoreCase("true")) {

                    finish();

                } else {

                    //finish();
                }
            }else if (callNo.equalsIgnoreCase("3")) {
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


    private void getMackDecision(String status, String oid, String time) {
        custPrograssbar.prograssCreate(ChooseDeliveryActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", oid);
            jsonObject.put("status", status);
            jsonObject.put("d_partner", doid);
            jsonObject.put("dtime", time);
            JsonParser jsonParser = new JsonParser();

            Log.i("makedecisionjson", String.valueOf((JsonObject) jsonParser.parse(jsonObject.toString())));

            Call<JsonObject> call = APIClient.getInterface().getMackDecision((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}