package com.waayu.owner.activity;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.model.LoginStore;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.waayu.owner.utils.Utiles;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.waayu.owner.utils.SessionManager.curruncy;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {


    TextInputEditText edUsername;

    TextInputEditText edPassword;

    CheckBox chkRemember;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);


        if (Build.VERSION.SDK_INT >= 33) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
            else {
                //Toast.makeText(this, "Check1", Toast.LENGTH_SHORT).show();
                //createChannel();
            }
        }else {
            isStoragePermissionGranted();
        }

       // requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, POST_NOTIFICATIONS}, 101);



        if (sessionManager.getBooleanData("rlogin")) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
           // getSharedPreferences("_", MODE_PRIVATE).edit().putString("notification_source", "no_source").apply();
            finish();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermission();
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            requestNotificationPermission();
            return true;
        }
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 101 );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }



    public void bottonLogin() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View rootView = getLayoutInflater().inflate(R.layout.login_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        edUsername = rootView.findViewById(R.id.ed_username);
        edPassword = rootView.findViewById(R.id.ed_password);
        chkRemember = rootView.findViewById(R.id.chk_remember);
        TextView txtLogin = rootView.findViewById(R.id.txt_login);
        txtLogin.setOnClickListener(view -> {
            if (validation()) {
                loginUser();
            }
        });
        mBottomSheetDialog.show();


    }

    @OnClick(R.id.btn_loginnow)
    public void onClick() {
        bottonLogin();

    }

    private void loginUser() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            {
                if (!task.isSuccessful()) {
                    Log.e("MainActivity TAG", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = Objects.requireNonNull(task.getResult());

                Log.e("TAGMainActivity token", token);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mobile", edUsername.getText().toString());
                    jsonObject.put("password", edPassword.getText().toString());
                    jsonObject.put("fcm_id", token);
                    jsonObject.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    jsonObject.put("imei", Utiles.getIMEI(LoginActivity.this));
                    JsonParser jsonParser = new JsonParser();

                    Log.i("FCM--TOKEN", token);
                    Log.i("Device Id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

                    Call<JsonObject> call = APIClient.getInterface().getLogin((JsonObject) jsonParser.parse(jsonObject.toString()));
                    GetResult getResult = new GetResult();
                    getResult.setMyListener(this);
                    getResult.callForLogin(call, "1");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            Gson gson = new Gson();
            LoginStore response = gson.fromJson(result.toString(), LoginStore.class);
            Toast.makeText(LoginActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
            if (response.getResult().equals("true")) {
              //  OneSignal.sendTag("storeid", response.getUser().getId());
                sessionManager.setUserDetails("", response.getUser());
                sessionManager.setStringData(curruncy, response.getCurrency());
                sessionManager.setBooleanData("status", response.getUser().getStatus().equalsIgnoreCase("1"));
                if (chkRemember.isChecked()) {
                    sessionManager.setBooleanData("rlogin", true);
                }
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
              //  getSharedPreferences("_", MODE_PRIVATE).edit().putString("notification_source", "no_source").apply();
                finish();
            }
        } catch (Exception e) {
            Log.e("error", " --> " + e.toString());
        }
    }

    public boolean validation() {
        if (edUsername.getText().toString().isEmpty()) {
            edUsername.setError("Enter Email No");
            return false;
        }
        if (edPassword.getText().toString().isEmpty()) {
            edPassword.setError("Enter Password");
            return false;
        }
        return true;
    }
}
