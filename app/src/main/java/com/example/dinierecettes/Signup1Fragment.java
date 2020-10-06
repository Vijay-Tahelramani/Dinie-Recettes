package com.example.dinierecettes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class Signup1Fragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private ImageView back_icon_on_signup1_fragment;
    public NavController navController;
    private TextInputLayout signup_first_name,signup_last_name;
    private Button signup1_next_btn;
    private TextInputEditText edit_first_name,edit_last_name;
    private ScrollView signup1_parent_layout;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Main2Activity)this.getActivity()).signup_progess.setVisibility(View.VISIBLE);
        ((Main2Activity)this.getActivity()).signup_progess.setMax(5);
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(1);
        return inflater.inflate(R.layout.fragment_signup1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if(PreferenceData.getUserfirstname(getActivity().getApplicationContext())!=null && PreferenceData.getUserfirstname(getActivity().getApplicationContext())!=null){
            signup_first_name.getEditText().setText(PreferenceData.getUserfirstname(getActivity().getApplicationContext()));
            signup_last_name.getEditText().setText(PreferenceData.getUserfirstname(getActivity().getApplicationContext()));
        }*/

        context = getActivity().getApplicationContext();

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        signup1_parent_layout = view.findViewById(R.id.signup1_parent_layout);

        signup_first_name = view.findViewById(R.id.signup_first_name);
        signup_last_name = view.findViewById(R.id.signup_last_name);
        signup1_next_btn =  view.findViewById(R.id.signup1_next_btn);
        edit_first_name = view.findViewById(R.id.edit_first_name);
        edit_last_name = view.findViewById(R.id.edit_last_name);
        back_icon_on_signup1_fragment = view.findViewById(R.id.back_icon_on_signup1_fragment);
        back_icon_on_signup1_fragment.setOnClickListener(this);
        signup1_next_btn.setOnClickListener(this);
        edit_last_name.setOnEditorActionListener(this);
        edit_first_name.setOnEditorActionListener(this);
        signup1_parent_layout.setOnClickListener(this);

        Constants.HideErrorOnTyping(signup_first_name);
        Constants.HideErrorOnTyping(signup_last_name);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if(view == back_icon_on_signup1_fragment){
            getActivity().onBackPressed();

        }
        else if(view==signup1_next_btn){
            String first_name = signup_first_name.getEditText().getText().toString().trim();
            String last_name = signup_last_name.getEditText().getText().toString().trim();
            if(TextUtils.isEmpty(first_name)){
                signup_first_name.setError(getResources().getString(R.string.required_field_missing));

            }
            else if(TextUtils.isEmpty(last_name)){
                signup_last_name.setError(getResources().getString(R.string.required_field_missing));
            }
            else {
                PreferenceData.clearUserData(context);
                PreferenceData.setUserName(context,first_name+" "+last_name);
                navController.navigate(R.id.action_signup1Fragment_to_signup2Fragment);
            }

        }
        else if(view==signup1_parent_layout){

        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT){
            if(signup_first_name.hasFocus()){
                signup_first_name.clearFocus();
            }
        }
        else if(i == EditorInfo.IME_ACTION_DONE) {
            if(signup_last_name.hasFocus()){
                signup_last_name.clearFocus();
            }
        }
        return false;
    }
}
