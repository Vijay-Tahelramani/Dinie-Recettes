package com.example.dinierecettes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Cuisine_Fragment extends Fragment {

    private RecyclerView cuisine_recycler_view;
    private Context context;
    private ArrayList<CuisineOrCategory_List> cuisineLists;
    private Cuisine_adapter cuisineAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cuisine, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();

        /*--------Cuisine Adapter Configuration--------*/
        cuisine_recycler_view = view.findViewById(R.id.cuisine_recycler_view);
        cuisineLists = new ArrayList<CuisineOrCategory_List>();
        cuisineLists.clear();
        cuisineAdapter = new Cuisine_adapter(cuisineLists,context);
        cuisineAdapter.notifyDataSetChanged();
        cuisine_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        cuisine_recycler_view.setAdapter(cuisineAdapter);

        new AttachCuisinesToRecyclerView().execute();
        cuisineAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                Intent i = new Intent(getActivity(),ListOfRecipesActivity.class);
                i.putExtra("coming_from","cuisine_fragment");
                i.putExtra("cuisine",cuisineLists.get(position).getC_name());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });

    }

    private class AttachCuisinesToRecyclerView extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i< Constants.cuisine.length; i++){
                        cuisineLists.add(new CuisineOrCategory_List(Constants.cuisine[i], Constants.cuisine_images[i]));
                    }
                    cuisineAdapter.notifyDataSetChanged();
                }
            });
            return "";

        }
    }
}
