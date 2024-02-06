package com.waayu.owner.fregment;

import static com.waayu.owner.utils.Utiles.updatestatus;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waayu.owner.R;
import com.waayu.owner.adepter.CategoryRecyclerView;
import com.waayu.owner.model.MenuitemDataItem;
import com.waayu.owner.model.ProductDataItem;
import com.waayu.owner.model.Restorent;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class ProductFragment extends Fragment implements GetResult.MyListener {
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store user;
    @BindView(R.id.txtNodata)
    TextView txtNodata;
    @BindView(R.id.edtsearch)
    EditText edtsearch;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    private ArrayList<MenuitemDataItem> arrayList;
    private Restorent product;
    private CategoryRecyclerView myOrderAdepter;

    public ProductFragment() {
        // Required empty public constructor
        //todays task
        // product search and category search
        //by default expanded  if user wants to collaps then he can
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ButterKnife.bind(this, view);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        getProduct();
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProduct();
                refreshlayout.setRefreshing(false);
            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                if (query.equals("")) {
                    myOrderAdepter = new CategoryRecyclerView(getActivity(), product.getResultData().getProductData());
                    recyclerView.setAdapter(myOrderAdepter);
                } else {
                    List<ProductDataItem> filteredlist = new ArrayList<>();
                    for (ProductDataItem item : product.getResultData().getProductData()) {
                        if (item.getTitle().toLowerCase().equalsIgnoreCase(query.toLowerCase())) {
                            filteredlist.add(item);
                        } else {
                            for (int i = 0; i < item.getMenuitemData().size(); i++) {
                                if (item.getMenuitemData().get(i).getTitle().toLowerCase().equalsIgnoreCase(query.toLowerCase())) {
                                    filteredlist.add(item);
                                }
                            }
                        }
                    }
                    if (filteredlist.isEmpty()) {
//                    Toast.makeText(RecentOrderActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
                    } else {
                        myOrderAdepter.filterList(filteredlist);
                    }
                }

            }
        });


        return view;
    }

    private void getProduct() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getREstroProduct((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                product = gson.fromJson(result.toString(), Restorent.class);
                if (product.getResult().equalsIgnoreCase("true")) {
                    myOrderAdepter = new CategoryRecyclerView(getActivity(), product.getResultData().getProductData());
                    recyclerView.setAdapter(myOrderAdepter);
                } else {
                    txtNodata.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*
    public class ProductAdepter extends RecyclerView.Adapter<ProductAdepter.ViewHolder> {
        private List<ProductDataItem> productDataItems;

        public ProductAdepter(List<ProductDataItem> pendinglist) {
            this.productDataItems = pendinglist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item1, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            Log.e("position", "" + position);
            ProductDataItem productItem = productDataItems.get(position);
            holder.txtTitle.setText("" + productItem.getProductName());
            holder.txtCategory.setText("" + productItem.getProductCategory());


            holder.txtPrice.setText(sessionManager.getStringData(curruncy) + productItem.getProductPrice());

            if (productItem.getProductStatus() != 0) {
                holder.txt_available.setText("Available");
            } else {
                holder.txt_available.setText("Not Available");
            }

            if (productItem.getIsVeg().equalsIgnoreCase("0")) {
                holder.txtPtype.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_nonveg));
            } else if (productItem.getIsVeg().equalsIgnoreCase("1")) {

                holder.txtPtype.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_veg));

            } else if (productItem.getIsVeg().equalsIgnoreCase("2")) {
                holder.txtPtype.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_egg));
            }
            Glide.with(getActivity()).load(baseUrl + productItem.getProductImage()).placeholder(R.drawable.slider).into(holder.imgIcon);
            holder.lvlClicl.setOnClickListener(view -> startActivity(new Intent(getActivity(), ProductActivity.class).putExtra("MyClass", productItem).putParcelableArrayListExtra("MyList", productItem.getAddondata())));
        }

        @Override
        public int getItemCount() {
            return productDataItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_icon)
            ImageView imgIcon;
            @BindView(R.id.txt_title)
            TextView txtTitle;

            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.lvl_clicl)
            RelativeLayout lvlClicl;

            @BindView(R.id.txt_ptype)
            ImageView txtPtype;
            @BindView(R.id.txt_category)
            TextView txtCategory;

            @BindView(R.id.txt_available)
            TextView txt_available;

            @BindView(R.id.rlt_detail)
            RelativeLayout rltDetail;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        if (updatestatus) {
            updatestatus = false;
            getProduct();
        }
    }
}
