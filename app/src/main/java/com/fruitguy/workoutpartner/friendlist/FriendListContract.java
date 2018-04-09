package com.fruitguy.workoutpartner.friendlist;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;

/**
 * Created by heliao on 3/5/18.
 */

public interface FriendListContract {

    interface Presenter extends BasePresenter<View> {

    }

    interface View extends BaseView<Presenter> {

    }
}
