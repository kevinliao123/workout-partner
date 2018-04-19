package com.fruitguy.workoutpartner.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.util.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public View mView;

    @BindView(R.id.user_image)
    CircleImageView mUserImage;

    @BindView(R.id.user_name)
    TextView mUserName;

    @BindView(R.id.user_status)
    TextView mUserStatus;

    String userId;

    public UserViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, mView);
    }

    public void setImage(Context context, String url) {
        ImageUtils.loadImage(context, url, mUserImage);
    }

    public void setUserName(String name) {
        mUserName.setText(name);
    }

    public void setUserStatus(String status) {
        mUserStatus.setText(status);
    }
}
