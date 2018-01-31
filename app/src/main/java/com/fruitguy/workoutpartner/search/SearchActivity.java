package com.fruitguy.workoutpartner.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.fruitguy.workoutpartner.Injection;
import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.data.UserMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by heliao on 1/15/18.
 */

public class SearchActivity extends AppCompatActivity implements SearchContract.View
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.message_list)
    RecyclerView mMessageRecyclerView;

    @BindView(R.id.subscribe_switch)
    SwitchCompat mSubscribeSwitch;

    @BindView(R.id.publish_button)
    Button mPublishButton;

    @BindView(R.id.message_edit_text)
    EditText mMessageEditText;

    private SearchContract.Presenter mSearchPresenter;
    private MessageListAdapter mMessageListAdatper;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        buildGoogleApiClient();
        initializeSearchPresenter();
        initializeRecyclerView();
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
    }

    private void initializeSearchPresenter() {
        SearchInteractor searchInteractor = Injection.provideSearchInteractor(mGoogleApiClient);
        FirebaseUser User = Injection.provideFirebaseUser();
        mSearchPresenter = new SearchPresenter(User, searchInteractor, this);
    }

    private void initializeRecyclerView() {
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mMessageRecyclerView.setLayoutManager(mLayoutManager);
        mMessageListAdatper = new MessageListAdapter();
        mMessageRecyclerView.setAdapter(mMessageListAdatper);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMessageListAdatper.refresh();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @OnCheckedChanged(R.id.subscribe_switch)
    public void onSubscribeSwitchCheckedChange(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mSearchPresenter.subscribe();
        } else {
            mSearchPresenter.unsubscribe();
            mSearchPresenter.unpublish();
            refreshMessage();
        }
    }

    @OnClick(R.id.publish_button)
    public void onPublishButtonClicked(View view) {
        mSearchPresenter.publish(mMessageEditText.getText().toString());
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mSearchPresenter = presenter;
    }

    @Override
    public void showSnackbar(final String text) {
        Log.w(TAG, text);
        View container = findViewById(R.id.activity_search_container);
        Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyPostAlertBox() {
        showAlertBox(getString(R.string.error_empty_message));
    }

    private void showAlertBox(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .create().show();
    }

    @Override
    public void emptyMessageEditText() {
        mMessageEditText.getText().clear();
    }

    @Override
    public void setSubscribeSwitch(boolean value) {
        mSubscribeSwitch.setChecked(value);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ");
        mPublishButton.setEnabled(false);
        mSubscribeSwitch.setEnabled(false);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
    }

    @Override
    public void addToMessageList(UserMessage message) {
        mMessageListAdatper.add(message);
    }

    @Override
    public void removeFromMessageList(UserMessage message) {
        mMessageListAdatper.remove(message);
    }

    @Override
    public void clearAllMessage() {
        mMessageListAdatper.clearAllMessage();
    }

    @Override
    public void refreshMessage() {
        mMessageListAdatper.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
