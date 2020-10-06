package com.example.dinierecettes;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit_Recipe_IngredientsFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    public NavController EraNavController;
    private Context context;
    private ConstraintLayout er_ingredients_error_layout;
    private ImageButton er_ingredients_hide_error_btn;
    private TextInputLayout er_ingredients_search_layout;
    private TextInputEditText er_ingredients_search_edittxt;
    private MaterialCardView er_ingredients_search_card_view;
    private RecyclerView er_searched_recycler_view, er_selected_recycler_view;
    private ArrayList<Ingredients_List> searched_ArrayList;
    private ArrayList<Ingredients_List> selected_ArrayList;
    private ArrayList<Ingredients_List> Fetched_Ingredients;
    private Searched_Item_Recyadapter searchedItemRecyadapter;
    private Selected_item_Recyadapater selectedItemRecyadapater;
    private TextView er_ingredient_error_text;
    private MaterialButton er2_next_btn;
    private String recipe_data = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_recipe_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EraNavController = Navigation.findNavController(getActivity(), R.id.era_host_fragment);
        context =getActivity().getApplicationContext();

        er_ingredients_error_layout = view.findViewById(R.id.er_ingredients_error_layout);
        er_ingredient_error_text = view.findViewById(R.id.er_ingredient_error_text);
        er_ingredients_hide_error_btn = view.findViewById(R.id.er_ingredients_hide_error_btn);
        er_ingredients_search_layout = view.findViewById(R.id.er_ingredients_search_layout);
        er_ingredients_search_edittxt = view.findViewById(R.id.er_ingredients_search_edittxt);
        er_ingredients_search_card_view = view.findViewById(R.id.er_ingredients_search_card_view);
        er_searched_recycler_view = view.findViewById(R.id.er_searched_recycler_view);
        er_selected_recycler_view = view.findViewById(R.id.er_selected_recycler_view);
        er2_next_btn = view.findViewById(R.id.er2_next_btn);

        er_ingredients_hide_error_btn.setOnClickListener(this);
        er2_next_btn.setOnClickListener(this);
        er_ingredients_search_edittxt.setOnEditorActionListener(this);


        /*--------Searched Ingredients Adapter Configuration--------*/
        searched_ArrayList = new ArrayList<Ingredients_List>();
        searchedItemRecyadapter = new Searched_Item_Recyadapter(searched_ArrayList,context);
        er_searched_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        er_searched_recycler_view.setAdapter(searchedItemRecyadapter);

        /*---------Selected Ingredients Adapter Configuration---------*/
        selected_ArrayList = new ArrayList<Ingredients_List>();
        selectedItemRecyadapater = new Selected_item_Recyadapater(selected_ArrayList,context);
        er_selected_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        er_selected_recycler_view.setAdapter(selectedItemRecyadapater);

        if(getActivity().getIntent()!=null){
            recipe_data = getActivity().getIntent().getStringExtra("recipe_response");
            try {
                JSONObject jsonObject = new JSONObject(recipe_data);
                JSONArray ingredient_details_array = jsonObject.getJSONArray("ingredient_details");
                for(int i=0;i<ingredient_details_array.length();i++){
                    JSONObject ingredient_object = ingredient_details_array.getJSONObject(i);
                    selected_ArrayList.add(new Ingredients_List(ingredient_object.getString("ingredient_name"),ingredient_object.getString("ingredient_id"),ingredient_object.getString("ingredient_amount")));
                }
                selectedItemRecyadapater.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*--------------- On Typing Perform Search Operation----------*/
        er_ingredients_search_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    if(TextUtils.isEmpty(charSequence)){
                        searched_ArrayList.clear();
                        searchedItemRecyadapter.notifyDataSetChanged();
                        er_ingredients_search_card_view.setVisibility(View.GONE);
                    }
                    else {
                        fetch_ingredients(charSequence);
                    }
                }
                else {
                    searched_ArrayList.clear();
                    searchedItemRecyadapter.notifyDataSetChanged();
                    er_ingredients_search_card_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void fetch_ingredients(final CharSequence charSequence){
        searched_ArrayList.clear();
        searchedItemRecyadapter.notifyDataSetChanged();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEARCH_INGREDIENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        searched_ArrayList.clear();
                        searchedItemRecyadapter.notifyDataSetChanged();
                        JSONArray jsonArray = jsonObject.getJSONArray("ingredients_details");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject ingredient_object = jsonArray.getJSONObject(i);
                            searched_ArrayList.add(new Ingredients_List(ingredient_object.getString("name"),ingredient_object.getString("id")));
                        }
                        searchedItemRecyadapter.notifyDataSetChanged();
                        er_ingredients_search_card_view.setVisibility(View.VISIBLE);
                        searchedItemRecyadapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                er_ingredients_search_card_view.setVisibility(View.GONE);

                                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                int position = viewHolder.getAdapterPosition();
                                String name = searched_ArrayList.get(position).getIngredientItemName();
                                String id = searched_ArrayList.get(position).getIngredientItemId();
                                selected_ArrayList.add(new Ingredients_List(name,id));
                                selectedItemRecyadapater.notifyDataSetChanged();
                            }
                        });
                    }
                    else {
                        er_ingredients_search_card_view.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        selected_ArrayList.clear();
                        searchedItemRecyadapter.notifyDataSetChanged();
                        er_ingredient_error_text.setText(getResources().getString(R.string.check_internet));
                        er_ingredients_error_layout.setVisibility(View.VISIBLE);
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
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if (er_ingredients_search_layout.hasFocus()) {
            er_ingredients_search_layout.clearFocus();
            er_ingredients_search_card_view.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view == er_ingredients_hide_error_btn){
            er_ingredients_error_layout.setVisibility(View.GONE);
        }
        else if(view==er2_next_btn){
            if(selected_ArrayList.size()>0){
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("selected ingredients", selected_ArrayList);
                EraNavController.navigate(R.id.action_edit_Recipe_IngredientsFragment_to_edit_Recipe_Ingre_Quantity_Fragment,bundle);
            }
            else {
                er_ingredient_error_text.setText(getResources().getString(R.string.selecct_one_ingredient));
                er_ingredients_error_layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
