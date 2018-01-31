package com.fruitguy.workoutpartner.search;

import com.fruitguy.workoutpartner.data.UserMessage;
import com.google.android.gms.nearby.messages.Message;

/**
 * Created by heliao on 1/16/18.
 */

public interface SearchInteractor {

    interface SearchCallBack{
        void onSubscribeSuccess();

        void onSubscribeFailed();

        void onSubscribeExpired();

        void onPublishSuccess(UserMessage message);

        void onPublishFailed();

        void onPublishExpired();

        void onMessageFound(UserMessage message);

        void onMessageLost(UserMessage message);
    }

    void initialize(SearchCallBack callBack);

    void subscribe(SearchCallBack callback);

    void unsubscribe();

    void publish(UserMessage publishMessage, SearchCallBack callback);

    void unpublish();
}
