package com.finin.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.finin.R;
import com.finin.models.user.User;
import com.finin.viewmodels.UserDataViewModel;
import com.finin.views.adapters.UserListRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private UserDataViewModel model;
    private RecyclerView rvUserList;
    private UserListRVAdapter userListRVAdapter;
    ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        rvUserList = findViewById(R.id.rvUserList);
        userListRVAdapter = new UserListRVAdapter(UserListActivity.this, userList);
        rvUserList.setLayoutManager(new LinearLayoutManager(UserListActivity.this));
        rvUserList.setAdapter(userListRVAdapter);
        model = new ViewModelProvider(UserListActivity.this).get(UserDataViewModel.class);
        model.getObservableUser().observe(UserListActivity.this, userDataObserver);

    }

    final Observer<List<User>> userDataObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(@Nullable final List<User> newData) {
            // Update the UI, in this case, a TextView.
            userList.addAll(newData);
            userListRVAdapter.notifyDataSetChanged();
        }
    };
}
