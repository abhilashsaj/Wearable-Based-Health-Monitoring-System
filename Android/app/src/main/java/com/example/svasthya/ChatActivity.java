package com.example.svasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    EditText editText;
    ImageView addButton;
    ListView listView;
    ArrayList<ChatMessage> dataModels;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String msg_builder = "";
    private static CustomChatAdapter adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editText = (EditText) findViewById(R.id.editText);
        addButton = (ImageView) findViewById(R.id.fab_img);
        listView = (ListView) findViewById(R.id.listView);


        mAuth = FirebaseAuth.getInstance();
        dataModels= new ArrayList<>();

        dataModels.add(new ChatMessage("Hello! How can I help you", ""));
        adapter= new CustomChatAdapter(dataModels,getApplicationContext());
//
        listView.setAdapter(adapter);
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final String message = editText.getText().toString().trim();


                if (!message.equals("")) {
                    if(message.equals("files")){
                        db.collection("user_health_record")
                                .whereEqualTo("UID", currentUser.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("Chat Activity", document.getId() + " => " + document.getData());
                                                Toast.makeText(ChatActivity.this, document.getData().toString(), Toast.LENGTH_SHORT).show();
                                                msg_builder = msg_builder + document.getString("url")+":"+ document.getString("url")+"\n";
                                                ChatMessage chatMessage = new ChatMessage(msg_builder, message);
                                                dataModels.add(chatMessage);
                                                adapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            Log.d("Chat Activity", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                    else if(message.equals("files"){
                        db.collection("")
                                .whereEqualTo("UID", currentUser.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("Chat Activity", document.getId() + " => " + document.getData());
                                                Toast.makeText(ChatActivity.this, document.getData().toString(), Toast.LENGTH_SHORT).show();
                                                msg_builder = msg_builder + document.getString("url")+":"+ document.getString("url")+"\n";
                                                ChatMessage chatMessage = new ChatMessage(msg_builder, message);
                                                dataModels.add(chatMessage);
                                                adapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            Log.d("Chat Activity", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                    else{
                        ChatMessage chatMessage = new ChatMessage("Sorry. I did not get You.", message);
                        dataModels.add(chatMessage);
                        adapter.notifyDataSetChanged();
                    }


                }

                editText.setText("");



            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position,
//                                    long id) {
//                Toast.makeText(ChatActivity.this, "Clicked", Toast.LENGTH_LONG)
//                        .show();
//            }
//        });

    }
}