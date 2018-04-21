package com.fruitguy.workoutpartner.chat;

import android.net.Uri;
import android.text.TextUtils;

import com.fruitguy.workoutpartner.data.ChatRepository;
import com.fruitguy.workoutpartner.data.FirebaseRepository;

import javax.inject.Inject;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.CHAT_IMAGE_STORAGE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_IMAGE;

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

    @Override
    public void sendImageMessage(Uri uri, String friendUserId) {
        String pushId = mRepository.getChatMessagePushId(friendUserId);
        FirebaseRepository.UploadCallBack callback = new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof Uri) {
                    mRepository.sendMessage((Uri) data, friendUserId, new FirebaseRepository.UploadCallBack() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                        }

                        @Override
                        public void onFailure() {
                            super.onFailure();
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
            }
        };
        FirebaseRepository.UploadImageContainer container =
                new FirebaseRepository.UploadImageContainer(uri, pushId, CHAT_IMAGE_STORAGE, callback);
        mRepository.uploadImage(container);

    }
}
