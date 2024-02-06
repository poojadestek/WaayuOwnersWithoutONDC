package com.waayu.owner.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.waayu.owner.R;
import com.waayu.owner.utils.SessionManager;
/*import com.ismaeldivita.chipnavigation.ChipNavigationBar;*/

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivityTest extends AppCompatActivity  {

    @BindView(R.id.fragment_frame)
    FrameLayout fragmentFrame;
    SessionManager sessionManager;
  //  ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_test);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(HomeActivityTest.this);



      /* chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        chipNavigationBar.setItemSelected(R.id.navigation_home,
                true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame,
                        new HomeFragment()).commit();
        bottomMenu();*/


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

  /*  @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_product:
                fragment = new ProductFragment();
                item.setIcon(R.drawable.ic_bell);
                break;

            case R.id.navigation_review:
                fragment = new ReviewFragment();
                break;
            case R.id.navigation_order:
                fragment = new PendingFragment();
                sessionManager.setPendingData("status_fragment", "Pending");
                break;

*//* case R.id.navigation_notifications:
                fragment = new NotificationFragment();
                break;*//*

            case R.id.navigation_account:
                fragment = new ProfileFragment();
                break;
            default:
                break;
        }
        return callFragment(fragment);
    }*/

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

/*
    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Fragment fragment = null;
                        switch (i){
                            case R.id.navigation_home:
                                fragment = new HomeFragment();

                                break;
                            case R.id.navigation_product:
                                fragment = new ProductFragment();
                                break;
                            case R.id.navigation_order:
                                fragment = new PendingFragment();
                                break;

                            case R.id.navigation_review:
                                fragment = new ReviewFragment();
                                break;

                            case R.id.navigation_account:
                                fragment = new ProfileFragment();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_frame,
                                        fragment).commit();
                    }
                });
    }
*/
}

