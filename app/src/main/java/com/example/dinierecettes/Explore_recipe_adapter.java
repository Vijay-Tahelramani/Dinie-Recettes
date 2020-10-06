package com.example.dinierecettes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Explore_recipe_adapter extends RecyclerView.Adapter<Explore_recipe_adapter.ViewHolder>{

    private ArrayList<Recipes_List> Explore_Recipes_ArrayList;
    private Context context;
    private View.OnClickListener ExploreRecipeListener;

    public Explore_recipe_adapter(ArrayList<Recipes_List> Explore_Recipes_ArrayList, Context context) {
        this.Explore_Recipes_ArrayList = Explore_Recipes_ArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Explore_recipe_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_recycler_item,parent,false);

        return new Explore_recipe_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Explore_recipe_adapter.ViewHolder holder, int position) {

        holder.exp_recipe_name.setText(Explore_Recipes_ArrayList.get(position).getRecipe_name());
        holder.exp_recipe_calories_txt.setText(Explore_Recipes_ArrayList.get(position).getCalories()+"\nCal");
        holder.exp_recipe_cook_time_txt.setText(Explore_Recipes_ArrayList.get(position).getCook_time()+"\nMin");
        holder.exp_recipe_rating_num.setText("("+Explore_Recipes_ArrayList.get(position).getRating_number()+")");
        if(!Explore_Recipes_ArrayList.get(position).getPicture_of_recipe().equals("no image")){
            Glide.with(context)
                    .load("https://dinierecettes.online/images/"+Explore_Recipes_ArrayList.get(position).getPicture_of_recipe()+".jpg")
                    .placeholder(R.drawable.recipe_place_holder)
                    .into(holder.exp_recipe_image);
        }
        int rating = Integer.parseInt(Explore_Recipes_ArrayList.get(position).getRating());
        if(rating==1){
            holder.exp_recipe_rating_image.setImageResource(R.drawable.one_star);
        }else if(rating==2){
            holder.exp_recipe_rating_image.setImageResource(R.drawable.two_stars);
        }else if(rating==3){
            holder.exp_recipe_rating_image.setImageResource(R.drawable.three_stars);
        }else if(rating==4){
            holder.exp_recipe_rating_image.setImageResource(R.drawable.four_stars);
        }else {
            holder.exp_recipe_rating_image.setImageResource(R.drawable.five_stars);
        }


    }

    public void setOnClickListener(View.OnClickListener clickListener){

        ExploreRecipeListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return Explore_Recipes_ArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView exp_recipe_name,exp_recipe_calories_txt,exp_recipe_cook_time_txt,exp_recipe_rating_num;
        ImageView exp_recipe_image,exp_recipe_rating_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            exp_recipe_name = itemView.findViewById(R.id.exp_recipe_name);
            exp_recipe_calories_txt = itemView.findViewById(R.id.exp_recipe_calories_txt);
            exp_recipe_cook_time_txt = itemView.findViewById(R.id.exp_recipe_cook_time_txt);
            exp_recipe_rating_num = itemView.findViewById(R.id.exp_recipe_rating_num);
            exp_recipe_image = itemView.findViewById(R.id.exp_recipe_image);
            exp_recipe_rating_image = itemView.findViewById(R.id.exp_recipe_rating_image);


            itemView.setTag(this);

            itemView.setOnClickListener(ExploreRecipeListener);

        }
    }

}
