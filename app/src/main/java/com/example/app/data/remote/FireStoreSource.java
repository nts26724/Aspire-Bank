package com.example.app.data.remote;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.model.Receipt;
import com.example.app.data.model.Transaction;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.ReceiptPaymentCallback;
import com.example.app.interfaces.TransactionCallback;
import com.example.app.utils.SessionManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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

    public void getCustomerByUsername(String username, HomeCustomerCallback homeCustomerCallback) {
        db.collection("customer")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {

                if (queryDocumentSnapshots.isEmpty()) {
                    homeCustomerCallback.onSuccess(null);
                    return;
                }

                homeCustomerCallback.onSuccess(
                    queryDocumentSnapshots.getDocuments().get(0)
                            .toObject(Customer.class).getFullName()
                );
            })
            .addOnFailureListener(e -> {
                homeCustomerCallback.onFailure();
            });
    }


    public void getTransactionByUsername(String username, TransactionCallback transactionCallback) {
        Task<QuerySnapshot> q1 = db.collection("transaction")
                .whereEqualTo("usernameTransfer", username)
                .get();

        Task<QuerySnapshot> q2 = db.collection("transaction")
                .whereEqualTo("usernameReceive", username)
                .get();



        Tasks.whenAllSuccess(q1, q2)
                .addOnSuccessListener(results -> {
                    List<DocumentSnapshot> allDocs = new ArrayList<>();

                    QuerySnapshot r1 = (QuerySnapshot) results.get(0);
                    if (r1 != null) allDocs.addAll(r1.getDocuments());

                    QuerySnapshot r2 = (QuerySnapshot) results.get(1);
                    if (r2 != null) allDocs.addAll(r2.getDocuments());

                    if (allDocs.isEmpty()) {
                        transactionCallback.onSuccess(null);
                        return;
                    }

                    List<Transaction> allTransactions = new ArrayList<>();
                    for (DocumentSnapshot doc : allDocs) {
                        allTransactions.add(doc.toObject(Transaction.class));
                    }
                    transactionCallback.onSuccess(allTransactions);
                })
                .addOnFailureListener(e -> transactionCallback.onFailure());
    }


    public void getReceiptByUsername(String username, ReceiptPaymentCallback receiptPaymentCallback) {
        db.collection("receipt")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        receiptPaymentCallback.onSuccess(null);
                        return;
                    }

                    List<Receipt> receipts = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        receipts.add(doc.toObject(Receipt.class));
                    }
                    receiptPaymentCallback.onSuccess(receipts);
                })
                .addOnFailureListener(e -> receiptPaymentCallback.onFailure());
    }
}
