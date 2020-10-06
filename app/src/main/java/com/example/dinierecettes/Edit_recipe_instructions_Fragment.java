package com.example.dinierecettes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit_recipe_instructions_Fragment extends Fragment implements View.OnClickListener {

    public NavController EraNavController;
    private Context context;
    private ConstraintLayout er_instruction_error_layout;
    private ImageButton er_instruction_hide_error_btn;
    private TableLayout er_steps_table;
    private MaterialButton er_more_step_btn, final_edit_recipe_btn;
    private ArrayList<String> final_Instruction_Steps;
    int step_num = 0;
    private ProgressBar final_er_progress;
    private String recipe_data = "",recipe_id="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_recipe_instructions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        EraNavController = Navigation.findNavController(getActivity(), R.id.era_host_fragment);

        er_instruction_error_layout = view.findViewById(R.id.er_instruction_error_layout);
        er_instruction_hide_error_btn = view.findViewById(R.id.er_instruction_hide_error_btn);
        er_more_step_btn = view.findViewById(R.id.er_more_step_btn);
        final_edit_recipe_btn = view.findViewById(R.id.final_edit_recipe_btn);
        final_er_progress = view.findViewById(R.id.final_er_progress);

        er_instruction_hide_error_btn.setOnClickListener(this);
        er_more_step_btn.setOnClickListener(this);
        final_edit_recipe_btn.setOnClickListener(this);
        er_steps_table = view.findViewById(R.id.er_steps_table);
        final_Instruction_Steps = new ArrayList<String>();

        if(getActivity().getIntent()!=null){
            recipe_data = getActivity().getIntent().getStringExtra("recipe_response");
            try {
                JSONObject jsonObject = new JSONObject(recipe_data);
                JSONObject recipe_detailsObj = jsonObject.getJSONObject("recipe_details");
                recipe_id = recipe_detailsObj.getString("recipe_id");
                JSONArray instruction_details_array = jsonObject.getJSONArray("instruction_details");
                if(instruction_details_array.length()>0) {
                    for (int i = 0; i < instruction_details_array.length(); i++) {
                        step_num = step_num + 1;
                        TableRow item_Row = (TableRow) getLayoutInflater().inflate(R.layout.instruction_table_items, null);
                        TextView step_text = item_Row.findViewById(R.id.instruction_step_num);
                        step_text.setText(getResources().getString(R.string.step) + " " + step_num + ":");

                        TextInputLayout instruction_steps_layout = item_Row.findViewById(R.id.instruction_steps_layout);
                        instruction_steps_layout.getEditText().setText(instruction_details_array.getString(i));
                        er_steps_table.addView(item_Row);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void CreateDynamicSteps() {

        step_num = step_num + 1;
        TableRow item_Row = (TableRow) getLayoutInflater().inflate(R.layout.instruction_table_items, null);
        TextView step_text = item_Row.findViewById(R.id.instruction_step_num);
        step_text.setText(getResources().getString(R.string.step) + " " + step_num + ":");

        TextInputLayout instruction_steps_layout = item_Row.findViewById(R.id.instruction_steps_layout);
        er_steps_table.addView(item_Row);
    }

    @Override
    public void onClick(View view) {
        if (view == er_instruction_hide_error_btn) {
            er_instruction_error_layout.setVisibility(View.GONE);
        } else if (view == er_more_step_btn) {
            CreateDynamicSteps();
        } else if (view == final_edit_recipe_btn) {
            int TableChildCount = er_steps_table.getChildCount();
            for (int i = 0; i < TableChildCount; i++) {
                View viewChild = er_steps_table.getChildAt(i);
                if (viewChild instanceof TableRow) {
                    View viewChild2 = ((TableRow) viewChild).getChildAt(0);
                    if (viewChild2 instanceof ConstraintLayout) {
                        View viewChild3 = ((ConstraintLayout) viewChild2).getChildAt(1);
                        if (viewChild3 instanceof TextInputLayout) {
                            String Instruction_txt = ((TextInputLayout) viewChild3).getEditText().getText().toString().trim();
                            if (TextUtils.isEmpty(Instruction_txt)) {
                            } else {
                                er_instruction_error_layout.setVisibility(View.GONE);
                                final_Instruction_Steps.add(Instruction_txt);
                            }
                        }
                    }
                }
            }
            if (final_Instruction_Steps.size() > 0) {
                JSONArray instructions_json_array = new JSONArray(final_Instruction_Steps);
                PreferenceData.SetRecipeInstructions(context, instructions_json_array.toString());
                UpdateRecipe();
            } else if(!PreferenceData.getRecipeLink(context).equals("no link")){
                JSONArray instructions_json_array = new JSONArray();
                PreferenceData.SetRecipeInstructions(context, instructions_json_array.toString());
                UpdateRecipe();
            }
            else {
                final_Instruction_Steps.clear();
                er_instruction_error_layout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void UpdateRecipe() {
        final_er_progress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        /*Log.i("user_id",PreferenceData.getUserID(context));
        Log.i("recipe_name",PreferenceData.getRecipeName(context));
        Log.i("picture_of_recipe",PreferenceData.getRecipeImage(context));
        Log.i("iname",PreferenceData.getRecipeImageName(context));
        Log.i("recipe_category",PreferenceData.getRecipeCategory(context));
        Log.i("recipe_cuisine",PreferenceData.getRecipeCuisine(context));
        Log.i("cook_time",PreferenceData.getRecipeCookTime(context));
        Log.i("number_of_serving",PreferenceData.getRecipeServing(context));
        Log.i("calories",PreferenceData.getRecipeCalories(context));
        Log.i("video_link",PreferenceData.getRecipeLink(context));
        Log.i("ingredient_list",PreferenceData.getRecipeIngredients(context));
        Log.i("instruction_list",PreferenceData.getRecipeInstructions(context));*/
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_EDIT_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final_er_progress.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){

                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        PreferenceData.clearRecipeData(context);
                        Intent intent = new Intent();
                        intent.putExtra("recipe_id", recipe_id);
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        //EraNavController.navigate(R.id.action_add_Recipe_InstructionsFragment_to_homeFragment);
                    }
                    else if(jsonObject.getString("status").equals("WRONG")){
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
                        final_er_progress.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("recipe_id",recipe_id);
                params.put("recipe_name",PreferenceData.getRecipeName(context));
                params.put("picture_of_recipe",PreferenceData.getRecipeImage(context));
                params.put("iname",PreferenceData.getRecipeImageName(context));
                params.put("recipe_category",PreferenceData.getRecipeCategory(context));
                params.put("recipe_cuisine",PreferenceData.getRecipeCuisine(context));
                params.put("cook_time",PreferenceData.getRecipeCookTime(context));
                params.put("number_of_serving",PreferenceData.getRecipeServing(context));
                params.put("calories",PreferenceData.getRecipeCalories(context));
                params.put("video_link",PreferenceData.getRecipeLink(context));
                params.put("ingredient_list",PreferenceData.getRecipeIngredients(context));
                params.put("instruction_list",PreferenceData.getRecipeInstructions(context));

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
