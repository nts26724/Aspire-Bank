package com.example.app;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("TEST", "ON CREATE START");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("TEST", "DB INIT OK");


        db.collection("test")
                .document("EDKVzgXMxPz8urHMaqde")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        Log.d("FIRESTORE", "Name: " + name + ", Age: ");
                    }
                    Log.d("FIRESTORE", "SUCCESS: " + documentSnapshot.getData());
                })
                .addOnFailureListener(e -> Log.d("FIRESTORE", "Error", e));

    }
}