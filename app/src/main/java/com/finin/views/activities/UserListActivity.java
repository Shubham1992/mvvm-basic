package com.finin.views.activities;

import android.os.Bundle;
import android.util.Log;
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
import com.finin.models.internetconnection.ConnectionLiveData;
import com.finin.models.internetconnection.ConnectionModel;
import com.finin.models.user.User;
import com.finin.utils.AppHelper;
import com.finin.utils.Constants;
import com.finin.utils.SharedPrefsUtils;
import com.finin.viewmodels.UserDataViewModel;
import com.finin.views.adapters.UserListRVAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.finin.utils.Constants.MobileData;
import static com.finin.utils.Constants.WifiData;

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
        progressBarLayout.setVisibility(View.GONE);

        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(UserListActivity.this);

        ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());
        connectionLiveData.observe(this, internetObserver);

        setupViewModels();

    }

    private void setupViewModels() {
        model = new ViewModelProvider(UserListActivity.this).get(UserDataViewModel.class);
        model.getObservableUser().observe(UserListActivity.this, userDataObserver);

    }


    final Observer<List<User>> userDataObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(@Nullable final List<User> newData) {
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

    final Observer<ConnectionModel> internetObserver = new Observer<ConnectionModel>() {
        @Override
        public void onChanged(@Nullable ConnectionModel connection) {
            if (connection.getIsConnected()) {
                switch (connection.getType()) {
                    case WifiData:
                    case MobileData:
                        performRetry();
                        break;

                }
            } else {
                Toast.makeText(UserListActivity.this, String.format("No Internet Connection"), Toast.LENGTH_SHORT).show();
                if (userList.size() > 0) {
                    return;
                }
                container.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
                btnRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppHelper.isNetworkConnected(UserListActivity.this)) {
                            performRetry();
                        } else {
                            Toast.makeText(UserListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };


    final Observer<Boolean> internetConnectionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable final Boolean newData) {
            if (false) {
                Log.e(TAG, "no internet");
            }

        }
    };

    @Override
    public void onLoadMore() {
        if (!AppHelper.isNetworkConnected(UserListActivity.this)) {
            return;
        }
        final int page = SharedPrefsUtils.getIntegerPreference(UserListActivity.this, Constants.PAGE, 0) + 1;
        int totalPages = SharedPrefsUtils.getIntegerPreference(UserListActivity.this, Constants.TOTAL_PAGES, Integer.MAX_VALUE);
        if (page > totalPages) return;

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

    void performRetry() {
        noDataLayout.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        container.startShimmerAnimation();
        model.refreshData();
    }

}
