package com.example.dinierecettes;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class EditRecipe1Fragment extends Fragment implements View.OnClickListener {

    private NavController EraNavController;
    private ImageView er_image;
    private TextInputLayout er_name_layout,er_cook_time_input_layout,er_serving_input_layout,er_calories_input_layout,er_video_link_input_layout,er_category_selection_layout,er_cuisine_selection_layout;
    private TextInputEditText er_name_edittxt,er_cook_time_edit_txt,er_serving_edit_text,er_calories_edit_txt,er_video_link_edit_txt;
    private TextView edit_photo_txt;
    private AutoCompleteTextView er_category_selection,er_cuisine_selection;
    private Context context;
    public Uri image_uri;
    private ConstraintLayout er_error_layout;
    private ImageButton er_hide_error_btn;
    private MaterialButton er1_next_btn;
    private ProgressBar er_progress1;
    String calories = "0";
    String recipe_data = "";
    String recipe_image_name = "no image";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_recipe1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EraNavController = Navigation.findNavController(getActivity(), R.id.era_host_fragment);
        context =getActivity().getApplicationContext();

        er_error_layout = view.findViewById(R.id.er_error_layout);
        er_hide_error_btn = view.findViewById(R.id.er_hide_error_btn);
        er_image = view.findViewById(R.id.er_image);
        er_name_layout = view.findViewById(R.id.er_name_layout);
        er_name_edittxt = view.findViewById(R.id.er_name_edittxt);
        edit_photo_txt = view.findViewById(R.id.edit_photo_txt);
        er_category_selection_layout = view.findViewById(R.id.er_category_selection_layout);
        er_cuisine_selection_layout = view.findViewById(R.id.er_cuisine_selection_layout);
        er_category_selection = view.findViewById(R.id.er_category_selection);
        er_cuisine_selection = view.findViewById(R.id.er_cuisine_selection);
        er_cook_time_input_layout = view.findViewById(R.id.er_cook_time_input_layout);
        er_serving_input_layout = view.findViewById(R.id.er_serving_input_layout);
        er_calories_input_layout = view.findViewById(R.id.er_calories_input_layout);
        er_video_link_input_layout = view.findViewById(R.id.er_video_link_input_layout);
        er_cook_time_edit_txt = view.findViewById(R.id.er_cook_time_edit_txt);
        er_serving_edit_text = view.findViewById(R.id.er_serving_edit_text);
        er_calories_edit_txt = view.findViewById(R.id.er_calories_edit_txt);
        er_video_link_edit_txt = view.findViewById(R.id.er_video_link_edit_txt);
        er1_next_btn = view.findViewById(R.id.er1_next_btn);
        er_progress1 = view.findViewById(R.id.er_progress1);

        er_hide_error_btn.setOnClickListener(this);
        er_image.setOnClickListener(this);
        edit_photo_txt.setOnClickListener(this);
        er1_next_btn.setOnClickListener(this);

        Constants.HideErrorOnTyping(er_name_layout);
        Constants.HideErrorOnTyping(er_cook_time_input_layout);
        Constants.HideErrorOnTyping(er_serving_input_layout);
        Constants.HideErrorOnTyping(er_calories_input_layout);
        Constants.HideErrorOnTyping(er_video_link_input_layout);

        ArrayAdapter<String> Categoryadapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, Constants.category);
        er_category_selection.setAdapter(Categoryadapter);

        ArrayAdapter<String> Cuisineadapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, Constants.cuisine);
        er_cuisine_selection.setAdapter(Cuisineadapter);

        if(getActivity().getIntent()!=null){
            recipe_data = getActivity().getIntent().getStringExtra("recipe_response");
            try {
                JSONObject jsonObject = new JSONObject(recipe_data);
                JSONObject recipe_detailsobj = jsonObject.getJSONObject("recipe_details");
                er_name_layout.getEditText().setText(recipe_detailsobj.getString("recipe_name"));
                recipe_image_name = recipe_detailsobj.getString("picture_of_recipe");
                if(!recipe_detailsobj.getString("picture_of_recipe").equals("no image")){
                    Glide.with(context)
                            .load("https://dinierecettes.online/images/"+recipe_detailsobj.getString("picture_of_recipe")+".jpg")
                            .centerCrop()
                            .into(er_image);
                }
                er_category_selection.setText(recipe_detailsobj.getString("recipe_category"));
                er_cuisine_selection.setText(recipe_detailsobj.getString("recipe_cuisine"));
                er_cook_time_input_layout.getEditText().setText(recipe_detailsobj.getString("cook_time"));
                er_serving_input_layout.getEditText().setText(recipe_detailsobj.getString("number_of_serving"));
                if(!recipe_detailsobj.getString("calories").equals("0")){
                    er_calories_input_layout.getEditText().setText(recipe_detailsobj.getString("calories"));
                }
                if(!recipe_detailsobj.getString("video_link").equals("no link")){
                    er_video_link_input_layout.getEditText().setText(recipe_detailsobj.getString("video_link"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View view) {
        if(view==er_image | view == edit_photo_txt){
            SelectRecipePic();
        }
        else if(view == er_hide_error_btn){
            er_error_layout.setVisibility(View.GONE);
        }
        else if(view==er1_next_btn){
            if(TextUtils.isEmpty(er_name_layout.getEditText().getText().toString().trim())){
                er_name_layout.setError(" ");
                er_error_layout.setVisibility(View.VISIBLE);

            }
            else if(TextUtils.isEmpty(er_cook_time_input_layout.getEditText().getText().toString().trim())){
                er_cook_time_input_layout.setError(" ");
                er_error_layout.setVisibility(View.VISIBLE);

            }
            else if(TextUtils.isEmpty(er_serving_input_layout.getEditText().getText().toString().trim())){
                er_serving_input_layout.setError(" ");
                er_error_layout.setVisibility(View.VISIBLE);
            }
            else if(TextUtils.isEmpty(er_category_selection.getText().toString().trim())){
                er_error_layout.setVisibility(View.VISIBLE);

            }
            else if(TextUtils.isEmpty(er_cuisine_selection.getText().toString().trim())){
                er_error_layout.setVisibility(View.VISIBLE);
            }
            else {
                er_error_layout.setVisibility(View.GONE);
                PreferenceData.clearRecipeData(context);
                if(image_uri!=null){
                    new EncodeImage(image_uri).execute();
                }
                else {
                    PreferenceData.SetRecipeImage(context,recipe_image_name,recipe_image_name);
                }
                if(!TextUtils.isEmpty(er_calories_input_layout.getEditText().getText().toString().trim())){
                    calories = er_calories_input_layout.getEditText().getText().toString().trim();
                }
                String recipeLink = "no link";
                if(!TextUtils.isEmpty(er_video_link_input_layout.getEditText().getText().toString().trim())){
                    recipeLink = er_video_link_input_layout.getEditText().getText().toString().trim();
                    er_progress1.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new ValidateURL(recipeLink).execute();
                }
                else {
                    PreferenceData.SetRecipeDetails(context, er_name_layout.getEditText().getText().toString().trim(),
                            er_category_selection.getText().toString(),
                            er_cuisine_selection.getText().toString(),
                            er_cook_time_input_layout.getEditText().getText().toString().trim(),
                            er_serving_input_layout.getEditText().getText().toString().trim(),
                            calories, recipeLink);
                    EraNavController.navigate(R.id.action_editRecipe1Fragment_to_edit_Recipe_IngredientsFragment);
                }
            }
        }

    }

    /*-------- Below Code is for selecting image from galary or camera -----------*/

    private void SelectRecipePic() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, 1000);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(Intent.createChooser(intent,"Select Image"), 2);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(takePictureIntent, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    //permisiion from pop up was denied.
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Denied...", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    er_image.setImageURI(image_uri);
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    er_image.setImageURI(image_uri);
                    break;
            }

        }
    }


    /*--------Class to Validate URL-------*/
    private class ValidateURL extends AsyncTask<Void, Void, Boolean> {
        private String link;

        public ValidateURL(String link) {
            this.link = link;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                final URL url =new URL(link);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1){
                return false;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            er_progress1.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if(aBoolean){
                PreferenceData.SetRecipeDetails(context, er_name_layout.getEditText().getText().toString().trim(),
                        er_category_selection.getText().toString(),
                        er_cuisine_selection.getText().toString(),
                        er_cook_time_input_layout.getEditText().getText().toString().trim(),
                        er_serving_input_layout.getEditText().getText().toString().trim(),
                        calories, link);
                EraNavController.navigate(R.id.action_editRecipe1Fragment_to_edit_Recipe_IngredientsFragment);
            }
            else {
                er_video_link_input_layout.setError(getResources().getString(R.string.link_not_valid));
            }
        }
    }


    /*--------Class to Encode Image-------*/
    public class EncodeImage extends AsyncTask<Void, Void, String> {
        Uri uri;
        String encodedImage = "";
        public EncodeImage(Uri uri){
            this.uri = uri;
        }
        @Override
        protected String doInBackground(Void... params){

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image_uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
                byte[] byteArray = outputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return encodedImage;

        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(!result.equals("")) {
                PreferenceData.SetRecipeImage(context,result,"recipe_"+System.currentTimeMillis());
            }
        }

    }
}
