package com.finin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finin.models.user.User;
import com.finin.models.user.UserRepository;

import java.util.List;

public class UserDataViewModel extends AndroidViewModel {
    private MutableLiveData<List<User>> observableUser;

    public UserDataViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<User>> getObservableUser() {
        observableUser = UserRepository.getInstance().getUser(getApplication().getApplicationContext(), true);
        return observableUser;
    }

    public void loadMore(){
        UserRepository.getInstance().getUser(getApplication().getApplicationContext(), false);
    }

    public void refreshData() {
        UserRepository.getInstance().refreshUserData(getApplication().getApplicationContext());
    }
}
