package com.fruitguy.workoutpartner.user;

import com.fruitguy.workoutpartner.data.FirebaseRepository;
import com.fruitguy.workoutpartner.data.User;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.*;

import javax.inject.Inject;

public class UserPresenter implements UserContract.Presenter {

    private FirebaseRepository mRepository;
    private UserContract.View mView;
    private String mCurrentState = STATE_NOT_FRIENDS;

    @Inject
    public UserPresenter(FirebaseRepository repository) {
        mRepository = repository;
    }

    @Override
    public void start() {
        mRepository.init();
    }

    @Override
    public void takeView(UserContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
        mRepository.shutdown();
    }

    public void retrieveRequestState(String friendUserId) {
        mRepository.retrieveRequestState(friendUserId, new FirebaseRepository.FriendRequestCallback() {
            @Override
            public void onReceived() {
                mCurrentState = STATE_REQUEST_RECEIVE;
                mView.setFriendRequestButtonText("Accept Friend Request");
            }

            @Override
            public void onSent() {
                mCurrentState = STATE_REQUEST_SENT;
                mView.setFriendRequestButtonText("Cancel Friend Request");
            }

            @Override
            public void onFriend() {
                mCurrentState = STATE_FRIENDS;
                mView.setFriendRequestButtonText("Unfriend");
            }
        });
    }

    @Override
    public void retrieveUserInfoById(String id) {
        mRepository.getSelectedUserInfoById(id, data -> {
            User user = (User) data;
            mView.updateUi(user);
        });
    }

    @Override
    public void onFriendRequestButtonClicked(String friendUserId) {
        switch (mCurrentState) {
            case STATE_NOT_FRIENDS:
                sentFriendRequest(friendUserId);
                break;
            case STATE_REQUEST_SENT:
                cancelFriendRequest(friendUserId);
                break;
            case STATE_REQUEST_RECEIVE:
                addFriend(friendUserId);
                break;
            case STATE_FRIENDS:
                unfriend(friendUserId);
                break;
        }
    }

    private void sentFriendRequest(String friendUserId) {
        mView.disableFriendRequest();
        mRepository.sentFriendRequest(friendUserId, new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess() {
                mCurrentState = STATE_REQUEST_SENT;
                mView.enableFriendRequest();
                mView.setFriendRequestButtonText("Cancel Friend Request");
            }

            @Override
            public void onFailure() {
                mView.enableFriendRequest();
            }
        });
    }

    private void cancelFriendRequest(String friendUserId) {
        mView.disableFriendRequest();
        mRepository.removeFriendRequest(friendUserId, new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess() {
                mCurrentState = STATE_NOT_FRIENDS;
                mView.enableFriendRequest();
                mView.setFriendRequestButtonText("Send Friend Request");
            }

            @Override
            public void onFailure() {
                mView.enableFriendRequest();
            }
        });
    }

    private void addFriend(String friendUserId) {
        mView.disableFriendRequest();
        mRepository.addFriend(friendUserId, new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess() {
                mRepository.removeFriendRequest(friendUserId, new FirebaseRepository.UploadCallBack() {
                    @Override
                    public void onSuccess() {
                        mCurrentState = STATE_FRIENDS;
                        mView.enableFriendRequest();
                        mView.setFriendRequestButtonText("Unfriend");
                    }

                    @Override
                    public void onFailure() {
                        mView.enableFriendRequest();
                    }
                });
            }

            @Override
            public void onFailure() {
                mView.enableFriendRequest();
            }
        });
    }

    private void unfriend(String friendUserId) {
        mView.disableFriendRequest();
        mRepository.unfriend(friendUserId, new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess() {
                mCurrentState = STATE_NOT_FRIENDS;
                mView.enableFriendRequest();
                mView.setFriendRequestButtonText("Send Friend Request");
            }

            @Override
            public void onFailure() {
                mView.enableFriendRequest();
            }
        });
    }
}
