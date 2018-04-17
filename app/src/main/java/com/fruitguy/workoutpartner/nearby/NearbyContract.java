package com.fruitguy.workoutpartner.nearby;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;
import com.fruitguy.workoutpartner.data.UserMessage;

/**
 * Created by heliao on 1/15/18.
 */

public interface NearbyContract {
    interface Presenter extends BasePresenter {
        void subscribe();

        void publish(String message);

        void unsubscribe();

        void unpublish();

    }

    interface View extends BaseView<Presenter> {
        void showSnackbar(String message);

        void showEmptyPostAlertBox();

        void setSubscribeSwitch(boolean value);

        void addToMessageList(UserMessage message);

        void removeFromMessageList(UserMessage message);

        void clearAllMessage();

        void refreshMessage();

    }
}
