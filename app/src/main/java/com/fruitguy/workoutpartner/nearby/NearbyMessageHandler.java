package com.fruitguy.workoutpartner.nearby;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fruitguy.workoutpartner.data.UserMessage;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.internal.zzbq.checkNotNull;

/**
 * Created by heliao on 1/15/18.
 */

public class NearbyMessageHandler implements NearbyInteractor {

    private static final String TAG = NearbyMessageHandler.class.getSimpleName();
    private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.
    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).build();
    private static NearbyMessageHandler INSTANCE = null;

    private GoogleApiClient mGoogleApiClient;
    private MessageListener mMessageListener;
    private List<Message> mMessageList = new ArrayList<>();

    private NearbyMessageHandler(GoogleApiClient googleApiClient) {
        checkNotNull(googleApiClient);
        mGoogleApiClient = googleApiClient;
    }

    public static NearbyMessageHandler getInstance(GoogleApiClient googleApiClient) {
        if (INSTANCE == null) {
            INSTANCE = new NearbyMessageHandler(googleApiClient);
        }

        return INSTANCE;
    }

    @Override
    public void initialize(final SearchCallBack callBack) {

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                Log.i(TAG, "onFound: ");
                UserMessage userMessage = UserMessage.fromNearbyMessage(message);
                callBack.onMessageFound(userMessage);

            }

            @Override
            public void onLost(final Message message) {
                Log.i(TAG, "onFound: ");
                UserMessage userMessage = UserMessage.fromNearbyMessage(message);
                callBack.onMessageLost(userMessage);
            }
        };
    }

    @Override
    public void subscribe(final SearchCallBack callback) {
        if (mGoogleApiClient == null && !mGoogleApiClient.isConnected()) {
            return;
        }

        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new SubscribeCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer subscribing");
                        callback.onSubscribeExpired();
                    }
                }).build();

        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Subscribed successfully.");
                        } else {
                            Log.i(TAG, "Could not subscribe, status = " + status);
                            callback.onSubscribeFailed();
                        }
                    }
                });
    }

    @Override
    public void publish(final UserMessage userMessage, final SearchCallBack callback) {
        if (mGoogleApiClient == null && !mGoogleApiClient.isConnected()) {
            return;
        }

        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new PublishCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer publishing");
                        callback.onPublishExpired();
                    }
                }).build();
        final Message publishMessage = UserMessage.newNearbyMessage(userMessage);
        Nearby.Messages.publish(mGoogleApiClient, publishMessage, options)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Published successfully.");
                            callback.onPublishSuccess(userMessage);
                            mMessageList.add(publishMessage);
                        } else {
                            Log.i(TAG, "Could not publish, status = " + status);
                            callback.onPublishFailed();
                        }
                    }
                });
    }

    @Override
    public void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    }

    @Override
    public void unpublish() {
        Log.i(TAG, "Unpublishing.");
        for (Message message : mMessageList) {
            Nearby.Messages.unpublish(mGoogleApiClient, message);
        }
        mMessageList.clear();
    }
}
