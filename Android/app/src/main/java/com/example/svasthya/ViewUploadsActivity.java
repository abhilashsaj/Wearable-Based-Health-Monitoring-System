package com.example.svasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewUploadsActivity extends AppCompatActivity {

    ListView listView;

    //database reference to get uploads data
    DatabaseReference mDatabaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //list to store uploads data
    ArrayList<Upload> uploadList;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_uploads);

        uploadList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);




        //adding a clicklistener on listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Upload upload = uploadList.get(i);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getUrl()));
                startActivity(intent);
            }
        });

        Map<String, Object> file = new HashMap<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users").document(user.getUid()).collection("health_records")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Upload upload = new Upload(document.getString("name"),document.getString("url"));
                                uploadList.add(upload);
                                Log.d("Success", document.getId() + " => " + document.getData());
//                                Toast.makeText(getApplicationContext(), "Success " + document.getId() + " => " + document.getData(), Toast.LENGTH_LONG).show();
                                String[] uploads = new String[uploadList.size()];

                                for (int i = 0; i < uploads.length; i++) {
                                    uploads[i] = uploadList.get(i).getName();
                                }

                                adapter= new CustomAdapter(uploadList,getApplicationContext());
                                listView.setAdapter(adapter);
                            }
                        } else {
                            Log.w("Fail", "Error getting documents.", task.getException());
                        }
                    }
                });



        //retrieving upload data from firebase database
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Upload upload = postSnapshot.getValue(Upload.class);
//                    uploadList.add(upload);
//                }
//
//                String[] uploads = new String[uploadList.size()];
//
//                for (int i = 0; i < uploads.length; i++) {
//                    uploads[i] = uploadList.get(i).getName();
//                }
//
//                //displaying it to list
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
//                listView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}