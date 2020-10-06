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

public class Your_Recipes_Adapter extends RecyclerView.Adapter<Your_Recipes_Adapter.ViewHolder>{

    private ArrayList<Recipes_List> Your_Recipes_ArrayList;
    private Context context;
    private View.OnClickListener YourRecipeListener;

    public Your_Recipes_Adapter(ArrayList<Recipes_List> Your_Recipes_ArrayList, Context context) {
        this.Your_Recipes_ArrayList = Your_Recipes_ArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Your_Recipes_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_recipes_recycler_item,parent,false);

        return new Your_Recipes_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Your_Recipes_Adapter.ViewHolder holder, int position) {

        holder.your_recipe_name.setText(Your_Recipes_ArrayList.get(position).getRecipe_name());
        holder.your_recipe_cook_time.setText(Your_Recipes_ArrayList.get(position).getCook_time()+" Min");
        holder.your_recipe_numbers.setText("("+Your_Recipes_ArrayList.get(position).getRating_number()+")");
        if(!Your_Recipes_ArrayList.get(position).getPicture_of_recipe().equals("no image")){
            Glide.with(context)
                    .load("https://dinierecettes.online/images/"+Your_Recipes_ArrayList.get(position).getPicture_of_recipe()+".jpg")
                    .placeholder(R.drawable.recipe_place_holder)
                    .into(holder.your_recipe_image);
        }
        int rating = Integer.parseInt(Your_Recipes_ArrayList.get(position).getRating());
        if(rating==1){
            holder.your_recipe_stars.setImageResource(R.drawable.one_star);
        }else if(rating==2){
            holder.your_recipe_stars.setImageResource(R.drawable.two_stars);
        }else if(rating==3){
            holder.your_recipe_stars.setImageResource(R.drawable.three_stars);
        }else if(rating==4){
            holder.your_recipe_stars.setImageResource(R.drawable.four_stars);
        }else {
            holder.your_recipe_stars.setImageResource(R.drawable.five_stars);
        }


    }


    public void setOnClickListener(View.OnClickListener clickListener){

        YourRecipeListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return Your_Recipes_ArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView your_recipe_name,your_recipe_cook_time,your_recipe_numbers;
        ImageView your_recipe_image,your_recipe_stars;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            your_recipe_name = itemView.findViewById(R.id.your_recipe_name);
            your_recipe_cook_time = itemView.findViewById(R.id.your_recipe_cook_time);
            your_recipe_numbers = itemView.findViewById(R.id.your_recipe_numbers);
            your_recipe_image = itemView.findViewById(R.id.your_recipe_image);
            your_recipe_stars = itemView.findViewById(R.id.your_recipe_stars);


            itemView.setTag(this);

            itemView.setOnClickListener(YourRecipeListener);

        }
    }
}
