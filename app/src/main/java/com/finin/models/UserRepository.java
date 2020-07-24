package com.finin.models;

import androidx.lifecycle.MutableLiveData;

import com.finin.apis.ApiService;
import com.finin.apis.RetrofitClientInstance;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class UserRepository {

    private static UserRepository userRepository;

    private UserRepository() {

    }

    public static UserRepository getInstance() {
        if (userRepository == null)
            userRepository = new UserRepository();
        return userRepository;
    }

    public MutableLiveData<String> getUser() {
        final MutableLiveData<String> data = new MutableLiveData<>();
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        ApiService service = retrofit.create(ApiService.class);
        service.getUsers(1,3).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        data.setValue(jsonObject.toString());

                    }

                    @Override
                    public void onError(Throwable e) {
                        data.setValue(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return data;
    }

}
