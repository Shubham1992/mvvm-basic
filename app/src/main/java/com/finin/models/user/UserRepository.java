package com.finin.models.user;

import android.content.Context;
import android.widget.Toast;

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

    public MutableLiveData<List<User>> getUser(final Context applicationContext, boolean fetchFromDB) {

        if (isCacheInvalid(applicationContext)) {
            SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.PAGE, 0);
        }

        if (fetchFromDB && AppDatabase.getInstance(applicationContext).userDao().getAll().size() > 0) {
            data.setValue(AppDatabase.getInstance(applicationContext).userDao().getAll());
            if (!isCacheInvalid(applicationContext))
                return data;
        }

        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        ApiService service = retrofit.create(ApiService.class);
        int perPage = AppHelper.getDensity(applicationContext).density > 2 ? 10 : 6;
        final int page = SharedPrefsUtils.getIntegerPreference(applicationContext, Constants.PAGE, 0) + 1;
        int totalPages = SharedPrefsUtils.getIntegerPreference(applicationContext, Constants.TOTAL_PAGES, Integer.MAX_VALUE);
        if (page > totalPages) {
            return data;
        }

        service.getUsers(page, Constants.DELAY, perPage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        if (jsonObject != null) {
                            if (page == 1) {
                                AppDatabase.getInstance(applicationContext).userDao().deleteAll();
                            }
                            SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.PAGE, jsonObject.get("page").getAsInt());
                            SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.TOTAL, jsonObject.get("total").getAsInt());
                            SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.TOTAL_PAGES, jsonObject.get("total_pages").getAsInt());

                            ArrayList<User> users = parseJsonResponse(jsonObject);
                            cacheData(applicationContext, users);
                            data.setValue(AppDatabase.getInstance(applicationContext).userDao().getAll());
                            if (page == 1) {
                                SharedPrefsUtils.setLongPreference(applicationContext, Constants.LAST_CACHE_UPDATE_TIMESTAMP, System.currentTimeMillis());
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return data;
    }

    private boolean isCacheInvalid(Context context) {
        if (System.currentTimeMillis() - SharedPrefsUtils.getLongPreference(context, Constants.LAST_CACHE_UPDATE_TIMESTAMP, 0) > Constants.CACHE_EXPIRY_IN_MILLISECONDS)
            return true;
        return false;
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

    public void refreshUserData(Context applicationContext) {
        AppDatabase.getInstance(applicationContext).userDao().deleteAll();
        SharedPrefsUtils.setIntegerPreference(applicationContext, Constants.PAGE, 0);
        getUser(applicationContext, false);

    }
}
