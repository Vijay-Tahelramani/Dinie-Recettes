package com.example.dinierecettes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Searched_Item_Recyadapter extends RecyclerView.Adapter<Searched_Item_Recyadapter.ViewHolder> {

    private ArrayList<Ingredients_List> ingredientsArray;
    private  Context c;
    private View.OnClickListener SearchItemListener;

    public Searched_Item_Recyadapter(ArrayList<Ingredients_List> ingredientsArray, Context c) {
        this.ingredientsArray = ingredientsArray;
        this.c = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_recycler_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.item_ingredient_name.setText(ingredientsArray.get(position).getIngredientItemName());

    }

    public void setOnClickListener(View.OnClickListener clickListener){

        SearchItemListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return ingredientsArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


         TextView item_ingredient_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_ingredient_name = itemView.findViewById(R.id.item_ingredient_name);

            itemView.setTag(this);

            itemView.setOnClickListener(SearchItemListener);

        }
    }

}
