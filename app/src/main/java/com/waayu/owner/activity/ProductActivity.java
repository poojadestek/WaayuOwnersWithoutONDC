package com.waayu.owner.activity;

import static com.waayu.owner.utils.SessionManager.curruncy;
import static com.waayu.owner.utils.Utiles.updatestatus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.model.AddonItem;
import com.waayu.owner.model.Addondata;
import com.waayu.owner.model.ProductData;
import com.waayu.owner.model.RestResponse;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class ProductActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_isVegtitle)
    TextView txt_isVegtitle;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_disc)
    TextView txtDisc;
    @BindView(R.id.switch_)
    Switch switch_;
    @BindView(R.id.img_product)
    ImageView imgProduct;
    @BindView(R.id.img_isvage)
    ImageView imgIsvage;
    @BindView(R.id.package_lst)
    RecyclerView packageLst;

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.switchForActionBar)
    TextView switchForActionBar;
    int a = 0;
    String dataItem;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    private ProductData response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        switch_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    a = 1;
                    productstatus(response.getProductdetails().getId(), String.valueOf(a));
                } else {
                    a = 0;
                    productstatus(response.getProductdetails().getId(), String.valueOf(a));
                }
            }
        });
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(ProductActivity.this);

        dataItem = getIntent().getStringExtra("pid");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switchForActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductActivity.this, ProductEditActivity.class).putExtra("pid", dataItem));
            }
        });

        getProductDetails();


        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        packageLst.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(packageLst.getContext(),
                        recyclerLayoutManager.getOrientation());
        packageLst.addItemDecoration(dividerItemDecoration);


    }

    private void getProductDetails() {
        custPrograssbar.prograssCreate(ProductActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", dataItem);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getProductDetails((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static class PackageRecyclerViewAdapter extends
            RecyclerView.Adapter<PackageRecyclerViewAdapter.ViewHolder> {
        SessionManager sessionManager;

        private List<Addondata> packageList;
        private Context context;


        public PackageRecyclerViewAdapter(List<Addondata> packageListIn
                , Context ctx) {
            packageList = packageListIn;
            context = ctx;
            sessionManager = new SessionManager(context);

        }

        @Override
        public PackageRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.package_item, parent, false);

            PackageRecyclerViewAdapter.ViewHolder viewHolder =
                    new PackageRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PackageRecyclerViewAdapter.ViewHolder holder,
                                     int position) {
            Addondata packageModel = packageList.get(position);
            if (packageModel.getAddonLimit() == 0) {
                holder.packageName.setText(packageModel.getTitle());
            } else {
                holder.packageName.setText(packageModel.getTitle() + "(" + packageModel.getAddonLimit() + ")");

            }

            if (packageModel.getAddonIsRadio() == 1) {

                for (AddonItem item : packageModel.getAddonItemData()) {
                    TextView rb = new TextView(PackageRecyclerViewAdapter.this.context);
                    rb.setPadding(5, 5, 5, 5);
                    rb.setId(Integer.parseInt(item.getId()));
                    rb.setText(item.getTitle() + " " + sessionManager.getStringData(SessionManager.curruncy) + item.getPrice());
                    rb.setTextSize(14);
                    holder.priceGroup.addView(rb);
                }
            } else {
                for (AddonItem item : packageModel.getAddonItemData()) {
                    TextView box = new TextView(PackageRecyclerViewAdapter.this.context);
                    box.setId(Integer.parseInt(item.getId()));
                    box.setPadding(5, 5, 5, 5);
                    box.setText(item.getTitle() + "  " + sessionManager.getStringData(SessionManager.curruncy) + item.getPrice());
                    box.setTextSize(14);
                    holder.lvlChackbox.addView(box);

                }
            }
            holder.priceGroup.setOnCheckedChangeListener((radioGroup, i) -> {


                View radioButton = holder.priceGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                int idx = holder.priceGroup.indexOfChild(radioButton);
                for (int a = 0; a < packageModel.getAddonItemData().size(); a++) {

                    packageModel.getAddonItemData().get(a).setSelect(false);
                }
                packageModel.getAddonItemData().get(idx).setSelect(true);

            });


        }

        @Override
        public int getItemCount() {
            return packageList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView packageName;
            public RadioGroup priceGroup;
            public LinearLayout lvlChackbox;


            public ViewHolder(View view) {
                super(view);
                packageName = view.findViewById(R.id.package_name);
                priceGroup = view.findViewById(R.id.price_grp);
                lvlChackbox = view.findViewById(R.id.lvl_chackbox);


            }
        }
    }


    private void productstatus(String id, String status) {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("status", status);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().ProductStatus((JsonObject) jsonParser.parse(jsonObject.toString()));
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
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);

                if (response.getResult().equalsIgnoreCase("true")) {
                    Toast.makeText(ProductActivity.this, response.getResponseMsg(), Toast.LENGTH_LONG).show();
                    updatestatus = true;
//                    finish();
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                response = gson.fromJson(result.toString(), ProductData.class);
                if (response.getmResult().equalsIgnoreCase("true")) {
                    txtTitle.setText("" + response.getProductdetails().getTitle());
                    txt_isVegtitle.setText("" + response.getProductdetails().getCdesc());
                    txtDisc.setText(response.getProductdetails().getCdesc());
                    txtTotal.setText(sessionManager.getStringData(curruncy) + response.getProductdetails().getBase_price());
                    Glide.with(ProductActivity.this).load(APIClient.baseUrl + "/" + response.getProductdetails().getItem_img()).placeholder(R.drawable.slider).into(imgProduct);
                    if (response.getProductdetails().getStatus().equals("1")) {
                        switch_.setChecked(true);
                    } else {
                        switch_.setChecked(false);
                    }
                    if (response.getProductdetails().getIs_egg().equals("1")) {
                        imgIsvage.setImageDrawable(getResources().getDrawable(R.drawable.ic_egg));
                    }else  if (response.getProductdetails().getIs_veg().equals("0")) {
                        imgIsvage.setImageDrawable(getResources().getDrawable(R.drawable.ic_nonveg));
                    } else if (response.getProductdetails().getIs_veg().equals("1")) {
                        imgIsvage.setImageDrawable(getResources().getDrawable(R.drawable.ic_veg));
                    } else if (response.getProductdetails().getIs_veg().equals("2")) {
                        imgIsvage.setImageDrawable(getResources().getDrawable(R.drawable.ic_egg));
                    }


                    /*PackageRecyclerViewAdapter recyclerViewAdapter = new
                            PackageRecyclerViewAdapter(productInfoItems, this);
                    packageLst.setAdapter(recyclerViewAdapter);*/
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

    @Override
    protected void onResume() {
        getProductDetails();
        super.onResume();
    }
}