package com.finin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.finin.models.UserRepository;

public class UserDataViewModel extends ViewModel {
    private MutableLiveData<String> observableUser;

    public MutableLiveData<String> getObservableUser() {
        observableUser = UserRepository.getInstance().getUser();
        return observableUser;
    }
}
