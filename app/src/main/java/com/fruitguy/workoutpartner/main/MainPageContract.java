package com.fruitguy.workoutpartner.main;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by heliao on 1/9/18.
 */

public interface MainPageContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        boolean doesUserExist();

        FirebaseUser getCurrentUser();
    }


}
