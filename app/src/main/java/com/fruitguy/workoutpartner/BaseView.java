package com.fruitguy.workoutpartner;

/**
 * Created by heliao on 1/7/18.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
