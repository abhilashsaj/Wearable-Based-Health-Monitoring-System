package com.example.svasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.gauriinfotech.commons.Commons;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManualEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String url = "http://" + "192.168.1.36" + ":" + 5000 + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    //this is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    TextView textViewStatus;
    EditText editTextFilename;
    ProgressBar progressBar;

    //the firebase objects for storage and database
    StorageReference mStorageReference;
//    DatabaseReference mDatabaseReference;



    private static final int SELECT_PDF = 0;
    private Spinner post_meal_textview;
    private EditText blood_sugar_level_textview;
    private EditText breaths_per_minute_textview;
    private Spinner is_running_textview;
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
    private String SelectedPDF;


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
        setContentView(R.layout.activity_manual_entry);

        mStorageReference = FirebaseStorage.getInstance().getReference();
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        //getting the views
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextFilename = (EditText) findViewById(R.id.editTextFileName);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //attaching listeners to views
//        findViewById(R.id.buttonUploadFile).setOnClickListener((View.OnClickListener) this);
//        findViewById(R.id.textViewUploads).setOnClickListener((View.OnClickListener) this);



        post_meal_textview = (Spinner) findViewById(R.id.post_meal);
        String[] items = new String[]{"true", "false"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        post_meal_textview.setAdapter(adapter);
        post_meal_textview.setOnItemSelectedListener(this);

        blood_sugar_level_textview = (EditText) findViewById(R.id.blood_sugar_level);
        breaths_per_minute_textview = (EditText) findViewById(R.id.breaths_per_minute);
        is_running_textview = (Spinner) findViewById(R.id.is_running);
        is_running_textview.setAdapter(adapter);
        is_running_textview.setOnItemSelectedListener(this);

        breath_shortness_severity_textview = (EditText) findViewById(R.id.breath_shortness_severity);
        cough_frequency_textview = (EditText) findViewById(R.id.cough_frequency);
        cough_severity_textview = (EditText) findViewById(R.id.cough_severity);
        blood_pressure_sys_textview = (EditText) findViewById(R.id.blood_pressure_sys);
        blood_pressure_dia_textview = (EditText) findViewById(R.id.blood_pressure_dia);
        heart_rate_textview = (EditText) findViewById(R.id.heart_rate);
        cholestorol_textview = (EditText) findViewById(R.id.cholestorol);
        oxygen_saturation_textview = (EditText) findViewById(R.id.oxygen_saturation);

        diabetes_textview = (TextView)findViewById(R.id.diabetes_m);
        bronchi_textview = (TextView)findViewById(R.id.bronchi_m);
        chd_textview = (TextView)findViewById(R.id.chd_m);
        hypoxemia_textview = (TextView)findViewById(R.id.hypoxemia_m);
        asthma_textview = (TextView)findViewById(R.id.asthma_m);
        stress_textview = (TextView)findViewById(R.id.stress_m);

    }
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //this method is uploading the file
    private void uploadFile(Uri data) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        final String format = simpleDateFormat.format(new Date());

        progressBar.setVisibility(View.VISIBLE);
        final StorageReference sRef = mStorageReference.child("med_records/" + user.getUid()+"/"+ System.currentTimeMillis() + ".pdf");
        sRef.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        //Do what you want with the url
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File Uploaded Successfully");
                        Toast.makeText(getApplicationContext(), "Successfully uploaded file to storage", Toast.LENGTH_LONG).show();

                        Upload upload = new Upload(editTextFilename.getText().toString(), downloadUrl.toString());


                        Map<String, Object> file = new HashMap<>();
                        file.put("name", upload.getName());
                        file.put("url", upload.getUrl());

                        db.collection("users").document(user.getUid()).collection("health_records").document(format)
                                .set(file)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Success", "DocumentSnapshot successfully written!");
//                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Fail", "Error writing document", e);
                                    }
                                });
//                    Toast.makeText(ManualEntryActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                    }

                });
//
//        sRef.putFile(data)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @SuppressWarnings("VisibleForTests")
//                    @Override
//
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        progressBar.setVisibility(View.GONE);
//                        textViewStatus.setText("File Uploaded Successfully");
//                        Toast.makeText(getApplicationContext(), "Successfully uploaded file to storage", Toast.LENGTH_LONG).show();
//
//                        Upload upload = new Upload(editTextFilename.getText().toString(), taskSnapshot.getStorage().getDownloadUrl().toString());
//
//
//                        Map<String, Object> file = new HashMap<>();
//                        file.put("name", upload.getName());
//                        file.put("url", taskSnapshot.);
//
//                        db.collection("users").document(user.getUid()).collection("health_records").document(format)
//                                .set(file)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                            Log.d("Success", "DocumentSnapshot successfully written!");
//                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                            Log.w("Fail", "Error writing document", e);
//                                    }
//                                });
////                        mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @SuppressWarnings("VisibleForTests")
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        textViewStatus.setText((int) progress + "% Uploading...");
//                    }
//                });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                textViewStatus.setText((int) progress + "% Uploading...");
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonUploadFile:
                getPDF();
                break;
            case R.id.textViewUploads:
                startActivity(new Intent(this, ViewUploadsActivity.class));
                break;
        }
    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ManualEntryActivity.this, MainActivity.class));
        finish();
    }

    public void submitToServer(View view) {


//        post_meal = post_meal_textview.getText().toString();
        blood_sugar_level = blood_sugar_level_textview.getText().toString();
        breaths_per_minute = breaths_per_minute_textview.getText().toString();
//        is_running = is_running_textview.getText().toString();
        breath_shortness_severity = breath_shortness_severity_textview.getText().toString();
        cough_frequency = cough_frequency_textview.getText().toString();
        cough_severity = cough_severity_textview.getText().toString();
        blood_pressure_sys= blood_pressure_sys_textview.getText().toString();
        blood_pressure_dia = blood_pressure_dia_textview.getText().toString();
        heart_rate = heart_rate_textview.getText().toString();
        cholestorol = cholestorol_textview.getText().toString();
        oxygen_saturation = oxygen_saturation_textview.getText().toString();



        if(!blood_sugar_level.trim().isEmpty() &&
        !breaths_per_minute.trim().isEmpty() &&
        !breath_shortness_severity.trim().isEmpty() &&
                !cough_frequency.trim().isEmpty() &&
        !cough_severity.trim().isEmpty() &&
                !blood_pressure_sys.trim().isEmpty() &&
        !blood_pressure_dia.trim().isEmpty() &&
                !heart_rate.trim().isEmpty() &&
        !cholestorol.trim().isEmpty() &&
                !oxygen_saturation.trim().isEmpty())
        {


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

            db.collection("users").document(user.getUid()).collection("manual").document(format)
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

            Toast.makeText(ManualEntryActivity.this, "Sumit function invoked Successfully", Toast.LENGTH_LONG).show();
            Log.d("function", "submit invoke");

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
                            Toast.makeText(ManualEntryActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(ManualEntryActivity.this, obj.toString(), Toast.LENGTH_LONG).show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            });
        }
        else {
            Toast.makeText(ManualEntryActivity.this, "Please Enter all values", Toast.LENGTH_LONG).show();
        }





    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
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


    public void uploadFile(View view) {
        getPDF();
    }

    public void viewUploads(View view) {
        startActivity(new Intent(this, ViewUploadsActivity.class));
    }

    public void viewEntries(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.post_meal)
        {
            switch (position) {
                case 0:
                    // Whatever you want to happen when the first item gets selected
                    post_meal = "true";
                    break;
                case 1:
                    // Whatever you want to happen when the second item gets selected
                    post_meal = "false";
                    break;

            }
        }
        else if(parent.getId() == R.id.is_running)
        {
            switch (position) {
                case 0:
                    // Whatever you want to happen when the first item gets selected
                    is_running = "true";
                    break;
                case 1:
                    // Whatever you want to happen when the second item gets selected
                    is_running = "false";
                    break;

            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}