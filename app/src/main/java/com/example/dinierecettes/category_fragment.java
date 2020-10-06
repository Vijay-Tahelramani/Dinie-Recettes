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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class category_fragment extends Fragment {


    private RecyclerView category_recycle_view;
    private Context context;
    private ArrayList<CuisineOrCategory_List> categoryLists;
    private category_adapter categoryAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();

        /*--------Cuisine Adapter Configuration--------*/
        category_recycle_view = view.findViewById(R.id.category_recycle_view);
        categoryLists = new ArrayList<CuisineOrCategory_List>();
        categoryLists.clear();
        categoryAdapter = new category_adapter(categoryLists,context);
        category_recycle_view.setLayoutManager(new GridLayoutManager(context,2));
        category_recycle_view.setAdapter(categoryAdapter);

        new AttachCategoriesToRecyclerView().execute();

        categoryAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                Intent i = new Intent(getActivity(),ListOfRecipesActivity.class);
                i.putExtra("coming_from","category_fragment");
                i.putExtra("category",categoryLists.get(position).getC_name());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        });



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private class AttachCategoriesToRecyclerView extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i< Constants.category.length; i++){
                        categoryLists.add(new CuisineOrCategory_List(Constants.category[i], Constants.category_images[i]));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            });
            return "";

        }
    }

}
