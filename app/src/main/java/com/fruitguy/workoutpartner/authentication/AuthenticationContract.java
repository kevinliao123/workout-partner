package com.fruitguy.workoutpartner.authentication;

import android.content.Intent;
import android.support.annotation.StringRes;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by heliao on 1/7/18.
 */

public interface AuthenticationContract {

    interface View extends BaseView<Presenter> {
       void showSnackbar(@StringRes int errorMessageRes);

       void finishActivity();

       void startMainActivity();

    }

    interface Presenter extends BasePresenter {

        void onAuthenticationResponse(int requestCode, int resultCode, Intent data);

        FirebaseUser getCurrentUser();

        boolean isSignInSuccessful();
    }
}
