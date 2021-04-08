package com.example.inframindfinals;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String url = "http://192.168.1.36:5000/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    String start_time ;
    String end_time;

    int break_times = 0;
    int meetings_attended = 0;
    int meetings_not_attended = 0;

    long hours;
    LocalTime startTimeObj;
    TextView meeting_textView;
    TextView message_textView;
    TextView duration_textView;
    TextView meeting_attended_textView;
    TextView meetings_not_attended_textView;
    TextView breaks_taken_textView;
    TextView date_textView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        meeting_textView = findViewById(R.id.meeting_textview);
        message_textView = findViewById(R.id.message_textview);

//        getHttpResponse();
        postRequest("hello", url);
        Thread t = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000*60*60);
                        postRequest("hello", url);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        };
        t.start();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        Date myDate = null;
        try {
            myDate = dateFormat.parse(LocalDate.now().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Date oneDayBefore = new Date(myDate.getTime() - 2);
//        String result = dateFormat.format(oneDayBefore);

//        Toast.makeText(MainActivity.this, "Result" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        db.collection("user_daily")
                .whereEqualTo("date", "2021-03-10")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals("breaks")){
                                    breaks_taken_textView.setText(document.getData().toString());
                                }
                                if(document.getId().equals("date")){
                                    date_textView.setText(document.getData().toString());
                                }
                                if(document.getId().equals("duration")){
                                    duration_textView.setText(document.getData().toString());
                                }
                                if(document.getId().equals("meetings_attended")){
                                    meeting_attended_textView.setText(document.getData().toString());
                                }
                                if(document.getId().equals("meetings_not_attended")){
                                    meetings_not_attended_textView.setText(document.getData().toString());
                                }

                                Toast.makeText(MainActivity.this, "Data: "+document.getId() + " => " + document.getData(), Toast.LENGTH_LONG).show();
//                                Log.d("Data", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("Data", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }


    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json= new JSONObject(response.body().string());
                            String event = json.getString("event");
                            String time = json.getString("time");
                            String message = json.getString("message");
                            int meeting_attended = json.getInt("meeting_attended");
                            meeting_textView.setText(event);
                            message_textView.setText(message);

                            if(event.equals("meeting") && meeting_attended == 1){
                                meetings_attended++;
                            }else{
                                meetings_not_attended++;
                            }

                            if(event.equals("break")){
                                break_times++;
                            }

                            // Create a new user with a first and last name
                            Map<String, Object> user_task = new HashMap<>();
                            user_task.put("e_id", "e_001");
                            user_task.put("time", time);
                            user_task.put("event", event);
                            user_task.put("type", "auto");
                            user_task.put("meeting_attended",meeting_attended);

// Add a new document with a generated ID
                            db.collection("user_details")
                                    .add(user_task)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                                            Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Firebase", "Error adding document", e);

                                        }
                                    });
//                            Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    public void goToManual(View view) {
        Intent i = new Intent(this, ManualActivity.class);
        startActivity(i);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startSession(View view) {
        startTimeObj = LocalTime.now();
        start_time = startTimeObj.toString();
        Toast.makeText(MainActivity.this, "Session started", Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stopSession(View view) {
        LocalTime myObj = LocalTime.now();
        hours = Duration.between(startTimeObj, myObj).toHours();

        Map<String, Object> user = new HashMap<>();
        user.put("duration", hours);
        user.put("meetings_attended", meetings_attended);
        user.put("meetings_not_attended", meetings_not_attended);
        user.put("date", LocalDate.now().toString());
        user.put("e_id", "e_001");
        user.put("breaks",break_times);

        db.collection("user_daily")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this, "Session Stopped" , Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error adding document", e);
                    }
                });


    }

    public void goToChatbot(View view) {

        Intent i = new Intent(this, ChatActivity.class);

        i.putExtra("duration", start_time);
        startActivity(i);
    }

    public void goToDino(View view) {
        Intent i = new Intent(this, DinoGame.class);


        startActivity(i);
    }
}