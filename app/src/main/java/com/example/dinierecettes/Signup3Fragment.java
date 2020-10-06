package com.example.dinierecettes;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class Signup3Fragment extends Fragment implements View.OnClickListener{

    private ImageView back_icon_on_signup3_fragment,signup_userprofile_image;
    public NavController navController;
    public Uri image_uri;
    private Button signup3_next_btn;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)this.getActivity()).signup_progess.setProgress(3);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);
        back_icon_on_signup3_fragment = view.findViewById(R.id.back_icon_on_signup3_fragment);
        signup_userprofile_image = view.findViewById(R.id.signup_userprofile_image);
        signup3_next_btn = view.findViewById(R.id.signup3_next_btn);

        back_icon_on_signup3_fragment.setOnClickListener(this);
        signup_userprofile_image.setOnClickListener(this);
        signup3_next_btn.setOnClickListener(this);



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if (view==back_icon_on_signup3_fragment){
            getActivity().onBackPressed();
        }
        else if(view == signup_userprofile_image){
            SelectProfilePic();
        }
        else if(view == signup3_next_btn){
            if(image_uri!=null){
                new EncodeImage(image_uri).execute();
            }
            navController.navigate(R.id.action_signup3Fragment_to_signup4Fragment);
        }
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
                    signup_userprofile_image.setImageURI(image_uri);
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    signup_userprofile_image.setImageURI(image_uri);
                    break;
            }

        }
        if (image_uri != null) {
            //UploadImage();
            signup3_next_btn.setText(getResources().getString(R.string.next_btn_txt));
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
                PreferenceData.setUserprofile(context, result);
                PreferenceData.setUserImagename(context, String.valueOf(System.currentTimeMillis()));
            }
        }

    }


}
