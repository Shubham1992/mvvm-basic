package com.finin.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.finin.R;
import com.finin.models.user.User;
import com.finin.utils.AppHelper;
import com.finin.viewmodels.UserDataViewModel;
import com.finin.views.adapters.UserListRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserListRVAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = UserListActivity.class.getSimpleName();
    private UserDataViewModel model;
    private RecyclerView rvUserList;
    private UserListRVAdapter userListRVAdapter;
    ArrayList<User> userList = new ArrayList<>();
    private ShimmerFrameLayout container;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noDataLayout;
    Button btnRetry;
    private FrameLayout progressBarLayout;

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
        noDataLayout = findViewById(R.id.noDataLayout);
        btnRetry = findViewById(R.id.btnRetry);
        progressBarLayout = findViewById(R.id.progressBarLayout);

        checkInternet();

        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(UserListActivity.this);

        model = new ViewModelProvider(UserListActivity.this).get(UserDataViewModel.class);
        model.getObservableUser().observe(UserListActivity.this, userDataObserver);

    }


    final Observer<List<User>> userDataObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(@Nullable final List<User> newData) {
            // Update the UI, in this case, a TextView.
            userList.clear();
            userList.addAll(newData);
            container.stopShimmerAnimation();
            container.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            noDataLayout.setVisibility(View.GONE);
            progressBarLayout.setVisibility(View.GONE);

            userListRVAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onLoadMore() {
        if (!AppHelper.isNetworkConnected(UserListActivity.this)) {
            return;
        }
        progressBarLayout.setVisibility(View.VISIBLE);
        model.loadMore();
    }

    @Override
    public void onRefresh() {
        if (!AppHelper.isNetworkConnected(UserListActivity.this)) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(UserListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        userListRVAdapter.refreshRVScrollData();
        model.refreshData();
    }

    private void checkInternet() {
        if (!AppHelper.isNetworkConnected(UserListActivity.this)) {
            container.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppHelper.isNetworkConnected(UserListActivity.this)) {
                        container.setVisibility(View.VISIBLE);
                        container.startShimmerAnimation();
                        model.refreshData();
                    } else {
                        Toast.makeText(UserListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
