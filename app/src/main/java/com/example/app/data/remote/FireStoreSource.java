package com.example.app.data.remote;

import com.example.app.data.model.Account;
import com.example.app.interfaces.LoginCallback;
import com.example.app.utils.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreSource {
    private FirebaseFirestore db;

    public FireStoreSource() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAccountByUsername(String username, LoginCallback loginCallback) {
        db.collection("account")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (queryDocumentSnapshots.isEmpty()) {
                        loginCallback.onSuccess(null);
                        return;
                    }

                    loginCallback.onSuccess(
                            queryDocumentSnapshots.getDocuments().get(0)
                            .toObject(Account.class)
                    );

                })
                .addOnFailureListener(e -> {
                    loginCallback.onFailure();
                });
    }
}
