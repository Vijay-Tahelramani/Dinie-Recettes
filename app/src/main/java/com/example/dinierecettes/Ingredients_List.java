package com.example.dinierecettes;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients_List implements Parcelable {

    private String IngredientItemName;
    private String IngredientItemId;
    private String IngredirntItemQuantity;

    public Ingredients_List(String IngredientItemName, String IngredientItemId) {
        this.IngredientItemName = IngredientItemName;
        this.IngredientItemId = IngredientItemId;
    }

    public Ingredients_List(String ingredientItemName, String ingredientItemId, String ingredirntItemQuantity) {
        IngredientItemName = ingredientItemName;
        IngredientItemId = ingredientItemId;
        IngredirntItemQuantity = ingredirntItemQuantity;
    }

    public String getIngredientItemName() {
        return IngredientItemName;
    }

    public String getIngredientItemId() {
        return IngredientItemId;
    }

    public String getIngredirntItemQuantity(){
        return IngredirntItemQuantity;
    }

    protected Ingredients_List(Parcel in) {
        IngredientItemName = in.readString();
        IngredientItemId = in.readString();
        IngredirntItemQuantity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IngredientItemName);
        dest.writeString(IngredientItemId);
        dest.writeString(IngredirntItemQuantity);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredients_List> CREATOR = new Parcelable.Creator<Ingredients_List>() {
        @Override
        public Ingredients_List createFromParcel(Parcel in) {
            return new Ingredients_List(in);
        }

        @Override
        public Ingredients_List[] newArray(int size) {
            return new Ingredients_List[size];
        }
    };
}