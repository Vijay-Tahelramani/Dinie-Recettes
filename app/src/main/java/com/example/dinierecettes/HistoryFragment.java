package com.example.dinierecettes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HistoryFragment extends Fragment {

    private Context context;
    private TextView history_progress_txt;
    private RecyclerView history_recyclerView;
    private ConstraintLayout history_progress_layout;
    private ProgressBar history_progressbar;
    private ImageView history_sad_icon;
    private ArrayList<History_List> history_lists;
    private HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();

        history_recyclerView = view.findViewById(R.id.history_recyclerView);
        history_progress_layout = view.findViewById(R.id.history_progress_layout);
        history_progressbar = view.findViewById(R.id.history_progressbar);
        history_sad_icon = view.findViewById(R.id.history_sad_icon);
        history_progress_txt = view.findViewById(R.id.history_progress_txt);

        /*--------History Recipes Adapter Configuration--------*/
        history_lists = new ArrayList<History_List>();
        historyAdapter = new HistoryAdapter(history_lists,context);
        history_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        history_recyclerView.setAdapter(historyAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        getHistory();
    }

    private void getHistory(){
        history_lists.clear();
        history_progress_layout.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        JSONArray jsonArray = jsonObject.getJSONArray("recipe_details");
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe_object = jsonArray.getJSONObject(i);
                                history_lists.add(new History_List(
                                        recipe_object.getString("history_id"),
                                        recipe_object.getString("recipe_id"),
                                        recipe_object.getString("recipe_name"),
                                        recipe_object.getString("history_date"),
                                        recipe_object.getString("picture_of_recipe")));
                            }
                            historyAdapter.notifyDataSetChanged();
                            history_progress_layout.setVisibility(View.GONE);
                            historyAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                                    int position = viewHolder.getAdapterPosition();

                                    Intent i = new Intent(context, Recipe_Details_Activity.class);
                                    i.putExtra("recipe_id", history_lists.get(position).getRecipe_id());
                                    startActivity(i);
                                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                }
                            });
                        }
                        else {
                            history_progressbar.setVisibility(View.GONE);
                            history_sad_icon.setVisibility(View.GONE);
                            history_progress_txt.setText(getResources().getString(R.string.no_hitory_recipes));
                        }
                    }
                    else {
                        history_progressbar.setVisibility(View.GONE);
                        history_sad_icon.setVisibility(View.VISIBLE);
                        history_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }

                } catch (JSONException e) {
                    history_progressbar.setVisibility(View.GONE);
                    history_sad_icon.setVisibility(View.VISIBLE);
                    history_progress_txt.setText(getResources().getString(R.string.some_wrong));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        history_lists.clear();
                        history_progressbar.setVisibility(View.GONE);
                        history_sad_icon.setVisibility(View.VISIBLE);
                        history_progress_txt.setText(getResources().getString(R.string.some_wrong));
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", PreferenceData.getUserID(context));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
