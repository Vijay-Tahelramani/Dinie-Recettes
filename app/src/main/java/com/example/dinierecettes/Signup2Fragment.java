package com.example.dinierecettes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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

public class Signup2Fragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private ImageView back_icon_on_signup2_fragment;
    public NavController navController;
    private TextInputLayout signup_email,signup_contact;
    private Button signup2_next_btn;
    private TextInputEditText edit_email,edit_contact;
    private ScrollView signup2_parent_layout;
    private Context context;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(2);
        return inflater.inflate(R.layout.fragment_signup2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        progressBar = view.findViewById(R.id.signup2_progress);
        signup2_parent_layout = view.findViewById(R.id.signup2_parent_layout);

        signup_email = view.findViewById(R.id.signup_email);
        signup_contact = view.findViewById(R.id.signup_contact);
        signup2_next_btn =  view.findViewById(R.id.signup2_next_btn);
        edit_email = view.findViewById(R.id.edit_email);
        edit_contact = view.findViewById(R.id.edit_contact);
        back_icon_on_signup2_fragment = view.findViewById(R.id.back_icon_on_signup2_fragment);

        back_icon_on_signup2_fragment.setOnClickListener(this);
        signup2_next_btn.setOnClickListener(this);
        edit_email.setOnEditorActionListener(this);
        edit_contact.setOnEditorActionListener(this);

        Constants.HideErrorOnTyping(signup_email);
        Constants.HideErrorOnTyping(signup_contact);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if(view == back_icon_on_signup2_fragment){
            getActivity().onBackPressed();

        }
        else if(view==signup2_next_btn){
            String emial = signup_email.getEditText().getText().toString().trim();
            String contact = signup_contact.getEditText().getText().toString().trim();
            if(contact.equals("")){
                contact = "1234567890";
            }
            if(!Constants.isEmailValid(emial)){
                signup_email.setError(getResources().getString(R.string.invalid_email));

            }
            else if(contact.length()!=10 | !TextUtils.isDigitsOnly(contact)){
                signup_contact.setError(getResources().getString(R.string.invalid_user_contact));
            }
            else {
                    checkEmailExist(emial,contact);
            }
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            if(signup_email.hasFocus()){
                signup_email.clearFocus();
            }
        }
        else if(i == EditorInfo.IME_ACTION_DONE) {
            if(signup_contact.hasFocus()){
                signup_contact.clearFocus();
            }
        }
        return false;
    }

    private void checkEmailExist(final String email,final String contact) {

        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_EMAIL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        PreferenceData.setUseremail(context,email);
                        PreferenceData.setUsercontact(context,contact);
                        navController.navigate(R.id.action_signup2Fragment_to_signup3Fragment);
                    }
                    else if(jsonObject.getString("status").equals("WRONG")){
                        signup_email.setError(getResources().getString(R.string.email_exist));
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
                params.put("email",email);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

}
