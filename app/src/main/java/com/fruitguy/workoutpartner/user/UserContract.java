package com.fruitguy.workoutpartner.user;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;
import com.fruitguy.workoutpartner.data.User;

public interface UserContract {

    interface Presenter extends BasePresenter<View> {
        void retrieveUserInfoById(String id);

        void onFriendRequestButtonClicked(String friendUserId);

        void retrieveRequestState(String friendUserId);
    }

    interface View extends BaseView<Presenter> {

        void updateUi(User user);

        void enableFriendRequest();

        void disableFriendRequest();

        void setFriendRequestButtonText(String string);

    }
}
