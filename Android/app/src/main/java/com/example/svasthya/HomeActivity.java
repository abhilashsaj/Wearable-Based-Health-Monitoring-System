package com.example.svasthya;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
    private FirebaseAuth mAuth;


    private TextView diabetes_textview;
    private TextView bronchi_textview;
    private TextView hypoxemia_textview;
    private TextView asthma_textview;
    private TextView chd_textview;
    private TextView stress_textview;

    private String diabetes;
    private String bronchi;
    private String hypoxemia;
    private String asthma;
    private String chd ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

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

        diabetes_textview = (TextView)findViewById(R.id.diabetes);
        bronchi_textview = (TextView)findViewById(R.id.bronchi);
        chd_textview = (TextView)findViewById(R.id.chd);
        hypoxemia_textview = (TextView)findViewById(R.id.hypoxemia);
        asthma_textview = (TextView)findViewById(R.id.asthma);
        stress_textview = (TextView)findViewById(R.id.stress);


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

                            Map<String, Object> healthParam = new HashMap<>();

                            healthParam.put( "post_meal",post_meal);
                            healthParam.put( "blood_sugar_level",blood_sugar_level);
                            healthParam.put(   "breaths_per_minute",breaths_per_minute);
                            healthParam.put("is_running",is_running);
                            healthParam.put( "breath_shortness_severity",breath_shortness_severity);
                            healthParam.put( "cough_frequency",cough_frequency);
                            healthParam.put(   "cough_severity",cough_severity);
                            healthParam.put( "blood_pressure_sys",blood_pressure_sys);
                            healthParam.put( "blood_pressure_dia",blood_pressure_dia);
                            healthParam.put(   "heart_rate",heart_rate);
                            healthParam.put(   "cholestorol",cholestorol);
                            healthParam.put(  "oxygen_saturation",oxygen_saturation);
                            healthParam.put(  "entry_type","iot device");
                            healthParam.put("DEVICE_ID", getDeviceId(HomeActivity.this));




                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                            String format = simpleDateFormat.format(new Date());
                            Log.d("HomeActivity", "Current Timestamp: " + format);
                            healthParam.put("TIMESTAMP", format);

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            db.collection("users").document(user.getUid()).collection("health_data").document(format)
                                    .set(healthParam)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
//                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

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
//                            Toast.makeText(HomeActivity.this, "Calling Server...", Toast.LENGTH_SHORT).show();

//                            RequestBody requestBody = buildRequestBody("your message here");


                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("post_meal", post_meal)
                                    .addFormDataPart("blood_sugar_level", blood_sugar_level)
                                    .addFormDataPart("breaths_per_minute", breaths_per_minute)
                                    .addFormDataPart("is_running", is_running)
                                    .addFormDataPart("breath_shortness_severity", breath_shortness_severity)
                                    .addFormDataPart("cough_frequency", cough_frequency)
                                    .addFormDataPart("cough_severity", cough_severity)
                                    .addFormDataPart("blood_pressure_sys", blood_pressure_sys)
                                    .addFormDataPart("blood_pressure_dia", blood_pressure_dia)
                                    .addFormDataPart("heart_rate", heart_rate)
                                    .addFormDataPart("cholestorol", cholestorol)
                                    .addFormDataPart("oxygen_saturation", oxygen_saturation)
                                    .build();

                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request request = new Request
                                    .Builder()
                                    .post(requestBody)
                                    .url("http://" + "192.168.1.36" + ":" + 5000 + "/prediction_models")
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
                                                diabetes = obj.getString("diabetes");
                                                bronchi = obj.getString("bronchi");
                                                hypoxemia =  obj.getString("hypoxemia");
                                                asthma =  obj.getString("asthma");
                                                chd =  obj.getString("chd");

                                                diabetes_textview.setText(diabetes);
                                                bronchi_textview.setText(bronchi);
                                                hypoxemia_textview.setText(hypoxemia);
                                                asthma_textview.setText(asthma);
                                                chd_textview.setText(chd);

//                                                Toast.makeText(HomeActivity.this, obj.toString(), Toast.LENGTH_LONG).show();
                                            } catch (IOException | JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                }
                            });

//                            Toast.makeText(HomeActivity.this, obj.toString() + " "+ blood_sugar_level, Toast.LENGTH_LONG).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    public static String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
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