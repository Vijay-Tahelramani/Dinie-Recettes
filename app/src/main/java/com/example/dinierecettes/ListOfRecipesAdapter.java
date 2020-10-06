package com.example.dinierecettes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListOfRecipesAdapter extends RecyclerView.Adapter<ListOfRecipesAdapter.ViewHolder> {

    private ArrayList<Recipes_List> lorArrayList;
    private Context context;
    private View.OnClickListener lorListener;
    private ArrayList<Recipes_List> offlineArrayList;
    private boolean is_offline = false;

    public ListOfRecipesAdapter(ArrayList<Recipes_List> lorArrayList, Context context) {
        this.lorArrayList = lorArrayList;
        this.context = context;
    }

    public ListOfRecipesAdapter(Context context, ArrayList<Recipes_List> offlineArrayList, boolean is_offline) {
        this.context = context;
        this.offlineArrayList = offlineArrayList;
        this.is_offline = is_offline;
    }

    @NonNull
    @Override
    public ListOfRecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_recipes_items,parent,false);

        return new ListOfRecipesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfRecipesAdapter.ViewHolder holder, int position) {

        if(is_offline){
            holder.lor_recipe_name.setText(offlineArrayList.get(position).getRecipe_name());
            holder.lor_recipe_cook_time.setText(offlineArrayList.get(position).getCook_time() + " Min");
            holder.lor_recipe_cuisine.setText("Cuisine: " + offlineArrayList.get(position).getRecipe_cuisine());
            holder.lor_recipe_category.setText("Category: " + offlineArrayList.get(position).getRecipe_category());
            holder.lor_recipe_numbers.setVisibility(View.GONE);
            holder.lor_recipe_stars.setVisibility(View.GONE);
            if(offlineArrayList.get(position).getRecipe_image().length>0){
                holder.lor_recipe_image.setImageBitmap(BitmapFactory.decodeByteArray(offlineArrayList.get(position).getRecipe_image(), 0, offlineArrayList.get(position).getRecipe_image().length));
            }
        }
        else {
            holder.lor_recipe_name.setText(lorArrayList.get(position).getRecipe_name());
            holder.lor_recipe_cook_time.setText(lorArrayList.get(position).getCook_time() + " Min");
            holder.lor_recipe_cuisine.setText("Cuisine: " + lorArrayList.get(position).getRecipe_cuisine());
            holder.lor_recipe_category.setText("Category: " + lorArrayList.get(position).getRecipe_category());
            holder.lor_recipe_numbers.setText("(" + lorArrayList.get(position).getRating_number() + ")");
            if (!lorArrayList.get(position).getPicture_of_recipe().equals("no image")) {
                Glide.with(context)
                        .load("https://dinierecettes.online/images/" + lorArrayList.get(position).getPicture_of_recipe() + ".jpg")
                        .placeholder(R.drawable.recipe_place_holder)
                        .into(holder.lor_recipe_image);
            }
            else {
                holder.lor_recipe_image.setImageResource(R.drawable.recipe_place_holder);
            }
            int rating = Integer.parseInt(lorArrayList.get(position).getRating());
            if (rating == 1) {
                holder.lor_recipe_stars.setImageResource(R.drawable.one_star);
            } else if (rating == 2) {
                holder.lor_recipe_stars.setImageResource(R.drawable.two_stars);
            } else if (rating == 3) {
                holder.lor_recipe_stars.setImageResource(R.drawable.three_stars);
            } else if (rating == 4) {
                holder.lor_recipe_stars.setImageResource(R.drawable.four_stars);
            } else {
                holder.lor_recipe_stars.setImageResource(R.drawable.five_stars);
            }
        }


    }

    public void setOnClickListener(View.OnClickListener clickListener){

        lorListener = clickListener;

    }

    @Override
    public int getItemCount() {
        if(is_offline){
            return offlineArrayList.size();
        }
        else {
            return lorArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView lor_recipe_name,lor_recipe_cook_time,lor_recipe_cuisine,lor_recipe_category,lor_recipe_numbers;
        ImageView lor_recipe_image,lor_recipe_stars;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lor_recipe_name = itemView.findViewById(R.id.lor_recipe_name);
            lor_recipe_cook_time = itemView.findViewById(R.id.lor_recipe_cook_time);
            lor_recipe_cuisine = itemView.findViewById(R.id.lor_recipe_cuisine);
            lor_recipe_category = itemView.findViewById(R.id.lor_recipe_category);
            lor_recipe_numbers = itemView.findViewById(R.id.lor_recipe_numbers);
            lor_recipe_image = itemView.findViewById(R.id.lor_recipe_image);
            lor_recipe_stars = itemView.findViewById(R.id.lor_recipe_stars);

            itemView.setTag(this);

            itemView.setOnClickListener(lorListener);

        }
    }
}
