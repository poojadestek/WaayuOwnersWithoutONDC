package com.waayu.owner.fregment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.waayu.owner.R;

import com.waayu.owner.model.RestuarantDatum;
import com.waayu.owner.model.ReviewData;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class ReviewFragment extends Fragment implements GetResult.MyListener {


    @BindView(R.id.recycle_review)
    RecyclerView recyclerReview;
    @BindView(R.id.txt_review_count)
    TextView txt_review_count;
    @BindView(R.id.iv_notification)
    ImageView iv_notification;
    @BindView(R.id.txtNodata)
    TextView txtNodata;
    @BindView(R.id.txt_sub_title)
    TextView txt_sub_title;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store store;
    ReviewAdapter reviewAdapter;
    List<RestuarantDatum> restuarantDataList = new ArrayList<>();
    private final ImageView[] imageViewStars = new ImageView[5];
    private final ImageView[] imageViewRiderStars = new ImageView[5];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, view);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        store = sessionManager.getUserDetails("");



        txt_sub_title.setText(store.getName() + " , " + store.getMobile());

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerReview.setLayoutManager(recyclerLayoutManager);
        reviewAdapter = new ReviewAdapter(restuarantDataList, getContext());
        recyclerReview.setAdapter(reviewAdapter);
        getReview();


        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NotificationFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack(null).commit();
                }
            }
        });
        return view;

    }

    private void getReview() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", store.getId());
            jsonObject.put("offset", "0");
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getReview((JsonObject) jsonParser.parse(jsonObject.toString()));
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
                ReviewData reviewData = gson.fromJson(result.toString(), ReviewData.class);
                if (reviewData.getResult().equalsIgnoreCase("true")) {
                    recyclerReview.setVisibility(View.VISIBLE);
                    txtNodata.setVisibility(View.GONE);
                    restuarantDataList = reviewData.getRestuarantData();
                    reviewAdapter = new ReviewAdapter(restuarantDataList, getContext());
                    recyclerReview.setAdapter(reviewAdapter);
                    setRevieCount(String.valueOf(restuarantDataList.size()));
                } else {
                    recyclerReview.setVisibility(View.GONE);
                    txtNodata.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setRevieCount(String size) {
        if (size.length() <= 1) {
            txt_review_count.setText("0" + size);
        } else {
            txt_review_count.setText(size);
        }


    }

    class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
        private List<RestuarantDatum> dataItemList;
        Context mContext;


        public ReviewAdapter(List<RestuarantDatum> dataItemList, Context context) {
            this.dataItemList = dataItemList;
            mContext = context;

        }

        @Override
        public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_item, parent, false);
            ReviewAdapter.ViewHolder viewHolder = new ReviewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ReviewAdapter.ViewHolder holder,
                                     int position) {


            RestuarantDatum item = dataItemList.get(position);
            holder.txtCustName.setText("" + item.getCustomerName());
            holder.txtReview.setText("" + item.getRestComment());
            holder.txtDeliveryName.setText("" + item.getRiderName());
            holder.txtReviewRider.setText("" + item.getRiderComment());

            if (item.getSelfPickup().equalsIgnoreCase("1")) {
                holder.lin_delivery.setVisibility(View.GONE);
                holder.lin_del_review_rate.setVisibility(View.GONE);
            } else {
                holder.lin_delivery.setVisibility(View.VISIBLE);
                holder.lin_del_review_rate.setVisibility(View.VISIBLE);

            }


            imageViewStars[0] = holder.image_view_star_1;
            imageViewStars[1] = holder.image_view_star_2;
            imageViewStars[2] = holder.image_view_star_3;
            imageViewStars[3] = holder.image_view_star_4;
            imageViewStars[4] = holder.image_view_star_5;
            imageViewRiderStars[0] = holder.image_view_star_review_1;
            imageViewRiderStars[1] = holder.image_view_star_review_2;
            imageViewRiderStars[2] = holder.image_view_star_review_3;
            imageViewRiderStars[3] = holder.image_view_star_review_4;
            imageViewRiderStars[4] = holder.image_view_star_review_5;


            setStarBar(Integer.parseInt(item.getRestRating()), Integer.parseInt(item.getRiderRating()));


            // Glide.with(mContext).load(baseUrl + item.getImgurl()).placeholder(R.drawable.ic_complet_order).into(holder.imgTop);


        }

        @Override
        public int getItemCount() {
            return dataItemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.text_name)
            TextView txtCustName;
            @BindView(R.id.txt_review)
            TextView txtReview;
            @BindView(R.id.txt_delivery)
            TextView txtDeliveryName;
            @BindView(R.id.txt_review_ride_rate)
            TextView txtReviewRider;
            @BindView(R.id.image_view_star_1)
            ImageView image_view_star_1;
            @BindView(R.id.image_view_star_2)
            ImageView image_view_star_2;
            @BindView(R.id.image_view_star_3)
            ImageView image_view_star_3;
            @BindView(R.id.image_view_star_4)
            ImageView image_view_star_4;
            @BindView(R.id.image_view_star_5)
            ImageView image_view_star_5;
            @BindView(R.id.image_view_star_review_1)
            ImageView image_view_star_review_1;
            @BindView(R.id.image_view_star_review_2)
            ImageView image_view_star_review_2;
            @BindView(R.id.image_view_star_review_3)
            ImageView image_view_star_review_3;
            @BindView(R.id.image_view_star_review_4)
            ImageView image_view_star_review_4;
            @BindView(R.id.image_view_star_review_5)
            ImageView image_view_star_review_5;
            @BindView(R.id.lin_delivery)
            LinearLayout lin_delivery;
            @BindView(R.id.lin_del_review_rate)
            LinearLayout lin_del_review_rate;


            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    private void setStarBar(int rest_star_number, int rider_star_number) {

        for (int i = 0; i < imageViewStars.length; i++) {
            if (i < rest_star_number) {
                this.imageViewStars[i].setImageResource(R.drawable.ic_round_star_on);
            } else {
                this.imageViewStars[i].setImageResource(R.drawable.ic_round_star);
            }
        }

        for (int i = 0; i < imageViewRiderStars.length; i++) {
            if (i < rider_star_number) {
                this.imageViewRiderStars[i].setImageResource(R.drawable.ic_round_star_on_green);
            } else {
                this.imageViewRiderStars[i].setImageResource(R.drawable.ic_round_star_green);
            }
        }
    }

}

