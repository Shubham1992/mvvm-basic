package com.finin.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.finin.R;
import com.finin.models.user.User;

import java.util.List;

public class UserListRVAdapter extends RecyclerView.Adapter<UserListRVAdapter.CustomViewHolder> {

    private List<User> dataList;
    private Context context;

    public UserListRVAdapter(Context context, List<User> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final User appointmentEntity = dataList.get(position);
        holder.tvFullName.setText(dataList.get(position).getFirst_name());

    }

    public void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
        context.startActivity(browse);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView tvFullName;
        TextView tvEmail;
        ImageView imgAvatar;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }

}