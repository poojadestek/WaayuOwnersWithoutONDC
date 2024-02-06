package com.waayu.owner.fregment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.waayu.owner.R;
import com.waayu.owner.model.CustomerData;
import com.waayu.owner.model.CustomerList;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;
import com.waayu.owner.utils.Utiles;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class CustomerListFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.recycle_customer)
    RecyclerView recycle_customer;
    @BindView(R.id.txt_cust)
    TextView txt_cust;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    LinearLayoutManager layoutManager;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store user;
    @BindView(R.id.txtNodata)
    TextView txtNodata;
    CustomerDataAdapter customerDataAdapter;
    private int currentPage = 0;
    boolean reload = false;
    public static ArrayList<CustomerData> resultArrayList;
    @BindView(R.id.iv_notification)
    ImageView ivNotification;

    @BindView(R.id.txt_titel)
    TextView txt_titel;


    public CustomerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.lin_total_customer).setVisibility(View.VISIBLE);
        txt_titel.setText("Customer List");
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails("");


        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_customer.setLayoutManager(layoutManager);

        fetchData(String.valueOf(currentPage));

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NotificationFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack(null).commit();
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new HomeFragment();
                if (fragment != null) {
                    Utiles.backPress = false;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack(null).commit();
                }
            }
        });


        recycle_customer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                Log.i("DivyaScroll:-", +currentItems + "--" + totalItems + "--" + scrollOutItems);

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    Log.i("DivyaScroll1:-", String.valueOf(currentPage));
                    currentPage = currentPage + 10;
                    Log.i("DivyaScroll2:-", String.valueOf(currentPage));
                    reload = true;
                    fetchData(String.valueOf(currentPage));

                }
            }
        });
        return view;
    }

    private void getNotification() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getNoti((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchData(String offset) {
        Log.i("DivyaScroll3:-", offset);
        // custPrograssbar.prograssCreate(Customer_list_activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            jsonObject.put("offset", offset);
            JsonParser jsonParser = new JsonParser();

            Log.i("DivyaJson", jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getCustomerList((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("DivyaError", e.toString());
        }
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            // custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Log.i("DivyaScroll4:-", "-->");
                Gson gson = new Gson();
                CustomerList customerList = gson.fromJson(result.toString(), CustomerList.class);

                if (customerList.getResult().equalsIgnoreCase("true")) {
                    Log.i("DivyaScroll5:-", "-->");

                    if (!customerList.getUserData().isEmpty()) {
                        Log.i("DivyaScroll6:-", "-->" + customerList.getUserData().size());

                        txtNodata.setVisibility(View.GONE);
                        recycle_customer.setVisibility(View.VISIBLE);
                        txt_cust.setText(customerList.getTotal_user());
                        List<CustomerData> customerData = customerList.getUserData();
                        String cust_name = "", cust_mobile = "", cust_email = "", cust_order = "";
                        if (reload) {
                            Log.i("DivyaScroll7:-", "-->");

                            for (int i = 0; i < customerData.size(); i++) {
                                cust_name = customerData.get(i).getName();
                                cust_mobile = customerData.get(i).getMobile();
                                cust_email = customerData.get(i).getEmail();
                                cust_order = customerData.get(i).getOrder_placed();
                                resultArrayList.add(new CustomerData(cust_name, cust_mobile, cust_email, cust_order));
                                customerDataAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.i("DivyaScroll8:-", "-->");
                            resultArrayList = new ArrayList<>();
                            for (int i = 0; i < customerData.size(); i++) {
                                cust_name = customerData.get(i).getName();
                                cust_mobile = customerData.get(i).getMobile();
                                cust_email = customerData.get(i).getEmail();
                                cust_order = customerData.get(i).getOrder_placed();


                                resultArrayList.add(new CustomerData(cust_name, cust_mobile, cust_email, cust_order));


                            }
                            customerDataAdapter = new CustomerDataAdapter(resultArrayList, getContext());
                            recycle_customer.setAdapter(customerDataAdapter);
                        }


                    }
                }
            }

        } catch (Exception e) {
            e.toString();
        }

    }

    public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.ViewHolder> {
        private List<CustomerData> dataItemList;
        Context mContext;

        public CustomerDataAdapter(List<CustomerData> dataItemList, Context context) {
            this.dataItemList = dataItemList;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {

            CustomerData item = dataItemList.get(position);
            holder.txtCustName.setText("" + item.getName());
            holder.txtMobNo.setText("Mobile No - " + item.getMobile());
            holder.txtEmail.setText("Email Id - " + item.getEmail());
            holder.txtOrder.setText("Order Placed - " + item.getOrder_placed());


            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.poppins_regular);
            Typeface typeface1 = ResourcesCompat.getFont(mContext, R.font.poppins_bold);
            holder.txtCustName.setTypeface(typeface1);
            holder.txtMobNo.setTypeface(typeface);
            holder.txtEmail.setTypeface(typeface);
            holder.txtOrder.setTypeface(typeface);

       /* holder.txtCustName.setTypeface(null, Typeface.BOLD);
        holder.txtMobNo.setTypeface(null, Typeface.NORMAL);
        holder.txtEmail.setTypeface(null, Typeface.NORMAL);
        holder.txtOrder.setTypeface(null,Typeface.NORMAL);*/

            // holder.txtOrder.setText(""+item.get());


        }

        @Override
        public int getItemCount() {
            return dataItemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txt_cust_name)
            TextView txtCustName;
            @BindView(R.id.txt_mob_no)
            TextView txtMobNo;
            @BindView(R.id.txt_order)
            TextView txtOrder;
            @BindView(R.id.txt_email_id)
            TextView txtEmail;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


}

