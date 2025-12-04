package com.example.app.data.remote;

import android.util.Log;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.model.Receipt;
import com.example.app.data.model.Transaction;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.PhoneNumberCallBack;
import com.example.app.interfaces.ReceiptPaymentCallback;
import com.example.app.interfaces.TransactionCallback;
import com.example.app.utils.SessionManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

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


    public void widthraw(String username, long price) {
        db.collection("account")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                    DocumentReference accountRef = doc.getReference();

                    db.runTransaction( transaction -> {
                        DocumentSnapshot snapshot = transaction.get(accountRef);

                        Long currentBalance = snapshot.getLong("balance");
                        if (currentBalance == null) currentBalance = 0L;

                        if (currentBalance < price) {
                            throw new FirebaseFirestoreException(
                                    "Insufficient balance",
                                    FirebaseFirestoreException.Code.ABORTED
                            );
                        }

                        Long newBalance = currentBalance - price;

                        transaction.update(accountRef, "balance", newBalance);

                        return null;
                    }).addOnSuccessListener(unused -> {
                        Log.d("Withdraw", "Withdraw success");
                    }).addOnFailureListener(e -> {
                        Log.e("Withdraw", "Failed: " + e.getMessage());
                    });
                }
            });
    }


    public void addTransaction(long amount, String content, boolean transfer,
                               String usernameTransfer, String usernameReceive) {

        CollectionReference transRef = db.collection("transaction");

        transRef.orderBy("transactionID", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {

                    long nextID = 1;

                    if (!snapshot.isEmpty()) {
                        DocumentSnapshot doc = snapshot.getDocuments().get(0);
                        String lastIDStr = doc.getString("transactionID");

                        try {
                            long lastID = Long.parseLong(lastIDStr);
                            nextID = lastID + 1;
                        } catch (Exception e) {
                            nextID = 1;
                        }
                    }

                    Map<String, Object> data = new HashMap<>();
                    data.put("amount", amount);
                    data.put("content", content);
                    data.put("time", System.currentTimeMillis());
                    data.put("transactionID", String.valueOf(nextID));
                    data.put("transfer", transfer);
                    data.put("usernameReceive", usernameReceive);
                    data.put("usernameTransfer", usernameTransfer);

                    transRef.add(data)
                            .addOnSuccessListener(ref -> {
                                Log.d("Transaction", "Create success: " + ref.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Transaction", "Create FAIL: " + e.getMessage());
                            });

                })
                .addOnFailureListener(e -> {
                    Log.e("Transaction", "Cannot get max ID: " + e.getMessage());
                });
    }


    public void getPhoneNumberByUsername(String username, PhoneNumberCallBack phoneNumberCallback) {
        db.collection("customer")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        String phone = doc.getString("phoneNumber");
                        phoneNumberCallback.onSuccess(phone);
                    } else {
                        phoneNumberCallback.onSuccess(null);
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    phoneNumberCallback.onFailure();
                });
    }


    public void deleteReceiptByReceiptID(String receiptID) {
        db.collection("receipt")
                .whereEqualTo("receiptID", receiptID)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String docId = querySnapshot.getDocuments().get(0).getId();

                        db.collection("receipt")
                                .document(docId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("deleteReceiptByReceiptID", "Success");
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("deleteReceiptByReceiptID", "Failure");
                                });

                    } else {
                        Log.d("deleteReceiptByReceiptID", "Not found");
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Log.d("deleteReceiptByReceiptID", "can't counenct");
                });
    }

}
