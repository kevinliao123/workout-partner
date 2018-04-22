package com.fruitguy.workoutpartner.nearby;


import android.text.TextUtils;
import android.util.Log;

import com.fruitguy.workoutpartner.data.UserMessage;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by heliao on 1/15/18.
 */

public class NearbyPresenter implements NearbyContract.Presenter, NearbyInteractor.SearchCallBack {
    
    private static final String TAG = NearbyPresenter.class.getSimpleName();
    
    private FirebaseUser mCurrentUser;
    private NearbyInteractor mSearchInteractor;
    private NearbyContract.View mView;

    public NearbyPresenter(FirebaseUser user, NearbyInteractor searchInteractor, NearbyContract.View view) {
        mCurrentUser = user;
        mSearchInteractor = searchInteractor;
        mView = view;
        mSearchInteractor.initialize(this);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void takeView(Object view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void subscribe() {
        mSearchInteractor.subscribe(this);
    }

    @Override
    public void publish(String message) {
        if (TextUtils.isEmpty(message)) {
            mView.showEmptyPostAlertBox();
            return;
        }
        UserMessage userMessage = new UserMessage(mCurrentUser.getUid()
                , mCurrentUser.getPhotoUrl().toString()
                , message);
        mSearchInteractor.publish(userMessage, this);
    }

    @Override
    public void unsubscribe() {
        mSearchInteractor.unsubscribe();
        mView.setSubscribeSwitch(false);
        mView.enableSwitch(true);


    }

    @Override
    public void unpublish() {
        mSearchInteractor.unpublish();
        mView.clearAllMessage();
        mView.refreshMessage();
    }

    @Override
    public void onSubscribeSuccess() {
        Log.i(TAG, "onSubscribeSuccess: ");
        mView.enableSwitch(true);
    }

    @Override
    public void onSubscribeFailed() {
        Log.i(TAG, "onSubscribeFailed: ");
        mView.showSnackbar("No longer subscribing");
        mView.setSubscribeSwitch(false);
        mView.enableSwitch(true);
    }

    @Override
    public void onSubscribeExpired() {
        Log.i(TAG, "onSubscribeExpired: ");
        mView.showSnackbar("Could not subscribe");
        mView.setSubscribeSwitch(false);
        mView.enableSwitch(true);
    }

    @Override
    public void onPublishSuccess(UserMessage message) {
        Log.i(TAG, "onPublishSuccess: ");
        mView.addToMessageList(message);
        mView.refreshMessage();
    }

    @Override
    public void onPublishFailed() {
        Log.i(TAG, "onPublishFailed: ");
        mView.showSnackbar("No longer publishing");
    }

    @Override
    public void onPublishExpired() {
        Log.i(TAG, "onPublishExpired: ");
        mView.showSnackbar("Could not publish,");
    }

    @Override
    public void onMessageFound(UserMessage message) {
        mView.addToMessageList(message);
        mView.refreshMessage();
    }

    @Override
    public void onMessageLost(UserMessage message) {
        mView.removeFromMessageList(message);
        mView.refreshMessage();
    }
}
