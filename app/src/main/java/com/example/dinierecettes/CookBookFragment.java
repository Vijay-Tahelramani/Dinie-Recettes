package com.example.dinierecettes;


import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CookBookFragment extends Fragment implements View.OnClickListener {

    private RecyclerView cb_recycler_view;
    private ConstraintLayout cb_progress_layout, add_cb_layout;
    private ProgressBar cb_progressbar, add_cb_progressbar;
    private ImageView cb_sad_icon;
    private TextView cb_progress_txt;
    private Context context;
    private ArrayList<cookbook_list> cookbookList;
    private CookbookAdapter cookbookAdapter;
    private View add_cb_background_view;
    private MaterialCardView add_cb_card;
    private TextInputLayout add_cb_name_layout;
    private MaterialButton create_cb_btn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cook_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();

        cb_progress_layout = view.findViewById(R.id.cb_progress_layout);
        cb_progressbar = view.findViewById(R.id.cb_progressbar);
        cb_sad_icon = view.findViewById(R.id.cb_sad_icon);
        cb_progress_txt = view.findViewById(R.id.cb_progress_txt);
        add_cb_layout = view.findViewById(R.id.add_cb_layout);
        add_cb_background_view = view.findViewById(R.id.add_cb_background_view);
        add_cb_card = view.findViewById(R.id.add_cb_card);
        create_cb_btn = view.findViewById(R.id.create_cb_btn);
        add_cb_name_layout = view.findViewById(R.id.add_cb_name_layout);
        add_cb_progressbar = view.findViewById(R.id.add_cb_progressbar);

        /*---------------- Apply animation to add create book layout------------*/
        add_cb_layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        add_cb_background_view.setOnClickListener(this);
        create_cb_btn.setOnClickListener(this);

        Constants.HideErrorOnTyping(add_cb_name_layout);


        /*--------Cuisine Adapter Configuration--------*/
        cb_recycler_view = view.findViewById(R.id.cb_recycler_view);
        cookbookList = new ArrayList<cookbook_list>();
        cookbookList.clear();
        cookbookAdapter = new CookbookAdapter(cookbookList, context);
        cookbookAdapter.notifyDataSetChanged();
        cb_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        cb_recycler_view.setAdapter(cookbookAdapter);

        Fetch_Cookbook();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_btn_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.plus_icon_btn) {
            add_cb_card.setVisibility(View.VISIBLE);
            add_cb_layout.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == add_cb_background_view) {
            add_cb_name_layout.getEditText().setText("");
            add_cb_name_layout.clearFocus();
            HideKeyboard(view);
            add_cb_card.setVisibility(View.GONE);
            add_cb_layout.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT, 0));
        } else if (view == create_cb_btn) {
            if (TextUtils.isEmpty(add_cb_name_layout.getEditText().getText().toString())) {
                add_cb_name_layout.setError(getResources().getString(R.string.required_field_missing));
            } else {
                add_cb_name_layout.clearFocus();
                Create_Cookbook();
            }
        }
    }

    private void Create_Cookbook() {
        add_cb_progressbar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATE_CB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                add_cb_progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        add_cb_name_layout.getEditText().setText("");
                        HideKeyboard(getView());
                        add_cb_card.setVisibility(View.GONE);
                        add_cb_layout.setLayoutParams(new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT, 0));
                        Fetch_Cookbook();
                    } else if (jsonObject.getString("status").equals("EXIST")) {
                        add_cb_name_layout.setError(jsonObject.getString("message"));
                    } else {
                        add_cb_name_layout.setError(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    add_cb_progressbar.setVisibility(View.GONE);
                    add_cb_name_layout.setError(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        add_cb_progressbar.setVisibility(View.GONE);
                        add_cb_name_layout.setError(getResources().getString(R.string.some_wrong));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cb_name", add_cb_name_layout.getEditText().getText().toString().trim());
                params.put("user_id", PreferenceData.getUserID(context));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void Fetch_Cookbook() {
        cookbookList.clear();
        cb_progress_layout.setVisibility(View.VISIBLE);
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
                                cookbookList.add(new cookbook_list(
                                        cb_object.getString("cb_id"),
                                        cb_object.getString("cb_name"),
                                        cb_object.getString("number_of_recipes")));
                            }
                            cookbookAdapter.notifyDataSetChanged();
                            cb_progress_layout.setVisibility(View.GONE);
                            cookbookAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                    int position = viewHolder.getAdapterPosition();

                                    Intent i = new Intent(getActivity(), ListOfRecipesActivity.class);
                                    i.putExtra("coming_from","cookbook_fragment");
                                    i.putExtra("cb_id",cookbookList.get(position).getCookbook_id());
                                    i.putExtra("cb_name",cookbookList.get(position).getCookbook_name());
                                    startActivity(i);
                                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                }
                            });
                        }
                        else {
                            cb_progressbar.setVisibility(View.GONE);
                            cb_sad_icon.setVisibility(View.GONE);
                            cb_progress_txt.setText(getResources().getString(R.string.hit_plus_icon));
                        }
                    } else {
                        cb_progressbar.setVisibility(View.GONE);
                        cb_sad_icon.setVisibility(View.VISIBLE);
                        cb_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    cb_progressbar.setVisibility(View.GONE);
                    cb_sad_icon.setVisibility(View.VISIBLE);
                    cb_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cookbookList.clear();
                        cb_progressbar.setVisibility(View.GONE);
                        cb_sad_icon.setVisibility(View.VISIBLE);
                        cb_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", PreferenceData.getUserID(context));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void HideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
