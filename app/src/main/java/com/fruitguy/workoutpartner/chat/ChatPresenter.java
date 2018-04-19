package com.fruitguy.workoutpartner.chat;

import android.text.TextUtils;

import com.fruitguy.workoutpartner.data.ChatRepository;
import com.fruitguy.workoutpartner.data.FirebaseRepository;

import javax.inject.Inject;

public class ChatPresenter implements ChatContract.Presenter {

    private ChatRepository mRepository;
    private ChatContract.View mView;
    @Inject
    public ChatPresenter(ChatRepository repository) {
        mRepository = repository;
    }

    @Override
    public void start() {
    }

    @Override
    public void takeView(ChatContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
        mRepository.shutdown();
    }

    @Override
    public void startChat(String friendUserId) {
        mRepository.startChat(friendUserId, null);
    }

    @Override
    public void sendMessage(String message, String friendUserId) {
        if(TextUtils.isEmpty(message)) {
            return;
        }
        mRepository.sendMessage(message, friendUserId, new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess() {
                mView.clearMessageBox();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
