package com.project.travelbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.travelbooking.helper.TravelBookingDbHelper;

import java.io.Serializable;
import java.util.Map;

public class PhoneVerificationActivity extends AppCompatActivity {
    TextView tvPhoneNumber;
    String verificationId;
    EditText edOtpCode;
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        verificationId = getIntent().getStringExtra("verificationId");
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.setText(String.format("+84 %s", getIntent().getStringExtra("phoneNumber")));
        edOtpCode = findViewById(R.id.edOtpCode);
        btnVerify = findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edOtpCode.getText().toString();
                if (otp.trim().length() < 6) {
                    Toast.makeText(PhoneVerificationActivity.this, "OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = authResult.getUser();
                            String phone = String.format("0%s", getIntent().getStringExtra("phoneNumber"));
                            TravelBookingDbHelper.signInByPhoneNumber(new Callback<Map<String, Object>>() {
                                @Override
                                public void callback(Map<String, Object> data) {
                                    showToastInCallback(Toast.makeText(PhoneVerificationActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT));
                                    Intent i = new Intent(PhoneVerificationActivity.this, MainActivity.class);

                                    i.putExtra("loggedUserData", (Serializable) data);
                                    startActivity(i);
                                    finish();
                                }
                            }, phone);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PhoneVerificationActivity.this, "OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    private void showToastInCallback(Toast toast) {
        PhoneVerificationActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
}