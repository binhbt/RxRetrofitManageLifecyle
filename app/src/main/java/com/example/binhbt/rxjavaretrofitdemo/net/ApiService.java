package com.example.binhbt.rxjavaretrofitdemo.net;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;


/**
 * Created by binhbt on 6/7/2016.
 */
public class ApiService {
    static volatile ApiService singleton = null;
    private String baseUrl;
    private boolean isLogging;
    public ApiService(String baseUrl, boolean isLogging){
        this.baseUrl = baseUrl;
        this.isLogging = isLogging;
    }

    public <T> T create(final Class<T> clazz){
        Retrofit.Builder builder=  new Retrofit.Builder()
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build());
        if (this.isLogging){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            builder.client(client);
        }
        Retrofit retrofit = builder.build();
        return retrofit.create(clazz);
    }
    public static final class Builder {
        private String baseUrl;
        private boolean isLogging;
        public Builder baseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }
        public Builder logging(boolean isLogging){
            this.isLogging = isLogging;
            return this;
        }
        public ApiService build(){
            return new ApiService(this.baseUrl, this.isLogging);
        }
    }
}
