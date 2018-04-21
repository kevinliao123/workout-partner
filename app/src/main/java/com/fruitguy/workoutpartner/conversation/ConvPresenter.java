package com.fruitguy.workoutpartner.conversation;

import com.fruitguy.workoutpartner.data.ChatRepository;

import javax.inject.Inject;

public class ConvPresenter implements ConvContract.Presenter {

    ConvContract.View mView;
    ChatRepository mRepo;

    @Inject
    ConvPresenter(ChatRepository repository) {
        mRepo = repository;
    }

    @Override
    public void start() {

    }

    @Override
    public void takeView(ConvContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }


}
