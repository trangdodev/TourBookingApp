package com.project.travelbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.travelbooking.helper.CommonHelper;
import com.project.travelbooking.helper.TravelBookingDbHelper;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, name, phone;
    TextView loginButton;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginText);
        registerBtn = findViewById(R.id.registerBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CommonHelper.HideKeyboard((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE));
                String nameStr = name.getText().toString(),
                        phoneStr = phone.getText().toString(),
                        usernameStr = username.getText().toString(),
                        passwordStr = password.getText().toString();
                if (nameStr.isEmpty() || phoneStr.isEmpty() || usernameStr.isEmpty() || passwordStr.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT);
                } else {
                    TravelBookingDbHelper.checkExistUser(new Callback<Boolean>() {
                        @Override
                        public void callback(Boolean isExistUser) {
                            if (isExistUser) {
                                showToastInCallback(Toast.makeText(RegisterActivity.this, "Username đã được đăng ký!", Toast.LENGTH_SHORT));
                            } else {
                                TravelBookingDbHelper.createUser(new Callback<Map<String, Object>>() {
                                    @Override
                                    public void callback(Map<String, Object> createdUser) {
                                        showToastInCallback(Toast.makeText(RegisterActivity.this, "Đăng ký thành công, Đăng nhập để tiếp tục!", Toast.LENGTH_SHORT));
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }
                                }, nameStr, phoneStr, usernameStr, passwordStr);

                            }
                        }
                    }, usernameStr);
                }
            }
        });
    }

    private void showToastInCallback(Toast toast) {
        RegisterActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
}