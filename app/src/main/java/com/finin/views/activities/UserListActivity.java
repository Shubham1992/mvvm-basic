package com.finin.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.finin.R;
import com.finin.models.user.User;
import com.finin.utils.AppHelper;
import com.finin.viewmodels.UserDataViewModel;
import com.finin.views.adapters.UserListRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserListRVAdapter.OnLoadMoreListener {

    private static final String TAG = UserListActivity.class.getSimpleName();
    private UserDataViewModel model;
    private RecyclerView rvUserList;
    private UserListRVAdapter userListRVAdapter;
    ArrayList<User> userList = new ArrayList<>();
    private ShimmerFrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        AppHelper.setStatusBarColor(UserListActivity.this);
        rvUserList = findViewById(R.id.rvUserList);
        container = findViewById(R.id.shimmer_view_container);
        container.startShimmerAnimation();
        rvUserList.setLayoutManager(new LinearLayoutManager(UserListActivity.this));
        userListRVAdapter = new UserListRVAdapter(UserListActivity.this, userList, rvUserList);
        rvUserList.setAdapter(userListRVAdapter);
        userListRVAdapter.setOnLoadMoreListener(UserListActivity.this);
        model = new ViewModelProvider(UserListActivity.this).get(UserDataViewModel.class);
        model.getObservableUser().observe(UserListActivity.this, userDataObserver);

    }

    final Observer<List<User>> userDataObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(@Nullable final List<User> newData) {
            // Update the UI, in this case, a TextView.
            userList.addAll(newData);
            container.stopShimmerAnimation();
            container.setVisibility(View.GONE);
            userListRVAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onLoadMore() {
        Log.d(TAG, "load more called");
    }
}
