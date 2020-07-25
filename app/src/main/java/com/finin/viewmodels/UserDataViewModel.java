package com.finin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.finin.models.user.User;
import com.finin.models.user.UserRepository;

import java.util.List;

public class UserDataViewModel extends AndroidViewModel {
    private MutableLiveData<List<User>> observableUser;

    public UserDataViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<User>> getObservableUser() {
        observableUser = UserRepository.getInstance().getUser(getApplication().getApplicationContext());
        return observableUser;
    }
}
