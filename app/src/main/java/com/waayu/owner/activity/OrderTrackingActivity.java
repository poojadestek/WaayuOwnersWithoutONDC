package com.waayu.owner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.waayu.owner.R;
import com.waayu.owner.model.Track.OrderTrackResponse;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

public class OrderTrackingActivity extends AppCompatActivity implements GetResult.MyListener {
    SessionManager sessionManager;
    TextView txt_orderid, name, mob, text1, text2, text3, text4, text5, text6;
    CustPrograssbar custPrograssbar;
    ImageView img1, img2, img3, img4, img5, img6;
    TextView d1,d2,d3,d4,d5,d6;
    String dmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        txt_orderid = findViewById(R.id.txt_orderid);
        dmode = getIntent().getStringExtra("d_mode");





        name = findViewById(R.id.txt_names);
        mob = findViewById(R.id.txt_mobile);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);

        d1=findViewById(R.id.d1);
        d2=findViewById(R.id.d2);
        d3=findViewById(R.id.d3);
        d4=findViewById(R.id.d4);
        d5=findViewById(R.id.d5);
        d6=findViewById(R.id.d6);



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

        txt_orderid.setText("ORDER ID#" + SessionManager.oid);

        name.setText(SessionManager.nme);
        mob.setText(SessionManager.mobl);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getlisttrack();

    }

    private void getlisttrack() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", SessionManager.oid);
            // jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getordertrack((JsonObject) jsonParser.parse(jsonObject.toString()));
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

                OrderTrackResponse deliveryOption = gson.fromJson(result.toString(), OrderTrackResponse.class);


                if (deliveryOption.getData().size() == 1) {
                    img1.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    text1.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    d1.setText(deliveryOption.getData().get(0).getDateTime());
                } else if (deliveryOption.getData().size() == 2) {
                    img1.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img2.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    text1.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text2.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    d2.setText(deliveryOption.getData().get(1).getDateTime());
                } else if (deliveryOption.getData().size() == 3) {
                    img1.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img2.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img3.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    text1.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text2.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text3.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    d3.setText(deliveryOption.getData().get(2).getDateTime());

                } else if (deliveryOption.getData().size() == 4) {
                    img1.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img2.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img3.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img4.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    text1.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text2.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text3.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text4.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    d4.setText(deliveryOption.getData().get(3).getDateTime());
                } else if (deliveryOption.getData().size() == 5) {
                    if (dmode.equalsIgnoreCase("0")) {
                        img1.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img2.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img3.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img4.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img5.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        text1.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text2.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text3.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text4.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text5.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        d5.setText(deliveryOption.getData().get(4).getDateTime());
                    } else {
                        img1.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img2.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img3.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img4.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        img6.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                        text1.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text2.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text3.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text4.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        text6.setTextColor(ContextCompat.getColor(this, R.color.prim));
                        d6.setText(deliveryOption.getData().get(4).getDateTime());
                    }

                } else if (deliveryOption.getData().size() == 6) {
                    img1.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img2.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img3.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img4.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img5.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    img6.setColorFilter(ContextCompat.getColor(this, R.color.prim), android.graphics.PorterDuff.Mode.SRC_IN);
                    text1.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text2.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text3.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text4.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text5.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    text6.setTextColor(ContextCompat.getColor(this, R.color.prim));
                    d6.setText(deliveryOption.getData().get(5).getDateTime());

                }


            }


        } catch (Exception e) {
            e.toString();
        }
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