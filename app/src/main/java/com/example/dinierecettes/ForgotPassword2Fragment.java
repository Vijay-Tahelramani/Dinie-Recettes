package com.example.dinierecettes;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import java.util.Map;


public class ForgotPassword2Fragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private ImageView back_icon_on_forgot2_fragment;
    public NavController navController;
    private TextInputLayout forgot_otp;
    private Button forgot_validate_otp_btn;
    private TextInputEditText forgot_edit_otp;
    private ScrollView forgot2_parent_layout;
    private TextView forgot_resend_otp_txt;
    private Context context;
    protected static CountDownTimer timer;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(2);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);

        forgot2_parent_layout = view.findViewById(R.id.forgot2_parent_layout);
        progressBar = view.findViewById(R.id.forgot2_progress);
        forgot_otp = view.findViewById(R.id.forgot_otp);
        forgot_validate_otp_btn =  view.findViewById(R.id.forgot_validate_otp_btn);
        forgot_edit_otp = view.findViewById(R.id.forgot_edit_otp);
        back_icon_on_forgot2_fragment = view.findViewById(R.id.back_icon_on_forgot2_fragment);
        forgot_resend_otp_txt = view.findViewById(R.id.forgot_resend_otp_txt);


        back_icon_on_forgot2_fragment.setOnClickListener(this);
        forgot_validate_otp_btn.setOnClickListener(this);
        forgot_resend_otp_txt.setOnClickListener(this);
        forgot_edit_otp.setOnEditorActionListener(this);

        Constants.HideErrorOnTyping(forgot_otp);
        reset_otp_countdown();
    }

    @Override
    public void onClick(View view) {
        if(view == back_icon_on_forgot2_fragment){
            timer.cancel();
            getActivity().onBackPressed();

        }
        else if(view==forgot_validate_otp_btn){
            if(TextUtils.isEmpty(forgot_otp.getEditText().getText().toString().trim())){
                forgot_otp.setError(getResources().getString(R.string.enter_valid_otp));
            }
            else {
                if(forgot_otp.getEditText().getText().toString().trim().equals(PreferenceData.getReceivedOTP(context))) {
                    timer.cancel();
                    navController.navigate(R.id.action_forgotPassword2Fragment_to_forgotPassword3Fragment);
                }
                else {
                    forgot_otp.setError(getResources().getString(R.string.invalid_entered_otp));
                }
            }
        }
        else if(view== forgot_resend_otp_txt){
            forgot_resend_otp_txt.setEnabled(false);
            forgot_resend_otp_txt.setTextColor(getResources().getColor(R.color.offWhiteColor));
            forgot_resend_otp_txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.resend_off,0,0,0);
            resendOTPtoUser();

        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if(i == EditorInfo.IME_ACTION_DONE) {
            if(forgot_otp.hasFocus()){
                forgot_otp.clearFocus();

            }
        }
        return false;
    }

    private void resendOTPtoUser() {
        final String user_email = PreferenceData.getUseremail(context);
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    private void reset_otp_countdown(){
        timer = new CountDownTimer(31000, 1000) {

            public void onTick(long millisUntilFinished) {
                forgot_resend_otp_txt.setText("Resend OTP (" + millisUntilFinished / 1000+")");
            }

            public void onFinish() {
                forgot_resend_otp_txt.setText("Resend OTP");
                forgot_resend_otp_txt.setEnabled(true);
                forgot_resend_otp_txt.setTextColor(getResources().getColor(R.color.WhiteColor));
                forgot_resend_otp_txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.resend,0,0,0);
            }
        }.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
