package com.example.svasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ManualEntryActivity extends AppCompatActivity {

    private EditText post_meal_textview;
    private EditText blood_sugar_level_textview;
    private EditText breaths_per_minute_textview;
    private EditText is_running_textview;
    private EditText breath_shortness_severity_textview;
    private EditText cough_frequency_textview;
    private EditText cough_severity_textview;
    private EditText blood_pressure_sys_textview;
    private EditText blood_pressure_dia_textview;
    private EditText heart_rate_textview;
    private EditText cholestorol_textview;
    private EditText oxygen_saturation_textview;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        post_meal_textview = (EditText) findViewById(R.id.post_meal);
        blood_sugar_level_textview = (EditText) findViewById(R.id.blood_sugar_level);
        breaths_per_minute_textview = (EditText) findViewById(R.id.breaths_per_minute);
        is_running_textview = (EditText) findViewById(R.id.is_running);
        breath_shortness_severity_textview = (EditText) findViewById(R.id.breath_shortness_severity);
        cough_frequency_textview = (EditText) findViewById(R.id.cough_frequency);
        cough_severity_textview = (EditText) findViewById(R.id.cough_severity);
        blood_pressure_sys_textview = (EditText) findViewById(R.id.blood_pressure_sys);
        blood_pressure_dia_textview = (EditText) findViewById(R.id.blood_pressure_dia);
        heart_rate_textview = (EditText) findViewById(R.id.heart_rate);
        cholestorol_textview = (EditText) findViewById(R.id.cholestorol);
        oxygen_saturation_textview = (EditText) findViewById(R.id.oxygen_saturation);


    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ManualEntryActivity.this, MainActivity.class));
        finish();
    }

    public void submitToServer(View view) {

        post_meal = post_meal_textview.getText().toString();
        blood_sugar_level = blood_sugar_level_textview.getText().toString();
        breaths_per_minute = breaths_per_minute_textview.getText().toString();
        is_running = is_running_textview.getText().toString();
        breath_shortness_severity = breath_shortness_severity_textview.getText().toString();
        cough_frequency = cough_frequency_textview.getText().toString();
        cough_severity = cough_severity_textview.getText().toString();
        blood_pressure_sys= blood_pressure_sys_textview.getText().toString();
        blood_pressure_dia = blood_pressure_dia_textview.getText().toString();
        heart_rate = heart_rate_textview.getText().toString();
        cholestorol = cholestorol_textview.getText().toString();
        oxygen_saturation = oxygen_saturation_textview.getText().toString();

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
        healthParam.put(  "entry_type","manual");
        healthParam.put("DEVICE_ID", getDeviceId(ManualEntryActivity.this));

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
                        Toast.makeText(ManualEntryActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                                            Log.w(TAG, "Error writing document", e);
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
}