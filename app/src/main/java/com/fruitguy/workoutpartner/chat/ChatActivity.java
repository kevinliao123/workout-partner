package com.fruitguy.workoutpartner.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.util.ImageUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_IMAGE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_NAME;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.MESSAGES_NODE;

public class ChatActivity extends DaggerAppCompatActivity implements ChatContract.View {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final int TOTAL_ITEM_TO_LOAD = 10;

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

    @BindView(R.id.message_list)
    RecyclerView mMessageList;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    String mCurrentUserId;
    String mFriendUserId;
    String mFriendUserName;
    String mFriendUserImage;
    MessageAdapter mAdapter;
    RecyclerView.AdapterDataObserver mDataObserver;

    @Inject
    ChatContract.Presenter mPresenter;

    private int mCurrentPage = 1;

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

        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setupRecyclerView(mCurrentPage);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            setupRecyclerView(++mCurrentPage);
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mSwipeRefreshLayout.setRefreshing(false);
            hideSoftwareKeyboard();
        });
    }

    private void hideSoftwareKeyboard() {
        View view = this.getCurrentFocus();
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setupRecyclerView(int numOfPages) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mMessageList.setLayoutManager(manager);
        mMessageList.setHasFixedSize(false);
        Query message = createQuery(numOfPages);
        FirebaseRecyclerOptions options = constructOption(message);
        attachRecyclerAdapter(options, manager);

    }

    private Query createQuery(int numOfPages) {
        return FirebaseDatabase.getInstance().getReference()
                .child(MESSAGES_NODE)
                .child(mCurrentUserId)
                .child(mFriendUserId)
                .limitToLast(numOfPages * TOTAL_ITEM_TO_LOAD);
    }

    private FirebaseRecyclerOptions constructOption(Query query) {
        return new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();
    }

    private void attachRecyclerAdapter(FirebaseRecyclerOptions options, LinearLayoutManager manager) {
        mAdapter = new MessageAdapter(options, this, mFriendUserId, mFriendUserImage);
        mDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                    mMessageList.scrollToPosition(friendlyMessageCount-1);
                }
        };
        mAdapter.registerAdapterDataObserver(mDataObserver);
        mAdapter.startListening();
        mMessageList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        mAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.dropView();
        mAdapter.stopListening();
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

    @OnFocusChange(R.id.chat_message_view)
    public void onChatMessageFocused(View view, boolean hasFocus) {
        if(hasFocus && !mAdapter.hasObservers()) {
            mAdapter.registerAdapterDataObserver(mDataObserver);
        }
    }
}
