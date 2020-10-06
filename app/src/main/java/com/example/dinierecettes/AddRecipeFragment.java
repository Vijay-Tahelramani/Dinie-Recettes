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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;


public class AddRecipeFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private ImageView add_recipe_image;
    private TextInputLayout add_recipe_name,cook_time_input_layout,add_serving_input_layout,add_calories_input_layout,add_video_link_input_layout,add_category_selection_layout,add_cuisine_selection_layout;
    private TextInputEditText add_reciepe_name_edittxt,cook_time_edit_txt,add_serving_edit_text,add_calories_edit_txt,add_video_link_edit_txt;
    private TextView add_photo_txt;
    private AutoCompleteTextView add_category_selection,add_cuisine_selection;
    public NavController HomeNavController;
    private Context context;
    public Uri image_uri;
    private ConstraintLayout error_layout;
    private ImageButton add_recipe_hide_error_btn;
    private MaterialButton add_recipe1_next_btn;
    private ProgressBar add_recipe_progress1;
    String calories = "0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);
    }

    /*@Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context =getActivity().getApplicationContext();

        HomeNavController = Navigation.findNavController(getActivity(),R.id.home_host_fragment);
        error_layout = view.findViewById(R.id.error_layout);
        add_recipe_hide_error_btn = view.findViewById(R.id.add_recipe_hide_error_btn);
        add_recipe_image = view.findViewById(R.id.add_recipe_image);
        add_recipe_name = view.findViewById(R.id.add_recipe_name);
        add_reciepe_name_edittxt = view.findViewById(R.id.add_reciepe_name_edittxt);
        add_photo_txt = view.findViewById(R.id.add_photo_txt);
        add_category_selection_layout = view.findViewById(R.id.add_category_selection_layout);
        add_cuisine_selection_layout = view.findViewById(R.id.add_cuisine_selection_layout);
        add_category_selection = view.findViewById(R.id.add_category_selection);
        add_cuisine_selection = view.findViewById(R.id.add_cuisine_selection);
        cook_time_input_layout = view.findViewById(R.id.cook_time_input_layout);
        add_serving_input_layout = view.findViewById(R.id.add_serving_input_layout);
        add_calories_input_layout = view.findViewById(R.id.add_calories_input_layout);
        add_video_link_input_layout = view.findViewById(R.id.add_video_link_input_layout);
        cook_time_edit_txt = view.findViewById(R.id.cook_time_edit_txt);
        add_serving_edit_text = view.findViewById(R.id.add_serving_edit_text);
        add_calories_edit_txt = view.findViewById(R.id.add_calories_edit_txt);
        add_video_link_edit_txt = view.findViewById(R.id.add_video_link_edit_txt);
        add_recipe1_next_btn = view.findViewById(R.id.add_recipe1_next_btn);
        add_recipe_progress1 = view.findViewById(R.id.add_recipe_progress1);

        add_recipe_hide_error_btn.setOnClickListener(this);
        add_recipe_image.setOnClickListener(this);
        add_photo_txt.setOnClickListener(this);
        add_recipe1_next_btn.setOnClickListener(this);
        add_reciepe_name_edittxt.setOnEditorActionListener(this);
        cook_time_edit_txt.setOnEditorActionListener(this);
        add_serving_edit_text.setOnEditorActionListener(this);
        add_calories_edit_txt.setOnEditorActionListener(this);
        add_video_link_edit_txt.setOnEditorActionListener(this);

        Constants.HideErrorOnTyping(add_recipe_name);
        Constants.HideErrorOnTyping(cook_time_input_layout);
        Constants.HideErrorOnTyping(add_serving_input_layout);
        Constants.HideErrorOnTyping(add_calories_input_layout);
        Constants.HideErrorOnTyping(add_video_link_input_layout);

        ArrayAdapter<String> Categoryadapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,Constants.category);
        add_category_selection.setAdapter(Categoryadapter);

        ArrayAdapter<String> Cuisineadapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,Constants.cuisine);
        add_cuisine_selection.setAdapter(Cuisineadapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onClick(View view) {
        if(view==add_recipe_image | view == add_photo_txt){
            SelectProfilePic();
        }
        else if(view == add_recipe_hide_error_btn){
            error_layout.setVisibility(View.GONE);
        }
        else if(view==add_recipe1_next_btn){
            if(TextUtils.isEmpty(add_recipe_name.getEditText().getText().toString().trim())){
                add_recipe_name.setError(" ");
                error_layout.setVisibility(View.VISIBLE);

            }
            else if(TextUtils.isEmpty(cook_time_input_layout.getEditText().getText().toString().trim())){
                cook_time_input_layout.setError(" ");
                error_layout.setVisibility(View.VISIBLE);

            }
            else if(TextUtils.isEmpty(add_serving_input_layout.getEditText().getText().toString().trim())){
                add_serving_input_layout.setError(" ");
                error_layout.setVisibility(View.VISIBLE);
            }
            else if(TextUtils.isEmpty(add_category_selection.getText().toString().trim())){
                error_layout.setVisibility(View.VISIBLE);

            }
            else if(TextUtils.isEmpty(add_cuisine_selection.getText().toString().trim())){
                error_layout.setVisibility(View.VISIBLE);
            }
            else {
                error_layout.setVisibility(View.GONE);
                PreferenceData.clearRecipeData(context);
                if(image_uri!=null){
                    new EncodeImage(image_uri).execute();

                }
                else {
                    PreferenceData.SetRecipeImage(context,"no image","no image");
                }
                if(!TextUtils.isEmpty(add_calories_input_layout.getEditText().getText().toString().trim())){
                    calories = add_calories_input_layout.getEditText().getText().toString().trim();
                }
                String recipeLink = "no link";
                if(!TextUtils.isEmpty(add_video_link_input_layout.getEditText().getText().toString().trim())){
                    recipeLink = add_video_link_input_layout.getEditText().getText().toString().trim();
                    add_recipe_progress1.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new ValidateURL(recipeLink).execute();
                }
                else {
                    PreferenceData.SetRecipeDetails(context, add_recipe_name.getEditText().getText().toString().trim(),
                            add_category_selection.getText().toString(),
                            add_cuisine_selection.getText().toString(),
                            cook_time_input_layout.getEditText().getText().toString().trim(),
                            add_serving_input_layout.getEditText().getText().toString().trim(),
                            calories, recipeLink);
                    HomeNavController.navigate(R.id.action_addRecipeFragment_to_add_ingredientsFragment);
                }
            }
        }

    }



    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_NEXT) {
            if(add_recipe_name.hasFocus()){
                add_recipe_name.clearFocus();
            }

            else if(cook_time_input_layout.hasFocus()){
                cook_time_input_layout.clearFocus();
            }
            else if(add_serving_input_layout.hasFocus()){
                add_serving_input_layout.clearFocus();
            }
            else if(add_calories_input_layout.hasFocus()){
                add_calories_input_layout.clearFocus();
            }

        }
        if(i==EditorInfo.IME_ACTION_DONE){
            if(add_video_link_input_layout.hasFocus()){
                add_video_link_input_layout.clearFocus();
            }
        }
        return false;
    }

    /*-------- Below Code is for selecting image from galary or camera -----------*/

    private void SelectProfilePic() {
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
                    add_recipe_image.setImageURI(image_uri);
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    add_recipe_image.setImageURI(image_uri);
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
            add_recipe_progress1.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if(aBoolean){
                PreferenceData.SetRecipeDetails(context, add_recipe_name.getEditText().getText().toString().trim(),
                        add_category_selection.getText().toString(),
                        add_cuisine_selection.getText().toString(),
                        cook_time_input_layout.getEditText().getText().toString().trim(),
                        add_serving_input_layout.getEditText().getText().toString().trim(),
                        calories, link);
                HomeNavController.navigate(R.id.action_addRecipeFragment_to_add_ingredientsFragment);
            }
            else {
                add_video_link_input_layout.setError(getResources().getString(R.string.link_not_valid));
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
