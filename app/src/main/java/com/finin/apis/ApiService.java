package com.finin.apis;


import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "https://reqres.in/";

    @GET("api/users?page=1")
    Observable<JSONObject> getUsers(@Query("page") int page, @Query("delay") int delay);

}
