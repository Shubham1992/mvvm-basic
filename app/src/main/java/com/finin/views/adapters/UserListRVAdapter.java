package com.finin.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finin.R;
import com.finin.models.user.User;

import java.util.List;

public class UserListRVAdapter extends RecyclerView.Adapter<UserListRVAdapter.CustomViewHolder> {

    private List<User> dataList;
    private Context context;
    private RecyclerView rvUserList;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public UserListRVAdapter(Context context, List<User> dataList, RecyclerView rvUserList) {
        this.context = context;
        this.dataList = dataList;
        this.rvUserList = rvUserList;

        if (rvUserList.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvUserList
                    .getLayoutManager();


            rvUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final User user = dataList.get(position);
        holder.tvFullName.setText(dataList.get(position).getFullName());
        holder.tvEmail.setText(dataList.get(position).getEmail());
        Glide.with(context).load(user.getAvatar()).into(holder.imgAvatar);
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

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}