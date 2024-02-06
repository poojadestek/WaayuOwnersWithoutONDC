package com.waayu.owner.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.waayu.owner.fregment.MainHomeFragment;
import com.waayu.owner.fregment.ReviewFragment;
import com.waayu.owner.utils.SessionManager;
import com.waayu.owner.utils.Utiles;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.waayu.owner.R;
import com.waayu.owner.fregment.HomeFragment;
import com.waayu.owner.fregment.PendingFragment;
import com.waayu.owner.fregment.ProductFragment;
import com.waayu.owner.fregment.ProfileFragment;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;


import butterknife.BindView;
import butterknife.ButterKnife;
import in.myinnos.inappupdate.InAppUpdate;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fragment_frame)
    FrameLayout fragmentFrame;
    @BindView(R.id.bottom_navigation)
    public BottomNavigationView bottomNavigation;
    SessionManager sessionManager;
    AppUpdateManager appUpdateManager;
    String status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        appUpdateManager = AppUpdateManagerFactory.create(this);
        InAppUpdate.setImmediateUpdate(appUpdateManager, this);
       /* if (status.equalsIgnoreCase("0")) {
            InAppUpdate.setFlexibleUpdate(appUpdateManager, this);

        } else {
            InAppUpdate.setImmediateUpdate(appUpdateManager, this);
        }*/

        sessionManager = new SessionManager(HomeActivity.this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        bottomNavigation.setItemIconTintList(null);
        if (Build.VERSION.SDK_INT >= 33) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
            else {
                //Toast.makeText(this, "Check1", Toast.LENGTH_SHORT).show();
                //createChannel();
            }
        }else {
            isStoragePermissionGranted();
        }


        Log.i("Device Id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        Log.i("Notification:-", getSharedPreferences("_", MODE_PRIVATE).getString("notification_source", "empty"));

       /* if (getSharedPreferences("_", MODE_PRIVATE).getString("notification_source", "empty").equalsIgnoreCase("firebase_notification")) {
            PendingFragment frag = new PendingFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_frame,frag,"Pending Fragment");
            transaction.commit();
            sessionManager.setPendingData("status_fragment", "Pending");
        }*/


        /*bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.ic_home);
        bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.ic_product);
        bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.ic_order);
        bottomNavigation.getMenu().getItem(3).setIcon(R.drawable.ic_review);
        bottomNavigation.getMenu().getItem(4).setIcon(R.drawable.ic_account);*/


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

    public boolean callFragment(Fragment fragmentClass) {
        if (fragmentClass != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragmentClass).addToBackStack(null).commit();
            return true;
        }
        return false;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                // item.setIcon(R.drawable.ic_home_selected);
                break;

            case R.id.navigation_product:
                fragment = new ProductFragment();
                // item.setIcon(R.drawable.ic_product_selected);
                break;

            case R.id.navigation_review:
                fragment = new ReviewFragment();
                // item.setIcon(R.drawable.ic_review_selected);
                break;
            case R.id.navigation_order:
                fragment = new MainHomeFragment();
                //  item.setIcon(R.drawable.ic_order_selected);
                sessionManager.setPendingData("status_fragment", "Pending");
                break;

/* case R.id.navigation_notifications:
                fragment = new NotificationFragment();
                break;*/

            case R.id.navigation_account:
                fragment = new ProfileFragment();
                // item.setIcon(R.drawable.ic_account_selected);
                break;
            default:
                break;
        }
        return callFragment(fragment);
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


    @Override
    protected void onResume() {
        super.onResume();
        InAppUpdate.setImmediateUpdateOnResume(appUpdateManager, this);
        /*if (status.equalsIgnoreCase("0")) {
            InAppUpdate.setFlexibleUpdateOnResume(appUpdateManager, this);
        } else {

        }*/
    }


    @Override
    public void onBackPressed() {
        if (bottomNavigation.getSelectedItemId() == R.id.navigation_home && !Utiles.backPress) {
            // super.onBackPressed();
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Close?")
                    .setMessage("Are you sure you want to close ?")
                    .setCancelable(false)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            // Whatever...
                        }
                    }).show();
            // finish();
        } else {

            Utiles.backPress = false;
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }

    }
}
