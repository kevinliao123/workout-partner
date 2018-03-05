package com.fruitguy.workoutpartner.main;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by heliao on 1/9/18.
 */

public class MainPagePresenter implements MainPageContract.Presenter {
    private MainPageContract.View mView;

    public MainPagePresenter(MainPageContract.View view) {
        mView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void takeView(Object view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public boolean doesUserExist() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void signout() {
        FirebaseAuth.getInstance().signOut();
    }
}
