package com.example.dinierecettes;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment implements View.OnClickListener{

    private Context context;
    private ImageView ep_user_image;
    private TextInputLayout ep_first_name_layout, ep_last_name_layout, ep_contact_layout, ep_password_layout, ep_confirm_password_layout;
    private ProgressBar ep_progressbar;
    private MaterialButton ep_update_btn;
    private Uri image_uri;
    private String image_base_decoded="", iname = "",first_name = "",last_name = "",contact = "",password = "",confirm_password = "";
    public NavController HomeNavController;
    public DrawerLayout HomedrawerLayout;
    public NavigationView HomeNavigationView;
    public TextView username;
    public ImageView userImage;



    String[] Name = new String[2];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        HomeNavController = Navigation.findNavController(getActivity(), R.id.home_host_fragment);
        HomedrawerLayout =  ((HomeActivity)getActivity()).HomedrawerLayout;
        HomeNavigationView = ((HomeActivity)getActivity()).HomeNavigationView;
        userImage = ((HomeActivity)getActivity()).userImage;
        username = ((HomeActivity)getActivity()).username;

        ep_user_image = view.findViewById(R.id.ep_user_image);
        ep_first_name_layout = view.findViewById(R.id.ep_first_name_layout);
        ep_last_name_layout = view.findViewById(R.id.ep_last_name_layout);
        ep_contact_layout = view.findViewById(R.id.ep_contact_layout);
        ep_password_layout = view.findViewById(R.id.ep_password_layout);
        ep_confirm_password_layout = view.findViewById(R.id.ep_confirm_password_layout);
        ep_progressbar = view.findViewById(R.id.ep_progressbar);
        ep_update_btn = view.findViewById(R.id.ep_update_btn);

        ep_update_btn.setOnClickListener(this);
        ep_user_image.setOnClickListener(this);

        Name = PreferenceData.getUserName(context).split(" ");
        ep_first_name_layout.getEditText().setText(Name[0]);
        ep_last_name_layout.getEditText().setText(Name[1]);
        if(!PreferenceData.getUsercontact(context).equals("1234567890")){
            ep_contact_layout.getEditText().setText(PreferenceData.getUsercontact(context));
        }
        if(!PreferenceData.getUserprofile(context).equals("") && !PreferenceData.getUserprofile(context).equals("no image")){
            Glide.with(this)
                    .load("https://dinierecettes.online/images/" + PreferenceData.getUserprofile(context) + ".jpg")
                    .centerCrop()
                    .into(ep_user_image);
        }
        ep_password_layout.getEditText().setText(PreferenceData.getUserpassword(context));
        ep_confirm_password_layout.getEditText().setText(PreferenceData.getUserpassword(context));
    }

    @Override
    public void onClick(View view) {
        if(view == ep_update_btn){
            first_name = ep_first_name_layout.getEditText().getText().toString().trim();
            last_name = ep_last_name_layout.getEditText().getText().toString().trim();
            contact = ep_contact_layout.getEditText().getText().toString().trim();
            password = ep_password_layout.getEditText().getText().toString().trim();
            confirm_password = ep_confirm_password_layout.getEditText().getText().toString().trim();
            if(TextUtils.isEmpty(first_name)){
                ep_first_name_layout.setError(getResources().getString(R.string.invalid_user_name));
            }
            else if(TextUtils.isEmpty(last_name)){
                ep_last_name_layout.setError(getResources().getString(R.string.invalid_user_name));
            }
            else if(!Constants.isValidPassword(password)){
                ep_password_layout.setError(getResources().getString(R.string.invalid_password));
            }
            else if(!confirm_password.equals(password)){
                ep_confirm_password_layout.setError(getResources().getString(R.string.invalid_confirm_password));
            }
            else {
                if(contact.equals("")){
                    contact = "1234567890";
                }
                if(contact.length()!=10 | !TextUtils.isDigitsOnly(contact)){
                    ep_contact_layout.setError(getResources().getString(R.string.invalid_user_contact));
                }
                else {
                    if(image_uri!=null){
                        new EncodeImage(image_uri).execute();
                    }
                    else {
                        image_base_decoded = PreferenceData.getUserprofile(context);
                        iname = PreferenceData.getUserprofile(context);
                        UpdateProfile(first_name+" "+last_name,contact,password,image_base_decoded,iname);
                    }

                }
            }
        }
        else if(view==ep_user_image){
            SelectProfilePic();
        }
    }

    private void UpdateProfile(final String name, final String contact, final String password, final String image, final String image_name){
        ep_progressbar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ep_progressbar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("OK")){
                        PreferenceData.setUserName(context,name);
                        PreferenceData.setUsercontact(context,contact);
                        PreferenceData.setUserpassword(context,password);
                        PreferenceData.setUserImagename(context,image_name);
                        PreferenceData.setUserprofile(context,image_name);
                        ((HomeActivity)getActivity()).setupNavigation();
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        HomeNavController.navigate(R.id.action_editProfileFragment_to_homeFragment);
                    }
                    else if(jsonObject.getString("status").equals("WRONG")){

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ep_progressbar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", PreferenceData.getUserID(context));
                params.put("name",name);
                params.put("password",password);
                params.put("profile_photo",image);
                params.put("iname",image_name);
                params.put("phone_number",contact);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
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
                    ep_user_image.setImageURI(image_uri);
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    ep_user_image.setImageURI(image_uri);
                    break;
            }

        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public class EncodeImage extends AsyncTask<Void, Void, String> {
        Uri uri;
        String encodedImage = "";
        public EncodeImage(Uri uri){
            this.uri = uri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*ep_progressbar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
        }

        @Override
        protected String doInBackground(Void... params){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image_uri);
                InputStream input = context.getContentResolver().openInputStream(image_uri);
                ExifInterface ei;
                if (Build.VERSION.SDK_INT > 23) {
                    ei = new ExifInterface(input);
                }
                else {
                    ei = new ExifInterface(image_uri.getPath());
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
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
                image_base_decoded = result;
                iname =  String.valueOf(System.currentTimeMillis());
                UpdateProfile(first_name+" "+last_name,contact,password,image_base_decoded,iname);
            }
        }

    }

}
