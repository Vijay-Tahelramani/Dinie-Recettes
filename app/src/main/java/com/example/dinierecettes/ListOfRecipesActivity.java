package com.example.dinierecettes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListOfRecipesActivity extends AppCompatActivity {

    String recipe_name = "", category = "", cuisine = "", cook_time = "",rating_filter = "",cb_id="";
    private ConstraintLayout lor_progress_layout;
    private ProgressBar lor_progressbar;
    private ImageView lor_sad_icon;
    private TextView lor_progress_txt;
    private RecyclerView lor_recyclerView;
    private Toolbar lor_toolbar;
    private ArrayList<Recipes_List> lorArrayList;
    private ArrayList<Ingredients_List> ingredients_data_list;
    private JSONArray sbi_ingredient_idsArray;
    private ListOfRecipesAdapter listOfRecipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_recipes);

        /*-------- App Bar Configuration-------*/
        lor_toolbar = findViewById(R.id.lor_toolbar);
        setSupportActionBar(lor_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*-----------Connection reference between layot views and Class variables------------*/
        lor_progress_layout = findViewById(R.id.lor_progress_layout);
        lor_progressbar = findViewById(R.id.lor_progressbar);
        lor_sad_icon = findViewById(R.id.lor_sad_icon);
        lor_progress_txt = findViewById(R.id.lor_progress_txt);
        lor_recyclerView = findViewById(R.id.lor_recyclerView);

        /*--------List Of Recipes Adapter Configuration--------*/
        lorArrayList = new ArrayList<Recipes_List>();
        listOfRecipesAdapter = new ListOfRecipesAdapter(lorArrayList,getApplicationContext());
        lor_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        lor_recyclerView.setAdapter(listOfRecipesAdapter);

        if(getIntent().getStringExtra("coming_from").equals("search_by_name")){
            Bundle search_data = getIntent().getBundleExtra("search_data");
            recipe_name = search_data.getString("recipe_name");
            category = search_data.getString("category");
            cuisine = search_data.getString("cuisine");
            cook_time = search_data.getString("cook_time");
            rating_filter = search_data.getString("rating_filter");
        }
        else if(getIntent().getStringExtra("coming_from").equals("search_by_ingredients"))
        {
            Bundle search_data = getIntent().getBundleExtra("search_data");
            ingredients_data_list = search_data.getParcelableArrayList("selected ingredients");
            sbi_ingredient_idsArray = new JSONArray();
            for (int i=0;i<ingredients_data_list.size();i++){
                sbi_ingredient_idsArray.put(ingredients_data_list.get(i).getIngredientItemId());
            }

        }
        else if(getIntent().getStringExtra("coming_from").equals("cookbook_fragment")){
            cb_id = getIntent().getStringExtra("cb_id");
            lor_toolbar.setTitle(getIntent().getStringExtra("cb_name"));
        }
        else if(getIntent().getStringExtra("coming_from").equals("category_fragment")){
            category = getIntent().getStringExtra("category");
        }
        else if(getIntent().getStringExtra("coming_from").equals("cuisine_fragment")){
            cuisine = getIntent().getStringExtra("cuisine");
        }
        Fetch_Recipes();

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

    private void Fetch_Recipes() {
        lorArrayList.clear();
        lor_progress_layout.setVisibility(View.VISIBLE);
        String URL = "";
        if(getIntent().getStringExtra("coming_from").equals("search_by_name")|
                getIntent().getStringExtra("coming_from").equals("cuisine_fragment")|
                getIntent().getStringExtra("coming_from").equals("category_fragment")){
            URL = Constants.URL_VIEW_SEARCH_BY_NAME;
        }
        else if(getIntent().getStringExtra("coming_from").equals("cookbook_fragment")){
            URL = Constants.URL_FETCH_FAVOURITES;
        }
        else if(getIntent().getStringExtra("coming_from").equals("search_by_ingredients"))
        {
            URL = Constants.URL_VIEW_SEARCH_BY_INGREDIENTS;
        }
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        JSONArray jsonArray = jsonObject.getJSONArray("recipe_details");
                        if(jsonArray.length()>0){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject recipe_object = jsonArray.getJSONObject(i);
                                lorArrayList.add(new Recipes_List(
                                        recipe_object.getString("recipe_id"),
                                        recipe_object.getString("recipe_name"),
                                        recipe_object.getString("cook_time"),
                                        recipe_object.getString("recipe_cuisine"),
                                        recipe_object.getString("recipe_category"),
                                        recipe_object.getString("rating_number"),
                                        recipe_object.getString("rating"),
                                        recipe_object.getString("picture_of_recipe")));
                            }
                            listOfRecipesAdapter.notifyDataSetChanged();
                            lor_progress_layout.setVisibility(View.GONE);
                            listOfRecipesAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                    int position = viewHolder.getAdapterPosition();
                                    if(!PreferenceData.getUserID(getApplicationContext()).equals("")) {
                                        Constants.add_to_History(getApplicationContext(), PreferenceData.getUserID(getApplicationContext()), lorArrayList.get(position).getRecipe_id());
                                    }
                                    Intent i = new Intent(getApplicationContext(), Recipe_Details_Activity.class);
                                    i.putExtra("recipe_id",lorArrayList.get(position).getRecipe_id());
                                    startActivity(i);
                                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                }
                            });
                        }
                        else {
                            if(getIntent().getStringExtra("coming_from").equals("cookbook_fragment")){
                                lor_progressbar.setVisibility(View.GONE);
                                lor_sad_icon.setVisibility(View.VISIBLE);
                                lor_progress_txt.setText(getResources().getString(R.string.norecipe_in_cookbook));
                            }
                            else {
                                lor_progressbar.setVisibility(View.GONE);
                                lor_sad_icon.setVisibility(View.VISIBLE);
                                lor_progress_txt.setText(getResources().getString(R.string.no_recipe_found));
                            }
                        }
                    }
                    else {
                        lor_progressbar.setVisibility(View.GONE);
                        lor_sad_icon.setVisibility(View.VISIBLE);
                        lor_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    lor_progressbar.setVisibility(View.GONE);
                    lor_sad_icon.setVisibility(View.VISIBLE);
                    lor_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lorArrayList.clear();
                        lor_progressbar.setVisibility(View.GONE);
                        lor_sad_icon.setVisibility(View.VISIBLE);
                        lor_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                if(getIntent().getStringExtra("coming_from").equals("search_by_name")){
                    params.put("recipe_name",recipe_name);
                    params.put("recipe_category",category);
                    params.put("recipe_cuisine",cuisine);
                    params.put("rating",rating_filter);
                    params.put("cook_time",cook_time);
                }
                else if(getIntent().getStringExtra("coming_from").equals("search_by_ingredients"))
                {
                    params.put("ingredient_list",sbi_ingredient_idsArray.toString());
                }
                else if(getIntent().getStringExtra("coming_from").equals("cookbook_fragment"))
                {
                    params.put("cb_id",cb_id);
                }
                else if(getIntent().getStringExtra("coming_from").equals("category_fragment")){
                    params.put("recipe_category",category);
                }
                else if(getIntent().getStringExtra("coming_from").equals("cuisine_fragment")){
                    params.put("recipe_cuisine",cuisine);
                }
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

}
