package com.example.svasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewEntriesActivity extends AppCompatActivity {

    ArrayList<DataModel> dataModels;
    ListView listView;

    DatabaseReference mDatabaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static CustomAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);

        listView=(ListView)findViewById(R.id.listView);

        dataModels= new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //getting the upload
//                Upload upload = uploadList.get(i);
//
//                //Opening the upload file in browser using the upload url
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(upload.getUrl()));
//                startActivity(intent);
            }
        });

        Map<String, Object> record = new HashMap<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users").document(user.getUid()).collection("manual")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DataModel datamodel = new DataModel(document.getString("TIMESTAMP"),document.getString("url"));
                                uploadList.add(datamodel);
                                Log.d("Success", document.getId() + " => " + document.getData());
//                                Toast.makeText(getApplicationContext(), "Success " + document.getId() + " => " + document.getData(), Toast.LENGTH_LONG).show();
                                String[] data_models = new String[dataModels.size()];

                                for (int i = 0; i < uploads.length; i++) {
                                    data_models[i] = dataModels.get(i).getName();
                                }

                                //displaying it to list
                                adapter= new CustomAdapter(dataModels,getApplicationContext());

//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
//                                listView.setAdapter(adapter);



                                listView.setAdapter(adapter);
                            }
                        } else {
                            Log.w("Fail", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}