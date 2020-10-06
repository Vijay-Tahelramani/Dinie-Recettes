package com.example.dinierecettes;

import java.sql.Blob;

public class Recipes_List {

    String recipe_id,recipe_name,cook_time,number_of_serving,
            recipe_cuisine,recipe_category,rating_number,rating,
            picture_of_recipe,video_link,calories,user_id;

    int offline_recipe_id;
    byte[] recipe_image;
    String instructions_details, ingredients_details;

    public Recipes_List(String recipe_id,String recipe_name, String cook_time, String rating_number, String rating, String picture_of_recipe, String calories) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.cook_time = cook_time;
        this.rating_number = rating_number;
        this.rating = rating;
        this.picture_of_recipe = picture_of_recipe;
        this.calories = calories;
    }

    public Recipes_List(String recipe_id, String recipe_name, String cook_time, String rating_number, String rating, String picture_of_recipe) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.cook_time = cook_time;
        this.rating_number = rating_number;
        this.rating = rating;
        this.picture_of_recipe = picture_of_recipe;
    }

    public Recipes_List(String recipe_id, String recipe_name, String cook_time, String recipe_cuisine, String recipe_category, String rating_number, String rating, String picture_of_recipe) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.cook_time = cook_time;
        this.recipe_cuisine = recipe_cuisine;
        this.recipe_category = recipe_category;
        this.rating_number = rating_number;
        this.rating = rating;
        this.picture_of_recipe = picture_of_recipe;
    }

    public Recipes_List(int offline_recipe_id, String recipe_name, String cook_time, String number_of_serving, String recipe_cuisine, String recipe_category, String video_link, String calories, byte[] recipe_image, String ingredients_details, String instructions_details) {
        this.recipe_name = recipe_name;
        this.cook_time = cook_time;
        this.number_of_serving = number_of_serving;
        this.recipe_cuisine = recipe_cuisine;
        this.recipe_category = recipe_category;
        this.video_link = video_link;
        this.calories = calories;
        this.offline_recipe_id = offline_recipe_id;
        this.recipe_image = recipe_image;
        this.ingredients_details = ingredients_details;
        this.instructions_details = instructions_details;

    }

    public String getRecipe_id(){
        return recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String getCook_time() {
        return cook_time;
    }

    public String getRating_number() {
        return rating_number;
    }

    public String getRating() {
        return rating;
    }

    public String getPicture_of_recipe() {
        return picture_of_recipe;
    }

    public String getCalories() {
        return calories;
    }

    public String getRecipe_cuisine() {
        return recipe_cuisine;
    }

    public String getRecipe_category() {
        return recipe_category;
    }

    public String getNumber_of_serving() {
        return number_of_serving;
    }

    public String getVideo_link() {
        return video_link;
    }

    public int getOffline_recipe_id() {
        return offline_recipe_id;
    }

    public byte[] getRecipe_image() {
        return recipe_image;
    }

    public String getInstructions_details() {
        return instructions_details;
    }

    public String getIngredients_details() {
        return ingredients_details;
    }
}
