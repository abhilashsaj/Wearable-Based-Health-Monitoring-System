package com.example.svasthya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class FitbitApi extends AppCompatActivity {


    private URL url;
    String authUrl;
    TextView textView;
//    OkHttpClient client = new OkHttpClient();

    private static String CLIENT_ID = "22C8C7";
    //Use your own client id
    private static String CLIENT_SECRET ="2120c085ca17774b10d35c31db2e5337";
    //Use your own client secret
    private static String REDIRECT_URI="https://finished";
    private static String GRANT_TYPE="authorization_code";
    private static String TOKEN_URL ="https://api.fitbit.com/oauth2/token";
    private static String OAUTH_URL ="https://www.fitbit.com/oauth2/authorize";

    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit_api);

        authUrl = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id="+CLIENT_ID+"&redirect_uri=https%3A%2F%2Ffinished&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800";

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access =(TextView)findViewById(R.id.Access);
        auth = (Button)findViewById(R.id.auth);

        auth.setOnClickListener(new View.OnClickListener() {
            Dialog auth_dialog;
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                auth_dialog = new Dialog(FitbitApi.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(authUrl);
                web.setWebViewClient(new WebViewClient() {

                    boolean authComplete = false;
                    Intent resultIntent = new Intent();

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);

                    }
                    String authCode;
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

//                        Toast.makeText(getApplicationContext(),"Authorization Code is: " +url, Toast.LENGTH_SHORT).show();
                        if (url.contains("#access_token=") && authComplete != true) {
                            Uri uri = Uri.parse(url);
//                            authCode = uri.getQueryParameter("access_token");
                            int first = url.indexOf("=")+1;
                            int last = url.indexOf("&");
                            authCode=url.substring(first, last);
                            authComplete = true;
                            resultIntent.putExtra("code", authCode);
                            FitbitApi.this.setResult(Activity.RESULT_OK, resultIntent);
                            setResult(Activity.RESULT_CANCELED, resultIntent);


                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();
                            auth_dialog.dismiss();

                            new TokenGet().execute();
                            Toast.makeText(getApplicationContext(),"Authorization Code is: " +authCode, Toast.LENGTH_SHORT).show();
                        }else if(url.contains("error=access_denied")){
                            Log.i("", "ACCESS_DENIED_HERE");
                            resultIntent.putExtra("code", authCode);
                            authComplete = true;
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();


                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setTitle("Authorize Learn2Crack");
                auth_dialog.setCancelable(true);
            }

        });

//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
//
//        try {
//            url = new URL("https://api.fitbit.com/1/user/-/profile.json");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        textView = (TextView) findViewById(R.id.textView8);
//
//        try {
//            String message = getResponseFromHttpUrl(url);
//            if(message != null){
////                textView.setText(message);
//                JSONObject jsonObject = new JSONObject(message).getJSONObject("user");
////                ObjectMapper mapper = new ObjectMapper();
////                mapper.enable(SerializationFeature.INDENT_OUTPUT);
////                mapper.writeValueAsString(obj)
//                String name = "Name: "+jsonObject.getString("fullName");
//                String age = "\nAge: "+jsonObject.getString("age");
//                String height = "\nHeight: "+jsonObject.getString("height");
//                textView.setText(name+age+height);
//            }
//
//            Log.d("URL", getResponseFromHttpUrl(url));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

    }
    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Code;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FitbitApi.this);
            pDialog.setMessage("Contacting Fitbit ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Code = pref.getString("Code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
//            GetAccessToken jParser = new GetAccessToken();
//            JSONObject json = jParser.gettoken(TOKEN_URL,Code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI,GRANT_TYPE);
            String message ="";
            try {
                message = getResponseFromHttpUrl(new URL("https://api.fitbit.com/1/user/-/profile.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(message).getJSONObject("user");
                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (json != null){

                try {

                    String name = "Name: "+json.getString("fullName");
                    String age = "\nAge: "+json.getString("age");
                    String height = "\nHeight: "+json.getString("height");
                    String strideLengthRunning = "\nStride Length Running: "+ json.getString("strideLengthRunning");
                    String strideLengthWalking = "\nStride Length Walking: "+ json.getString("strideLengthWalking");
                    String averageDailySteps = "\nAverage Daily Steps: "+ json.getString("averageDailySteps");
                    String dateOfBirth = "\nDate Of Birth: "+ json.getString("dateOfBirth");


                    auth.setText("Authenticated");
                    Access.setText(name +age +dateOfBirth+height+averageDailySteps+strideLengthWalking+strideLengthRunning);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
    }


    public static String getResponseFromHttpUrl(java.net.URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Accept","application/json");
        connection.addRequestProperty("Content-Type","application/json");
        connection.addRequestProperty("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMkM4QzciLCJzdWIiOiI5NFJTQ0QiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJwcm8gcm51dCByc2xlIiwiZXhwIjoxNjE0MTczNDk3LCJpYXQiOjE2MTM1Njg2OTd9.4UtCquJjkCh6tdxAjdC9Y-d--DrNSo5JtWIy6Cbnkq4");

        try {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return "";
            }
        } finally {
            connection.disconnect();
        }
    }

}


//        final Request request = new Request.Builder()
//                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMkM4QzciLCJzdWIiOiI5NFJTQ0QiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJwcm8gcm51dCByc2xlIiwiZXhwIjoxNjE0MTczNDk3LCJpYXQiOjE2MTM1Njg2OTd9.4UtCquJjkCh6tdxAjdC9Y-d--DrNSo5JtWIy6Cbnkq4")
//                .url("https://api.fitbit.com/1/user/-/profile.json")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                } else {
//                    // do something wih the result
//                    response = client.newCall(request).execute();
//                    if (response!=null)
//                    textView.setText(response.toString() + "hi");
//
//                }
//            }
//
//        });
