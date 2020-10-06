package com.example.dinierecettes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;


public class SearchByNameFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout search_cook_time_input_layout;
    private AutoCompleteTextView search_cuisine_selection,search_category_selection;
    private ImageButton search_rate_1,search_rate_2,search_rate_3,search_rate_4,search_rate_5;
    private MaterialButton search_by_name_btn;
    private String recipe_name = "", rating_filter = "",category = "", cuisine = "", cook_time = "";
    private Context context;
    private ConstraintLayout sbn_error_layout;
    private ImageButton sbn_hide_error_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_by_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        search_cook_time_input_layout = view.findViewById(R.id.search_cook_time_input_layout);
        search_cuisine_selection = view.findViewById(R.id.search_cuisine_selection);
        search_category_selection = view.findViewById(R.id.search_category_selection);
        search_rate_1 = view.findViewById(R.id.search_rate_1);
        search_rate_2 = view.findViewById(R.id.search_rate_2);
        search_rate_3 = view.findViewById(R.id.search_rate_3);
        search_rate_4 = view.findViewById(R.id.search_rate_4);
        search_rate_5 = view.findViewById(R.id.search_rate_5);
        search_by_name_btn = view.findViewById(R.id.search_by_name_btn);
        sbn_error_layout = view.findViewById(R.id.sbn_error_layout);
        sbn_hide_error_btn = view.findViewById(R.id.sbn_hide_error_btn);

        search_rate_1.setOnClickListener(this);
        search_rate_2.setOnClickListener(this);
        search_rate_3.setOnClickListener(this);
        search_rate_4.setOnClickListener(this);
        search_rate_5.setOnClickListener(this);
        search_by_name_btn.setOnClickListener(this);
        sbn_hide_error_btn.setOnClickListener(this);

        ArrayAdapter<String> Categoryadapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,Constants.category);
        search_category_selection.setAdapter(Categoryadapter);

        ArrayAdapter<String> Cuisineadapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,Constants.cuisine);
        search_cuisine_selection.setAdapter(Cuisineadapter);
        Constants.HideErrorOnTyping(SearchActivity.search_recipe_layout);
    }

    @Override
    public void onClick(View view) {
        if(view==search_by_name_btn){
            /*if(TextUtils.isEmpty(SearchActivity.search_recipe_layout.getEditText().getText().toString())){
                SearchActivity.search_recipe_layout.setError(" ");
            }
            else {*/
                recipe_name = SearchActivity.search_recipe_layout.getEditText().getText().toString();
                if(!TextUtils.isEmpty(search_category_selection.getText().toString())){
                    category = search_category_selection.getText().toString();
                }
                else {
                    category = "";
                }
                if(!TextUtils.isEmpty(search_cuisine_selection.getText().toString())){
                    cuisine = search_cuisine_selection.getText().toString();
                }
                else {
                    cuisine = "";
                }
                if(!TextUtils.isEmpty(search_cook_time_input_layout.getEditText().getText().toString())){
                    cook_time = search_cook_time_input_layout.getEditText().getText().toString();
                }
                else {
                    cook_time = "";
                }

                if(cuisine.equals("") && cook_time.equals("") && category.equals("") && rating_filter.equals("") && recipe_name.equals("")){
                    sbn_error_layout.setVisibility(View.VISIBLE);
                }
                else {
                    Intent i = new Intent(getActivity(), ListOfRecipesActivity.class);
                    Bundle search_filters = new Bundle();
                    search_filters.putString("recipe_name", recipe_name);
                    search_filters.putString("category", category);
                    search_filters.putString("cuisine", cuisine);
                    search_filters.putString("cook_time", cook_time);
                    search_filters.putString("rating_filter", rating_filter);

                    i.putExtra("coming_from", "search_by_name");
                    i.putExtra("search_data", search_filters);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
                /*if(search_cook_time_input_layout.hasFocus()){
                    search_cook_time_input_layout.clearFocus();
                }*/
            /*}*/
        }
        else if(view==search_rate_1){
            search_rate_1.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_2.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            search_rate_3.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            search_rate_4.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            search_rate_5.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            rating_filter="1";
        }
        else if(view==search_rate_2){
            search_rate_1.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_2.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_3.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            search_rate_4.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            search_rate_5.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            rating_filter="2";
        }
        else if(view==search_rate_3){
            search_rate_1.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_2.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_3.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_4.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            search_rate_5.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            rating_filter="3";
        }
        else if(view==search_rate_4){
            search_rate_1.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_2.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_3.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_4.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_5.setColorFilter(ContextCompat.getColor(context, R.color.colorLightGrey));
            rating_filter="4";
        }
        else if(view==search_rate_5){
            search_rate_1.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_2.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_3.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_4.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            search_rate_5.setColorFilter(ContextCompat.getColor(context, R.color.ratingYellow));
            rating_filter="5";
        }
        else if(view == sbn_hide_error_btn){
            sbn_error_layout.setVisibility(View.GONE);
        }

    }

}
