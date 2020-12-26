package com.example.carexpertsystem.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.carexpertsystem.Common.Login_Signup.Welcome_Screen;
import com.example.carexpertsystem.HelperClasses.SliderAdapter;
import com.example.carexpertsystem.R;

public class OnBoardingScreen extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStartedBtn;
    Animation animation;
    int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar gone using this line
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding_screen);

        viewPager=findViewById(R.id.slider);
        dotsLayout=findViewById(R.id.dots);
        letsGetStartedBtn=findViewById(R.id.get_started_btn);

        //call adapters
        sliderAdapter=new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);



    }


    public void skip(View view){
        startActivity(new Intent(getApplicationContext(), Welcome_Screen.class));
        finish();
    }
    public void next(View view){
        viewPager.setCurrentItem(currentPosition+1);
    }

    private void addDots(int position){

        dots=new TextView[3];
        dotsLayout.removeAllViews();

        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPosition=position;


            if(position==0 || position==1 ){
                letsGetStartedBtn.setVisibility(View.INVISIBLE);
            }
            else {
                animation= AnimationUtils.loadAnimation(OnBoardingScreen.this,R.anim.bottom_anim);
                letsGetStartedBtn.setAnimation(animation);
                letsGetStartedBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}