package com.example.dinierecettes;

public class Ingredients_Quantity_List {

    private String IngredientItemId;
    private String IngredientItemQuantity;

    public Ingredients_Quantity_List(String ingredientItemId, String ingredientItemQuantity) {
        IngredientItemId = ingredientItemId;
        IngredientItemQuantity = ingredientItemQuantity;
    }

    public String getIngredientItemId() {
        return IngredientItemId;
    }

    public String getIngredientItemQuantity() {
        return IngredientItemQuantity;
    }
}
