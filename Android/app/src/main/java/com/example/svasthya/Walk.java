package com.example.svasthya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svasthya.pedometer.StepDetector;
import com.example.svasthya.pedometer.StepListener;
import com.example.svasthya.pedometer.SensorFilter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Walk extends AppCompatActivity implements SensorEventListener, StepListener {

    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "";
    private int numSteps;
    TextView TvSteps ;
    Button BtnStart;
    Button BtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(Walk.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(Walk.this);

            }
        });


//        Toast.makeText(Walk.this, "hi", Toast.LENGTH_SHORT).show();

//        String[] PERMISSIONS = {
//                Manifest.permission.ACTIVITY_RECOGNITION
//        };
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this,
//                        PERMISSIONS,
//                        PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
//            }
//        } else {
//            Toast.makeText(Walk.this, "google sign in called", Toast.LENGTH_LONG).show();
//            googleSignin();
//
//        }
//
//        googleSignin();






//        new Thread(new Runnable() {
//            public void run() {
//                // a potentially time consuming task
//                GoogleSignInOptionsExtension fitnessOptions =
//                        FitnessOptions.builder()
//                                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                                .build();
//
//                GoogleSignInAccount googleSignInAccount =
//                        GoogleSignIn.getAccountForExtension(getApplicationContext(), fitnessOptions);
//
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(new Date());
//                long endTime = cal.getTimeInMillis();
//                cal.add(Calendar.YEAR, -1);
//                long startTime = cal.getTimeInMillis();
//
//                Task<DataReadResponse> response = Fitness.getHistoryClient(getApplicationContext(), googleSignInAccount)
//                        .readData(new DataReadRequest.Builder()
//                                .read(DataType.TYPE_STEP_COUNT_DELTA)
//                                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                                .build());
//
//                DataReadResponse readDataResult = null;
//                try {
//                    readDataResult = Tasks.await(response);
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                DataSet dataSet = readDataResult.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
//                Toast.makeText(Walk.this, dataSet.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        }).start();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }
//    public void googleSignin() {
//        FitnessOptions fitnessOptions = FitnessOptions.builder()
//                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                .build();
//        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
//            GoogleSignIn.requestPermissions(
//                    this, // your activity
//                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
//                    GoogleSignIn.getLastSignedInAccount(this),
//                    fitnessOptions);
//            Toast.makeText(Walk.this, "last sign in", Toast.LENGTH_SHORT).show();
//        } else {
//            accessGoogleFit();
//            Toast.makeText(Walk.this, "sign in success", Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (GOOGLE_FIT_PERMISSIONS_REQUEST_CODE == requestCode) {
//
//                accessGoogleFit();
//                Toast.makeText(Walk.this, "access google fit", Toast.LENGTH_SHORT).show();
//            }
//            if (PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION == requestCode) {
//
//                accessGoogleFit();
//                Toast.makeText(Walk.this, "access google fit", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//        }
//
//    }
//
//    private void accessGoogleFit() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.YEAR, -1);
//        long startTime = cal.getTimeInMillis();
//
//
//        DataReadRequest readRequest = new DataReadRequest.Builder()
//                .read(DataType.TYPE_HEART_RATE_BPM)
//                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                .bucketByTime(365, TimeUnit.DAYS)
//                .build();
//
//
//        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
//                .readData(readRequest)
//                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
//                    @Override
//                    public void onSuccess(DataReadResponse dataReadResponse) {
//                        Log.d("Activity", "onSuccess()");
//
//                        for (Bucket bucket : dataReadResponse.getBuckets()) {
//                            Log.e("History", "Data returned for Data type: " + bucket.getDataSets());
//
//                            List<DataSet> dataSets = bucket.getDataSets();
//                            for (DataSet dataSet : dataSets) {
//                                showDataSet(dataSet);
//                            }
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure()", e);
//                    }
//                })
//                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataReadResponse> task) {
//                        Log.d(TAG, "onComplete()");
//                    }
//                });
//    }
//
//    private void showDataSet(DataSet dataSet) {
//        DateFormat dateFormat = DateFormat.getDateInstance();
//        DateFormat timeFormat = DateFormat.getTimeInstance();
//
//        for (DataPoint dp : dataSet.getDataPoints()) {
//            Log.e("History", "Data point:");
//            Log.e("History", "\tType: " + dp.getDataType().getName());
//            Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//            Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//            Calendar cal = Calendar.getInstance();
//            Toast.makeText(Walk.this, dp.toString(), Toast.LENGTH_SHORT).show();
//            cal.setTime(new Date());
//
//
//            for (Field field : dp.getDataType().getFields()) {
//
//                Log.e("History", "\tField: " + field.getName() +
//                        " Value: " + dp.getValue(field));
//
//            }
//
//
//
//
//        }
//    }

}