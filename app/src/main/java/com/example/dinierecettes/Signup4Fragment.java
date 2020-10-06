package com.example.dinierecettes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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


public class Signup4Fragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private ImageView back_icon_on_signup4_fragment;
    public NavController navController;
    private TextInputLayout signup_password,signup_confirm_password;
    private Button signup4_next_btn;
    private TextInputEditText edit_password,edit_confirm_password;
    private ScrollView signup4_parent_layout;
    private Context context;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(4);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        signup4_parent_layout = view.findViewById(R.id.signup4_parent_layout);
        progressBar = view.findViewById(R.id.signup4_progress);
        signup_password = view.findViewById(R.id.signup_password);
        signup_confirm_password = view.findViewById(R.id.signup_confirm_password);
        signup4_next_btn =  view.findViewById(R.id.signup4_next_btn);
        edit_password = view.findViewById(R.id.edit_password);
        edit_confirm_password = view.findViewById(R.id.edit_confirm_password);
        back_icon_on_signup4_fragment = view.findViewById(R.id.back_icon_on_signup4_fragment);

        back_icon_on_signup4_fragment.setOnClickListener(this);
        signup4_next_btn.setOnClickListener(this);
        edit_password.setOnEditorActionListener(this);
        edit_confirm_password.setOnEditorActionListener(this);

        Constants.HideErrorOnTyping(signup_password);
        Constants.HideErrorOnTyping(signup_confirm_password);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if(view == back_icon_on_signup4_fragment){
            getActivity().onBackPressed();

        }
        else if(view==signup4_next_btn){
            String password = signup_password.getEditText().getText().toString().trim();
            String confirm_password = signup_confirm_password.getEditText().getText().toString().trim();
            if(!Constants.isValidPassword(password)){
                signup_password.setError(getResources().getString(R.string.invalid_password));

            }
            else if(!confirm_password.equals(password)){
                signup_confirm_password.setError(getResources().getString(R.string.invalid_confirm_password));
            }
            else {
                sendOTPtoUser(password);
            }

        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            if(signup_password.hasFocus()){
                signup_password.clearFocus();
            }
        }
        else if(i == EditorInfo.IME_ACTION_DONE) {
            if(signup_confirm_password.hasFocus()){
                signup_confirm_password.clearFocus();
            }
        }
        return false;
    }

    private void sendOTPtoUser(final String password) {
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
                        PreferenceData.setReceivedOTP(context,jsonObject.getString("otp"));
                        PreferenceData.setUserpassword(context,password);
                        navController.navigate(R.id.action_signup4Fragment_to_signup5Fragment);
                    }
                    else {
                        Toast.makeText(context,getResources().getString(R.string.some_wrong),Toast.LENGTH_LONG).show();
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
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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
