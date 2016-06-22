package com.example.binhbt.rxjavaretrofitdemo.net;

import com.example.binhbt.rxjavaretrofitdemo.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gary on 08/02/16.
 */
public interface EndPoints {
	String API_ENDPOINT = "http://www.android10.org/myapi/";

	//http://www.android10.org/myapi/users.json
	@GET("users.json")
	public Observable<List<User>>
	userEntityList();

	//http://www.android10.org/myapi/user_1.json
	@GET("user_{userId}.json")
	public Observable<User>
	userEntityById(@Path("userId") int userId);
}