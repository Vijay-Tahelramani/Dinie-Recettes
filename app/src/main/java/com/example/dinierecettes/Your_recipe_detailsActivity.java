package com.example.dinierecettes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Your_recipe_detailsActivity extends AppCompatActivity  implements View.OnClickListener{

    private CollapsingToolbarLayout yrd_toolbar_layout;
    private Toolbar yrd_toolbar;
    private ImageView yrd_expandedImage,yrd_rating_stars,yrd_link_image;
    private TextView yrd_rating_numbers,yrd_category_name,yrd_cuisine_name,yrd_cook_time_txt_view,yrd_serving_txt_view,
            yrd_cal_txt_view,yrd_link_txt_view;
    private LinearLayout yrd_link_view;
    private MaterialButton yrd_show_instructions,yrd_show_ingredients;
    private TableLayout yrd_instructions_layout,yrd_ingredients_layout;
    private ConstraintLayout yrd_progress_layout;
    private ImageView yrd_sad_icon;
    private ProgressBar yrd_progressbar;
    private TextView yrd_progress_txt;
    private NestedScrollView yrd_content_view;
    private String video_link="";
    private DatabaseHandler mDatabase;
    private ArrayList<Recipes_List> OfflineRecipeDataList;
    private AlertDialog.Builder DRD_builder;
    private MaterialButton drd_cancel_btn,drd_continue_btn;
    private AlertDialog Delete_Recipe_Dialog;
    private TextView drd_text;
    private ProgressBar drd_progressbar;
    private String recipe_response = "";
    private TableRow yrd_ingredients_header_row;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_recipe_details);

        /*-------- App Bar Configuration-------*/
        yrd_toolbar_layout = findViewById(R.id.yrd_toolbar_layout);
        yrd_toolbar = findViewById(R.id.yrd_toolbar);
        setSupportActionBar(yrd_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*-----------Connection reference between layot views and Class variables------------*/
        yrd_expandedImage = findViewById(R.id.yrd_expandedImage);
        yrd_rating_stars = findViewById(R.id.yrd_rating_stars);
        yrd_link_image = findViewById(R.id.yrd_link_image);
        yrd_rating_numbers = findViewById(R.id.yrd_rating_numbers);
        yrd_category_name = findViewById(R.id.yrd_category_name);
        yrd_cuisine_name = findViewById(R.id.yrd_cuisine_name);
        yrd_cook_time_txt_view = findViewById(R.id.yrd_cook_time_txt_view);
        yrd_serving_txt_view = findViewById(R.id.yrd_serving_txt_view);
        yrd_cal_txt_view = findViewById(R.id.yrd_cal_txt_view);
        yrd_link_txt_view = findViewById(R.id.yrd_link_txt_view);
        yrd_link_view = findViewById(R.id.yrd_link_view);
        yrd_show_ingredients = findViewById(R.id.yrd_show_ingredients);
        yrd_show_instructions = findViewById(R.id.yrd_show_instructions);
        yrd_instructions_layout = findViewById(R.id.yrd_instructions_layout);
        yrd_ingredients_layout = findViewById(R.id.yrd_ingredients_layout);
        yrd_ingredients_header_row = findViewById(R.id.yrd_ingredients_header_row);

        yrd_content_view = findViewById(R.id.yrd_content_view);
        yrd_progress_layout = findViewById(R.id.yrd_progress_layout);
        yrd_progressbar = findViewById(R.id.yrd_progressbar);
        yrd_sad_icon = findViewById(R.id.yrd_sad_icon);
        yrd_progress_txt = findViewById(R.id.yrd_progress_txt);

        /*---------- Set On Click Listener References-----------*/
        yrd_show_instructions.setOnClickListener(this);
        yrd_show_ingredients.setOnClickListener(this);
        yrd_link_view.setOnClickListener(this);

        mDatabase = new DatabaseHandler(getApplicationContext());
        mDatabase.getReadableDatabase();

        if(getIntent().getStringExtra("coming_from").equals("YourRecipes")){
            Fetch_Details(getIntent().getStringExtra("recipe_id"));
        }
        else if(getIntent().getStringExtra("coming_from").equals("Offline")){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            yrd_progress_layout.setVisibility(View.GONE);
            yrd_content_view.setVisibility(View.VISIBLE);
            yrd_rating_numbers.setVisibility(View.GONE);
            yrd_rating_stars.setVisibility(View.GONE);
            String offline_recipe_id = getIntent().getStringExtra("offline_recipe_id");
            String user_id = PreferenceData.getUserID(getApplicationContext());
            OfflineRecipeDataList = mDatabase.getSpecificRecipe(getIntent().getStringExtra("offline_recipe_id"),PreferenceData.getUserID(getApplicationContext()));
            yrd_toolbar_layout.setTitle(OfflineRecipeDataList.get(0).getRecipe_name());
            yrd_cook_time_txt_view.setText(OfflineRecipeDataList.get(0).getCook_time() + " Min");
            yrd_cuisine_name.setText("Cuisine: " + OfflineRecipeDataList.get(0).getRecipe_cuisine());
            yrd_category_name.setText("Category: " + OfflineRecipeDataList.get(0).getRecipe_category());
            yrd_serving_txt_view.setText(OfflineRecipeDataList.get(0).getNumber_of_serving()+" Person");
            if(!OfflineRecipeDataList.get(0).getCalories().equals("0")){
                yrd_cal_txt_view.setText(OfflineRecipeDataList.get(0).getCalories()+" Cal");
            }
            else {
                yrd_cal_txt_view.setText("--- Cal");
            }
            if(!OfflineRecipeDataList.get(0).getVideo_link().equals("no link")){
                video_link = OfflineRecipeDataList.get(0).getVideo_link();
            }
            else {
                yrd_link_view.setEnabled(false);
                yrd_link_txt_view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorLightGrey));
                yrd_link_image.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorLightGrey));
            }
            if(OfflineRecipeDataList.get(0).getRecipe_image().length>0){
                yrd_expandedImage.setImageBitmap(BitmapFactory.decodeByteArray(OfflineRecipeDataList.get(0).getRecipe_image(), 0, OfflineRecipeDataList.get(0).getRecipe_image().length));
            }
            JSONArray ingredient_details_array = null;
            try {
                ingredient_details_array = new JSONArray(OfflineRecipeDataList.get(0).getIngredients_details());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CreateDynamicIngredientsTable(ingredient_details_array);
            if(OfflineRecipeDataList.get(0).getInstructions_details().equals("no instructions")){
                yrd_show_instructions.setVisibility(View.GONE);
                yrd_ingredients_layout.setVisibility(View.VISIBLE);
                yrd_show_ingredients.setEnabled(false);
            }
            else {
                JSONArray instruction_details_array = null;
                try {
                    instruction_details_array = new JSONArray(OfflineRecipeDataList.get(0).getInstructions_details());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CreateDynamicInstructionTable(instruction_details_array);
            }
        }
    }

    private void Fetch_Details(final String recipe_id) {
        yrd_progress_layout.setVisibility(View.VISIBLE);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));
        yrd_content_view.setVisibility(View.GONE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_VIEW_RECIPE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recipe_response = response;
                //final_add_recipe_progress.setVisibility(View.GONE);
                //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        JSONObject recipe_detailsobj = jsonObject.getJSONObject("recipe_details");
                        yrd_toolbar_layout.setTitle(recipe_detailsobj.getString("recipe_name"));
                        if(!recipe_detailsobj.getString("picture_of_recipe").equals("no image")){
                            Glide.with(getApplicationContext())
                                    .load("https://dinierecettes.online/images/"+recipe_detailsobj.getString("picture_of_recipe")+".jpg")
                                    .centerCrop()
                                    .placeholder(R.drawable.recipe_place_holder)
                                    .into(yrd_expandedImage);
                        }
                        yrd_rating_numbers.setText("("+recipe_detailsobj.getString("rating_number")+")");
                        int rating = Integer.parseInt(recipe_detailsobj.getString("rating"));
                        if(rating==1){
                            yrd_rating_stars.setImageResource(R.drawable.one_star);
                        }else if(rating==2){
                            yrd_rating_stars.setImageResource(R.drawable.two_stars);
                        }else if(rating==3){
                            yrd_rating_stars.setImageResource(R.drawable.three_stars);
                        }else if(rating==4){
                            yrd_rating_stars.setImageResource(R.drawable.four_stars);
                        }else {
                            yrd_rating_stars.setImageResource(R.drawable.five_stars);
                        }

                        yrd_category_name.setText("Categoty: "+recipe_detailsobj.getString("recipe_category"));
                        yrd_cuisine_name.setText("Cuisine: "+recipe_detailsobj.getString("recipe_cuisine"));
                        yrd_cook_time_txt_view.setText(recipe_detailsobj.getString("cook_time")+" Min");
                        yrd_serving_txt_view.setText(recipe_detailsobj.getString("number_of_serving")+" Person");
                        if(!recipe_detailsobj.getString("calories").equals("0")){
                            yrd_cal_txt_view.setText(recipe_detailsobj.getString("calories")+" Cal");
                        }
                        else {
                            yrd_cal_txt_view.setText("--- Cal");
                        }
                        if(!recipe_detailsobj.getString("video_link").equals("no link")){
                            video_link = recipe_detailsobj.getString("video_link");
                        }
                        else {
                            yrd_link_view.setEnabled(false);
                            yrd_link_txt_view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorLightGrey));
                            yrd_link_image.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorLightGrey));
                        }

                        JSONArray ingredient_details_array = jsonObject.getJSONArray("ingredient_details");
                        JSONArray instruction_details_array = jsonObject.getJSONArray("instruction_details");
                        CreateDynamicIngredientsTable(ingredient_details_array);
                        if(instruction_details_array.length()>0) {
                            CreateDynamicInstructionTable(instruction_details_array);
                        }
                        else {
                            yrd_show_instructions.setVisibility(View.GONE);
                            yrd_ingredients_layout.setVisibility(View.VISIBLE);
                            yrd_show_ingredients.setEnabled(false);
                        }
                        yrd_progress_layout.setVisibility(View.GONE);
                        getWindow().setStatusBarColor(Color.TRANSPARENT);
                        yrd_content_view.setVisibility(View.VISIBLE);
                    }
                    else if(jsonObject.getString("status").equals("WRONG")){
                        yrd_progressbar.setVisibility(View.GONE);
                        yrd_sad_icon.setVisibility(View.VISIBLE);
                        yrd_progress_txt.setText(getResources().getString(R.string.some_wrong));

                    }

                } catch (JSONException e) {
                    yrd_progressbar.setVisibility(View.GONE);
                    yrd_sad_icon.setVisibility(View.VISIBLE);
                    yrd_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        yrd_progressbar.setVisibility(View.GONE);
                        yrd_sad_icon.setVisibility(View.VISIBLE);
                        yrd_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("recipe_id",recipe_id);
                params.put("login_user_id",PreferenceData.getUserID(getApplicationContext()));

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void CreateDynamicIngredientsTable(JSONArray jsonArray){

        yrd_ingredients_layout.removeAllViews();
        yrd_ingredients_layout.addView(yrd_ingredients_header_row);
        for(int i=0;i<jsonArray.length();i++){
            try {
                JSONObject ingredient_object = jsonArray.getJSONObject(i);
                TableRow item_Row = (TableRow) getLayoutInflater().inflate(R.layout.rd_ingredients_table_items,null);
                TextView ingredient_Name = item_Row.findViewById(R.id.rd_ingre_name);
                TextView ingredient_Quantity = item_Row.findViewById(R.id.rd_quantity_value);

                ingredient_Name.setText(ingredient_object.getString("ingredient_name"));
                ingredient_Quantity.setText(ingredient_object.getString("ingredient_amount"));

                yrd_ingredients_layout.addView(item_Row);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void CreateDynamicInstructionTable(JSONArray jsonArray){

        yrd_instructions_layout.removeAllViews();
        for(int i=0;i<jsonArray.length();i++){
            try {
                TableRow item_Row = (TableRow) getLayoutInflater().inflate(R.layout.rd_instruction_table_items,null);
                final CheckedTextView checkedTextView = item_Row.findViewById(R.id.rd_instruction_step);
                checkedTextView.setText(jsonArray.getString(i));
                checkedTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkedTextView.isChecked()){
                            checkedTextView.setChecked(false);
                            checkedTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.black));
                        }
                        else {
                            checkedTextView.setChecked(true);
                            checkedTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
                        }
                    }
                });
                yrd_instructions_layout.addView(item_Row);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*--------Override Method to refresh recipe details when it is edited---------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Fetch_Details(data.getStringExtra("recipe_id"));
            }
        }
    }

    /*------------ App Bar On Back icon pressed action-----------*/
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);

    }

    @Override
    public void onClick(View view) {
        if(view == yrd_show_instructions){
            yrd_show_instructions.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
            yrd_show_instructions.setTextColor(ContextCompat.getColor(this,R.color.WhiteColor));
            yrd_show_ingredients.setBackgroundColor(ContextCompat.getColor(this,R.color.WhiteColor));
            yrd_show_ingredients.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

            yrd_ingredients_layout.setVisibility(View.GONE);
            yrd_instructions_layout.setVisibility(View.VISIBLE);
        }
        else if(view == yrd_show_ingredients){
            yrd_show_ingredients.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
            yrd_show_ingredients.setTextColor(ContextCompat.getColor(this,R.color.WhiteColor));
            yrd_show_instructions.setBackgroundColor(ContextCompat.getColor(this,R.color.WhiteColor));
            yrd_show_instructions.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

            yrd_instructions_layout.setVisibility(View.GONE);
            yrd_ingredients_layout.setVisibility(View.VISIBLE);
        }
        else if(view == yrd_link_view){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_link));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yrd_menu_items, menu);
        if(getIntent().getStringExtra("coming_from").equals("Offline")){
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.yrd_edit_item:
                Intent i = new Intent(Your_recipe_detailsActivity.this,EditRecipeActivity.class);
                i.putExtra("recipe_response",recipe_response);
                startActivityForResult(i, 1);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;
            case R.id.yrd_delete_item:
                /*------------ Configuration of Guest Login Dialog----------*/
                DRD_builder = new AlertDialog.Builder(this)
                        .setView(R.layout.delete_recipe_dialog_layout);
                Delete_Recipe_Dialog = DRD_builder.create();
                Delete_Recipe_Dialog.show();
                drd_cancel_btn = Delete_Recipe_Dialog.findViewById(R.id.drd_cancel_btn);
                drd_continue_btn = Delete_Recipe_Dialog.findViewById(R.id.drd_continue_btn);
                drd_text = Delete_Recipe_Dialog.findViewById(R.id.drd_text);
                drd_progressbar = Delete_Recipe_Dialog.findViewById(R.id.drd_progressbar);
                drd_cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Delete_Recipe_Dialog.dismiss();
                    }
                });
                if(getIntent().getStringExtra("coming_from").equals("Offline")){
                    drd_text.setText(getResources().getString(R.string.offline_no_longer_available));
                    drd_continue_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mDatabase.deleteRecipe(getIntent().getStringExtra("offline_recipe_id"),PreferenceData.getUserID(getApplicationContext()))!=1){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.some_wrong), Toast.LENGTH_LONG).show();
                            }else {
                                Delete_Recipe_Dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_recipe_success), Toast.LENGTH_LONG).show();
                                finish();
                                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                            }
                        }
                    });
                }
                else if(getIntent().getStringExtra("coming_from").equals("YourRecipes")){
                    drd_text.setText(getResources().getString(R.string.no_longer_available));
                    drd_continue_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DeleteYourRecipe();
                        }
                    });

                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteYourRecipe(){
        drd_progressbar.setVisibility(View.VISIBLE);
        drd_text.setVisibility(View.GONE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                drd_progressbar.setVisibility(View.GONE);
                drd_text.setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        Delete_Recipe_Dialog.dismiss();
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    }
                    else{
                        drd_text.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    drd_progressbar.setVisibility(View.GONE);
                    drd_text.setVisibility(View.VISIBLE);
                    drd_text.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        drd_progressbar.setVisibility(View.GONE);
                        drd_text.setVisibility(View.VISIBLE);
                        drd_text.setText(getResources().getString(R.string.some_wrong));
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",PreferenceData.getUserID(getApplicationContext()));
                params.put("recipe_id",getIntent().getStringExtra("recipe_id"));
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

}
