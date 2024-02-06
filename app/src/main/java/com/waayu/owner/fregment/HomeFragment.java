package com.waayu.owner.fregment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.waayu.owner.activity.HomeActivity;
import com.waayu.owner.model.GetCount;
import com.waayu.owner.model.StoreReportDataItem;
import com.waayu.owner.utils.Utiles;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.adepter.Homeadepter;
import com.waayu.owner.model.HomeStore;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class HomeFragment extends Fragment implements GetResult.MyListener, View.OnClickListener {


    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_itmecount)
    TextView txtItmecount;
    @BindView(R.id.recycle_home)
    RecyclerView recycleHome;
    @BindView(R.id.txt_order)
    TextView txt_order;
    @BindView(R.id.txt_sales)
    TextView txt_sales;
    @BindView(R.id.txt_new)
    TextView txt_neworder;
    @BindView(R.id.txt_complete)
    TextView txt_complete;
    @BindView(R.id.txt_cancel)
    TextView txt_cancel;
    @BindView(R.id.txt_new_item)
    TextView txt_item;
    @BindView(R.id.txt_overall)
    TextView txt_overall;

    @BindView(R.id.txt_dinein)
    TextView txt_dinein;
    @BindView(R.id.txt_category)
    TextView txt_category;

    @BindView(R.id.txt_qr)
    TextView txt_qr;

    @BindView(R.id.iv_notification)
    ImageView iv_notification;
    @BindView(R.id.cd_new)
    CardView cd_new;
    @BindView(R.id.cd_complete)
    CardView cd_complete;
    @BindView(R.id.cd_cancel)
    CardView cd_cancel;
    @BindView(R.id.cd_new_item)
    CardView cd_new_item;
    @BindView(R.id.cd_overall)
    CardView cd_overall;
    @BindView(R.id.cd_category)
    CardView cd_category;
    @BindView(R.id.txt_sub_title)
    TextView txt_sub_title;
    @BindView(R.id.lin_sales_report)
    LinearLayout lin_sales_report;
    @BindView(R.id.lin_customer_report)
    LinearLayout lin_customer_report;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store store;
    HomeActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        store = sessionManager.getUserDetails("");
        txtName.setText("Hello " + store.getName());
        txtItmecount.setText("" + store.getMobile());

        txt_sub_title.setText(store.getName()+" , "+store.getMobile());

        getData();
        view.findViewById(R.id.lin_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRListFragment qrListFragment = new QRListFragment();
                callFragment(qrListFragment);
                Utiles.backPress = true;
            }
        });


        recycleHome.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        if (getActivity() instanceof HomeActivity) {
            homeActivity = (HomeActivity) getActivity();
        }
        getDesbord();

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NotificationFragment();
                if (fragment != null) {
                    Utiles.backPress = true;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack(null).commit();

                }
            }
        });
        return view;

    }

    public  void getData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", store.getId());
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getCount((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getDesbord() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", store.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getDesbord((JsonObject) jsonParser.parse(jsonObject.toString()));
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
                HomeStore homeStore = gson.fromJson(result.toString(), HomeStore.class);
                if (homeStore.getResult().equalsIgnoreCase("true")) {
                    List<StoreReportDataItem> storeReportDataItems = homeStore.getStoreReportData();
                    for (int i = 0; i < storeReportDataItems.size(); i++) {
                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Total Order")) {
                            txt_order.setText(storeReportDataItems.get(i).getReportData());
                        }

                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Today Sales")) {

                            txt_sales.setText(storeReportDataItems.get(i).getReportData());
                        }
                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Total Sales")) {
                            txt_category.setText(numbercal(Long.parseLong(storeReportDataItems.get(i).getReportData())));
                        }

                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Total New Order")) {
                            txt_neworder.setText(storeReportDataItems.get(i).getReportData());
                        }

                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Total Completed Order")) {
                            txt_complete.setText(storeReportDataItems.get(i).getReportData());
                        }

                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Total Cancelled Order")) {
                            txt_cancel.setText(storeReportDataItems.get(i).getReportData());
                        }

                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Total Menu Item")) {
                            txt_item.setText(storeReportDataItems.get(i).getReportData());
                        }

                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Your Overall Rating")) {
                            txt_overall.setText(storeReportDataItems.get(i).getReportData());
                        }

                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("DineIn")) {
                            txt_dinein.setText(storeReportDataItems.get(i).getReportData());
                        }

//                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Total Category")) {
//                            txt_category.setText(storeReportDataItems.get(i).getReportData());
//                        }
                        if (storeReportDataItems.get(i).getTitle().equalsIgnoreCase("Qr scan  download")) {
                            txt_qr.setText(storeReportDataItems.get(i).getReportData());
                        }
                    }
                    Homeadepter homeadepter = new Homeadepter(homeStore.getStoreReportData(), getActivity());
                    recycleHome.setAdapter(homeadepter);
                }
            }else  if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                GetCount pending = gson.fromJson(result.toString(), GetCount.class);
                if (pending.getResult().equalsIgnoreCase("true")) {

                    Utiles.getDiningValue=pending.getDining();
                    Utiles.getDeliveryvalue=pending.getDelivery();

                }
            }
        } catch (Exception e) {
            Log.e("eror", "-->" + e.toString());
        }
    }

    @OnClick({R.id.cd_new, R.id.cd_complete, R.id.cd_cancel, R.id.cd_new_item,R.id.cd_dinein, R.id.cd_overall,R.id.lin_sales_report,R.id.lin_customer_report})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.cd_new:
                sessionManager.setPendingData("status_fragment", "Pending");
                MainHomeFragment fragment = new MainHomeFragment();
                callFragment(fragment);
                MenuItem item_a = homeActivity.bottomNavigation.getMenu().findItem(R.id.navigation_order);
                item_a.setChecked(true);
                break;
            case R.id.cd_complete:
                sessionManager.setPendingData("status_fragment", "Complete");
                MainHomeFragment fragment_comp = new MainHomeFragment();
                callFragment(fragment_comp);
                MenuItem item_b = homeActivity.bottomNavigation.getMenu().findItem(R.id.navigation_order);
                item_b.setChecked(true);
                break;
            case R.id.cd_cancel:
                sessionManager.setPendingData("status_fragment", "Cancel");
                MainHomeFragment fragment_can = new MainHomeFragment();
                callFragment(fragment_can);
                MenuItem item_c = homeActivity.bottomNavigation.getMenu().findItem(R.id.navigation_order);
                item_c.setChecked(true);
                break;

            case R.id.cd_new_item:
                ProductFragment fragment_product = new ProductFragment();
                callFragment(fragment_product);
                MenuItem item_product = homeActivity.bottomNavigation.getMenu().findItem(R.id.navigation_product);
                item_product.setChecked(true);
                break;

            case R.id.cd_overall:
                ReviewFragment fragement_review = new ReviewFragment();
                callFragment(fragement_review);
                MenuItem item_review = homeActivity.bottomNavigation.getMenu().findItem(R.id.navigation_review);
                item_review.setChecked(true);
                break;

            case R.id.cd_category:
                break;

            case R.id.cd_dinein:
                DineInListFragment fragement_dine = new DineInListFragment();
                callFragment(fragement_dine);
                Utiles.backPress = true;

                break;

            case R.id.lin_sales_report:
                break;

            case R.id.lin_customer_report:
                CustomerListFragment fragement_cust = new CustomerListFragment();
                callFragment(fragement_cust);
                Utiles.backPress = true;
                break;

            default:
                break;
        }


    }

    private void callFragment(Fragment fragment_class) {
        /*if (fragment_class != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_frame, fragment_class);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }*/

        if (fragment_class != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment_class).addToBackStack(null).commit();

        }

    }

    public String numbercal(long number){
        String numberString = "";
        if (Math.abs(number / 1000000) > 1) {
            numberString = (number / 1000000) + "m";

        } else if (Math.abs(number / 1000) > 1) {
            numberString = (number / 1000) + "k";

        } else {
            numberString = number+"";

        }

        return numberString;
    }


}