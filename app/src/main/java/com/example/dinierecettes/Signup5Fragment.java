package com.example.dinierecettes;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class Signup5Fragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private ImageView back_icon_on_signup5_fragment;
    public NavController navController;
    private TextInputLayout signup_otp;
    private Button validate_otp_btn;
    private TextInputEditText edit_otp;
    private ScrollView signup5_parent_layout;
    private TextView resend_otp_txt;
    protected static CountDownTimer timer;
    private Context context;
    private CircularProgressDrawable progressDrawable;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(5);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup5, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        progressDrawable = new CircularProgressDrawable(context);
        progressDrawable.setBackgroundColor(R.color.WhiteColor);
        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        progressBar = view.findViewById(R.id.signup5_progress);
        signup5_parent_layout = view.findViewById(R.id.signup5_parent_layout);

        signup_otp = view.findViewById(R.id.signup_otp);
        validate_otp_btn =  view.findViewById(R.id.validate_otp_btn);
        edit_otp = view.findViewById(R.id.edit_otp);
        back_icon_on_signup5_fragment = view.findViewById(R.id.back_icon_on_signup5_fragment);
        resend_otp_txt = view.findViewById(R.id.resend_otp_txt);


        back_icon_on_signup5_fragment.setOnClickListener(this);
        validate_otp_btn.setOnClickListener(this);
        resend_otp_txt.setOnClickListener(this);
        edit_otp.setOnEditorActionListener(this);
        Constants.HideErrorOnTyping(signup_otp);
        reset_otp_countdown();
    }

    @Override
    public void onClick(View view) {
        if(view == back_icon_on_signup5_fragment){
            timer.cancel();
            getActivity().onBackPressed();

        }
        else if(view==validate_otp_btn){
            if(TextUtils.isEmpty(signup_otp.getEditText().getText().toString().trim())){
                signup_otp.setError(getResources().getString(R.string.enter_valid_otp));
            }
            else {
                if(signup_otp.getEditText().getText().toString().trim().equals(PreferenceData.getReceivedOTP(context))) {
                    registerUser();
                }
                else {
                    signup_otp.setError(getResources().getString(R.string.invalid_entered_otp));
                }
            }
        }
        else if(view== resend_otp_txt){
            resend_otp_txt.setEnabled(false);
            resend_otp_txt.setTextColor(getResources().getColor(R.color.offWhiteColor));
            resend_otp_txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.resend_off,0,0,0);
            resendOTPtoUser();

        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if(i == EditorInfo.IME_ACTION_DONE) {
            if(signup_otp.hasFocus()){
                signup_otp.clearFocus();
            }
        }
        return false;
    }

    private void reset_otp_countdown(){
        timer =  new CountDownTimer(31000, 1000) {

            public void onTick(long millisUntilFinished) {
                resend_otp_txt.setText("Resend OTP (" + millisUntilFinished / 1000+")");
            }

            public void onFinish() {
                resend_otp_txt.setText("Resend OTP");
                resend_otp_txt.setEnabled(true);
                resend_otp_txt.setTextColor(getResources().getColor(R.color.WhiteColor));
                resend_otp_txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.resend,0,0,0);
            }
        }.start();
    }



    private void registerUser() {
        final String user_email = PreferenceData.getUseremail(context);
        final String user_name = PreferenceData.getUserName(context);
        final String user_password = PreferenceData.getUserpassword(context);
        final String user_contact = PreferenceData.getUsercontact(context);
        String user_profile = "no image";
        String userImage_name = "no image";
        if(!PreferenceData.getUserprofile(context).equals("")){
            user_profile = PreferenceData.getUserprofile(context);
            userImage_name = PreferenceData.getUserImagename(context);

        }

        final String finalUser_profile = user_profile;
        timer.cancel();
        final String finalUserImage_name = userImage_name;
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        PreferenceData.clearUserData(context);
                        navController.navigate(R.id.action_signup5Fragment_to_loginFragment);
                        Toast.makeText(context, jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(context,getResources().getString(R.string.some_wrong),Toast.LENGTH_LONG).show();
                        timer.start();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("name",user_name);
                params.put("email",user_email);
                params.put("password",user_password);
                params.put("phone_number",user_contact);
                params.put("profile_photo", finalUser_profile);
                params.put("iname", finalUserImage_name);
                return params;
            }
        };
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void resendOTPtoUser() {
        final String user_email = PreferenceData.getUseremail(context);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        PreferenceData.setReceivedOTP(context,jsonObject.getString("otp"));
                        reset_otp_countdown();
                    }
                    else {
                        reset_otp_countdown();
                        Toast.makeText(context,getResources().getString(R.string.some_wrong),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email",user_email);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
