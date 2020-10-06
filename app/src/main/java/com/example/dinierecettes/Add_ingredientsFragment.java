package com.example.dinierecettes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;

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
import java.util.regex.Pattern;


public class Add_ingredientsFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    public NavController HomeNavController;
    private Context context;
    private ConstraintLayout add_ingredients_error_layout;
    private ImageButton add_ingredients_hide_error_btn;
    private TextInputLayout add_ingredients_search_layout;
    private TextInputEditText add_ingredients_search_edittxt;
    private MaterialCardView add_ingredients_search_card_view;
    private RecyclerView add_searched_recycler_view, add_selected_recycler_view;
    private ArrayList<Ingredients_List> searched_ArrayList;
    private ArrayList<Ingredients_List> selected_ArrayList;
    private ArrayList<Ingredients_List> Fetched_Ingredients;
    private Searched_Item_Recyadapter searchedItemRecyadapter;
    private Selected_item_Recyadapater selectedItemRecyadapater;
    private TextView ingredient_error_text;
    private MaterialButton add_recipe2_next_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context =getActivity().getApplicationContext();

        HomeNavController = Navigation.findNavController(getActivity(),R.id.home_host_fragment);

        add_ingredients_error_layout = view.findViewById(R.id.add_ingredients_error_layout);
        ingredient_error_text = view.findViewById(R.id.ingredient_error_text);
        add_ingredients_hide_error_btn = view.findViewById(R.id.add_ingredients_hide_error_btn);
        add_ingredients_search_layout = view.findViewById(R.id.add_ingredients_search_layout);
        add_ingredients_search_edittxt = view.findViewById(R.id.add_ingredients_search_edittxt);
        add_ingredients_search_card_view = view.findViewById(R.id.add_ingredients_search_card_view);
        add_searched_recycler_view = view.findViewById(R.id.add_searched_recycler_view);
        add_selected_recycler_view = view.findViewById(R.id.add_selected_recycler_view);
        add_recipe2_next_btn = view.findViewById(R.id.add_recipe2_next_btn);

        add_ingredients_hide_error_btn.setOnClickListener(this);
        add_recipe2_next_btn.setOnClickListener(this);
        add_ingredients_search_edittxt.setOnEditorActionListener(this);


        /*--------Searched Ingredients Adapter Configuration--------*/
        searched_ArrayList = new ArrayList<Ingredients_List>();
        searchedItemRecyadapter = new Searched_Item_Recyadapter(searched_ArrayList,context);
        add_searched_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        add_searched_recycler_view.setAdapter(searchedItemRecyadapter);

        /*---------Selected Ingredients Adapter Configuration---------*/
        selected_ArrayList = new ArrayList<Ingredients_List>();
        selectedItemRecyadapater = new Selected_item_Recyadapater(selected_ArrayList,context);
        add_selected_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        add_selected_recycler_view.setAdapter(selectedItemRecyadapater);




        /*--------------- On Typing Perform Search Operation----------*/
        add_ingredients_search_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    if(TextUtils.isEmpty(charSequence)){
                        searched_ArrayList.clear();
                        searchedItemRecyadapter.notifyDataSetChanged();
                        add_ingredients_search_card_view.setVisibility(View.GONE);
                    }
                    else {
                        fetch_ingredients(charSequence);
                    }
                }
                else {
                    searched_ArrayList.clear();
                    searchedItemRecyadapter.notifyDataSetChanged();
                    add_ingredients_search_card_view.setVisibility(View.GONE);
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
                        add_ingredients_search_card_view.setVisibility(View.VISIBLE);
                        searchedItemRecyadapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                add_ingredients_search_card_view.setVisibility(View.GONE);

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
                        add_ingredients_search_card_view.setVisibility(View.GONE);
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
                        ingredient_error_text.setText(getResources().getString(R.string.check_internet));
                        add_ingredients_error_layout.setVisibility(View.VISIBLE);
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if (add_ingredients_search_layout.hasFocus()) {
            add_ingredients_search_layout.clearFocus();
            add_ingredients_search_card_view.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view == add_ingredients_hide_error_btn){
            add_ingredients_error_layout.setVisibility(View.GONE);
        }
        else if(view==add_recipe2_next_btn){
            if(selected_ArrayList.size()>0){
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("selected ingredients", selected_ArrayList);
                HomeNavController.navigate(R.id.action_add_ingredientsFragment_to_add_Ingre_Quantity_Fragment,bundle);
            }
            else {
                ingredient_error_text.setText(getResources().getString(R.string.selecct_one_ingredient));
                add_ingredients_error_layout.setVisibility(View.VISIBLE);
            }
        }

    }
}
