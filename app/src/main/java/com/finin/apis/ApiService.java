package com.finin.apis;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "https://reqres.in/";

    @GET("/api/users")
    Observable<JsonObject> getUsers(@Query("page") int page, @Query("delay") int delay, @Query("per_page") int perPage);

}
