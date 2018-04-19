package com.fruitguy.workoutpartner.nearby;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NearbyFragment extends Fragment implements NearbyContract.View
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = NearbyFragment.class.getSimpleName();

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.message_list)
    RecyclerView mMessageRecyclerView;

    @BindView(R.id.subscribe_switch)
    SwitchCompat mSubscribeSwitch;

    @BindView(R.id.post_button)
    FloatingActionButton mPostButton;

    private NearbyContract.Presenter mSearchPresenter;
    private MessageListAdapter mMessageListAdapter;
    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        ButterKnife.bind(this, rootView);
        mPostButton.setEnabled(false);
        mSubscribeSwitch.setEnabled(false);

        buildGoogleApiClient();
        initializeSearchPresenter();
        initializeRecyclerView();
        return rootView;
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(getActivity(), this)
                .build();
    }

    private void initializeSearchPresenter() {
        NearbyInteractor searchInteractor = Injection.provideSearchInteractor(mGoogleApiClient);
        FirebaseUser User = Injection.provideFirebaseUser();
        mSearchPresenter = new NearbyPresenter(User, searchInteractor, this);
    }

    private void initializeRecyclerView() {
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMessageRecyclerView.setLayoutManager(mLayoutManager);
        mMessageListAdapter = new MessageListAdapter();
        mMessageRecyclerView.setAdapter(mMessageListAdapter);
        mSwipeRefreshLayout.setOnRefreshListener( () -> {
                mMessageListAdapter.refresh();
                mSwipeRefreshLayout.setRefreshing(false);
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

    @OnClick(R.id.post_button)
    public void onPostButtonClicked(View view) {
        showChangeLangDialog();
    }

    @Override
    public void setPresenter(NearbyContract.Presenter presenter) {
        mSearchPresenter = presenter;
    }

    @Override
    public void showSnackbar(final String text) {
        Log.w(TAG, text);
        View container = getView().findViewById(R.id.fragment_search_container);
        Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyPostAlertBox() {
        showAlertBox(getString(R.string.error_empty_message));
    }

    private void showAlertBox(String message) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .create().show();
    }

    @Override
    public void setSubscribeSwitch(boolean value) {
        mSubscribeSwitch.setChecked(value);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ");
        mPostButton.setEnabled(false);
        mSubscribeSwitch.setEnabled(false);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        mPostButton.setEnabled(true);
        mSubscribeSwitch.setEnabled(true);
    }

    @Override
    public void addToMessageList(UserMessage message) {
        mMessageListAdapter.add(message);
    }

    @Override
    public void removeFromMessageList(UserMessage message) {
        mMessageListAdapter.remove(message);
    }

    @Override
    public void clearAllMessage() {
        mMessageListAdapter.clearAllMessage();
    }

    @Override
    public void refreshMessage() {
        mMessageListAdapter.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_post, null);
        dialogBuilder.setView(dialogView);

        final EditText messageEditText = dialogView.findViewById(R.id.post_edit_text);

        dialogBuilder.setTitle("Post Nearby Message");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", (dialog, which) -> mSearchPresenter.publish(messageEditText.getText().toString()));
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog postDialog = dialogBuilder.create();
        postDialog.show();
    }
}
