package com.example.svasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button regBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.reg_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        mAuth = FirebaseAuth.getInstance();


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                if( !name.isEmpty() && !password.isEmpty() && !email.isEmpty()){

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(Tag, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(RegisterActivity.this, "Account created successfully.",
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
//                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please fill empty field", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    public void signIn(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}