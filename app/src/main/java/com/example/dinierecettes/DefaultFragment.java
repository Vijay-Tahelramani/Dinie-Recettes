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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DefaultFragment extends Fragment implements View.OnClickListener{

    private Button login_fragment_btn,signup_fragment_btn;
    private TextView guest_access_txt;
    public NavController navController;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)this.getActivity()).signup_progess.setVisibility(View.GONE);
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(0);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_default, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);
        context = getActivity().getApplicationContext();
        login_fragment_btn = view.findViewById(R.id.login_fragment_btn);
        signup_fragment_btn = view.findViewById(R.id.signup_fragment_btn);
        guest_access_txt = view.findViewById(R.id.guest_access_txt);

        login_fragment_btn.setOnClickListener(this);
        signup_fragment_btn.setOnClickListener(this);
        guest_access_txt.setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {

        if(view == login_fragment_btn){
            navController.navigate(R.id.action_defaultFragment_to_loginFragment);
        }
        else if(view == signup_fragment_btn){
            PreferenceData.clearUserData(context);
            navController.navigate(R.id.action_defaultFragment_to_signup1Fragment);
        }
        else if(view == guest_access_txt){
            PreferenceData.clearUserData(context);
            Intent intent = new Intent(getActivity(),HomeActivity.class);
            startActivity(intent);
            getActivity().finish();

        }

    }
}
