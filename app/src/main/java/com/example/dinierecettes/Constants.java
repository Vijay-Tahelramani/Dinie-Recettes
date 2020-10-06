package com.example.dinierecettes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

public class Constants {

    private static final String ROOT_URL = "https://dinierecettes.online/";
    public static final String URL_REGISTER = ROOT_URL+"register.php";
    public static final String URL_LOGIN = ROOT_URL+"login.php";
    public static final String URL_EMAIL_CHECK = ROOT_URL+"email_verification.php";
    public static final String URL_REGISTER_OTP = ROOT_URL+"register_otp.php";
    public static final String URL_SEARCH_INGREDIENTS = ROOT_URL+"fetch_ingredients.php";
    public static final String URL_ADD_RECIPE = ROOT_URL+"add_recipe.php";
    public static final String URL_FORGOT_PASS = ROOT_URL+"forgot.php";
    public static final String URL_RESET_PASS = ROOT_URL+"setpassword.php";
    public static final String URL_VIEW_RECIPE = ROOT_URL+"view_recipe.php";
    public static final String URL_VIEW_RECIPE_DETAILS = ROOT_URL+"view_recipe_details.php";
    public static final String URL_VIEW_YOUR_RECIPES = ROOT_URL+"your_recipe.php";
    public static final String URL_VIEW_SEARCH_BY_NAME = ROOT_URL+"search_by_name.php";
    public static final String URL_VIEW_SEARCH_BY_INGREDIENTS = ROOT_URL+"search_by_ingredients.php";
    public static final String URL_RATE_RECIPE = ROOT_URL+"recipe_rating.php";
    public static final String URL_CREATE_CB = ROOT_URL+"create_cookbook.php";
    public static final String URL_FETCH_CB = ROOT_URL+"fetch_cookbook.php";
    public static final String URL_FETCH_FAVOURITES = ROOT_URL+"fetch_favourites.php";
    public static final String URL_ADD_TO_FAVOURITES = ROOT_URL+"add_to_favourites.php";
    public static final String URL_DELETE_FAVOURITES = ROOT_URL+"delete_favourites.php";
    public static final String URL_DELETE_RECIPE = ROOT_URL+"delete_recipe.php";
    public static final String URL_FETCH_HISTORY = ROOT_URL+"fetch_history.php";
    public static final String URL_ADD_HISTORY = ROOT_URL+"history_details.php";
    public static final String URL_DELETE_ACCOUNT = ROOT_URL+"delete_account.php";
    public static final String URL_EDIT_PROFILE = ROOT_URL+"edit_profile.php";
    public static final String URL_EDIT_RECIPE = ROOT_URL+"edit_recipe.php";


    public static final String[] quantity_unit_array= { "tsp","tbsp","unit","fl oz","cup","pint","quart","gallon","ml","L","dL","lb","oz","mg","g","kg" };
    public static final String[] category = { "Appetizer","Breads","Breakfast","Drinks","Desserts","Gluten free","Main course",
            "Salad","Sauce","Sea food","Side dish","Soup","Vegan","Vegetarian" };

    public static final int[] category_images = {R.drawable.appetizer,R.drawable.breads,R.drawable.breakfast,R.drawable.drinks,R.drawable.desserts,
            R.drawable.gluten_free,R.drawable.main_course,R.drawable.salad,R.drawable.sauce,R.drawable.sea_food,
            R.drawable.side_dish, R.drawable.soup,R.drawable.vegan,R.drawable.vegetarian};
    public static final String[] cuisine = { "African","American","BBQ","Brazilian","Caribbean ","Chinese","French","Fusion","German","Greek",
            "Indian","Italian","Japanese","Korean","Latin America","Mexican","Middle eastern","Thai","Vietnamese" };

    public static final int [] cuisine_images = {R.drawable.african,R.drawable.american,R.drawable.bbq,R.drawable.brazilian,R.drawable.caribbean,
            R.drawable.chinese,R.drawable.french,R.drawable.fusion,R.drawable.german,R.drawable.greek,R.drawable.indian,R.drawable.italian,
            R.drawable.japanese,R.drawable.korean,R.drawable.latin_american,R.drawable.mexican,R.drawable.middle_eastern,R.drawable.thai,R.drawable.vietnamese};

    public static void HideErrorOnTyping(final TextInputLayout layout){
        layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    layout.setError(null);
                    layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static boolean isValidPassword(String passwordhere) {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        boolean flag=true;

        if (passwordhere.length() < 8) {
            flag=false;
        }
        if (!specailCharPatten.matcher(passwordhere).find()) {
            flag=false;
        }
        if (!UpperCasePatten.matcher(passwordhere).find()) {
            flag=false;
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            flag=false;
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            flag=false;
        }

        return flag;

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void add_to_History(final Context context, final String user_id, final String recipe_id){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){

                    }

                } catch (JSONException e) {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("recipe_id",recipe_id);
                params.put("history_date",new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
