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


public class ForgotPassword3Fragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private ImageView back_icon_on_forgot3_fragment;
    public NavController navController;
    private TextInputLayout forgot_password,forgot_confirm_password;
    private Button forgot_reset_password_btn;
    private Context context;
    private TextInputEditText forgot_edit_password,forgot_edit_confirm_password;
    private ScrollView forgot3_parent_layout;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(3);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.forgot3_progress);
        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);
        context = getActivity().getApplicationContext();
        forgot3_parent_layout = view.findViewById(R.id.forgot3_parent_layout);

        forgot_password = view.findViewById(R.id.forgot_password);
        forgot_confirm_password = view.findViewById(R.id.forgot_confirm_password);
        forgot_reset_password_btn =  view.findViewById(R.id.forgot_reset_password_btn);
        forgot_edit_password = view.findViewById(R.id.forgot_edit_password);
        forgot_edit_confirm_password = view.findViewById(R.id.forgot_edit_confirm_password);
        back_icon_on_forgot3_fragment = view.findViewById(R.id.back_icon_on_forgot3_fragment);

        back_icon_on_forgot3_fragment.setOnClickListener(this);
        forgot_reset_password_btn.setOnClickListener(this);
        forgot_edit_password.setOnEditorActionListener(this);
        forgot_edit_confirm_password.setOnEditorActionListener(this);

        Constants.HideErrorOnTyping(forgot_password);
        Constants.HideErrorOnTyping(forgot_confirm_password);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if(view == back_icon_on_forgot3_fragment){
            getActivity().onBackPressed();

        }
        else if(view==forgot_reset_password_btn){
            String password = forgot_password.getEditText().getText().toString().trim();
            String confirm_password = forgot_confirm_password.getEditText().getText().toString().trim();
            if(!Constants.isValidPassword(password)){
                forgot_password.setError(getResources().getString(R.string.invalid_password));

            }
            else if(!confirm_password.equals(password)){
                forgot_confirm_password.setError(getResources().getString(R.string.invalid_confirm_password));
            }
            else {
                reset_password(password);

            }
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            if(forgot_password.hasFocus()){
                forgot_password.clearFocus();
            }

        }
        else if(i == EditorInfo.IME_ACTION_DONE) {
            if(forgot_confirm_password.hasFocus()){
                forgot_confirm_password.clearFocus();

            }
        }
        return false;
    }
    private void reset_password(final String password) {
        final String user_email = PreferenceData.getUseremail(context);

        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RESET_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        navController.navigate(R.id.action_forgotPassword3Fragment_to_loginFragment);
                    }
                    else {
                        Toast.makeText(context,getResources().getString(R.string.some_wrong),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
