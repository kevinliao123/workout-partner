package com.fruitguy.workoutpartner.search;

/**
 * Created by heliao on 3/5/18.
 */

public class SearchPresenter implements SearchContract.Presenter {

    SearchContract.View mView;
    @Override
    public void start() {

    }

    @Override
    public void takeView(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
