package com.example.dinierecettes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Offline_Recipe_Fragment extends Fragment {

    private RecyclerView offline_recyclerView;
    private Context context;
    private ArrayList<Recipes_List> OfflineArrayList;
    private ListOfRecipesAdapter OfflineRecipesAdapter;
    private DatabaseHandler mDatabase;
    private TextView no_offline_recipe_txt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        offline_recyclerView = view.findViewById(R.id.offline_recyclerView);
        no_offline_recipe_txt = view.findViewById(R.id.no_offline_recipe_txt);

        mDatabase = new DatabaseHandler(context);
        mDatabase.getReadableDatabase();

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllRecipes();
    }

    private void getAllRecipes(){

        /*--------List Of Recipes Adapter Configuration--------*/
        //OfflineArrayList = new ArrayList<Recipes_List>();
        OfflineArrayList = mDatabase.getAllRecipes(PreferenceData.getUserID(context));
        if(OfflineArrayList.size()>0) {
            no_offline_recipe_txt.setVisibility(View.GONE);
            OfflineRecipesAdapter = new ListOfRecipesAdapter(context, OfflineArrayList, true);
            OfflineRecipesAdapter.notifyDataSetChanged();
            offline_recyclerView.setLayoutManager(new LinearLayoutManager(context));
            offline_recyclerView.setAdapter(OfflineRecipesAdapter);
            OfflineRecipesAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    Intent i = new Intent(context, Your_recipe_detailsActivity.class);
                    i.putExtra("offline_recipe_id", String.valueOf(OfflineArrayList.get(position).getOffline_recipe_id()));
                    i.putExtra("coming_from","Offline");
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
            });
        }
        else {
            offline_recyclerView.setVisibility(View.GONE);
            no_offline_recipe_txt.setVisibility(View.VISIBLE);
        }
    }
}
