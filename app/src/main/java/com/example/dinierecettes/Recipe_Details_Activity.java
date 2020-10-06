package com.example.dinierecettes;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Recipe_Details_Activity extends AppCompatActivity implements View.OnClickListener {

    private String video_link = "",current_recipe_id="";
    private CollapsingToolbarLayout toolbar_layout;
    private Toolbar recipe_details_toolbar;
    private ImageView rd_expandedImage, rd_userprofile_image, rd_rating_stars, rd_link_image;
    private TextView rd_user_name, rd_rating_numbers, rd_category_name, rd_cuisine_name, rd_cook_time_txt_view, rd_serving_txt_view,
            rd_cal_txt_view, rd_link_txt_view;
    private LinearLayout rd_link_view;
    private MaterialButton rd_show_instructions, rd_show_ingredients;
    private TableLayout rd_instructions_layout, rd_ingredients_layout;
    private ImageButton rd_rate_1, rd_rate_2, rd_rate_3, rd_rate_4, rd_rate_5;
    private ConstraintLayout rd_progress_layout, rd_rate_layout;
    private TextView rd_like_it_txt, rd_rate_it_txt;
    private ImageView rd_sad_icon;
    private ProgressBar rd_progressbar, rd_rating_progressbar, create_new_cb_progressbar;
    private TextView rd_progress_txt;
    private NestedScrollView rd_content_view;
    private AlertDialog.Builder Loginbuilder, add_to_fav_builder,view_profile_builder;
    private MaterialButton guest_login_first_btn;
    private AlertDialog Guest_Login_dialog, add_to_fav_dialog,view_profile_dialog;
    private RecyclerView add_fav_cb_recycler_view;
    private ConstraintLayout add_fav_progress_layout;
    private TextView add_fav_progress_txt;
    private ProgressBar add_fav_progressbar;
    private TextInputLayout add_fav_cb_name_layout;
    private MaterialButton add_fav_create_cb_btn;
    private ArrayList<cookbook_list> cbList;
    private CookbookAdapter cookbookAdapter;
    private Menu menu;
    private boolean in_favourites = false;
    private String favourites_id = "";
    private LinearLayout add_to_fav_parent_layout;
    private ViewPager FVP_pager;

    /*--------- View Profile Data variables---------*/
    private ImageView vr_user_imageView;
    private TextView vr_user_name;
    private ImageButton vr_cancel_btn;
    private RecyclerView users_recipe_recycler_view;
    private ConstraintLayout view_profile_layout, vr_progress_layout;
    private ProgressBar vr_progressbar;
    private  TextView vr_progress_txt;
    private ImageView vr_sad_icon;
    private ArrayList<Recipes_List> users_recipes_list;
    private Your_Recipes_Adapter userRecipesAdapter;
    private String user_id = "",user_name = "", user_image = "";

    /*--------- Offline Recipe Data variables--------------*/
    private DatabaseHandler mDatabase;
    private String offline_recipe_name, offline_recipe_cooktime, offline_recipe_cuisine, offline_recipe_num_of_serving,
            offline_recipe_category, offline_recipe_video_link, offline_recipe_calories, offline_recipe_instructions_details, offline_recipe_ingredients_details;
    private byte[] offline_image = new byte[0];

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_recipe_details);

        mDatabase = new DatabaseHandler(getApplicationContext());
        mDatabase.getReadableDatabase();

        /*-------- App Bar Configuration-------*/
        toolbar_layout = findViewById(R.id.toolbar_layout);
        recipe_details_toolbar = findViewById(R.id.recipe_details_toolbar);
        setSupportActionBar(recipe_details_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*-----------Connection reference between layot views and Class variables------------*/
        rd_expandedImage = findViewById(R.id.rd_expandedImage);
        rd_userprofile_image = findViewById(R.id.rd_userprofile_image);
        rd_rating_stars = findViewById(R.id.rd_rating_stars);
        rd_link_image = findViewById(R.id.rd_link_image);
        rd_user_name = findViewById(R.id.rd_user_name);
        rd_rating_numbers = findViewById(R.id.rd_rating_numbers);
        rd_category_name = findViewById(R.id.rd_category_name);
        rd_cuisine_name = findViewById(R.id.rd_cuisine_name);
        rd_cook_time_txt_view = findViewById(R.id.rd_cook_time_txt_view);
        rd_serving_txt_view = findViewById(R.id.rd_serving_txt_view);
        rd_cal_txt_view = findViewById(R.id.rd_cal_txt_view);
        rd_link_txt_view = findViewById(R.id.rd_link_txt_view);
        rd_link_view = findViewById(R.id.rd_link_view);
        rd_show_ingredients = findViewById(R.id.rd_show_ingredients);
        rd_show_instructions = findViewById(R.id.rd_show_instructions);
        rd_instructions_layout = findViewById(R.id.rd_instructions_layout);
        rd_ingredients_layout = findViewById(R.id.rd_ingredients_layout);
        rd_rate_1 = findViewById(R.id.rd_rate_1);
        rd_rate_2 = findViewById(R.id.rd_rate_2);
        rd_rate_3 = findViewById(R.id.rd_rate_3);
        rd_rate_4 = findViewById(R.id.rd_rate_4);
        rd_rate_5 = findViewById(R.id.rd_rate_5);
        rd_rate_layout = findViewById(R.id.rd_rate_layout);
        rd_content_view = findViewById(R.id.rd_content_view);
        rd_progress_layout = findViewById(R.id.rd_progress_layout);
        rd_progress_txt = findViewById(R.id.rd_progress_txt);
        rd_sad_icon = findViewById(R.id.rd_sad_icon);
        rd_progressbar = findViewById(R.id.rd_progressbar);
        rd_like_it_txt = findViewById(R.id.rd_like_it_txt);
        rd_rate_it_txt = findViewById(R.id.rd_rate_it_txt);
        rd_rating_progressbar = findViewById(R.id.rd_rating_progressbar);

        /*---------- Set On Click Listener References-----------*/
        rd_show_instructions.setOnClickListener(this);
        rd_show_ingredients.setOnClickListener(this);
        rd_rate_1.setOnClickListener(this);
        rd_rate_2.setOnClickListener(this);
        rd_rate_3.setOnClickListener(this);
        rd_rate_4.setOnClickListener(this);
        rd_rate_5.setOnClickListener(this);
        rd_link_view.setOnClickListener(this);
        rd_user_name.setOnClickListener(this);
        rd_userprofile_image.setOnClickListener(this);

        if (PreferenceData.getUserID(getApplicationContext()).equals("")) {
            rd_rate_layout.setVisibility(View.GONE);
        }

        if (getIntent().getStringExtra("recipe_id") != null) {
            Fetch_Details(getIntent().getStringExtra("recipe_id"));
        }

    }

    private void Fetch_Details(final String recipe_id) {
        rd_progress_layout.setVisibility(View.VISIBLE);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        rd_content_view.setVisibility(View.GONE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_VIEW_RECIPE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {

                        JSONObject recipe_detailsobj = jsonObject.getJSONObject("recipe_details");

                        current_recipe_id = recipe_detailsobj.getString("recipe_id");

                        if(!PreferenceData.getUserID(getApplicationContext()).equals("")) {
                            if (mDatabase.checkRecipeExist(current_recipe_id,PreferenceData.getUserID(getApplicationContext()))) {
                                menu.getItem(1).setVisible(false);
                            }
                            else {
                                menu.getItem(1).setVisible(true);
                            }
                        }

                        offline_recipe_name = recipe_detailsobj.getString("recipe_name");
                        toolbar_layout.setTitle(recipe_detailsobj.getString("recipe_name"));
                        if (!recipe_detailsobj.getString("picture_of_recipe").equals("no image")) {
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load("https://dinierecettes.online/images/" + recipe_detailsobj.getString("picture_of_recipe") + ".jpg")
                                    .centerCrop()
                                    .placeholder(R.drawable.recipe_place_holder)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            rd_expandedImage.setImageBitmap(resource);
                                            offline_image = getBitmapAsByteArray(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });


                        }
                        rd_rating_numbers.setText("(" + recipe_detailsobj.getString("rating_number") + ")");
                        int rating = Integer.parseInt(recipe_detailsobj.getString("rating"));
                        if (rating == 1) {
                            rd_rating_stars.setImageResource(R.drawable.one_star);
                        } else if (rating == 2) {
                            rd_rating_stars.setImageResource(R.drawable.two_stars);
                        } else if (rating == 3) {
                            rd_rating_stars.setImageResource(R.drawable.three_stars);
                        } else if (rating == 4) {
                            rd_rating_stars.setImageResource(R.drawable.four_stars);
                        } else {
                            rd_rating_stars.setImageResource(R.drawable.five_stars);
                        }

                        offline_recipe_category = recipe_detailsobj.getString("recipe_category");
                        rd_category_name.setText("Categoty: " + recipe_detailsobj.getString("recipe_category"));

                        offline_recipe_cuisine = recipe_detailsobj.getString("recipe_cuisine");
                        rd_cuisine_name.setText("Cuisine: " + recipe_detailsobj.getString("recipe_cuisine"));

                        offline_recipe_cooktime = recipe_detailsobj.getString("cook_time");
                        rd_cook_time_txt_view.setText(recipe_detailsobj.getString("cook_time") + " Min");

                        offline_recipe_num_of_serving = recipe_detailsobj.getString("number_of_serving");
                        rd_serving_txt_view.setText(recipe_detailsobj.getString("number_of_serving") + " Person");

                        offline_recipe_calories = recipe_detailsobj.getString("calories");
                        if (!recipe_detailsobj.getString("calories").equals("0")) {
                            rd_cal_txt_view.setText(recipe_detailsobj.getString("calories") + " Cal");
                        } else {
                            rd_cal_txt_view.setText("--- Cal");
                        }

                        offline_recipe_video_link = recipe_detailsobj.getString("video_link");
                        if (!recipe_detailsobj.getString("video_link").equals("no link")) {
                            video_link = recipe_detailsobj.getString("video_link");
                        } else {
                            rd_link_view.setEnabled(false);
                            rd_link_txt_view.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                            rd_link_image.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                        }

                        user_id = recipe_detailsobj.getString("user_id");
                        user_name = recipe_detailsobj.getString("name");
                        user_image = recipe_detailsobj.getString("profile_photo");
                        rd_user_name.setText(recipe_detailsobj.getString("name"));
                        if (!recipe_detailsobj.getString("profile_photo").equals("no image")) {
                            Glide.with(getApplicationContext())
                                    .load("https://dinierecettes.online/images/" + recipe_detailsobj.getString("profile_photo") + ".jpg")
                                    .centerCrop()
                                    .placeholder(R.drawable.user)
                                    .into(rd_userprofile_image);
                        }
                        if (recipe_detailsobj.getString("number_of_stars") != "null") {
                            rd_like_it_txt.setText(getResources().getString(R.string.thanks_for_rating));
                            rd_rate_it_txt.setVisibility(View.GONE);
                            rd_rate_1.setEnabled(false);
                            rd_rate_2.setEnabled(false);
                            rd_rate_3.setEnabled(false);
                            rd_rate_4.setEnabled(false);
                            rd_rate_5.setEnabled(false);
                            switch (recipe_detailsobj.getString("number_of_stars")) {
                                case "1":
                                    rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    break;
                                case "2":
                                    rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    break;
                                case "3":
                                    rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    break;
                                case "4":
                                    rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
                                    break;
                                case "5":
                                    rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
                                    break;
                            }

                        }
                        if (recipe_detailsobj.getString("favourites_id") != "null") {
                            in_favourites = true;
                            favourites_id = recipe_detailsobj.getString("favourites_id");
                            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favourite_added));
                        }
                        else {
                            in_favourites = false;
                            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favourite));
                        }

                        JSONArray ingredient_details_array = jsonObject.getJSONArray("ingredient_details");
                        offline_recipe_ingredients_details = jsonObject.getJSONArray("ingredient_details").toString();

                        JSONArray instruction_details_array = jsonObject.getJSONArray("instruction_details");
                        CreateDynamicIngredientsTable(ingredient_details_array);
                        if (instruction_details_array.length() > 0) {
                            offline_recipe_instructions_details = instruction_details_array.toString();
                            CreateDynamicInstructionTable(instruction_details_array);
                        } else {
                            offline_recipe_instructions_details = "no instructions";
                            rd_show_instructions.setVisibility(View.GONE);
                            rd_ingredients_layout.setVisibility(View.VISIBLE);
                            rd_show_ingredients.setEnabled(false);
                        }
                        rd_progress_layout.setVisibility(View.GONE);
                        getWindow().setStatusBarColor(Color.TRANSPARENT);
                        rd_content_view.setVisibility(View.VISIBLE);
                    } else {
                        rd_progressbar.setVisibility(View.GONE);
                        rd_sad_icon.setVisibility(View.VISIBLE);
                        rd_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    rd_progressbar.setVisibility(View.GONE);
                    rd_sad_icon.setVisibility(View.VISIBLE);
                    rd_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rd_progressbar.setVisibility(View.GONE);
                        rd_sad_icon.setVisibility(View.VISIBLE);
                        rd_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("recipe_id", recipe_id);
                params.put("login_user_id", PreferenceData.getUserID(getApplicationContext()));

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void CreateDynamicIngredientsTable(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject ingredient_object = jsonArray.getJSONObject(i);
                TableRow item_Row = (TableRow) getLayoutInflater().inflate(R.layout.rd_ingredients_table_items, null);
                TextView ingredient_Name = item_Row.findViewById(R.id.rd_ingre_name);
                TextView ingredient_Quantity = item_Row.findViewById(R.id.rd_quantity_value);

                ingredient_Name.setText(ingredient_object.getString("ingredient_name"));
                ingredient_Quantity.setText(ingredient_object.getString("ingredient_amount"));

                rd_ingredients_layout.addView(item_Row);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void CreateDynamicInstructionTable(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                TableRow item_Row = (TableRow) getLayoutInflater().inflate(R.layout.rd_instruction_table_items, null);
                final CheckedTextView checkedTextView = item_Row.findViewById(R.id.rd_instruction_step);
                checkedTextView.setText(jsonArray.getString(i));
                checkedTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkedTextView.isChecked()) {
                            checkedTextView.setChecked(false);
                            checkedTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                        } else {
                            checkedTextView.setChecked(true);
                            checkedTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                        }
                    }
                });
                rd_instructions_layout.addView(item_Row);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*------------ App Bar On Back icon pressed action-----------*/
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

    }

    @Override
    public void onClick(View view) {
        if (view == rd_show_instructions) {
            rd_show_instructions.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            rd_show_instructions.setTextColor(ContextCompat.getColor(this, R.color.WhiteColor));
            rd_show_ingredients.setBackgroundColor(ContextCompat.getColor(this, R.color.WhiteColor));
            rd_show_ingredients.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

            rd_ingredients_layout.setVisibility(View.GONE);
            rd_instructions_layout.setVisibility(View.VISIBLE);
        } else if (view == rd_show_ingredients) {
            rd_show_ingredients.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            rd_show_ingredients.setTextColor(ContextCompat.getColor(this, R.color.WhiteColor));
            rd_show_instructions.setBackgroundColor(ContextCompat.getColor(this, R.color.WhiteColor));
            rd_show_instructions.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            rd_instructions_layout.setVisibility(View.GONE);
            rd_ingredients_layout.setVisibility(View.VISIBLE);
        } else if (view == rd_link_view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_link));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        } else if (view == rd_rate_1) {
            rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_1.setEnabled(false);
            rd_rate_2.setEnabled(false);
            rd_rate_3.setEnabled(false);
            rd_rate_4.setEnabled(false);
            rd_rate_5.setEnabled(false);
            rate_recipe("1");
        } else if (view == rd_rate_2) {
            rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_1.setEnabled(false);
            rd_rate_2.setEnabled(false);
            rd_rate_3.setEnabled(false);
            rd_rate_4.setEnabled(false);
            rd_rate_5.setEnabled(false);
            rate_recipe("2");

        } else if (view == rd_rate_3) {
            rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_1.setEnabled(false);
            rd_rate_2.setEnabled(false);
            rd_rate_3.setEnabled(false);
            rd_rate_4.setEnabled(false);
            rd_rate_5.setEnabled(false);
            rate_recipe("3");

        } else if (view == rd_rate_4) {
            rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGrey));
            rd_rate_1.setEnabled(false);
            rd_rate_2.setEnabled(false);
            rd_rate_3.setEnabled(false);
            rd_rate_4.setEnabled(false);
            rd_rate_5.setEnabled(false);
            rate_recipe("4");

        } else if (view == rd_rate_5) {
            rd_rate_1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.ratingYellow));
            rd_rate_1.setEnabled(false);
            rd_rate_2.setEnabled(false);
            rd_rate_3.setEnabled(false);
            rd_rate_4.setEnabled(false);
            rd_rate_5.setEnabled(false);
            rate_recipe("5");
        }
        else if(view == rd_user_name| view == rd_userprofile_image){
            ViewProfile();
        }
    }

    private void ViewProfile(){
        view_profile_builder = new AlertDialog.Builder(this)
                .setView(R.layout.view_profile_layout_dialog);
        view_profile_dialog = view_profile_builder.create();
        view_profile_dialog.show();
        view_profile_layout = view_profile_dialog.findViewById(R.id.view_profile_layout);
        vr_user_imageView = view_profile_dialog.findViewById(R.id.vr_user_imageView);;
        vr_user_name = view_profile_dialog.findViewById(R.id.vr_user_name);
        vr_cancel_btn = view_profile_dialog.findViewById(R.id.vr_cancel_btn);
        users_recipe_recycler_view = view_profile_dialog.findViewById(R.id.users_recipe_recycler_view);
        vr_progress_layout = view_profile_dialog.findViewById(R.id.vr_progress_layout);
        vr_progressbar = view_profile_dialog.findViewById(R.id.vr_progressbar);
        vr_progress_txt = view_profile_dialog.findViewById(R.id.vr_progress_txt);
        vr_sad_icon = view_profile_dialog.findViewById(R.id.vr_sad_icon);

        vr_user_name.setText(user_name);
        if(!user_image.equals("no image")){
            Glide.with(getApplicationContext())
                    .load("https://dinierecettes.online/images/" + user_image+ ".jpg")
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .into(vr_user_imageView);
        }

        vr_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_profile_dialog.dismiss();
            }
        });

        /*--------Your Recipes Adapter Configuration--------*/
        users_recipes_list = new ArrayList<Recipes_List>();
        userRecipesAdapter = new Your_Recipes_Adapter(users_recipes_list,getApplicationContext());
        users_recipe_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        users_recipe_recycler_view.setAdapter(userRecipesAdapter);
        Fetch_Users_Recipes(user_id);

    }

    private void Fetch_Users_Recipes(final String user_id) {
        users_recipes_list.clear();
        vr_progress_layout.setVisibility(View.VISIBLE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_VIEW_YOUR_RECIPES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        JSONArray jsonArray = jsonObject.getJSONArray("recipe_details");
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe_object = jsonArray.getJSONObject(i);
                                users_recipes_list.add(new Recipes_List(
                                        recipe_object.getString("recipe_id"),
                                        recipe_object.getString("recipe_name"),
                                        recipe_object.getString("cook_time"),
                                        recipe_object.getString("rating_number"),
                                        recipe_object.getString("rating"),
                                        recipe_object.getString("picture_of_recipe")));
                            }
                            userRecipesAdapter.notifyDataSetChanged();
                            vr_progress_layout.setVisibility(View.GONE);
                            userRecipesAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                    int position = viewHolder.getAdapterPosition();
                                    view_profile_dialog.dismiss();
                                    Fetch_Details(users_recipes_list.get(position).getRecipe_id());
                                }
                            });
                        }
                        else {
                            vr_progressbar.setVisibility(View.GONE);
                            vr_sad_icon.setVisibility(View.VISIBLE);
                            vr_progress_txt.setText(getResources().getString(R.string.no_added_recipe));
                        }
                    }
                    else {
                        vr_progressbar.setVisibility(View.GONE);
                        vr_sad_icon.setVisibility(View.VISIBLE);
                        vr_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    vr_progressbar.setVisibility(View.GONE);
                    vr_sad_icon.setVisibility(View.VISIBLE);
                    vr_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        users_recipes_list.clear();
                        vr_progressbar.setVisibility(View.GONE);
                        vr_sad_icon.setVisibility(View.VISIBLE);
                        vr_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void rate_recipe(final String rating_stars) {
        rd_rating_progressbar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RATE_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rd_rating_progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        rd_rating_numbers.setText("(" + jsonObject.getString("rating_number") + ")");
                        int rating = Integer.parseInt(jsonObject.getString("overall_rating"));
                        if (rating == 1) {
                            rd_rating_stars.setImageResource(R.drawable.one_star);
                        } else if (rating == 2) {
                            rd_rating_stars.setImageResource(R.drawable.two_stars);
                        } else if (rating == 3) {
                            rd_rating_stars.setImageResource(R.drawable.three_stars);
                        } else if (rating == 4) {
                            rd_rating_stars.setImageResource(R.drawable.four_stars);
                        } else {
                            rd_rating_stars.setImageResource(R.drawable.five_stars);
                        }
                        rd_like_it_txt.setText(getResources().getString(R.string.thanks_for_rating));
                        rd_rate_it_txt.setVisibility(View.GONE);
                    } else {
                        rd_like_it_txt.setText(getResources().getString(R.string.some_wrong));
                        rd_rate_1.setEnabled(true);
                        rd_rate_2.setEnabled(true);
                        rd_rate_3.setEnabled(true);
                        rd_rate_4.setEnabled(true);
                        rd_rate_5.setEnabled(true);
                    }

                } catch (JSONException e) {
                    rd_rating_progressbar.setVisibility(View.GONE);
                    rd_like_it_txt.setText(getResources().getString(R.string.some_wrong));
                    rd_rate_1.setEnabled(true);
                    rd_rate_2.setEnabled(true);
                    rd_rate_3.setEnabled(true);
                    rd_rate_4.setEnabled(true);
                    rd_rate_5.setEnabled(true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rd_rating_progressbar.setVisibility(View.GONE);
                        rd_like_it_txt.setText(getResources().getString(R.string.some_wrong));
                        rd_rate_1.setEnabled(true);
                        rd_rate_2.setEnabled(true);
                        rd_rate_3.setEnabled(true);
                        rd_rate_4.setEnabled(true);
                        rd_rate_5.setEnabled(true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", PreferenceData.getUserID(getApplicationContext()));
                params.put("recipe_id", current_recipe_id);
                params.put("number_of_stars", rating_stars);

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_details_menu_items, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favourite) {
            if (PreferenceData.getUserID(getApplicationContext()).equals("")) {
                /*------------ Configuration of Guest Login Dialog----------*/
                Loginbuilder = new AlertDialog.Builder(this)
                        .setView(R.layout.login_first_alert_dialog_layout);
                Guest_Login_dialog = Loginbuilder.create();
                Guest_Login_dialog.show();
                guest_login_first_btn = Guest_Login_dialog.findViewById(R.id.guest_login_first_btn);
                guest_login_first_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Guest_Login_dialog.dismiss();
                        Intent i = new Intent(Recipe_Details_Activity.this, Main2Activity.class);
                        startActivity(i);
                    }
                });
            } else {
                if (in_favourites) {
                    Remove_From_Favourites();
                } else {
                    /*------------ Configuration of Add to Favourite Dialog----------*/
                    add_to_fav_builder = new AlertDialog.Builder(this)
                            .setView(R.layout.add_tofavourite_layout_dialog);
                    add_to_fav_dialog = add_to_fav_builder.create();
                    add_to_fav_dialog.show();

                    AddToFavViewPagerAdapter FVP_adapter = new AddToFavViewPagerAdapter();
                    FVP_pager = (ViewPager) add_to_fav_dialog.findViewById(R.id.add_to_fav_pager);
                    FVP_pager.setAdapter(FVP_adapter);

                    add_to_fav_parent_layout = add_to_fav_dialog.findViewById(R.id.add_to_fav_parent_layout);
                    add_to_fav_parent_layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                    add_fav_cb_recycler_view = add_to_fav_dialog.findViewById(R.id.add_fav_cb_recycler_view);
                    add_fav_progress_layout = add_to_fav_dialog.findViewById(R.id.add_fav_progress_layout);
                    add_fav_progress_txt = add_to_fav_dialog.findViewById(R.id.add_fav_progress_txt);
                    add_fav_progressbar = add_to_fav_dialog.findViewById(R.id.add_fav_progressbar);
                    add_fav_cb_name_layout = add_to_fav_dialog.findViewById(R.id.add_fav_cb_name_layout);
                    add_fav_create_cb_btn = add_to_fav_dialog.findViewById(R.id.add_fav_create_cb_btn);
                    create_new_cb_progressbar = add_to_fav_dialog.findViewById(R.id.create_new_cb_progressbar);

                    add_to_fav_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            add_fav_cb_name_layout.getEditText().setText("");
                            add_fav_cb_name_layout.setError(null);
                            add_fav_cb_name_layout.clearFocus();
                        }
                    });
                    cbList = new ArrayList<cookbook_list>();
                    cbList.clear();
                    cookbookAdapter = new CookbookAdapter(cbList, getApplicationContext());
                    cookbookAdapter.notifyDataSetChanged();
                    add_fav_cb_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    add_fav_cb_recycler_view.setAdapter(cookbookAdapter);
                    Fetch_Cookbook();
                    Constants.HideErrorOnTyping(add_fav_cb_name_layout);
                    add_fav_create_cb_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (add_fav_cb_name_layout.getVisibility() == View.VISIBLE) {
                                if (TextUtils.isEmpty(add_fav_cb_name_layout.getEditText().getText().toString().trim())) {
                                    add_fav_cb_name_layout.setError(getResources().getString(R.string.required_field_missing));
                                } else {
                                    if (view != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    }
                                    add_fav_cb_name_layout.clearFocus();
                                    Create_Cookbook();
                                }
                            } else {
                                add_fav_cb_name_layout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        } else if (item.getItemId() == R.id.download) {
            if (PreferenceData.getUserID(getApplicationContext()).equals("")) {
                /*------------ Configuration of Guest Login Dialog----------*/
                Loginbuilder = new AlertDialog.Builder(this)
                        .setView(R.layout.login_first_alert_dialog_layout);
                Guest_Login_dialog = Loginbuilder.create();
                Guest_Login_dialog.show();
                guest_login_first_btn = Guest_Login_dialog.findViewById(R.id.guest_login_first_btn);
                guest_login_first_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Guest_Login_dialog.dismiss();
                        Intent i = new Intent(Recipe_Details_Activity.this, Main2Activity.class);
                        startActivity(i);
                    }
                });
            } else {
                if (mDatabase.checkRecipeExist(current_recipe_id,PreferenceData.getUsercontact(getApplicationContext()))) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.recipe_exist_download),Toast.LENGTH_LONG).show();
                }
                else {
                    if ((mDatabase.addRecipe(Integer.parseInt(PreferenceData.getUserID(getApplicationContext())),Integer.parseInt(current_recipe_id), offline_recipe_name, offline_recipe_cooktime, offline_recipe_num_of_serving,
                            offline_recipe_cuisine, offline_recipe_category, offline_recipe_video_link, offline_recipe_calories, offline_image, offline_recipe_ingredients_details, offline_recipe_instructions_details) == -1)) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.some_wrong), Toast.LENGTH_LONG).show();
                    } else {
                        menu.getItem(1).setVisible(false);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.recipe_download_success), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }


    /*------------- Below 4 Methods are for adding recipe to favourites -  User's Cookbook---------*/
    private void Remove_From_Favourites() {
        menu.getItem(0).setEnabled(false);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_FAVOURITES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        in_favourites = false;
                        menu.getItem(0).setEnabled(true);
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favourite));
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } else if (jsonObject.getString("status").equals("WRONG")) {
                        menu.getItem(0).setEnabled(true);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        menu.getItem(0).setEnabled(true);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.some_wrong), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    menu.getItem(0).setEnabled(true);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.some_wrong), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        menu.getItem(0).setEnabled(true);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.some_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("favourites_id", favourites_id);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void Create_Cookbook() {
        add_fav_cb_name_layout.setError(null);
        create_new_cb_progressbar.setVisibility(View.VISIBLE);
        add_fav_create_cb_btn.setEnabled(false);
        add_fav_create_cb_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLight));
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATE_CB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    create_new_cb_progressbar.setVisibility(View.GONE);
                    add_fav_create_cb_btn.setEnabled(true);
                    add_fav_create_cb_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    if (jsonObject.getString("status").equals("OK")) {
                        add_fav_cb_name_layout.getEditText().setText("");
                        FVP_pager.setCurrentItem(0);
                        Fetch_Cookbook();
                    } else if (jsonObject.getString("status").equals("EXIST")) {
                        add_fav_cb_name_layout.setError(jsonObject.getString("message"));

                    } else {
                        add_fav_cb_name_layout.setError(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    add_fav_cb_name_layout.setError(getResources().getString(R.string.some_wrong));
                    add_fav_create_cb_btn.setEnabled(true);
                    add_fav_create_cb_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    create_new_cb_progressbar.setVisibility(View.GONE);

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        add_fav_cb_name_layout.setError(getResources().getString(R.string.some_wrong));
                        add_fav_create_cb_btn.setEnabled(true);
                        add_fav_create_cb_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                        create_new_cb_progressbar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cb_name", add_fav_cb_name_layout.getEditText().getText().toString().trim());
                params.put("user_id", PreferenceData.getUserID(getApplicationContext()));
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void Fetch_Cookbook() {
        cbList.clear();
        add_fav_cb_recycler_view.setVisibility(View.GONE);
        add_fav_progress_layout.setVisibility(View.VISIBLE);
        add_fav_progressbar.setVisibility(View.VISIBLE);
        add_fav_progress_txt.setText(getResources().getString(R.string.fetch_your_cb));
        add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_CB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("cookbook_list");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cb_object = jsonArray.getJSONObject(i);
                                cbList.add(new cookbook_list(
                                        cb_object.getString("cb_id"),
                                        cb_object.getString("cb_name"),
                                        cb_object.getString("number_of_recipes")));
                            }
                            cookbookAdapter.notifyDataSetChanged();
                            add_fav_progress_layout.setVisibility(View.GONE);
                            add_fav_cb_recycler_view.setVisibility(View.VISIBLE);
                            cookbookAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                    int position = viewHolder.getAdapterPosition();
                                    Add_to_Fav(cbList.get(position).getCookbook_id());
                                }
                            });
                        } else {
                            add_fav_progressbar.setVisibility(View.GONE);
                            add_fav_progress_txt.setText(getResources().getString(R.string.no_cb));
                            add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));
                        }
                    } else {
                        add_fav_progressbar.setVisibility(View.GONE);
                        add_fav_progress_txt.setText(getResources().getString(R.string.some_wrong));
                        add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));
                    }

                } catch (JSONException e) {
                    add_fav_progressbar.setVisibility(View.GONE);
                    add_fav_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cbList.clear();
                        add_fav_progressbar.setVisibility(View.GONE);
                        add_fav_progress_txt.setText(getResources().getString(R.string.some_wrong));
                        add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", PreferenceData.getUserID(getApplicationContext()));
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void Add_to_Fav(final String cb_id) {
        add_fav_cb_recycler_view.setVisibility(View.GONE);
        add_fav_progress_layout.setVisibility(View.VISIBLE);
        add_fav_progressbar.setVisibility(View.VISIBLE);
        add_fav_progress_txt.setVisibility(View.VISIBLE);
        add_fav_progress_txt.setText(getResources().getString(R.string.adding_to_cb));
        add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_TO_FAVOURITES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        favourites_id = jsonObject.getString("favourites_id");
                        add_to_fav_dialog.dismiss();
                        in_favourites = true;
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favourite_added));

                    } else if (jsonObject.getString("status").equals("EXIST")) {
                        add_fav_progressbar.setVisibility(View.GONE);
                        add_fav_progress_txt.setText(jsonObject.getString("message"));
                        add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));

                    } else {
                        add_fav_progressbar.setVisibility(View.GONE);
                        add_fav_progress_txt.setText(getResources().getString(R.string.some_wrong));
                        add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));

                    }

                } catch (JSONException e) {
                    add_fav_progressbar.setVisibility(View.GONE);
                    add_fav_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        add_fav_progressbar.setVisibility(View.GONE);
                        add_fav_progress_txt.setText(getResources().getString(R.string.some_wrong));
                        add_fav_progress_txt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkerGrey));

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cb_id", cb_id);
                params.put("user_id", PreferenceData.getUserID(getApplicationContext()));
                params.put("recipe_id", current_recipe_id);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
