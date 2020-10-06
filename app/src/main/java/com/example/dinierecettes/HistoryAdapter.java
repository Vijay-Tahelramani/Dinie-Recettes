package com.example.dinierecettes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>  {

    private ArrayList<History_List> HistoryArrayList;
    private Context context;
    private View.OnClickListener HistoryListener;

    public HistoryAdapter(ArrayList<History_List> HistoryArrayList, Context context) {
        this.HistoryArrayList = HistoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hisyory_recycler_item,parent,false);

        return new HistoryAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        holder.history_recipe_name.setText(HistoryArrayList.get(position).getHistory_recipe_name());
        holder.history_date.setText("Last viewed on "+HistoryArrayList.get(position).getHistory_date());

        if(!HistoryArrayList.get(position).getHistory_recipe_image().equals("no image")){
            Glide.with(context)
                    .load("https://dinierecettes.online/images/"+HistoryArrayList.get(position).getHistory_recipe_image()+".jpg")
                    .placeholder(R.drawable.recipe_place_holder)
                    .into(holder.history_recipe_image);
        }


    }

    public void setOnClickListener(View.OnClickListener clickListener){

        HistoryListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return HistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView history_recipe_name,history_date;
        ImageView history_recipe_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            history_recipe_name = itemView.findViewById(R.id.history_recipe_name);
            history_recipe_image = itemView.findViewById(R.id.history_recipe_image);
            history_date = itemView.findViewById(R.id.history_date);

            itemView.setTag(this);

            itemView.setOnClickListener(HistoryListener);

        }
    }


}
