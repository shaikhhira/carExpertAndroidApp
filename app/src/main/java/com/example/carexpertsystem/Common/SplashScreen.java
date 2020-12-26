package com.example.carexpertsystem.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carexpertsystem.Common.Login_Signup.Welcome_Screen;
import com.example.carexpertsystem.HelperClasses.SharedPrefManager;
import com.example.carexpertsystem.R;
import com.example.carexpertsystem.User.Dashboard;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN=5000;
    //variables
    Animation topAnim,bottomAnim;
    TextView slogan,logoname;
    ImageView logo_image;

    SharedPreferences onBoardingScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar gone using this line
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);


        //calling
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //hooks
        logo_image=findViewById(R.id.logo_image);
        logoname=findViewById(R.id.logo_name);
        slogan=findViewById(R.id.slogan);

        logo_image.setAnimation(topAnim);
        logoname.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen=getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime=onBoardingScreen.getBoolean("firstTime",true);

                if(isFirstTime){

                    SharedPreferences.Editor editor=onBoardingScreen.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();

                    Intent mainIntent=new Intent(SplashScreen.this,OnBoardingScreen.class);
                    startActivity(mainIntent);
                    finish();
                }
                else {
                   if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                       startActivity(new Intent(getApplicationContext(), Dashboard.class));
                       finish();

                   }else {
                       Intent mainIntent=new Intent(SplashScreen.this, Welcome_Screen.class);
                       startActivity(mainIntent);
                       finish();
                   }


                }


            }
        },SPLASH_SCREEN);
    }

}