package com.project.travelbooking;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {
    Button btnSendOtp;
    EditText edPhoneNumber;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("vi");

        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+84" + edPhoneNumber.getText().toString().trim();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber(phoneNumber)       // Phone number to verify
                                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(PhoneAuthActivity.this)                 // (optional) Activity for callback binding
                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                        // This callback will be invoked in two situations:
                                        // 1 - Instant verification. In some cases the phone number can be instantly
                                        //     verified without needing to send or enter a verification code.
                                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                        //     detect the incoming verification SMS and perform verification without
                                        //     user action.
                                        Log.d(TAG, "onVerificationCompleted:" + credential);

                                        //signInWithPhoneAuthCredential(credential);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        // This callback is invoked in an invalid request for verification is made,
                                        // for instance if the the phone number format is not valid.
                                        Log.w(TAG, "onVerificationFailed", e);

                                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                            showToastInCallback(Toast.makeText(PhoneAuthActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT));
                                        } else if (e instanceof FirebaseTooManyRequestsException) {
                                            showToastInCallback(Toast.makeText(PhoneAuthActivity.this, "Hết SMS quota hôm nay", Toast.LENGTH_SHORT));
                                        }
//                                        else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
//                                            // reCAPTCHA verification attempted with null Activity
//                                        }
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        // The SMS verification code has been sent to the provided phone number, we
                                        // now need to ask the user to enter the code and then construct a credential
                                        // by combining the code with a verification ID.
                                        Log.d(TAG, "onCodeSent:" + verificationId);
                                        showToastInCallback(Toast.makeText(PhoneAuthActivity.this, "Mã OTP đã được gửi", Toast.LENGTH_SHORT));

                                        // Save verification ID and resending token so we can use them later
                                        mVerificationId = verificationId;
                                        mResendToken = token;

                                        Intent i = new Intent(PhoneAuthActivity.this, PhoneVerificationActivity.class);
                                        i.putExtra("phoneNumber", edPhoneNumber.getText().toString().trim());
                                        i.putExtra("verificationId", verificationId);
                                        startActivity(i);
                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });


    }

    private void showToastInCallback(Toast toast) {
        PhoneAuthActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
}