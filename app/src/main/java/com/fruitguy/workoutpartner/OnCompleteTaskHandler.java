package com.fruitguy.workoutpartner;

/**
 * Created by heliao on 11/7/17.
 */

public interface OnCompleteTaskHandler<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}
