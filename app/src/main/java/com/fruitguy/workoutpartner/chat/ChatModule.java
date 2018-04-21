package com.fruitguy.workoutpartner.chat;

import com.fruitguy.workoutpartner.data.ChatRepository;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ChatModule {

    @Binds
    abstract ChatContract.Presenter chaPresenter(ChatPresenter presenter);

}
