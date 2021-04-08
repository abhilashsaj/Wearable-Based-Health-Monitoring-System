package com.example.inframindfinals;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    EditText editText;
    ImageView addButton;
    ListView listView;
    ArrayList<ChatMessage> dataModels;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String msg_builder = "";
    private static CustomChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle b = new Bundle();
        b = getIntent().getExtras();

        final String start = b.getString("duration");


        editText = (EditText) findViewById(R.id.editText);
        addButton = (ImageView) findViewById(R.id.fab_img);
        listView = (ListView) findViewById(R.id.listView);
        listView.setDivider(null);


        dataModels= new ArrayList<>();

        dataModels.add(new ChatMessage("Hello! How can I help you", ""));
        adapter= new CustomChatAdapter(dataModels,getApplicationContext());
//
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {

                final String message = editText.getText().toString().trim().toLowerCase();


                if (!message.equals("")) {
                    msg_builder = "";
                    if(message.contains("started")){
                        msg_builder = "You started at " + start ;
                        ChatMessage chatMessage = new ChatMessage(msg_builder, message);
                        dataModels.add(chatMessage);
                        adapter.notifyDataSetChanged();

                    }
                    else if(message.contains("status")){

                        LocalTime lc2=LocalTime.now().plusHours(9);
                        msg_builder = "Your session completes at " +lc2;

                        ChatMessage chatMessage = new ChatMessage(msg_builder, message);
                        dataModels.add(chatMessage);
                        adapter.notifyDataSetChanged();

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


    }
}