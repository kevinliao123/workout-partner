package com.fruitguy.workoutpartner.chat;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;

public interface ChatContract {

    interface View extends BaseView<Presenter> {
        void clearMessageBox();

    }

    interface Presenter extends BasePresenter<View> {
        void startChat(String friendUserId);

        void sendMessage(String message, String friendUserId);
    }
}
