package com.waayu.owner.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.waayu.owner.R;
import com.waayu.owner.adepter.MyAdapter;
import com.google.android.material.tabs.TabLayout;


public class TabActivity extends AppCompatActivity {
    private AlertDialog dialog;
    public static final int PERMISSION_READ = 0;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean isCheckedForUpdate = false;
    private FrameLayout frameLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    private final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;

    private int[] tabIcons = {
            R.drawable.size_drawable,
            /* R.drawable.ic_light_calculator_icon,*/
            R.drawable.ic_product
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        /*tabLayout.addTab(tabLayout.newTab().setText(""));*/
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setupTabIcons();




        // Obtain the FirebaseAnalytics instance.



        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {

                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_selected);
                    /*  tabLayout.getTabAt(1).setIcon(R.drawable.ic_light_calculator_icon);*/
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_product);
                } else if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                    /* tabLayout.getTabAt(1).setIcon(R.drawable.ic_dark_calculator_icon__1_);*/
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_product_selected);
                } /*else if (tab.getPosition() == 2) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_light_calculator_icon);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_me_selected);
                }*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.tab_icon_layout);
        }


    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        /*tabLayout.getTabAt(2).setIcon(tabIcons[2]);*/
    }




    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(TabActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {



    }



/*
    private void showExitDialog() {
        try {
            if (dialog == null || !dialog.isShowing()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, viewGroup, false);
                Button btnYes = dialogView.findViewById(R.id.buttonYes);
                Button btnNo = dialogView.findViewById(R.id.buttonNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        TabActivity.super.onBackPressed();
                        finishAffinity();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                builder.setView(dialogView);
                dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/









}