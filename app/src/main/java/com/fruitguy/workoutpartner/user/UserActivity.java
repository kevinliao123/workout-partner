package com.fruitguy.workoutpartner.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.data.User;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class UserActivity extends DaggerAppCompatActivity implements UserContract.View {

    @BindView(R.id.profile_image)
    ImageView mImageView;

    @BindView(R.id.user_name)
    TextView mName;

    @BindView(R.id.user_status)
    TextView mStatus;

    @BindView(R.id.total_friends)
    TextView mCountOfFriend;

    @BindView(R.id.user_age)
    TextView mAge;

    @BindView(R.id.user_weight)
    TextView mWeight;

    @BindView(R.id.gender)
    TextView mGender;

    @BindView(R.id.send_request_button)
    Button mFriendRequest;

    @BindView(R.id.decline_request_button)
    Button mRequestDeny;

    private String mFriendUserId;




    @Inject
    UserContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mFriendUserId= getIntent().getStringExtra("user_id");
        mPresenter.start();
        mPresenter.takeView(this);
        mPresenter.retrieveRequestState(mFriendUserId);
        mPresenter.retrieveUserInfoById(mFriendUserId);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {

    }

    public void updateUi(User user) {
        Picasso.with(this).load(user.getImage()).into(mImageView);
        mName.setText(user.getName());
        mStatus.setText(user.getStatus());
        mAge.setText(String.valueOf(user.getAge()));
        mWeight.setText(String.valueOf(user.getWeight()));
        mGender.setText(user.getGender());
    }

    @OnClick(R.id.send_request_button)
    public void onFriendRequestClicked(View view) {
        mPresenter.onFriendRequestButtonClicked(mFriendUserId);
    }

    public void enableFriendRequest() {
        mFriendRequest.setEnabled(true);
    }

    public void disableFriendRequest() {
        mFriendRequest.setEnabled(false);
    }

    public void setFriendRequestButtonText(String string) {
        mFriendRequest.setText(string);
    }


}
