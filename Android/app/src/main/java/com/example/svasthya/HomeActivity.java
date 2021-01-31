package com.example.svasthya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {

    private String url = "http://" + "192.168.1.36" + ":" + 5000 + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;
    private Button connect;

    private String post_meal;
    private String blood_sugar_level;
    private String breaths_per_minute;
    private String is_running;
    private String breath_shortness_severity;
    private String cough_frequency;
    private String cough_severity;
    private String blood_pressure_sys;
    private String blood_pressure_dia;
    private String heart_rate;
    private String cholestorol;
    private String oxygen_saturation;

    private TextView post_meal_textview;
    private TextView blood_sugar_level_textview;
    private TextView breaths_per_minute_textview;
    private TextView is_running_textview;
    private TextView breath_shortness_severity_textview;
    private TextView cough_frequency_textview;
    private TextView cough_severity_textview;
    private TextView blood_pressure_sys_textview;
    private TextView blood_pressure_dia_textview;
    private TextView heart_rate_textview;
    private TextView cholestorol_textview;
    private TextView oxygen_saturation_textview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        post_meal_textview = (TextView)findViewById(R.id.post_meal);
        blood_sugar_level_textview = (TextView)findViewById(R.id.blood_sugar_level);
        breaths_per_minute_textview = (TextView)findViewById(R.id.breaths_per_minute);
        is_running_textview = (TextView)findViewById(R.id.is_running);
        breath_shortness_severity_textview = (TextView)findViewById(R.id.breath_shortness_severity);
        cough_frequency_textview = (TextView)findViewById(R.id.cough_frequency);
        cough_severity_textview = (TextView)findViewById(R.id.cough_severity);
        blood_pressure_sys_textview = (TextView)findViewById(R.id.blood_pressure_sys);
        blood_pressure_dia_textview = (TextView)findViewById(R.id.blood_pressure_dia);
        heart_rate_textview = (TextView)findViewById(R.id.heart_rate);
        cholestorol_textview = (TextView)findViewById(R.id.cholestorol);
        oxygen_saturation_textview = (TextView)findViewById(R.id.oxygen_saturation);

        postRequest("your message here", url);

        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {

                postRequest("your message here", url);

                h.postDelayed(this, 30000);
            }
        });

        connect = findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest("your message here", url);

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


                        Toast.makeText(HomeActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            JSONObject obj = new JSONObject(response.body().string());

                            post_meal = obj.getString("post_meal");
                            blood_sugar_level = obj.getString("blood_sugar_level");
                            breaths_per_minute =  obj.getString("breaths_per_minute");
                            is_running =  obj.getString("is_running");
                            breath_shortness_severity =  obj.getString("breath_shortness_severity");
                            cough_frequency =  obj.getString("cough_frequency");
                            cough_severity =  obj.getString("cough_severity");
                            blood_pressure_sys =  obj.getString("blood_pressure_sys");
                            blood_pressure_dia =  obj.getString("blood_pressure_dia");
                            heart_rate =  obj.getString("heart_rate");
                            cholestorol =  obj.getString("cholestorol");
                            oxygen_saturation =  obj.getString("oxygen_saturation");

                            post_meal_textview.setText(post_meal);
                            blood_sugar_level_textview.setText(blood_sugar_level);
                            breaths_per_minute_textview.setText(breaths_per_minute);
                            is_running_textview.setText(is_running);
                            breath_shortness_severity_textview.setText(breath_shortness_severity);
                            cough_frequency_textview.setText(cough_frequency);
                            cough_severity_textview.setText(cough_severity);
                            blood_pressure_sys_textview.setText(blood_pressure_sys);
                            blood_pressure_dia_textview.setText(blood_pressure_dia);
                            heart_rate_textview.setText(heart_rate);
                            cholestorol_textview.setText(cholestorol);
                            oxygen_saturation_textview.setText(oxygen_saturation);

//                            Toast.makeText(HomeActivity.this, obj.toString() + " "+ blood_sugar_level, Toast.LENGTH_LONG).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }

    public void syncValues(View view) {

    }

    public void goToManual(View view) {
        Intent intent = new Intent(HomeActivity.this, ManualEntryActivity.class);
        startActivity(intent);
    }
}