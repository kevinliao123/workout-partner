package com.fruitguy.workoutpartner;

import com.fruitguy.workoutpartner.nearby.NearbyMessageHandler;
import com.fruitguy.workoutpartner.nearby.NearbyInteractor;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by heliao on 1/16/18.
 */

public class Injection {
    public static NearbyInteractor provideSearchInteractor(GoogleApiClient googleApiClient) {
        return NearbyMessageHandler.getInstance(googleApiClient);
    }

    public static FirebaseUser provideFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
