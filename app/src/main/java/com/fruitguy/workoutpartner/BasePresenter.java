package com.fruitguy.workoutpartner;

/**
 * Created by heliao on 1/7/18.
 */

public interface BasePresenter<T> {

    void start();

    void takeView(T view);

    void dropView();
}
