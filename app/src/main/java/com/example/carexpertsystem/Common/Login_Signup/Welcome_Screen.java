package com.example.carexpertsystem.Common.Login_Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.carexpertsystem.R;

public class Welcome_Screen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar gone using this line
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome__screen);


    }

    public void login(View view){
        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
    }

    public void signUp(View view){
        startActivity(new Intent(getApplicationContext(),SignUpScreen.class));
    }
}