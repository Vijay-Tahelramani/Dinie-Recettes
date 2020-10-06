package com.example.dinierecettes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView img_logo;
    private TextView text_logo;
    private LinearLayout launch_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);


        img_logo = findViewById(R.id.img_logo);
        text_logo = findViewById(R.id.text_logo);
        launch_layout = findViewById(R.id.launch_layout);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.launch_icon_anim);
        //img_logo.setAnimation(animation);
        //text_logo.setAnimation(animation);
        launch_layout.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!PreferenceData.getUserID(getApplicationContext()).equals("")){
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(i);
                    finish();
                }

            }
        },3000);
    }
}
