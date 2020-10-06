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

public class category_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> {

    private ArrayList<CuisineOrCategory_List> Category_ArrayList;
    private Context context;
    private View.OnClickListener CategoryListener;

    public category_adapter(ArrayList<CuisineOrCategory_List> Category_ArrayList, Context context) {
        this.Category_ArrayList = Category_ArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public category_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycler_item,parent,false);

        return new category_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull category_adapter.ViewHolder holder, int position) {

        holder.category_name.setText(Category_ArrayList.get(position).getC_name());
        holder.category_image.setImageResource(Category_ArrayList.get(position).getC_image());
    }

    public void setOnClickListener(View.OnClickListener clickListener){

        CategoryListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return Category_ArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView category_name;
        ImageView category_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category_name = itemView.findViewById(R.id.category_name);
            category_image = itemView.findViewById(R.id.category_image);

            itemView.setTag(this);

            itemView.setOnClickListener(CategoryListener);

        }
    }
}
