package com.example.dinierecettes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CookbookAdapter extends RecyclerView.Adapter<CookbookAdapter.ViewHolder>  {

    private ArrayList<cookbook_list> CB_ArrayList;
    private Context context;
    private View.OnClickListener CBListener;

    public CookbookAdapter(ArrayList<cookbook_list> CB_ArrayList, Context context) {
        this.CB_ArrayList = CB_ArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cb_recycler_items,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cb_name.setText(CB_ArrayList.get(position).getCookbook_name());
        holder.cb_num_of_recipes.setText("Number of recipes: "+CB_ArrayList.get(position).getCookbook_num_of_recipess());


    }

    public void setOnClickListener(View.OnClickListener clickListener){
        CBListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return CB_ArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cb_name,cb_num_of_recipes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cb_num_of_recipes = itemView.findViewById(R.id.cb_num_of_recipes);
            cb_name = itemView.findViewById(R.id.cb_name);

            itemView.setTag(this);

            itemView.setOnClickListener(CBListener);

        }
    }


}
