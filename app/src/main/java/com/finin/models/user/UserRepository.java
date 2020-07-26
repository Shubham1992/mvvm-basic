package com.finin.models.user;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.finin.apis.ApiService;
import com.finin.apis.RetrofitClientInstance;
import com.finin.models.database.AppDatabase;
import com.finin.utils.AppHelper;
import com.finin.utils.Constants;
import com.finin.utils.SharedPrefsUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class UserRepository {

    private static UserRepository userRepository;
    final MutableLiveData<List<User>> data = new MutableLiveData<>();

    private UserRepository() {

    }

    public static UserRepository getInstance() {
        if (userRepository == null)
            userRepository = new UserRepository();
        return userRepository;
    }

    public MutableLiveData<List<User>> getUser(final Context applicationContext) {

        if (false && AppDatabase.getInstance(applicationContext).userDao().getAll().size() > 0) {
            data.setValue(AppDatabase.getInstance(applicationContext).userDao().getAll());
            return data;
        }

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        ApiService service = retrofit.create(ApiService.class);
        int perPage = AppHelper.getDensity(applicationContext).density > 2 ? 10 : 6;
        service.getUsers(1, Constants.DELAY, perPage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        if (jsonObject != null) {
                            SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.PAGE, jsonObject.get("page").getAsInt());
                            SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.TOTAL, jsonObject.get("total").getAsInt());
                            SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.TOTAL_PAGES, jsonObject.get("total_pages").getAsInt());
                            ArrayList<User> users = parseJsonResponse(jsonObject);
                            cacheData(applicationContext, users);
                            data.setValue( AppDatabase.getInstance(applicationContext).userDao().getAll());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return data;
    }

    private void cacheData(Context applicationContext, ArrayList<User> users) {
        AppDatabase.getInstance(applicationContext).userDao().insertAll(users);
        AppDatabase.getInstance(applicationContext).userDao().getAll();
    }

    private ArrayList<User> parseJsonResponse(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            User user = new User();
            user.setId(jsonArray.get(i).getAsJsonObject().get("id").getAsInt());
            user.setAvatar(jsonArray.get(i).getAsJsonObject().get("avatar").getAsString());
            user.setFirst_name(jsonArray.get(i).getAsJsonObject().get("first_name").getAsString());
            user.setLast_name(jsonArray.get(i).getAsJsonObject().get("last_name").getAsString());
            user.setEmail(jsonArray.get(i).getAsJsonObject().get("email").getAsString());
            users.add(user);
        }
        return users;
    }

}
