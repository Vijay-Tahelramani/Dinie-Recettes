package com.example.dinierecettes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceData {

    static final String UserID = "id",UserName="name",Useremail="email",Userpassword="password",
            Usercontact="contact",Userprofile="profile",UserImagename = "imageName",ReceivedOTP="receivedOTP";

    static final String RecipeName="recipe_name",RecipeImage="recipe_image",RecipeImageName="recipe_image_name",RecipeCategory="recipe_category",RecipeCuisine="recipe_cuisine",
            RecipeCookTime="recipe_cook_time",RecipeServing="recipe_serving",RecipeCalories="recipe_calories",RecipeLink="recipe_link",
            RecipeIngredients="recipe_ingredients",RecipeInstructions="recipe_instructions";


    public static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserID(Context ctx, String userID){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UserID, userID);
        editor.commit();
    }

    public static void setUserName(Context ctx, String userName){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UserName, userName);
        editor.commit();
    }

    public static void setUseremail(Context ctx, String useremail){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Useremail, useremail);
        editor.commit();
    }
    public static void setUserpassword(Context ctx, String userpassword){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Userpassword, userpassword);
        editor.commit();
    }
    public static void setUsercontact(Context ctx, String usercontact){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Usercontact, usercontact);
        editor.commit();
    }
    public static void setUserprofile(Context ctx, String userprofile){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Userprofile, userprofile);
        editor.commit();
    }

    public static void setUserImagename(Context ctx, String userImagename){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UserImagename, userImagename);
        editor.commit();
    }

    public static void setReceivedOTP(Context ctx, String receivedOTP){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ReceivedOTP, receivedOTP);
        editor.commit();
    }

    public static String getReceivedOTP(Context ctx)
    {
        return getSharedPreferences(ctx).getString(ReceivedOTP,"");
    }


    public static String getUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(UserID,"");
    }


    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(UserName,"");
    }

    public static String getUseremail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(Useremail,"");
    }

    public static String getUserpassword(Context ctx)
    {
        return getSharedPreferences(ctx).getString(Userpassword,"");
    }

    public static String getUsercontact(Context ctx)
    {
        return getSharedPreferences(ctx).getString(Usercontact,"");
    }

    public static String getUserprofile(Context ctx)
    {
        return getSharedPreferences(ctx).getString(Userprofile,"");
    }

    public static String getUserImagename(Context ctx)
    {
        return getSharedPreferences(ctx).getString(UserImagename,"");
    }

    public static void clearUserData(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(UserID);
        editor.remove(UserName);
        editor.remove(Useremail);
        editor.remove(Userpassword);
        editor.remove(Usercontact);
        editor.remove(Userprofile);
        editor.remove(UserImagename);
        editor.remove(ReceivedOTP);

        editor.commit();
    }



    /*------------ Methods For Recipes---------*/
    public static void SetRecipeDetails(Context ctx,String recipeName, String recipeCategory,
                                        String recipeCuisine,String recipeCookTime,String recipeServing,
                                        String recipeCalories,String recipeLink){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(RecipeName, recipeName);
        editor.putString(RecipeCategory, recipeCategory);
        editor.putString(RecipeCuisine, recipeCuisine);
        editor.putString(RecipeCookTime, recipeCookTime);
        editor.putString(RecipeServing, recipeServing);
        editor.putString(RecipeCalories, recipeCalories);
        editor.putString(RecipeLink, recipeLink);

        editor.commit();

    }

    public static void SetRecipeImage(Context ctx,String recipeImageData,String recipeImageName){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(RecipeImage, recipeImageData);
        editor.putString(RecipeImageName, recipeImageName);
        editor.commit();
    }

    public static void SetRecipeIngredients(Context ctx,String recipeIngredients){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(RecipeIngredients, recipeIngredients);
        editor.commit();
    }

    public static void SetRecipeInstructions(Context ctx,String recipeInstructions){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(RecipeInstructions, recipeInstructions);
        editor.commit();
    }

    public static String getRecipeName(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeName,"");
    }

    public static String getRecipeImage(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeImage,"");
    }

    public static String getRecipeImageName(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeImageName,"");
    }

    public static String getRecipeCategory(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeCategory,"");
    }

    public static String getRecipeCuisine(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeCuisine,"");
    }

    public static String getRecipeCookTime(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeCookTime,"");
    }

    public static String getRecipeServing(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeServing,"");
    }

    public static String getRecipeCalories(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeCalories,"");
    }

    public static String getRecipeLink(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeLink,"");
    }

    public static String getRecipeIngredients(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeIngredients,"");
    }

    public static String getRecipeInstructions(Context ctx) {
        return getSharedPreferences(ctx).getString(RecipeInstructions,"");
    }

    public static void clearRecipeData(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(RecipeName);
        editor.remove(RecipeImage);
        editor.remove(RecipeImageName);
        editor.remove(RecipeCategory);
        editor.remove(RecipeCuisine);
        editor.remove(RecipeCookTime);
        editor.remove(RecipeServing);
        editor.remove(RecipeCalories);
        editor.remove(RecipeLink);
        editor.remove(RecipeIngredients);
        editor.remove(RecipeInstructions);

        editor.commit();
    }



}
