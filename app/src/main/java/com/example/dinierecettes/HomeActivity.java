package com.example.dinierecettes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public Toolbar HomeToolbar;
    public DrawerLayout HomedrawerLayout;
    public NavigationView HomeNavigationView;
    public NavController HomeNavController;
    public TextView username;
    public ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupNavigation();
    }

    public void setupNavigation(){

        HomeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(HomeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        HomedrawerLayout = findViewById(R.id.home_drawer_layout);

        HomeNavigationView = findViewById(R.id.home_navigationview);
        username = HomeNavigationView.getHeaderView(0).findViewById(R.id.nav_header_user_name);
        userImage = HomeNavigationView.getHeaderView(0).findViewById(R.id.nav_header_userprofile_image);

        if(!PreferenceData.getUserName(this).equals("")){
            username.setText(PreferenceData.getUserName(this));
            HomeNavigationView.getMenu().clear();
            HomeNavigationView.inflateMenu(R.menu.drawer_menu);
        }
        else {
            HomeNavigationView.getMenu().clear();
            HomeNavigationView.inflateMenu(R.menu.guest_menu);
        }
        if(!PreferenceData.getUserprofile(this).equals("") && !PreferenceData.getUserprofile(this).equals("no image")){
            Glide.with(this)
                    .load("https://dinierecettes.online/images/"+PreferenceData.getUserprofile(this)+".jpg")
                    .centerCrop()
                    .into(userImage);
        }

        HomeNavController = Navigation.findNavController(this,R.id.home_host_fragment);

        NavigationUI.setupActionBarWithNavController(this,HomeNavController,HomedrawerLayout);
        NavigationUI.setupWithNavController(HomeNavigationView,HomeNavController);

        HomeNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.home_host_fragment),HomedrawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (HomedrawerLayout.isDrawerOpen(GravityCompat.START)){

            HomedrawerLayout.closeDrawer(GravityCompat.START);

        }else {
            super.onBackPressed();
        }
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setCheckable(true);

        HomedrawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id){

            case R.id.edit_profile_drawer:
                if(HomeNavController.getCurrentDestination().getId()==R.id.homeFragment) {
                    HomeNavController.navigate(R.id.action_homeFragment_to_editProfileFragment);
                }
                break;

            case R.id.your_recipes_drawer:
                if(HomeNavController.getCurrentDestination().getId()==R.id.homeFragment) {
                    HomeNavController.navigate(R.id.action_homeFragment_to_yourRecipesFragment);
                }
                break;

            case R.id.logout_drawer:
                PreferenceData.clearUserData(getApplicationContext());
                Intent intent = new Intent(HomeActivity.this,Main2Activity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.your_cb_drawer:
                if(HomeNavController.getCurrentDestination().getId()==R.id.homeFragment) {
                    HomeNavController.navigate(R.id.action_homeFragment_to_cookBookFragment);
                }
                break;

            case R.id.offline_recipe_drawer:
                if(HomeNavController.getCurrentDestination().getId()==R.id.homeFragment) {
                    HomeNavController.navigate(R.id.action_homeFragment_to_offline_Recipe_Fragment);
                }
                break;

            case R.id.history_drawer:
                if(HomeNavController.getCurrentDestination().getId()==R.id.homeFragment) {
                    HomeNavController.navigate(R.id.action_homeFragment_to_historyFragment);
                }
                break;

            case R.id.delete_account_drawer:
                DeleteAccount();
                break;

            case R.id.login_btn_drawer:
                PreferenceData.clearUserData(getApplicationContext());
                Intent intent1 = new Intent(HomeActivity.this,Main2Activity.class);
                startActivity(intent1);
                finish();
                break;

        }

        return true;
    }

    private void DeleteAccount(){

        /*------------ Configuration of Guest Login Dialog----------*/
        AlertDialog.Builder DAC_builder = new AlertDialog.Builder(this)
                .setView(R.layout.delete_recipe_dialog_layout);
        final AlertDialog Delete_Account_Dialog = DAC_builder.create();
        Delete_Account_Dialog.show();
        MaterialButton drd_cancel_btn = Delete_Account_Dialog.findViewById(R.id.drd_cancel_btn);
        MaterialButton drd_continue_btn = Delete_Account_Dialog.findViewById(R.id.drd_continue_btn);
        TextView delete_title_txt = Delete_Account_Dialog.findViewById(R.id.delete_title_txt);
        delete_title_txt.setText(getResources().getString(R.string.delete_acc));
        final TextView drd_text = Delete_Account_Dialog.findViewById(R.id.drd_text);
        drd_text.setText(getResources().getString(R.string.no_premium_feature));
        final ProgressBar drd_progressbar = Delete_Account_Dialog.findViewById(R.id.drd_progressbar);

        drd_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete_Account_Dialog.dismiss();
            }
        });
        drd_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drd_progressbar.setVisibility(View.VISIBLE);
                drd_text.setVisibility(View.GONE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_ACCOUNT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        drd_progressbar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("OK")){
                                Delete_Account_Dialog.dismiss();
                                PreferenceData.clearUserData(getApplicationContext());
                                Intent intent1 = new Intent(HomeActivity.this,Main2Activity.class);
                                startActivity(intent1);
                                finish();
                            }
                            else {
                                drd_text.setVisibility(View.VISIBLE);
                                drd_text.setText(getResources().getString(R.string.some_wrong));
                            }

                        } catch (JSONException e) {
                            drd_progressbar.setVisibility(View.GONE);
                            drd_text.setVisibility(View.VISIBLE);
                            drd_text.setText(getResources().getString(R.string.some_wrong));

                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                drd_progressbar.setVisibility(View.GONE);
                                drd_text.setVisibility(View.VISIBLE);
                                drd_text.setText(getResources().getString(R.string.some_wrong));
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id",PreferenceData.getUserID(getApplicationContext()));
                        return params;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        });
    }
}
