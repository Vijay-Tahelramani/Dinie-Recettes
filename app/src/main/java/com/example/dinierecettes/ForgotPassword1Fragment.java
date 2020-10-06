package com.example.dinierecettes;

import android.content.Context;
import android.os.Bundle;
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

public class ForgotPassword1Fragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private ImageView back_icon_on_forgot1_fragment;
    public NavController navController;
    private TextInputLayout forgot_email;
    private Button forgot_send_otp_btn;
    private TextInputEditText edit_email_forgot;
    private ScrollView forgot1_parent_layout;
    private Context context;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)this.getActivity()).signup_progess.setVisibility(View.VISIBLE);
        ((Main2Activity)this.getActivity()).signup_progess.setMax(3);
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(1);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);

        progressBar = view.findViewById(R.id.forgot_progress);


        forgot1_parent_layout = view.findViewById(R.id.forgot1_parent_layout);

        forgot_email = view.findViewById(R.id.forgot_email);
        forgot_send_otp_btn =  view.findViewById(R.id.forgot_send_otp_btn);
        edit_email_forgot = view.findViewById(R.id.edit_email_forgot);
        back_icon_on_forgot1_fragment = view.findViewById(R.id.back_icon_on_forgot1_fragment);


        back_icon_on_forgot1_fragment.setOnClickListener(this);
        forgot_send_otp_btn.setOnClickListener(this);
        edit_email_forgot.setOnEditorActionListener(this);

        Constants.HideErrorOnTyping(forgot_email);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if(view == back_icon_on_forgot1_fragment){
            getActivity().onBackPressed();

        }
        else if(view==forgot_send_otp_btn){

            String email = forgot_email.getEditText().getText().toString().trim();
            if(!Constants.isEmailValid(email)){
                forgot_email.setError(getResources().getString(R.string.invalid_email));

            }
            else {
                checkEmailExist(email);
            }

        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if(i == EditorInfo.IME_ACTION_DONE) {
            if(forgot_email.hasFocus()){
                forgot_email.clearFocus();

            }
        }
        return false;
    }
    private void checkEmailExist(final String email) {

        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FORGOT_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("status").equals("OK")){
                        PreferenceData.setUseremail(context,email);
                        PreferenceData.setReceivedOTP(context,jsonObject.getString("otp"));

                        navController.navigate(R.id.action_forgotPassword1Fragment_to_forgotPassword2Fragment);

                    }
                    else if(jsonObject.getString("status").equals("WRONG")){
                        progressBar.setVisibility(View.GONE);
                        forgot_email.setError(getResources().getString(R.string.email_not_found));
                    }
                    else if(jsonObject.getString("status").equals("ERROR")){
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(context,error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }



}
