package com.waayu.owner.retrofit;


import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    static Retrofit retrofit = null;
//old
//    public static final String baseUrl = "https://owner.waayu.app/";

    public static final String baseUrl = "https://master.waayu.app/";

//     public static final String baseUrl = "https://waayupro.in/";
    public static final String APPEND_URL = "/sapi/";

    public static final String APPEND_URL_e = "/eapi/";

    public static UserService getInterface() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .client(client)
                .build();

        return retrofit.create(UserService.class);

    }

}
