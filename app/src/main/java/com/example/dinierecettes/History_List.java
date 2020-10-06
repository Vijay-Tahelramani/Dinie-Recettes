package com.example.dinierecettes;

public class History_List {

    String history_id,recipe_id,history_recipe_name,history_date,history_recipe_image;

    public History_List(String history_id, String recipe_id, String history_recipe_name, String history_date, String history_recipe_image) {
        this.history_id = history_id;
        this.recipe_id = recipe_id;
        this.history_recipe_name = history_recipe_name;
        this.history_date = history_date;
        this.history_recipe_image = history_recipe_image;
    }

    public String getHistory_id() {
        return history_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public String getHistory_recipe_name() {
        return history_recipe_name;
    }

    public String getHistory_date() {
        return history_date;
    }

    public String getHistory_recipe_image() {
        return history_recipe_image;
    }
}
