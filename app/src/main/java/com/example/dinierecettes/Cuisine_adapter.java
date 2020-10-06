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

public class Cuisine_adapter extends RecyclerView.Adapter<Cuisine_adapter.ViewHolder> {

    private ArrayList<CuisineOrCategory_List> Cuisine_ArrayList;
    private Context context;
    private View.OnClickListener CuisineListener;

    public Cuisine_adapter(ArrayList<CuisineOrCategory_List> Cuisine_ArrayList, Context context) {
        this.Cuisine_ArrayList = Cuisine_ArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Cuisine_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuisine_recycler_item,parent,false);

        return new Cuisine_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cuisine_adapter.ViewHolder holder, int position) {

        holder.cuisine_name.setText(Cuisine_ArrayList.get(position).getC_name());
        holder.cuisine_image.setImageResource(Cuisine_ArrayList.get(position).getC_image());


    }

    public void setOnClickListener(View.OnClickListener clickListener){

        CuisineListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return Cuisine_ArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView cuisine_name;
        ImageView cuisine_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cuisine_name = itemView.findViewById(R.id.cuisine_name);
            cuisine_image = itemView.findViewById(R.id.cuisine_image);

            itemView.setTag(this);

            itemView.setOnClickListener(CuisineListener);

        }
    }


}
