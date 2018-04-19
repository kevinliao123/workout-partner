package com.fruitguy.workoutpartner.chat;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.util.ImageUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_IMAGE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_NAME;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;

public class ChatActivity extends DaggerAppCompatActivity implements ChatContract.View {

    private static final String TAG = ChatActivity.class.getSimpleName();
    @BindView(R.id.chat_app_bar)
    Toolbar mToolbar;

    @BindView(R.id.user_name)
    TextView mUserName;

    @BindView(R.id.user_image)
    CircleImageView mUserImage;

    @BindView(R.id.add_button)
    ImageButton mAddButton;

    @BindView(R.id.chat_message_view)
    EditText mMessage;

    @BindView(R.id.chat_send_btn)
    ImageButton mSendButton;

    String mFriendUserId;
    String mFriendUserName;
    String mFriendUserImage;

    @Inject
    ChatContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate: ");

        mFriendUserId = getIntent().getStringExtra(FRIEND_USER_ID);
        mFriendUserName = getIntent().getStringExtra(FRIEND_NAME);
        mFriendUserImage = getIntent().getStringExtra(FRIEND_IMAGE);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mUserName.setText(mFriendUserName);
        ImageUtils.loadImage(this, mFriendUserImage, mUserImage);
        mPresenter.startChat(mFriendUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.dropView();
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {

    }

    @Override
    public void clearMessageBox() {
        mMessage.setText("");
    }

    @OnClick(R.id.chat_send_btn)
    public void onSendButtonClicked(View view) {
        String message = mMessage.getText().toString();
        mPresenter.sendMessage(message, mFriendUserId);
    }
}
