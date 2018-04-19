package com.fruitguy.workoutpartner.chat;

import com.fruitguy.workoutpartner.data.ChatRepository;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ChatModule {

    @Provides
    static ChatContract.Presenter provideChatPresenter(ChatRepository repository) {
        return new ChatPresenter(repository);
    }

}
