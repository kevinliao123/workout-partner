package com.fruitguy.workoutpartner.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import dagger.Module;
import dagger.Provides;

/**
 * Created by heliao on 2/28/18.
 */
@Module
public class DataModule {

    @Provides
    static FirebaseRepository provideUserRepository() {
        return new FirebaseRepository(FirebaseAuth.getInstance()
        , FirebaseDatabase.getInstance().getReference()
        , FirebaseStorage.getInstance().getReference());
    }
}
