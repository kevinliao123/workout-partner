package com.fruitguy.workoutpartner.conversation;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;

public interface ConvContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
