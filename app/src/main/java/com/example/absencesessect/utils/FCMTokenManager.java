package com.example.absencesessect.utils;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMTokenManager {

    private static final String TAG = "FCMTokenManager";

    public static void fetchToken(TokenCallback callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        callback.onTokenReceived(token);
                    } else {
                        Log.e(TAG, "Failed to fetch FCM token", task.getException());
                        callback.onTokenReceived(null);
                    }
                });
    }

    public interface TokenCallback {
        void onTokenReceived(String token);
    }
}
