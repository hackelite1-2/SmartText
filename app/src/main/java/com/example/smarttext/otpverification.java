package com.example.smarttext;


import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpverification<mCallBacks> extends AppCompatActivity {

    TextView mOtpTimer;
    TextView mResendB;
    String phoneVerify;
    private String mVerificationId;
    int otpFlag =0;
    private EditText phoneText,phoneText1,codeText;
    private LinearLayout phoneLayout,codeLayout;
    private ProgressBar phoneBar,codeBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressBar otpLoading;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        init();
        allListener();
        timer();
        Bundle b1=getIntent().getExtras();
        phoneVerify =b1.getString("phone_number");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneVerify,        // Phone number to verify
                10,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                otpverification.this,
                mCallBacks);


        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
                Intent I1=new Intent(otpverification.this,MainActivity.class);
                startActivity(I1);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(otpverification.this,"error in verification",Toast.LENGTH_LONG).show();

            }


            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                phoneLayout.setVisibility(View.GONE);
                codeLayout.setVisibility(View.VISIBLE);
            }

        };

    }
    void timer()
    {
        if(otpFlag ==0)
        {
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(otpFlag ==0)
                        mOtpTimer.setText( ""+millisUntilFinished / 1000);
                }
                public void onFinish() {
                    mOtpTimer.setText("0");
                    otpFlag =1;
                    mResendB.setTextColor(getResources().getColor(R.color.mediumBlue));
                }

            }.start();
        }
        else
        {
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    mOtpTimer.setText( ""+millisUntilFinished / 1000);
                }
                public void onFinish() {
                    mResendB.setTextColor(getResources().getColor(R.color.mediumBlue));
                }

            }.start();
        }
    }
    void allListener()
    {
        mResendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOtpTimer.getText()=="0")
                {
                    mResendB.setTextColor(getResources().getColor(R.color.GREY));
                }
                timer();
                //TODO: Resand Otp;
            }
        });
    }
    void init()
    {
        otpLoading=(ProgressBar)findViewById(R.id.otpProgressBar);
        mOtpTimer=findViewById(R.id.otpTimer);
        mResendB=findViewById(R.id.otpResend);
    }

    public void OtptoLogin(View view) {
        Intent I1=new Intent(this,MainActivity.class);
        startActivity(I1);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();

                            Intent LoggedIn = new Intent(otpverification.this,MainActivity.class);
                            startActivity(LoggedIn);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(otpverification.this,"error",Toast.LENGTH_LONG).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
