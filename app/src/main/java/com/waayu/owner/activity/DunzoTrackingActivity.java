package com.waayu.owner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.adepter.DunzoTrackingAdapter;
import com.waayu.owner.fregment.QRListFragment;
import com.waayu.owner.model.DunzoResponse;
import com.waayu.owner.model.QrModel;
import com.waayu.owner.model.Track.OrderTrackResponse;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

public class DunzoTrackingActivity extends AppCompatActivity implements GetResult.MyListener{
    SessionManager sessionManager;
    TextView txt_orderid;
    CustPrograssbar custPrograssbar;
    RecyclerView recyclerView;
    ArrayList<DunzoResponse> dunzoResponses =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dunzo_tracking);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        txt_orderid = findViewById(R.id.txt_orderid);
        recyclerView=findViewById(R.id.recycler_dunzo);
        txt_orderid.setText("ORDER ID#" + SessionManager.oid);

        getlisttrack();


    }

    private void getlisttrack() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", SessionManager.oid);
            // jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getdunzotrack((JsonObject) jsonParser.parse(jsonObject.toString()));
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

                JSONObject jsonObject = new JSONObject(new Gson().toJson(result));

                if (jsonObject.getString("Result").equalsIgnoreCase("true")){
                    //txt_cust.setText(""+jsonObject.getString("total"));


                    for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {

                        DunzoResponse dunzoResponse = new DunzoResponse(jsonObject.getJSONArray("data").getJSONObject(i).getString("response"),
                                jsonObject.getJSONArray("data").getJSONObject(i).getString("created_at"),
                                jsonObject.getJSONArray("data").getJSONObject(i).getString("order_id"),
                                jsonObject.getJSONArray("data").getJSONObject(i).getString("taskid")

                        );

                        dunzoResponses.add(dunzoResponse);

                        recyclerView.setAdapter(new DunzoTrackingAdapter(DunzoTrackingActivity.this,dunzoResponses));
                    }



                }




            }


        } catch (Exception e) {
            e.toString();
        }
    }
}