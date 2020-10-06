package com.example.dinierecettes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchByIngredientsFragment extends Fragment implements View.OnClickListener{


    private RecyclerView sbi_selected_recycler_view, sbi_searched_recycler_view;
    private MaterialButton search_by_ingredient_btn;
    private MaterialCardView sbi_card_view;
    private ImageButton sbi_hide_error_btn;
    private ConstraintLayout sbi_error_layout;
    private Context context;
    private ArrayList<Ingredients_List> sbi_searched_ArrayList;
    private ArrayList<Ingredients_List> sbi_selected_ArrayList;
    private Searched_Item_Recyadapter searchedItemRecyadapter;
    private Selected_item_Recyadapater selectedItemRecyadapater;
    private TextView sbi_error_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_by_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();

        sbi_selected_recycler_view = view.findViewById(R.id.sbi_selected_recycler_view);
        sbi_searched_recycler_view = view.findViewById(R.id.sbi_searched_recycler_view);
        search_by_ingredient_btn = view.findViewById(R.id.search_by_ingredient_btn);
        sbi_card_view = view.findViewById(R.id.sbi_card_view);
        sbi_hide_error_btn = view.findViewById(R.id.sbi_hide_error_btn);
        sbi_error_layout = view.findViewById(R.id.sbi_error_layout);
        sbi_error_text = view.findViewById(R.id.sbi_error_text);

        search_by_ingredient_btn.setOnClickListener(this);
        sbi_hide_error_btn.setOnClickListener(this);

        /*--------Searched Ingredients Adapter Configuration--------*/
        sbi_searched_ArrayList = new ArrayList<Ingredients_List>();
        searchedItemRecyadapter = new Searched_Item_Recyadapter(sbi_searched_ArrayList,context);
        sbi_searched_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        sbi_searched_recycler_view.setAdapter(searchedItemRecyadapter);

        /*---------Selected Ingredients Adapter Configuration---------*/
        sbi_selected_ArrayList = new ArrayList<Ingredients_List>();
        selectedItemRecyadapater = new Selected_item_Recyadapater(sbi_selected_ArrayList,context);
        sbi_selected_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        sbi_selected_recycler_view.setAdapter(selectedItemRecyadapater);

        /*--------------- On Typing Perform Search Operation----------*/
        SearchActivity.search_recipe_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    if(TextUtils.isEmpty(charSequence)){
                        sbi_searched_ArrayList.clear();
                        searchedItemRecyadapter.notifyDataSetChanged();
                        sbi_card_view.setVisibility(View.GONE);
                    }
                    else {
                        fetch_ingredients(charSequence);
                    }
                }
                else {
                    sbi_searched_ArrayList.clear();
                    searchedItemRecyadapter.notifyDataSetChanged();
                    sbi_card_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void fetch_ingredients(final CharSequence charSequence){
        sbi_searched_ArrayList.clear();
        searchedItemRecyadapter.notifyDataSetChanged();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEARCH_INGREDIENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        sbi_searched_ArrayList.clear();
                        searchedItemRecyadapter.notifyDataSetChanged();
                        JSONArray jsonArray = jsonObject.getJSONArray("ingredients_details");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject ingredient_object = jsonArray.getJSONObject(i);
                            sbi_searched_ArrayList.add(new Ingredients_List(ingredient_object.getString("name"),ingredient_object.getString("id")));
                        }
                        searchedItemRecyadapter.notifyDataSetChanged();
                        sbi_card_view.setVisibility(View.VISIBLE);
                        searchedItemRecyadapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sbi_card_view.setVisibility(View.GONE);

                                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                int position = viewHolder.getAdapterPosition();
                                String name = sbi_searched_ArrayList.get(position).getIngredientItemName();
                                String id = sbi_searched_ArrayList.get(position).getIngredientItemId();
                                sbi_selected_ArrayList.add(new Ingredients_List(name,id));
                                selectedItemRecyadapater.notifyDataSetChanged();
                            }
                        });
                    }
                    else {
                        sbi_card_view.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sbi_selected_ArrayList.clear();
                        searchedItemRecyadapter.notifyDataSetChanged();
                        sbi_error_text.setText(getResources().getString(R.string.check_internet));
                        sbi_error_layout.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("ingredient_name",charSequence.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view==sbi_hide_error_btn){
            sbi_error_layout.setVisibility(View.GONE);
        }
        else if(view==search_by_ingredient_btn){
            if(sbi_selected_ArrayList.size()>0){
                Bundle ingredients_bundle = new Bundle();
                ingredients_bundle.putParcelableArrayList("selected ingredients", sbi_selected_ArrayList);
                Intent i = new Intent(getActivity(),ListOfRecipesActivity.class);
                i.putExtra("coming_from","search_by_ingredients");
                i.putExtra("search_data",ingredients_bundle);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
            else {
                sbi_error_layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
