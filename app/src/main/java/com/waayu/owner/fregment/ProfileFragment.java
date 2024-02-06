package com.waayu.owner.fregment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.waayu.owner.R;
import com.waayu.owner.activity.LoginActivity;
import com.waayu.owner.activity.ProfileActivity;
import com.waayu.owner.model.Ostatus;
import com.waayu.owner.model.RestResponse;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ProfileFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.img_logout)
    ImageView imgLogout;
    @BindView(R.id.ed_username)
    TextView edUsername;
    @BindView(R.id.ed_email)
    TextView edEmail;
    @BindView(R.id.ed_phone)
    TextView edPhone;
    @BindView(R.id.switch1)
    Switch switch1;

    @BindView(R.id.txt_status)
    TextView txtStatus;









    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    SessionManager sessionManager;
    Store user;
    CustPrograssbar custPrograssbar;
    String number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        ProfileActivity.listener = this;
        sessionManager = new SessionManager(getActivity());
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        edUsername.setText("" + user.getName());
        edEmail.setText("" + user.getEmail());
        edPhone.setText("" + user.getMobile());

        getmobinformation();
        view.findViewById(R.id.cc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dashboard.ccavenue.com/jsp/merchant/merchantLogin.jsp"));
                startActivity(browserIntent);
            }
        });

        view.findViewById(R.id.dunzo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dunzo.d4b.merchant"));
                startActivity(browserIntent);
            }
        });

        view.findViewById(R.id.what).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openWhatsApp("+919449355900","Hello");
                String url = "https://api.whatsapp.com/send?phone=+91"+number+"&text=Hello, Waayu App Support Team!";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



        if (sessionManager.getBooleanData("status")) {
            txtStatus.setText("Avaliable");
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
            txtStatus.setText("Not Avaliable");
        }
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                getStatus("1");
                txtStatus.setText("Avaliable");
            } else {
                getStatus("0");
                txtStatus.setText("Not Avaliable");

            }
        });
        orderStatus();
        return view;
    }

    private void getStatus(String key) {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            jsonObject.put("status", key);
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getStatus((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getmobinformation() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());

            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getinformationList((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "4");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void orderStatus() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());

            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getStatus((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Notioff() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rest_id", user.getId());
            jsonObject.put("device_id", Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));


            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().notioff((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");

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
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setBooleanData("status", switch1.isChecked());
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Ostatus ostatus = gson.fromJson(result.toString(), Ostatus.class);

                if (ostatus.getOrderData().getRiderStatus().equalsIgnoreCase("1")) {
                    switch1.setChecked(true);
                    sessionManager.setBooleanData("status", true);
                } else {
                    switch1.setChecked(false);
                    sessionManager.setBooleanData("status", false);
                }

            }else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                //Ostatus ostatus = gson.fromJson(result.toString(), Ostatus.class);
                sessionManager.logoutUser();
                //getStatus("0");
                //txtStatus.setText("Not Avaliable");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();


            }else if (callNo.equalsIgnoreCase("4")) {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(result));
                //Ostatus ostatus = gson.fromJson(result.toString(), Ostatus.class);
                number =jsonObject.getJSONArray("extradetails").getJSONObject(0).getString("link");




            }

        } catch (Exception e) {
            e.toString();
        }

    }

    @OnClick(R.id.img_logout)
    public void onClick() {

    }

    public void onrefaress() {
        if (user != null && sessionManager != null) {
            user = sessionManager.getUserDetails("");
            edUsername.setText("" + user.getName());
            edEmail.setText("" + user.getEmail());
            edPhone.setText("" + user.getMobile());
        }

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @OnClick({R.id.img_logout})
    public void onClick(View view) {
        if (view.getId() == R.id.img_logout) {
            AlertDialog myDelete = new AlertDialog.Builder(getActivity())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout")
                    .setIcon(R.drawable.ic_logout)

                    .setPositiveButton("Logout", (dialog, whichButton) -> {

                        //your deleting code
                        dialog.dismiss();
                        Notioff();

                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create();
            myDelete.show();
        }
    }

    private void openWhatsApp(String numero,String mensaje){

        try{
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {

                Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();

            }
        } catch(Exception e) {
            Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();

        }

    }
}
