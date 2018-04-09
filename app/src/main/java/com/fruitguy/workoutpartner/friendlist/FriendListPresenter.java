package com.fruitguy.workoutpartner.friendlist;

/**
 * Created by heliao on 3/5/18.
 */

public class FriendListPresenter implements FriendListContract.Presenter {

    FriendListContract.View mView;
    @Override
    public void start() {

    }

    @Override
    public void takeView(FriendListContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
