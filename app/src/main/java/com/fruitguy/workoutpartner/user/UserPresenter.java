package com.fruitguy.workoutpartner.user;

import com.fruitguy.workoutpartner.data.FirebaseRepository;
import com.fruitguy.workoutpartner.data.User;

import javax.inject.Inject;

public class UserPresenter implements UserContract.Presenter {

    private FirebaseRepository mRepository;
    private UserContract.View mView;

    @Inject
    public UserPresenter(FirebaseRepository repository) {
        mRepository = repository;
    }

    @Override
    public void start() {

    }

    @Override
    public void takeView(UserContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void retrieveUserInfoById(String id) {
        mRepository.getSelectedUserInfoById(id, data -> {
            User user = (User) data;
            mView.updateUi(user);
        });
    }
}
