package com.waayu.owner.fregment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.waayu.owner.R;
import com.waayu.owner.model.Store;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.waayu.owner.utils.Utiles;


public class MainHomeFragment extends Fragment {


    public MainHomeFragment() {
        // Required empty public constructor
    }

    LinearLayout lindeliery;
    LinearLayout lindinein;
    View viewdinein;
    TextView txt_ongoing;
    TextView txt_neworder;
    static TextView txt_dining;
    static TextView txt_del;
    View view_delievry;
    FrameLayout simpleFrameLayout;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store user;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_home, container, false);
        lindeliery = v.findViewById(R.id.lindeliery);
        lindinein = v.findViewById(R.id.lindinein);
        viewdinein = v.findViewById(R.id.viewdinein);
        view_delievry = v.findViewById(R.id.view_delievry);
        simpleFrameLayout = v.findViewById(R.id.simpleFrameLayout);
        simpleFrameLayout = v.findViewById(R.id.simpleFrameLayout);
        txt_ongoing = v.findViewById(R.id.txt_ongoing);
        txt_neworder = v.findViewById(R.id.txt_neworder);
        txt_del = v.findViewById(R.id.txt_del);
        txt_dining = v.findViewById(R.id.txt_dining);

        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        view_delievry.setVisibility(View.VISIBLE);
        viewdinein.setVisibility(View.GONE);
        txt_neworder.setTextColor(getActivity().getResources().getColor(R.color.main_blue_color));
        txt_ongoing.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
        PendingFragment fragment = new PendingFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        setTextCountData();

        lindeliery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_delievry.setVisibility(View.VISIBLE);
                viewdinein.setVisibility(View.GONE);
                txt_neworder.setTextColor(getActivity().getResources().getColor(R.color.main_blue_color));
                txt_ongoing.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                PendingFragment fragment = new PendingFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
        txt_neworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_delievry.setVisibility(View.VISIBLE);
                viewdinein.setVisibility(View.GONE);
                txt_neworder.setTextColor(getActivity().getResources().getColor(R.color.main_blue_color));
                txt_ongoing.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                PendingFragment fragment = new PendingFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        lindinein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_delievry.setVisibility(View.GONE);
                viewdinein.setVisibility(View.VISIBLE);
                txt_neworder.setTextColor(getActivity().getResources().getColor(R.color.colorBalck));
                txt_ongoing.setTextColor(getActivity().getResources().getColor(R.color.main_blue_color));
                TableBookingFragment fragment = new TableBookingFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        return v;
    }

    public static void setTextCountData() {

        int del,din;

        del = Integer.parseInt(Utiles.getDeliveryvalue);
        din = Integer.parseInt(Utiles.getDiningValue);

        if (del>9){
            txt_del.setText("9+");

        }else {
            txt_del.setText(Utiles.getDeliveryvalue);

        }


        if (din>9){
            txt_dining.setText("9+");

        }else {
            txt_dining.setText(Utiles.getDiningValue);

        }

    }
}