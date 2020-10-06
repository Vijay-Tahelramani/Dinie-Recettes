package com.example.dinierecettes;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class explore_recipes_fragment extends Fragment {

    private RecyclerView explore_recipe_recycle_view;
    private ArrayList<Recipes_List> recipesLists;
    private Context context;
    private Explore_recipe_adapter exploreRecipeAdapter;
    private ConstraintLayout explore_progress_layout;
    private NavController HomeNavController;
    private ProgressBar explore_progressbar;
    private ImageView explore_sad_icon;
    private TextView explore_progress_txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_recipes, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Fetch_Recipes();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        HomeNavController = Navigation.findNavController(getActivity(), R.id.home_host_fragment);

        explore_recipe_recycle_view = view.findViewById(R.id.explore_recipe_recycle_view);
        explore_progress_layout = view.findViewById(R.id.explore_progress_layout);
        explore_progressbar = view.findViewById(R.id.explore_progressbar);
        explore_sad_icon = view.findViewById(R.id.explore_sad_icon);
        explore_progress_txt = view.findViewById(R.id.explore_progress_txt);

        /*--------Explore Recipes Adapter Configuration--------*/
        recipesLists = new ArrayList<Recipes_List>();
        exploreRecipeAdapter = new Explore_recipe_adapter(recipesLists,context);
        explore_recipe_recycle_view.setLayoutManager(new LinearLayoutManager(context));
        explore_recipe_recycle_view.setAdapter(exploreRecipeAdapter);


        //Fetch_Recipes();

    }

    private void Fetch_Recipes() {
        recipesLists.clear();
        explore_progress_layout.setVisibility(View.VISIBLE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_VIEW_RECIPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        JSONArray jsonArray = jsonObject.getJSONArray("recipe_details");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject recipe_object = jsonArray.getJSONObject(i);
                            recipesLists.add(new Recipes_List(
                                    recipe_object.getString("recipe_id"),
                                    recipe_object.getString("recipe_name"),
                                    recipe_object.getString("cook_time"),
                                    recipe_object.getString("rating_number"),
                                    recipe_object.getString("rating"),
                                    recipe_object.getString("picture_of_recipe"),
                                    recipe_object.getString("calories")));
                        }
                        exploreRecipeAdapter.notifyDataSetChanged();
                        explore_progress_layout.setVisibility(View.GONE);
                        exploreRecipeAdapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                int position = viewHolder.getAdapterPosition();
                                if(!PreferenceData.getUserID(context).equals("")){
                                    Constants.add_to_History(context,PreferenceData.getUserID(context),recipesLists.get(position).getRecipe_id());
                                }
                                Intent i = new Intent(context, Recipe_Details_Activity.class);
                                i.putExtra("recipe_id",recipesLists.get(position).getRecipe_id());
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        });
                    }
                    else {
                        explore_progressbar.setVisibility(View.GONE);
                        explore_sad_icon.setVisibility(View.VISIBLE);
                        explore_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    explore_progressbar.setVisibility(View.GONE);
                    explore_sad_icon.setVisibility(View.VISIBLE);
                    explore_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        recipesLists.clear();
                        explore_progressbar.setVisibility(View.GONE);
                        explore_sad_icon.setVisibility(View.VISIBLE);
                        explore_progress_txt.setText(getResources().getString(R.string.some_wrong));
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
}
