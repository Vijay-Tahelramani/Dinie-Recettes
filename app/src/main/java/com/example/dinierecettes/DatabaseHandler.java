package com.example.dinierecettes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Offline_RecipesDB";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECIPES = "Offline_Recipes";
    public static final String Recipe_ID = "recipe_id";
    public static final String User_ID = "user_id";
    public static final String Recipe_NAME = "recipe_name";
    public static final String Recipe_CookTime = "cook_time";
    public static final String Recipe_Num_of_Serving = "number_of_serving";
    public static final String Recipe_Cuisine = "cuisine";
    public static final String Recipe_Category = "category";
    public static final String Recipe_Calories = "calories";
    public static final String Recipe_Video_Link = "video_link";
    public static final String Recipe_Picture = "picture_of_recipe";
    public static final String Recipe_Ingredient_Details = "ingredient_details";
    public static final String Recipe_Instruction_Details = "instruction_details";

    public static final String OR_ID = "offline_recipe_id";

    long inserted;
    int deleted;
    int updated;


    String CREATE_RECIPE_TABLE = "CREATE TABLE "+TABLE_RECIPES+"("+OR_ID+" INTEGER PRIMARY KEY,"
            +User_ID+" INTEGER,"
            +Recipe_ID+" INTEGER ,"
            +Recipe_NAME+" TEXT," +
            ""+Recipe_CookTime+" TEXT,"
            +Recipe_Num_of_Serving+" TEXT,"
            +Recipe_Cuisine+" TEXT,"
            +Recipe_Category+" TEXT,"
            +Recipe_Video_Link+" TEXT,"
            +Recipe_Calories+" TEXT,"
            +Recipe_Picture+" BLOB,"
            +Recipe_Ingredient_Details+" TEXT,"
            +Recipe_Instruction_Details+" TEXT"+")";

    String DROP_RECIPE_TABLE = "DROP TABLE IF EXISTS "+TABLE_RECIPES;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_RECIPE_TABLE);
        onCreate(db);
    }
    public long addRecipe(int user_ID,int recipe_ID, String recipe_NAME, String recipe_CookTime,String recipe_Num_of_Serving,
                          String recipe_Cuisine, String recipe_Category, String recipe_Video_Link,String recipe_Calories, byte[] recipe_Picture,
                          String recipe_Ingredient_Details,String recipe_Instruction_Details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User_ID,user_ID);
        values.put(Recipe_ID,recipe_ID);
        values.put(Recipe_NAME,recipe_NAME);
        values.put(Recipe_CookTime,recipe_CookTime);
        values.put(Recipe_Num_of_Serving,recipe_Num_of_Serving);
        values.put(Recipe_Cuisine,recipe_Cuisine);
        values.put(Recipe_Category,recipe_Category);
        values.put(Recipe_Video_Link,recipe_Video_Link);
        values.put(Recipe_Calories,recipe_Calories);
        values.put(Recipe_Picture,recipe_Picture);
        values.put(Recipe_Ingredient_Details,recipe_Ingredient_Details);
        values.put(Recipe_Instruction_Details,recipe_Instruction_Details);

        inserted = db.insert(TABLE_RECIPES,null,values);
        db.close();
        //Here db.insert returns the long value of the row ID of the newly inserted row OR returns -1 when error occured
        return inserted;
    }

    public int deleteRecipe(String param1,String param2){

        SQLiteDatabase db = this.getWritableDatabase();
        deleted = db.delete(TABLE_RECIPES,Recipe_ID+"=? AND "+User_ID+"=?", new String[]{param1,param2});
        db.close();
        //Here db.delete returns 1 when the effective row is deleted otherwise it returns 0
        return deleted;
    }

    public boolean checkRecipeExist(String param1,String param2){
        //Check if user exist or not if yes return true else return false
        String selectQuery = "SELECT * FROM "+TABLE_RECIPES+" WHERE "+Recipe_ID+" = " + param1+" AND "+User_ID+" = "+param2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public ArrayList<Recipes_List> getSpecificRecipe(String param1, String param2){
        ArrayList<Recipes_List> offline_recipe_List = new ArrayList<Recipes_List>();
        //Select all query
        String selectQuery = "SELECT * FROM "+TABLE_RECIPES+" WHERE "+Recipe_ID+" = " + param1+" AND "+User_ID+" = "+param2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all rows add adding to list
        if(cursor.moveToFirst()){
            do{
                Recipes_List recipe_Details = new Recipes_List(
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getBlob(10),
                        cursor.getString(11),
                        cursor.getString(12));
                offline_recipe_List.add(recipe_Details);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return offline_recipe_List;
    }

    public ArrayList<Recipes_List> getAllRecipes(String param1){
        ArrayList<Recipes_List> offline_recipe_List = new ArrayList<Recipes_List>();
        //Select all query
        String selectQuery = "SELECT * FROM "+TABLE_RECIPES+" WHERE "+User_ID+" = " + param1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all rows add adding to list
        if(cursor.moveToFirst()){
            do{
                Recipes_List recipe_Details = new Recipes_List(
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getBlob(10),
                        cursor.getString(11),
                        cursor.getString(12));
                offline_recipe_List.add(recipe_Details);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return offline_recipe_List;
    }
}
