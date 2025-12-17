package com.example.app.data.remote;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.model.Receipt;
import com.example.app.data.model.Transaction;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.PhoneNumberCallBack;
import com.example.app.interfaces.ReceiptPaymentCallback;
import com.example.app.interfaces.RegisterCallback;
import com.example.app.interfaces.TransactionCallback;
import com.example.app.interfaces.CustomerCallback;
import com.example.app.interfaces.ReceiverCallback;
import com.example.app.utils.SessionManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreSource {
    private FirebaseFirestore db;

    public FireStoreSource() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAccountByNumber(String number, ReceiverCallback callback) {
        db.collection("account")
                .whereEqualTo("cardNumber", number)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String username = querySnapshot.getDocuments().get(0).getString("username");
                        getCustomerNameByUsername(username, callback);
                    } else {
                        checkAccountNumber(number, callback);
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Lỗi kết nối Server"));
    }

    private void checkAccountNumber(String number, ReceiverCallback callback) {
        db.collection("account")
                .whereEqualTo("accountNumber", number)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String username = querySnapshot.getDocuments().get(0).getString("username");
                        getCustomerNameByUsername(username, callback);
                    } else {
                        callback.onFailure("Số thẻ/Tài khoản không tồn tại");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Lỗi tìm kiếm"));
    }

    private void getCustomerNameByUsername(String username, ReceiverCallback callback) {
        db.collection("customer")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(custSnap -> {
                    if (!custSnap.isEmpty()) {
                        String fullName = custSnap.getDocuments().get(0).getString("fullName");
                        callback.onSuccess(fullName);
                    } else {
                        callback.onFailure("Tài khoản chưa định danh");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Lỗi lấy thông tin khách hàng"));
    }

    public void getAccountByUsername(String username, LoginCallback loginCallback) {
        db.collection("account").whereEqualTo("username", username).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        loginCallback.onSuccess(null);
                        return;
                    }
                    loginCallback.onSuccess(queryDocumentSnapshots.getDocuments().get(0).toObject(Account.class));
                })
                .addOnFailureListener(e -> loginCallback.onFailure());
    }

    public void getCustomerByUsername(String username, HomeCustomerCallback homeCustomerCallback) {
        db.collection("customer").whereEqualTo("username", username).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        homeCustomerCallback.onSuccess(null);
                        return;
                    }
                    homeCustomerCallback.onSuccess(queryDocumentSnapshots.getDocuments().get(0).toObject(Customer.class).getFullName());
                })
                .addOnFailureListener(e -> homeCustomerCallback.onFailure());
    }

    public void getTransactionByUsername(String username, TransactionCallback transactionCallback) {
        Task<QuerySnapshot> q1 = db.collection("transaction").whereEqualTo("usernameTransfer", username).get();
        Task<QuerySnapshot> q2 = db.collection("transaction").whereEqualTo("usernameReceive", username).get();

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

    public void widthraw(String username, long amount) {
        db.collection("account")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        DocumentReference accountRef = doc.getReference();
                        db.runTransaction(transaction -> {
                            DocumentSnapshot snapshot = transaction.get(accountRef);
                            Long currentBalance = snapshot.getLong("balance");
                            if (currentBalance == null) currentBalance = 0L;
                            if (currentBalance < amount) {
                                throw new FirebaseFirestoreException("Insufficient balance", FirebaseFirestoreException.Code.ABORTED);
                            }
                            transaction.update(accountRef, "balance", currentBalance - amount);
                            return null;
                        });
                    }
                });
    }

    public void addTransaction(long amount, String content, boolean transfer, String usernameTransfer, String usernameReceive) {
        CollectionReference transRef = db.collection("transaction");
        transRef.orderBy("transactionID", Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener(snapshot -> {
                    long nextID = 1;
                    if (!snapshot.isEmpty()) {
                        DocumentSnapshot doc = snapshot.getDocuments().get(0);
                        Long lastID = doc.getLong("transactionID");
                        try { nextID = lastID + 1; } catch (Exception e) { nextID = 1; }
                    }
                    Map<String, Object> data = new HashMap<>();
                    data.put("amount", amount);
                    data.put("content", content);
                    data.put("time", System.currentTimeMillis());
                    data.put("transactionID", nextID);
                    data.put("transfer", transfer);
                    data.put("usernameReceive", usernameReceive);
                    data.put("usernameTransfer", usernameTransfer);
                    transRef.add(data);
                });
    }

    public void getPhoneNumberByUsername(String username, PhoneNumberCallBack phoneNumberCallback) {
        db.collection("customer").whereEqualTo("username", username).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String phone = querySnapshot.getDocuments().get(0).getString("phoneNumber");
                        phoneNumberCallback.onSuccess(phone);
                    } else {
                        phoneNumberCallback.onSuccess(null);
                    }
                })
                .addOnFailureListener(e -> phoneNumberCallback.onFailure());
    }

    public void deleteReceiptByReceiptID(String receiptID) {
        db.collection("receipt").whereEqualTo("receiptID", receiptID).limit(1).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String docId = querySnapshot.getDocuments().get(0).getId();
                        db.collection("receipt").document(docId).delete();
                    }
                });
    }

    public void deposit(String username, long amount) {
        db.collection("account").whereEqualTo("username", username).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        DocumentReference accountRef = doc.getReference();
                        db.runTransaction(transaction -> {
                            DocumentSnapshot snapshot = transaction.get(accountRef);
                            Long currentBalance = snapshot.getLong("balance");
                            if (currentBalance == null) currentBalance = 0L;
                            transaction.update(accountRef, "balance", currentBalance + amount);
                            return null;
                        });
                    }
                });
    }

    public void getInterestRates(int termMonths, com.example.app.interfaces.RateCallback callback) {
        db.collection("rates").whereEqualTo("termMonths", termMonths).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        callback.onRateLoaded(querySnapshot.getDocuments().get(0).toObject(com.example.app.data.model.InterestRate.class));
                    } else {
                        callback.onRateLoaded(new com.example.app.data.model.InterestRate(termMonths, 3.5, 8.5));
                    }
                }).addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void registerUser(Account account, Customer customer, RegisterCallback callback) {
        WriteBatch batch = db.batch();
        batch.set(db.collection("account").document(), account);
        batch.set(db.collection("customer").document(), customer);
        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void updateSaving(String username, long amount) {
        db.collection("account").whereEqualTo("username", username).get()
                .addOnSuccessListener(doc -> {
                    if(!doc.isEmpty()) db.collection("account").document(doc.getDocuments().get(0).getId()).update("saving", FieldValue.increment(amount));
                });
    }

    public void updateMortgage(String username, long amount) {
        db.collection("account").whereEqualTo("username", username).get()
                .addOnSuccessListener(doc -> {
                    if(!doc.isEmpty()) db.collection("account").document(doc.getDocuments().get(0).getId()).update("mortgage", FieldValue.increment(amount));
                });
    }

    public void getCustomerDetail(String username, CustomerCallback callback) {
        db.collection("customer").whereEqualTo("username", username).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        callback.onSuccess(querySnapshot.getDocuments().get(0).toObject(Customer.class));
                    } else {
                        callback.onFailure("Không tìm thấy");
                    }
                }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}