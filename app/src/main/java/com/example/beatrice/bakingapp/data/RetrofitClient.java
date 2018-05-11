package com.example.beatrice.bakingapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    static API api;
    final static String URL_FINAL = "https://d17h27t6h515a5.cloudfront.net/";

    public static API initialize() {

        Gson gson = new GsonBuilder().create();
        //response body
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        api = new Retrofit.Builder()
                .baseUrl(URL_FINAL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client((httpClientBuilder))//
                .callFactory(httpClientBuilder)
                .build()
                .create(API.class);

        return api;

    }
}
