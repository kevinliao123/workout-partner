package com.fruitguy.workoutpartner.search;


import android.text.TextUtils;

import com.fruitguy.workoutpartner.data.UserMessage;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by heliao on 1/15/18.
 */

public class SearchPresenter implements SearchContract.Presenter, SearchInteractor.SearchCallBack {
    private FirebaseUser mCurrentUser;
    private SearchInteractor mSearchInteractor;
    private SearchContract.View mView;

    public SearchPresenter(FirebaseUser user, SearchInteractor searchInteractor, SearchContract.View view) {
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
        mView.setSubscribeSwitch(false);

    }

    @Override
    public void unpublish() {
        mSearchInteractor.unpublish();
        mView.clearAllMessage();
        mView.refreshMessage();
    }

    @Override
    public void onSubscribeSuccess() {

    }

    @Override
    public void onSubscribeFailed() {
        mView.showSnackbar("No longer subscribing");
        mView.setSubscribeSwitch(false);
    }

    @Override
    public void onSubscribeExpired() {
        mView.showSnackbar("Could not subscribe");
        mView.setSubscribeSwitch(false);
    }

    @Override
    public void onPublishSuccess(UserMessage message) {
        mView.addToMessageList(message);
        mView.refreshMessage();
    }

    @Override
    public void onPublishFailed() {
        mView.showSnackbar("No longer publishing");
    }

    @Override
    public void onPublishExpired() {
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
