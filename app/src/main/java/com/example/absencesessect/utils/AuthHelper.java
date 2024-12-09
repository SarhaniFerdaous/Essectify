package com.example.absencesessect.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthHelper {

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    // Get current authenticated user
    public static FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // Sign out user
    public static void signOut() {
        auth.signOut();
    }

    // Check if a user is logged in
    public static boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }
}
