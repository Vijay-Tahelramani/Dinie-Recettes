package com.example.dinierecettes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YourRecipesFragment extends Fragment implements View.OnClickListener {

    public NavController HomeNavController;
    private Context context;
    private FloatingActionButton create_recipe_btn;
    private RecyclerView your_recipes_recycler_view;
    private ArrayList<Recipes_List> your_recipes_list;
    private ConstraintLayout yr_progress_layout;
    private ImageView yr_sad_icon;
    private ProgressBar yr_progressbar;
    private TextView yr_progress_txt;

    private Your_Recipes_Adapter yourRecipesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context =getActivity().getApplicationContext();

        HomeNavController = Navigation.findNavController(getActivity(),R.id.home_host_fragment);
        create_recipe_btn = view.findViewById(R.id.create_recipe_btn);
        create_recipe_btn.setOnClickListener(this);
        your_recipes_recycler_view = view.findViewById(R.id.your_recipes_recycler_view);
        yr_progress_layout = view.findViewById(R.id.yr_progress_layout);
        yr_progressbar = view.findViewById(R.id.yr_progressbar);
        yr_sad_icon = view.findViewById(R.id.yr_sad_icon);
        yr_progress_txt = view.findViewById(R.id.yr_progress_txt);

        /*--------Your Recipes Adapter Configuration--------*/
        your_recipes_list = new ArrayList<Recipes_List>();
        yourRecipesAdapter = new Your_Recipes_Adapter(your_recipes_list,context);
        your_recipes_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        your_recipes_recycler_view.setAdapter(yourRecipesAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Fetch_Recipes();
    }

    private void Fetch_Recipes() {
        your_recipes_list.clear();
        yr_progress_layout.setVisibility(View.VISIBLE);

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
                                your_recipes_list.add(new Recipes_List(
                                        recipe_object.getString("recipe_id"),
                                        recipe_object.getString("recipe_name"),
                                        recipe_object.getString("cook_time"),
                                        recipe_object.getString("rating_number"),
                                        recipe_object.getString("rating"),
                                        recipe_object.getString("picture_of_recipe")));
                            }
                            yourRecipesAdapter.notifyDataSetChanged();
                            yr_progress_layout.setVisibility(View.GONE);
                            yourRecipesAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                    int position = viewHolder.getAdapterPosition();
                                    Intent i = new Intent(context, Your_recipe_detailsActivity.class);
                                    i.putExtra("recipe_id", your_recipes_list.get(position).getRecipe_id());
                                    i.putExtra("coming_from","YourRecipes");
                                    startActivity(i);
                                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                }
                            });
                        }
                        else {
                            yr_progressbar.setVisibility(View.GONE);
                            yr_sad_icon.setVisibility(View.VISIBLE);
                            yr_progress_txt.setText(getResources().getString(R.string.no_added_recipe));
                        }
                    }
                    else {
                        yr_progressbar.setVisibility(View.GONE);
                        yr_sad_icon.setVisibility(View.VISIBLE);
                        yr_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    yr_progressbar.setVisibility(View.GONE);
                    yr_sad_icon.setVisibility(View.VISIBLE);
                    yr_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        your_recipes_list.clear();
                        yr_progressbar.setVisibility(View.GONE);
                        yr_sad_icon.setVisibility(View.VISIBLE);
                        yr_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",PreferenceData.getUserID(context));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if(view==create_recipe_btn){
            HomeNavController.navigate(R.id.action_yourRecipesFragment_to_addRecipeFragment);
        }

    }
}
