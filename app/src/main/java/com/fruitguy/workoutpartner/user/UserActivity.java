package com.fruitguy.workoutpartner.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.data.User;
import com.fruitguy.workoutpartner.util.ImageUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FROM_USER_ID;

public class UserActivity extends DaggerAppCompatActivity implements UserContract.View {

    @BindView(R.id.profile_image)
    ImageView mImageView;

    @BindView(R.id.user_name)
    TextView mName;

    @BindView(R.id.user_status)
    TextView mStatus;

    @BindView(R.id.user_age)
    TextView mAge;

    @BindView(R.id.user_weight)
    TextView mWeight;

    @BindView(R.id.gender)
    TextView mGender;

    @BindView(R.id.send_request_button)
    Button mFriendRequest;

    @BindView(R.id.deny_request_button)
    Button mRequestDeny;

    private String mFriendUserId;




    @Inject
    UserContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mFriendUserId= getIntent().getStringExtra(FRIEND_USER_ID);
        mPresenter.start();
        mPresenter.takeView(this);
        mPresenter.retrieveRequestState(mFriendUserId);
        mPresenter.retrieveUserInfoById(mFriendUserId);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.dropView();
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {

    }

    public void updateUi(User user) {
        ImageUtils.loadImage(this, user.getImage(), mImageView);
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

    @OnClick(R.id.deny_request_button)
    public void onDenyRequestButtonClicked(View view) {
        mPresenter.removeFriendRequest(mFriendUserId);
    }

    @Override
    public void enableFriendRequest() {
        mFriendRequest.setEnabled(true);
    }

    @Override
    public void disableFriendRequest() {
        mFriendRequest.setEnabled(false);
    }

    @Override
    public void setFriendRequestButtonText(String string) {
        mFriendRequest.setText(string);
    }

    @Override
    public void showDenyRequestButton() {
        mRequestDeny.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDenyRequestButton() {
        mRequestDeny.setVisibility(View.INVISIBLE);
    }


}
