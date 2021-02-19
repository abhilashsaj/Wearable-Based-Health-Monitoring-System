package com.example.svasthya;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;
import  android.util.Base64;


public class HomeActivity extends AppCompatActivity {

    private String url = "http://" + "192.168.1.36" + ":" + 5000 + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;
    private Button connect;

    Handler h;

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
    private String lf_hf_ratio;

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
    private TextView lf_hf_ratio_textview;
    private TextView status_textview;

    private FirebaseAuth mAuth;


    private TextView diabetes_textview;
    private TextView bronchi_textview;
    private TextView hypoxemia_textview;
    private TextView asthma_textview;
    private TextView chd_textview;
    private TextView stress_textview;

    private String diabetes = "";
    private String bronchi = "";
    private String hypoxemia = "";
    private String asthma = "";
    private String chd  = "";
    private String stress  = "";
    String status = "";
    FirebaseUser user;

    private static String factoryInstance = "PBKDF2WithHmacSHA256";
    private static String cipherInstance = "AES/CBC/PKCS5PADDING";
    private static String secretKeyType = "AES";
    private static String fSalt = "anySaltYouCanUseOfOn";
    private static byte[] ivCode = new byte[16];
    private static String secretKey = "yourSecretKey";
    public static String encrypt(String secretKey, String salt, String value) throws Exception {
        Cipher cipher = initCipher(secretKey, salt, Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        byte[] cipherWithIv = addIVToCipher(encrypted);
        return Base64.encodeToString(cipherWithIv,Base64.DEFAULT);
    }
    public static String decrypt(String secretKey, String salt, String encrypted) throws Exception {
        Cipher cipher = initCipher(secretKey, salt, Cipher.DECRYPT_MODE);
        byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
        // unpad
        byte[] originalWithoutIv = Arrays.copyOfRange(original, 16, original.length);
        return new String(originalWithoutIv);
    }
    private static Cipher initCipher(String secretKey, String salt, int mode) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(factoryInstance);
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec skeySpec = new SecretKeySpec(tmp.getEncoded(), secretKeyType);
        Cipher cipher = Cipher.getInstance(cipherInstance);
        // Generating random IV
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivCode);

        cipher.init(mode, skeySpec, new IvParameterSpec(ivCode));
        return cipher;
    }

    private static byte[] addIVToCipher(byte[] encrypted) {
        byte[] cipherWithIv = new byte[ivCode.length + encrypted.length];
        System.arraycopy(ivCode, 0, cipherWithIv, 0, ivCode.length);
        System.arraycopy(encrypted, 0, cipherWithIv, encrypted.length, encrypted.length);
        return cipherWithIv;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//
//        FitnessOptions fitnessOptions = FitnessOptions.builder()
//                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                .build();


//        String fSalt = "anySaltYouCanUseOfOn";
//        String plainText = "M0993000353";
//        String cipherText = null;
//        try {
//            cipherText = encrypt(secretKey, fSalt, plainText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        System.out.println("Cipher: " + cipherText);
//        try {
//            String dcrCipherText = decrypt(secretKey, fSalt, cipherText);
////            JSONObject obj = new JSONObject("{\\\"post_meal\\\": true, \\\"blood_sugar_level\\\": 148, \\\"breaths_per_minute\\\": 19, \\\"is_running\\\": false, \\\"breath_shortness_severity\\\": 0, \\\"cough_frequency\\\": 3, \\\"cough_severity\\\": 8, \\\"blood_pressure_sys\\\": 187, \\\"blood_pressure_dia\\\": 161, \\\"heart_rate\\\": 140, \\\"cholestorol\\\": 159, \\\"oxygen_saturation\\\": 95, \\\"lf/hf ratio\\\": 1.5194699163878969}");
//
////            String x = "";
////            Toast.makeText(HomeActivity.this,"Sample: "+obj.toString()+ "\nPlain Text: " + plainText + "\nCipher Text: "+ cipherText +"\nDecrypted: " + dcrCipherText, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println("Decrypted: " + dcrCipherText);





        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        status_textview = (TextView)findViewById(R.id.status);
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
        lf_hf_ratio_textview = (TextView)findViewById(R.id.lf_hf_ratio);

        diabetes_textview = (TextView)findViewById(R.id.diabetes);
        bronchi_textview = (TextView)findViewById(R.id.bronchi);
        chd_textview = (TextView)findViewById(R.id.chd);
        hypoxemia_textview = (TextView)findViewById(R.id.hypoxemia);
        asthma_textview = (TextView)findViewById(R.id.asthma);
        stress_textview = (TextView)findViewById(R.id.stress);


        final DrawerLayout drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
//        final NavController navController=Navigation.findNavController(this, R.id.nav_host_fragment);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id==R.id.manual){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, ManualEntryActivity.class);
                    startActivity(intent);
                }
                if (id==R.id.relax){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, StressHomeActivity.class);
                    startActivity(intent);
                }
                if (id==R.id.relax){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, StressHomeActivity.class);
                    startActivity(intent);
                }
                if (id==R.id.meditation){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, FiveMinMediation.class);
                    startActivity(intent);
                }
                if (id==R.id.breathing){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, BreathingActivity.class);
                    startActivity(intent);
                }
                if (id==R.id.dino){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, DinoGame.class);
                    startActivity(intent);
                }

                if (id==R.id.walk){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, Walk.class);
                    startActivity(intent);
                }
                if (id==R.id.fitbit){
//                    Toast.makeText(getApplicationContext(), "Manual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, FitbitApi.class);
                    startActivity(intent);
                }
                //This is for maintaining the behavior of the Navigation view
//                NavigationUI.onNavDestinationSelected(menuItem,navController);

                //This is for closing the drawer after acting on it


                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.getMenu().getItem(0).setChecked(true);

        postRequest("your message here", url);

        h = new Handler();
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


                            String cipherText =  obj.getString("Cipher");
                            String dcrCipherText = decrypt(secretKey, fSalt, cipherText);
//                            String json_string = obj.getString("Decrypted");

                            obj = new JSONObject(dcrCipherText);
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
                            lf_hf_ratio =  obj.getString("lf/hf ratio");


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
                            lf_hf_ratio_textview.setText(lf_hf_ratio);
//                            Toast.makeText(HomeActivity.this, "Calling Server...", Toast.LENGTH_SHORT).show();

//                            RequestBody requ  estBody = buildRequestBody("your message here");

                            String email_id = user.getEmail();
//                            Toast.makeText(HomeActivity.this, email + "hi", Toast.LENGTH_SHORT).show();

                            final String fSalt = "anySaltYouCanUseOfOn";
                            String plainText = user.getDisplayName();

                            Log.e("Home",plainText);
                            String cipherText2 = encrypt(secretKey, fSalt, plainText);
//                            System.out.println("Cipher: " + cipherText);

                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("uid", cipherText2)
                                    .addFormDataPart("email_id", user.getEmail().toString())
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
                                    .addFormDataPart("lf_hf_ratio", lf_hf_ratio)
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
//                                                JSONObject obj = new JSONObject(response.body().string());
                                                JSONObject obj = new JSONObject(response.body().string());


                                                String cipherText =  obj.getString("Cipher");
                                                String dcrCipherText = decrypt(secretKey, fSalt, cipherText);
//                            String json_string = obj.getString("Decrypted");

                                                obj = new JSONObject(dcrCipherText);
                                                diabetes = obj.getString("diabetes");
                                                bronchi = obj.getString("bronchi");
                                                hypoxemia =  obj.getString("hypoxemia");
                                                asthma =  obj.getString("asthma");
                                                chd =  obj.getString("chd");
                                                stress =  obj.getString("stress");
                                                status = obj.getString("status");

                                                diabetes_textview.setText(diabetes);
                                                bronchi_textview.setText(bronchi);
                                                hypoxemia_textview.setText(hypoxemia);
                                                asthma_textview.setText(asthma);
                                                chd_textview.setText(chd);
                                                stress_textview.setText(stress);

                                                if(diabetes.equals("Yes") && diabetes!=null){
                                                    diabetes_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    blood_sugar_level_textview.setTextColor(Color.parseColor("#DC143C"));
                                                }
                                                else{
                                                    diabetes_textview.setTextColor(Color.BLACK);
                                                    blood_sugar_level_textview.setTextColor(Color.BLACK);
                                                }
                                                if(chd.equals("Yes" ) &&  chd!=null){
                                                    chd_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    blood_pressure_dia_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    blood_pressure_sys_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    heart_rate_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    cholestorol_textview.setTextColor(Color.parseColor("#DC143C"));
                                                }
                                                else{
                                                    chd_textview.setTextColor(Color.BLACK);
                                                    blood_pressure_dia_textview.setTextColor(Color.BLACK);
                                                    blood_pressure_sys_textview.setTextColor(Color.BLACK);
                                                    heart_rate_textview.setTextColor(Color.BLACK);
                                                    cholestorol_textview.setTextColor(Color.BLACK);
                                                }
                                                if(asthma.equals("Yes") &&  asthma!=null){
                                                    asthma_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    oxygen_saturation_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    heart_rate_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    cholestorol_textview.setTextColor(Color.parseColor("#DC143C"));
                                                }
                                                else{
                                                    asthma_textview.setTextColor(Color.BLACK);
                                                    oxygen_saturation_textview.setTextColor(Color.BLACK);
                                                    heart_rate_textview.setTextColor(Color.BLACK);
                                                    cholestorol_textview.setTextColor(Color.BLACK);
                                                }
                                                if(bronchi.equals("Yes")&&  bronchi!=null){
                                                    bronchi_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    breaths_per_minute_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    breath_shortness_severity_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    cough_frequency_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    cough_severity_textview.setTextColor(Color.parseColor("#DC143C"));
                                                }
                                                else{
                                                    bronchi_textview.setTextColor(Color.BLACK);
                                                    breaths_per_minute_textview.setTextColor(Color.BLACK);
                                                    breath_shortness_severity_textview.setTextColor(Color.BLACK);
                                                    cough_frequency_textview.setTextColor(Color.BLACK);
                                                    cough_severity_textview.setTextColor(Color.BLACK);
                                                }
                                                if(hypoxemia.equals("Yes")&&  hypoxemia!=null){
                                                    hypoxemia_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    oxygen_saturation_textview.setTextColor(Color.parseColor("#DC143C"));
                                                }
                                                else{
                                                    hypoxemia_textview.setTextColor(Color.BLACK);
                                                    oxygen_saturation_textview.setTextColor(Color.BLACK);
                                                }
                                                if(stress.equals("Yes") &&  stress!=null){
                                                    stress_textview.setTextColor(Color.parseColor("#DC143C"));
                                                    lf_hf_ratio_textview.setTextColor(Color.parseColor("#DC143C"));
                                                }
                                                else{
                                                    stress_textview.setTextColor(Color.BLACK);
                                                    lf_hf_ratio_textview.setTextColor(Color.BLACK);
                                                }


//                                                Toast.makeText(HomeActivity.this, status, Toast.LENGTH_LONG).show();
                                                status_textview.setText(status.trim());

//                                                Toast.makeText(HomeActivity.this, obj.toString(), Toast.LENGTH_LONG).show();
                                            } catch (IOException | JSONException e) {
                                                e.printStackTrace();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    Map<String, Object> healthParam = new HashMap<>();

                                    healthParam.put( "post_meal",post_meal);
                                    healthParam.put( "status",status);
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
                                    healthParam.put(  "lf_hf_ratio",lf_hf_ratio);
                                    healthParam.put(  "entry_type","iot device");
                                    healthParam.put("DEVICE_ID", getDeviceId(HomeActivity.this));

                                    healthParam.put(  "diabetes",diabetes);
                                    healthParam.put(  "chd",chd);
                                    healthParam.put(  "asthma",asthma);
                                    healthParam.put(  "hypoxemia",hypoxemia);
                                    healthParam.put(  "bronchi",bronchi);
                                    healthParam.put(  "stress",stress);

//                                    if(diabetes.equals("Yes") && diabetes!=null){
//                                        diabetes_textview.setBackgroundColor(Color.parseColor("#ffcccb"));
//                                    }
//                                    if(chd.equals("Yes" ) &&  chd!=null){
//                                        chd_textview.setBackgroundColor(Color.parseColor("#ffcccb"));
//                                    }
//                                    if(asthma.equals("Yes") &&  asthma!=null){
//                                        asthma_textview.setBackgroundColor(Color.parseColor("#ffcccb"));
//                                    }
//                                    if(bronchi.equals("Yes")&&  bronchi!=null){
//                                        bronchi_textview.setBackgroundColor(Color.parseColor("#ffcccb"));
//                                    }
//                                    if(hypoxemia.equals("Yes")&&  hypoxemia!=null){
//                                        hypoxemia_textview.setBackgroundColor(Color.parseColor("#ffcccb"));
//                                    }
//                                    if(stress.equals("Yes") &&  stress!=null){
//                                        stress_textview.setBackgroundColor(Color.parseColor("#ffcccb"));
//                                    }

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                                    String format = simpleDateFormat.format(new Date());
                                    String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                    String currentTime = new SimpleDateFormat("hh-mm-ss").format(new Date());
                                    healthParam.put(  "currentDate",currentDate);
                                    healthParam.put(  "currentTime",currentTime);
                                    Log.d("HomeActivity", "Current Timestamp: " + format);
                                    healthParam.put("TIMESTAMP", format);

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    db.collection("users").document(user.getUid())
                                            .collection("health_data").document(currentDate)
                                            .collection("time_slot").document(currentTime)
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
                                    healthParam.put("username", user.getEmail());
                                    healthParam.put("UID", user.getUid());
                                    db.collection("user_health").document(user.getUid()).set(healthParam)
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


                                }
                            });

//                            Toast.makeText(HomeActivity.this, obj.toString() + " "+ blood_sugar_level, Toast.LENGTH_LONG).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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

    public void goToStressActivity(View view) {
        Intent intent = new Intent(HomeActivity.this, StressHomeActivity.class);
        startActivity(intent);
    }

    public void goToChatBot(View view) {

        Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
        startActivity(intent);
    }
}