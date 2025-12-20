package com.example.app.data.remote;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.model.Officer;
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
                .addOnFailureListener(e -> loginCallback.onFailure());
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
                    Map<String, Transaction> uniqueTransactions = new HashMap<>();

                    QuerySnapshot r1 = (QuerySnapshot) results.get(0);
                    if (r1 != null) {
                        for (DocumentSnapshot doc : r1.getDocuments()) {
                            uniqueTransactions.put(doc.getId(), doc.toObject(Transaction.class));
                        }
                    }
                    QuerySnapshot r2 = (QuerySnapshot) results.get(1);
                    if (r2 != null) {
                        for (DocumentSnapshot doc : r2.getDocuments()) {
                            uniqueTransactions.put(doc.getId(), doc.toObject(Transaction.class));
                        }
                    }
                    if (uniqueTransactions.isEmpty()) {
                        transactionCallback.onSuccess(null);
                        return;
                    }
                    List<Transaction> allTransactions = new ArrayList<>(uniqueTransactions.values());
                    allTransactions.sort((t1, t2) -> Long.compare(t2.getTime(), t1.getTime()));
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
                                throw new FirebaseFirestoreException(
                                        "Insufficient balance",
                                        FirebaseFirestoreException.Code.ABORTED
                                );
                            }

                            Long newBalance = currentBalance - amount;

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
        db.collection("receipt")
                .whereEqualTo("receiptID", receiptID)
                .limit(1)
                .get()
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
        db.collection("rateProfit")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        Double rate = doc.getDouble("rate");
                        if (rate == null) rate = doc.getDouble("savingsRate");
                        double finalRate = (rate != null) ? rate : 5.0;

                        callback.onRateLoaded(new com.example.app.data.model.InterestRate(finalRate));
                    } else {
                        callback.onRateLoaded(new com.example.app.data.model.InterestRate(5.0));
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


    public void getNumberOfCustomer(MutableLiveData<String> numberOfCustomerLiveData) {
        db.collection("customer")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    numberOfCustomerLiveData.postValue(
                            String.valueOf(queryDocumentSnapshots.size())
                    );
                })
                .addOnFailureListener(e -> {
                    numberOfCustomerLiveData.postValue("0");
                });
    }


    public void getNumberOfOfficer(MutableLiveData<String> numberOfCustomerLiveData) {
        db.collection("officer")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    numberOfCustomerLiveData.postValue(
                            String.valueOf(queryDocumentSnapshots.size())
                    );
                })
                .addOnFailureListener(e -> {
                    numberOfCustomerLiveData.postValue("0");
                });
    }


    public void getRate(MutableLiveData<String> rateLiveData) {
        db.collection("rateProfit")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        Double rate = doc.getDouble("rate");

                        if (rate != null) {
                            rateLiveData.postValue(rate + " %");
                        } else {
                            rateLiveData.postValue("0 %");
                        }
                    } else {
                        rateLiveData.postValue("0 %");
                    }
                })
                .addOnFailureListener(e -> {
                    rateLiveData.postValue("0 %");
                });
    }


    public void getListCustomer(MutableLiveData<List<Account>> listCustomerLiveData) {
        db.collection("account")
                .whereEqualTo("role", "customer")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Account> customers = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Account account = doc.toObject(Account.class);
                        if (account != null) {
                            customers.add(account);
                        }
                    }

                    listCustomerLiveData.postValue(customers);
                })
                .addOnFailureListener(e -> {
                    listCustomerLiveData.postValue(new ArrayList<>());
                });
    }


    public void updateOfficer(String username, String fullName,
                              String birthDay, String phoneNumber,
                              String address, String email, String gender,
                              MutableLiveData<Boolean> isUpdateOfficerSuccess) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("fullName", fullName);
        updates.put("birthDay", birthDay);
        updates.put("phoneNumber", phoneNumber);
        updates.put("address", address);
        updates.put("email", email);
        updates.put("gender", gender);

        db.collection("officer")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener(qs -> {
                    if (qs.isEmpty()) {
                        isUpdateOfficerSuccess.postValue(false);
                        return;
                    }

                    DocumentSnapshot doc = qs.getDocuments().get(0);
                    db.collection("officer")
                            .document(doc.getId())
                            .update(updates);
                    isUpdateOfficerSuccess.postValue(true);
                })
                .addOnFailureListener(e -> {
                    isUpdateOfficerSuccess.postValue(false);
                    Log.d("updateOfficer", "updateOfficer: False");
                });
    }


    public void getOfficerByUserName(String username,
                                     MutableLiveData<Officer> officerLiveData) {

        db.collection("officer")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        Officer officer = doc.toObject(Officer.class);

                        if (officer != null) {
                            officerLiveData.postValue(officer);
                        } else {
                            officerLiveData.postValue(null);
                        }
                    } else {
                        officerLiveData.postValue(null);
                    }
                })
                .addOnFailureListener(e -> {
                    officerLiveData.postValue(null);
                });
    }


    public void updateRate(String rate, MutableLiveData<Boolean> isUpdateRateLiveData) {
        double rateValue;

        try {
            rateValue = Double.parseDouble(rate);
        } catch (NumberFormatException e) {
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("rate", rateValue);

        db.collection("rateProfit")
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);

                        db.collection("rateProfit")
                                .document(doc.getId())
                                .update(updates);
                        isUpdateRateLiveData.postValue(true);
                    } else {
                        isUpdateRateLiveData.postValue(false);
                    }
                })
                .addOnFailureListener(e -> {
                    isUpdateRateLiveData.postValue(false);
                    Log.d("updateRate", "updateRate: False");
                });
    }


    public void getCustomerObjectByUsername(String username, MutableLiveData<Customer> customerLiveData) {
        db.collection("customer")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        Customer customer = doc.toObject(Customer.class);

                        if (customer != null) {
                            customerLiveData.postValue(customer);
                        } else {
                            customerLiveData.postValue(null);
                        }
                    } else {
                        customerLiveData.postValue(null);
                    }
                })
                .addOnFailureListener(e -> {
                    customerLiveData.postValue(null);
                });
    }


    public void updateCustomer(String username, String fullName,
                               String birthDay, String phoneNumber,
                               String address, String email, String gender,
                               MutableLiveData<Boolean> isUpdateCustomerSuccess) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("fullName", fullName);
        updates.put("birthDay", birthDay);
        updates.put("phoneNumber", phoneNumber);
        updates.put("address", address);
        updates.put("email", email);
        updates.put("gender", gender);

        db.collection("customer")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener(qs -> {
                    if (qs.isEmpty()) {
                        isUpdateCustomerSuccess.postValue(false);
                        return;
                    }

                    DocumentSnapshot doc = qs.getDocuments().get(0);
                    db.collection("customer")
                            .document(doc.getId())
                            .update(updates);
                    isUpdateCustomerSuccess.postValue(true);
                })
                .addOnFailureListener(e -> {
                    isUpdateCustomerSuccess.postValue(false);
                    Log.d("updateOfficer", "updateOfficer: False");
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

    public void updateCustomerAvatar(String username, String avatarUrl, CustomerCallback callback) {
        db.collection("customer")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String docId = querySnapshot.getDocuments().get(0).getId();
                        db.collection("customer").document(docId)
                                .update("avatarUrl", avatarUrl)
                                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
                    } else {
                        callback.onFailure("Không tìm thấy thông tin khách hàng để cập nhật ảnh.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Lỗi kết nối: " + e.getMessage()));
    }
}