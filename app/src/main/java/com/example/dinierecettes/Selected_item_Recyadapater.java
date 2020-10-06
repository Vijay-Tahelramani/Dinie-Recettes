package com.example.dinierecettes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Selected_item_Recyadapater extends RecyclerView.Adapter<Selected_item_Recyadapater.ViewHolder> {

    private ArrayList<Ingredients_List> selected_Ingredients_array;
    private Context c;
    private View.OnClickListener SelectedItemListener;

    public Selected_item_Recyadapater(ArrayList<Ingredients_List> selected_Ingredients_array, Context c) {
        this.selected_Ingredients_array = selected_Ingredients_array;
        this.c = c;
    }

    @NonNull
    @Override
    public Selected_item_Recyadapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_recycler_item,parent,false);

        return new Selected_item_Recyadapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Selected_item_Recyadapater.ViewHolder holder, int position) {

        holder.selected_item_ingredient_name.setText(selected_Ingredients_array.get(position).getIngredientItemName());

    }

    public void setOnClickListener(View.OnClickListener clickListener){

        SelectedItemListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return selected_Ingredients_array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView selected_item_ingredient_name;
        ImageView selected_item_clear_btn;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            selected_item_ingredient_name = itemView.findViewById(R.id.selected_iten_name);
            selected_item_clear_btn = itemView.findViewById(R.id.cancel_selected_item_icon);

            itemView.setTag(this);

            itemView.setOnClickListener(SelectedItemListener);

            selected_item_clear_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected_Ingredients_array.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }
    }
}
