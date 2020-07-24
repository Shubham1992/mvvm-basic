package com.finin.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.finin.R;
import com.finin.viewmodels.UserDataViewModel;

public class UserListActivity extends AppCompatActivity {

    private UserDataViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        model = new ViewModelProvider(UserListActivity.this).get(UserDataViewModel.class);
        model.getObservableUser().observe(UserListActivity.this, userDataObserver);

    }

    final Observer<String> userDataObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable final String newData) {
            // Update the UI, in this case, a TextView.
            ((TextView)findViewById(R.id.tv)).setText(newData);
        }
    };
}
