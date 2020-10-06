package com.example.dinierecettes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {

    public NavController navController;
    private ImageView back_icon_on_login_fragment;
    private TextInputLayout login_email, login_password;
    private Button login_btn;
    private TextView forget_password_txt;
    private TextInputEditText email_edit_txt, password_edit_txt;
    private ScrollView login_parent_layout;
    private Context context;
    private ConstraintLayout edit_layout;
    private LinearLayout logo_layout;
    private ProgressBar login_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Main2Activity) this.getActivity()).signup_progess.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);

        login_parent_layout = view.findViewById(R.id.login_parent_layout);
        edit_layout = view.findViewById(R.id.edit_layout);
        logo_layout = view.findViewById(R.id.login_logo_layout);
        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        login_btn = view.findViewById(R.id.login_btn);
        forget_password_txt = view.findViewById(R.id.forget_password_txt);
        email_edit_txt = view.findViewById(R.id.email_edit_txt);
        password_edit_txt = view.findViewById(R.id.password_edit_txt);
        back_icon_on_login_fragment = view.findViewById(R.id.back_icon_on_login_fragment);
        login_progress = view.findViewById(R.id.login_progress);

        back_icon_on_login_fragment.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        forget_password_txt.setOnClickListener(this);
        forget_password_txt.setOnClickListener(this);
        email_edit_txt.setOnFocusChangeListener(this);
        password_edit_txt.setOnEditorActionListener(this);
        email_edit_txt.setOnEditorActionListener(this);
        login_email.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_email.getEditText().setText("");
            }
        });
        Constants.HideErrorOnTyping(login_email);
        Constants.HideErrorOnTyping(login_password);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if (view == login_btn) {

            String emial = login_email.getEditText().getText().toString().trim();
            String password = login_password.getEditText().getText().toString().trim();
            if (!Constants.isEmailValid(emial)) {
                login_email.setError(getResources().getString(R.string.invalid_email));

            } else if (!Constants.isValidPassword(password)) {
                login_password.setError(getResources().getString(R.string.invalid_password));
            } else {
                LoginUser(emial, password);
            }

        } else if (view == forget_password_txt) {
            PreferenceData.clearUserData(context);
            navController.navigate(R.id.action_loginFragment_to_forgotPassword1Fragment);
        } else if (view == back_icon_on_login_fragment) {
            getActivity().onBackPressed();
        } else if(view==login_email){
            login_email.getEditText().setText("");
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(view == email_edit_txt){
           }
        else if(view==password_edit_txt){
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_NEXT) {
            if (login_email.hasFocus()) {
                login_email.clearFocus();

            }

        } else if (i == EditorInfo.IME_ACTION_DONE) {
            if (login_password.hasFocus()) {
                login_password.clearFocus();
            }

        }
        return false;
    }


    private void LoginUser(final String email, final String password) {
        //String url = Constants.URL_LOGIN + "?email=" + email + "&password=" + password;
        login_progress.setVisibility(View.VISIBLE);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                login_progress.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        PreferenceData.setUserID(context,jsonObject.getString("id"));
                        PreferenceData.setUserName(context,jsonObject.getString("name"));
                        PreferenceData.setUseremail(context,jsonObject.getString("email"));
                        PreferenceData.setUserpassword(context,jsonObject.getString("password"));
                        PreferenceData.setUsercontact(context,jsonObject.getString("phonenumber"));
                        PreferenceData.setUserprofile(context,jsonObject.getString("profilepicture"));

                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getActivity(),HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else {
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
                        login_progress.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
