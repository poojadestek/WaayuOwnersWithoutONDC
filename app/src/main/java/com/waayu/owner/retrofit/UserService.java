package com.waayu.owner.retrofit;


import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {


    @POST(APIClient.APPEND_URL + "store_login.php")
    Call<JsonObject> getLogin(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "store_product_category_list.php")
    Call<JsonObject> getProductCat(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "store_product_list.php")
//https://waayupro.in/sapi/store_product_list.php
    Call<JsonObject> getREstroProduct(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "store_update_product_details.php")
//https://waayupro.in/sapi/store_update_product_details.php
    Call<JsonObject> store_update_product_details(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "store_product_details.php")
//https://waayupro.in/sapi/store_product_details.php
    Call<JsonObject> getProductDetails(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "store_appstatus.php")
    Call<JsonObject> getStatus(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "store_logout.php")
    Call<JsonObject> notioff(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "total_order_report.php")
    Call<JsonObject> getDesbord(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "olist.php")
    Call<JsonObject> getOlist(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "complete_order.php")
    Call<JsonObject> getComplete(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "order_status_wise.php")
    Call<JsonObject> getPending(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "e_table_booking_list.php")
    Call<JsonObject> getTablebooking(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "noti.php")
    Call<JsonObject> getNoti(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "ostatus.php")
    Call<JsonObject> getOstatus(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "area.php")
    Call<JsonObject> getArea(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "profile.php")
    Call<JsonObject> updateProfile(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "order_product_list.php")
    Call<JsonObject> getOrderDetail(@Body JsonObject object);

    @POST(APIClient.baseUrl + "rapi/order_status_change_rest.php")
    Call<JsonObject> getorderstatus(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "total_product_list.php")
    Call<JsonObject> getTotalProduct(@Body JsonObject object);

    @POST(APIClient.APPEND_URL_e + "e_dunzo_order_cancle.php")
    Call<JsonObject> e_dunzo_order_cancle(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "make_decision.php")
    Call<JsonObject> getMackDecision(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "e_count_table_booking_notification.php")
    Call<JsonObject> getCount(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "/table_booking_make_decision.php")
    Call<JsonObject> getTableMackDecision(@Body JsonObject object);

    @POST(APIClient.baseUrl + "eapi/e_delivery_partner.php")
    Call<JsonObject> getDeliverylist(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "order_tracking.php")
    Call<JsonObject> getordertrack(@Body JsonObject object);

    @POST(APIClient.APPEND_URL_e + "e_dunzo_history.php")
    Call<JsonObject> getdunzotrack(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "s_rider_list.php")
    Call<JsonObject> getRiderlist(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "ass_rider.php")
    Call<JsonObject> getAssrider(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "change_stock_status.php")
    Call<JsonObject> changestock(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "get_required_list.php")
    Call<JsonObject> getRequiredlist(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "product_status.php")
    Call<JsonObject> ProductStatus(@Body JsonObject object);

    @POST(APIClient.APPEND_URL_e + "e_rest_owner_data.php")
    Call<JsonObject> getReview(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "add_product.php")
    @Multipart
    Call<JsonObject> addProduct(@Part("sid") RequestBody sid,
                                @Part("cid") RequestBody cid,
                                @Part("pid") RequestBody pid,
                                @Part("status") RequestBody status,
                                @Part("productData") RequestBody productData,
                                @Part("title") RequestBody title,
                                @Part("description") RequestBody description,
                                @Part("size") RequestBody size,
                                @Part List<MultipartBody.Part> parts);

    @POST(APIClient.APPEND_URL_e + "e_user_list.php")
    Call<JsonObject> getCustomerList(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "dine_in_payment_history.php")
    Call<JsonObject> getDineinList(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "e_today_booking_list.php")
    Call<JsonObject> gettodayDineinList(@Body JsonObject object);

    @POST(APIClient.APPEND_URL_e + "e_scan_details.php")
    Call<JsonObject> getqrList(@Body JsonObject object);

    @POST(APIClient.APPEND_URL_e + "e_rest_details.php")
    Call<JsonObject> getinformationList(@Body JsonObject object);

}
