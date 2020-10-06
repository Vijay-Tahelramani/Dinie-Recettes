package com.example.dinierecettes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Main2Activity extends AppCompatActivity {

    public NavController navController;
    public ProgressBar signup_progess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        signup_progess = findViewById(R.id.signup_progress);

        navController = Navigation.findNavController(this,R.id.host_fragment);

        final View activityRootView = findViewById(R.id.activityRoot);
        /*activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(getApplicationContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
                    activityRootView.setY(activityRootView.getY()-150);
                }
            }
        });*/
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(ForgotPassword2Fragment.timer!=null) {
            ForgotPassword2Fragment.timer.cancel();
        }
        if(Signup5Fragment.timer!=null) {
            Signup5Fragment.timer.cancel();
        }
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
