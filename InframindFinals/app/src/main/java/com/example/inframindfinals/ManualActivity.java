package com.example.inframindfinals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManualActivity extends AppCompatActivity {

    EditText eventEditText ;
    EditText timeEditText ;
    EditText eventDetailsEditText;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        eventEditText = (EditText) findViewById(R.id.editText1);
        timeEditText = (EditText) findViewById(R.id.editText2);
        eventDetailsEditText = (EditText) findViewById(R.id.editText3);

    }

    public void submit_manual(View view) {

        String event = eventEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String details = eventDetailsEditText.getText().toString();

        Map<String, Object> user_details = new HashMap<>();
        user_details.put("e_id", "e_001");
        user_details.put("event", event);
        user_details.put("time", time);
        user_details.put("type", "manual");
        user_details.put("meeting_attended", details);

// Add a new document with a generated ID
        db.collection("user_details")
                .add(user_details)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ManualActivity.this, "Success", Toast.LENGTH_LONG).show();
                        Log.d("Manual", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Manual", "Error adding document", e);
                    }
                });


    }
}