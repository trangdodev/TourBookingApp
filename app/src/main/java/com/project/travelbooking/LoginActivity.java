package com.project.travelbooking;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.travelbooking.helper.CommonHelper;
import com.project.travelbooking.helper.SnackBarHelper;
import com.project.travelbooking.helper.TravelBookingDbHelper;

import java.io.Serializable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton, btnSmsLogin;

    TextView registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TravelBookingDbHelper.InitialFirestore();
        // Run Migration
        TravelBookingDbHelper.seedData();
        // End Migration
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.signupText);
        btnSmsLogin = findViewById(R.id.btnSmsLogin);
        btnSmsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, PhoneAuthActivity.class);

                startActivity(i);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);

                startActivity(i);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CommonHelper.HideKeyboard((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE));
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                if (usernameStr.isEmpty() || passwordStr.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Bạn phải điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    TravelBookingDbHelper.loginUser(new Callback<Map<String, Object>>() {
                        @Override
                        public void callback(Map<String, Object> user) {
                            if (user != null) {
                                showToastInCallback(Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT));
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);

                                i.putExtra("loggedUserData", (Serializable)user);
                                startActivity(i);
                                finish();
                            } else {
                                showToastInCallback(Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT));
                            }
                        }
                    }, usernameStr, passwordStr);
                }
            }
        });
    }

    private void showToastInCallback(Toast toast) {
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
}